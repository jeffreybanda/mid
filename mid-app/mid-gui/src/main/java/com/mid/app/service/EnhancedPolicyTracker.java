// File: com/mid/app/service/EnhancedPolicyTracker.java
package com.mid.app.service;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EnhancedPolicyTracker {
    
    private static final String TRACKING_DIR = System.getProperty("user.home") + 
        File.separator + ".policy_tracker";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    // Track policies by date to avoid loading everything into memory
    private final Map<String, Set<String>> dateToPolicies = new ConcurrentHashMap<>();
    private static EnhancedPolicyTracker instance;
    
    private EnhancedPolicyTracker() {
        // Initialize - policies are loaded on-demand when needed for a specific date
    }
    
    public static synchronized EnhancedPolicyTracker getInstance() {
        if (instance == null) {
            instance = new EnhancedPolicyTracker();
        }
        return instance;
    }
    
    /**
     * Check if a policy from a specific date has been processed
     */
    public boolean isPolicyProcessed(String polNo, Integer renCnt, Integer endtCnt, LocalDate processDate) {
        String dateKey = processDate.format(DATE_FORMATTER);
        String policyKey = createPolicyKey(polNo, renCnt, endtCnt);
        
        // Load policies for this date if not already loaded
        loadDatePolicies(dateKey);
        
        Set<String> processedPolicies = dateToPolicies.get(dateKey);
        return processedPolicies != null && processedPolicies.contains(policyKey);
    }
    
    /**
     * Mark a policy as processed for a specific date
     */
    public void markPolicyAsProcessed(String polNo, Integer renCnt, Integer endtCnt, LocalDate processDate) {
        String dateKey = processDate.format(DATE_FORMATTER);
        String policyKey = createPolicyKey(polNo, renCnt, endtCnt);
        
        // Load policies for this date if not already loaded
        loadDatePolicies(dateKey);
        
        Set<String> processedPolicies = dateToPolicies.computeIfAbsent(dateKey, k -> 
            Collections.synchronizedSet(new HashSet<>()));
        processedPolicies.add(policyKey);
        
        // Save to file for this date
        saveDatePolicies(dateKey);
    }
    
    /**
     * Get all processed policies for a specific date (for debugging)
     */
    public Set<String> getProcessedPoliciesForDate(LocalDate date) {
        String dateKey = date.format(DATE_FORMATTER);
        loadDatePolicies(dateKey);
        Set<String> policies = dateToPolicies.get(dateKey);
        return policies != null ? new HashSet<>(policies) : new HashSet<>();
    }
    
    /**
     * Clean up old tracking files (keep only last 30 days)
     */
    public void cleanupOldTrackingFiles(int keepDays) {
        File trackingDir = new File(TRACKING_DIR);
        if (!trackingDir.exists()) {
            return;
        }
        
        LocalDate cutoffDate = LocalDate.now().minusDays(keepDays);
        
        File[] trackingFiles = trackingDir.listFiles((dir, name) -> name.endsWith(".dat"));
        if (trackingFiles != null) {
            for (File file : trackingFiles) {
                try {
                    String filename = file.getName();
                    String dateStr = filename.substring(0, filename.indexOf('.'));
                    LocalDate fileDate = LocalDate.parse(dateStr, DATE_FORMATTER);
                    
                    if (fileDate.isBefore(cutoffDate)) {
                        file.delete();
                        dateToPolicies.remove(dateStr);
                        System.out.println("[TRACKER] Cleaned up old tracking file: " + filename);
                    }
                } catch (Exception e) {
                    // Ignore files that don't match the pattern
                }
            }
        }
    }
    
    /**
     * Export tracking data for a date range
     */
    public void exportTrackingData(LocalDate startDate, LocalDate endDate, String outputFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write("ProcessDate,PolicyNumber,RenewalCount,EndorsementCount");
            writer.newLine();
            
            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                String dateKey = currentDate.format(DATE_FORMATTER);
                loadDatePolicies(dateKey);
                
                Set<String> policies = dateToPolicies.get(dateKey);
                if (policies != null) {
                    for (String policyKey : policies) {
                        String[] parts = policyKey.split("\\|");
                        if (parts.length == 3) {
                            writer.write(String.format("%s,%s,%s,%s",
                                currentDate.format(DateTimeFormatter.ISO_DATE),
                                parts[0], parts[1], parts[2]));
                            writer.newLine();
                        }
                    }
                }
                
                currentDate = currentDate.plusDays(1);
            }
        }
    }
    
    /**
     * Reset tracking for a specific date (useful for testing or reprocessing)
     */
    public void resetDateTracking(LocalDate date) {
        String dateKey = date.format(DATE_FORMATTER);
        dateToPolicies.remove(dateKey);
        
        File trackingFile = new File(TRACKING_DIR + File.separator + dateKey + ".dat");
        if (trackingFile.exists()) {
            trackingFile.delete();
        }
    }
    
    private String createPolicyKey(String polNo, Integer renCnt, Integer endtCnt) {
        return String.format("%s|%d|%d", polNo, renCnt != null ? renCnt : 0, endtCnt != null ? endtCnt : 0);
    }
    
    private void loadDatePolicies(String dateKey) {
        // Already loaded?
        if (dateToPolicies.containsKey(dateKey)) {
            return;
        }
        
        File trackingFile = new File(TRACKING_DIR + File.separator + dateKey + ".dat");
        if (!trackingFile.exists()) {
            // Initialize empty set for this date
            dateToPolicies.put(dateKey, Collections.synchronizedSet(new HashSet<>()));
            return;
        }
        
        Set<String> policies = Collections.synchronizedSet(new HashSet<>());
        try (BufferedReader reader = new BufferedReader(new FileReader(trackingFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    policies.add(line.trim());
                }
            }
            dateToPolicies.put(dateKey, policies);
            System.out.println("[TRACKER] Loaded " + policies.size() + " processed policies for date " + dateKey);
        } catch (IOException e) {
            System.err.println("[TRACKER] Error loading policies for date " + dateKey + ": " + e.getMessage());
            dateToPolicies.put(dateKey, Collections.synchronizedSet(new HashSet<>()));
        }
    }
    
    private void saveDatePolicies(String dateKey) {
        Set<String> policies = dateToPolicies.get(dateKey);
        if (policies == null) {
            return;
        }
        
        // Create directory if it doesn't exist
        File directory = new File(TRACKING_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        // Write to temp file first
        File trackingFile = new File(TRACKING_DIR + File.separator + dateKey + ".dat");
        File tempFile = new File(TRACKING_DIR + File.separator + dateKey + ".tmp");
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            synchronized (policies) {
                for (String key : policies) {
                    writer.write(key);
                    writer.newLine();
                }
            }
            writer.flush();
            
            // Atomic rename
            Files.move(tempFile.toPath(), trackingFile.toPath(), 
                StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            
        } catch (IOException e) {
            System.err.println("[TRACKER] Error saving policies for date " + dateKey + ": " + e.getMessage());
            tempFile.delete();
        }
    }
    
    
 // Add to EnhancedPolicyTracker.java
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Get tracking directory info
        File trackingDir = new File(TRACKING_DIR);
        int fileCount = 0;
        long totalPolicies = 0;
        
        if (trackingDir.exists()) {
            File[] files = trackingDir.listFiles((dir, name) -> name.endsWith(".dat"));
            if (files != null) {
                fileCount = files.length;
                
                // Count total policies across all files
                for (File file : files) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        while (reader.readLine() != null) {
                            totalPolicies++;
                        }
                    } catch (IOException e) {
                        // Ignore for counting
                    }
                }
            }
        }
        
        // Today's count
        LocalDate today = LocalDate.now();
        String todayKey = today.format(DATE_FORMATTER);
        loadDatePolicies(todayKey);
        Set<String> todayPolicies = dateToPolicies.get(todayKey);
        int todayCount = todayPolicies != null ? todayPolicies.size() : 0;
        
        stats.put("trackingFiles", fileCount);
        stats.put("totalPoliciesTracked", totalPolicies);
        stats.put("todayProcessed", todayCount);
        stats.put("trackingDirectory", trackingDir.getAbsolutePath());
        
        return stats;
    }
}