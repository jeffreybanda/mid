// File: com/mid/app/service/PolicyTrackingManager.java
package com.mid.app.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Set;

public class PolicyTrackingManager {
    
    public static void main(String[] args) {
        EnhancedPolicyTracker tracker = EnhancedPolicyTracker.getInstance();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Policy Tracking Manager ===");
        
        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. View processed policies for a date");
            System.out.println("2. Reset tracking for a date");
            System.out.println("3. Export tracking data");
            System.out.println("4. Clean up old tracking files");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");
            
            String choice = scanner.nextLine();
            
            try {
                switch (choice) {
                    case "1":
                        System.out.print("Enter date (yyyy-MM-dd): ");
                        String dateStr = scanner.nextLine();
                        LocalDate date = LocalDate.parse(dateStr);
                        Set<String> policies = tracker.getProcessedPoliciesForDate(date);
                        System.out.println("Processed policies for " + date + ": " + policies.size());
                        for (String policy : policies) {
                            System.out.println("  " + policy);
                        }
                        break;
                        
                    case "2":
                        System.out.print("Enter date to reset (yyyy-MM-dd): ");
                        dateStr = scanner.nextLine();
                        date = LocalDate.parse(dateStr);
                        System.out.print("Are you sure? This will allow reprocessing of all policies for " + date + " (y/n): ");
                        String confirm = scanner.nextLine();
                        if (confirm.equalsIgnoreCase("y")) {
                            tracker.resetDateTracking(date);
                            System.out.println("Tracking reset for " + date);
                        }
                        break;
                        
                    case "3":
                        System.out.print("Enter start date (yyyy-MM-dd): ");
                        String startStr = scanner.nextLine();
                        System.out.print("Enter end date (yyyy-MM-dd): ");
                        String endStr = scanner.nextLine();
                        System.out.print("Enter output file path: ");
                        String outputPath = scanner.nextLine();
                        
                        tracker.exportTrackingData(
                            LocalDate.parse(startStr), 
                            LocalDate.parse(endStr), 
                            outputPath
                        );
                        System.out.println("Data exported to " + outputPath);
                        break;
                        
                    case "4":
                        System.out.print("Enter days to keep (e.g., 30): ");
                        int days = Integer.parseInt(scanner.nextLine());
                        tracker.cleanupOldTrackingFiles(days);
                        System.out.println("Cleanup completed");
                        break;
                        
                    case "5":
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                        
                    default:
                        System.out.println("Invalid option");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}