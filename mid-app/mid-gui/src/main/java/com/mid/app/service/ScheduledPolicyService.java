// File: com/mid/app/service/ScheduledPolicyService.java (updated with date tracking)
package com.mid.app.service;

import com.mid.app.politem.model.PolItem;
import com.mid.app.politemben.model.PolItemBen;
import com.mid.app.polmaster.model.PolMaster;
import com.mid.app.polmtrveh.model.PolMtrVeh;
import com.mid.app.polrisk.model.PolRisk;
import com.mid.app.swing.model.ImportResult;
import com.mid.app.swing.service.MotorPolicyJsonBuilder;
import com.mid.app.swing.service.PolicyDataService;
import com.mid.app.swing.service.PolicyImportService;
import com.mid.app.swing.service.PolicyValidationService;
import com.mid.app.utils.LoggingEngine;
import com.mid.app.xmm600.model.Xmm600;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

public class ScheduledPolicyService {
    
    private final PolicyImportService importService;
    private final PolicyValidationService validationService;
    private final MotorPolicyJsonBuilder jsonBuilder;
    private final PolicyDataService dataService;
    private final LoggingEngine logging;
    private final EnhancedPolicyTracker tracker;
    
    public ScheduledPolicyService() {
        this.logging = LoggingEngine.getInstance();
        this.dataService = new PolicyDataService();
        this.validationService = new PolicyValidationService();
        this.jsonBuilder = new MotorPolicyJsonBuilder();
        this.importService = new PolicyImportService(jsonBuilder, validationService, dataService);
        this.tracker = EnhancedPolicyTracker.getInstance();
        
        // Clean up old tracking files on startup (keep 30 days)
        tracker.cleanupOldTrackingFiles(30);
    }
    
    /**
     * Process policies created on the current date with duplicate prevention
     */
    public ImportResult processCurrentDatePolicies() {
        ImportResult result = new ImportResult();
        LocalDate today = LocalDate.now();
        
        EntityManagerFactory emf = null;
        EntityManager em = null;
        
        try {
            emf = Persistence.createEntityManagerFactory("midPU");
            em = emf.createEntityManager();
            
            // Find policies with today's transaction date using your query
            List<PolMaster> polMasters = findTodayPolicies(em, today);
            
            if (polMasters == null || polMasters.isEmpty()) {
                result.setMessage("No policies found for today");
                result.setSuccess(true);
                return result;
            }
            
            logInfo("Found " + polMasters.size() + " policies for today (" + today + ")");
            
            // Filter out already processed policies
            List<PolMaster> unprocessedPolicies = filterUnprocessedPolicies(polMasters, today);
            
            if (unprocessedPolicies.isEmpty()) {
                result.setMessage("All policies for today already processed");
                result.setSuccess(true);
                return result;
            }
            
            logInfo("Processing " + unprocessedPolicies.size() + " unprocessed policies for today");
            
            // Process each unprocessed policy
            int successCount = 0;
            int failureCount = 0;
            List<String> successfullyProcessed = new ArrayList<>();
            
            for (PolMaster polMaster : unprocessedPolicies) {
                try {
                    boolean processed = processSinglePolicy(em, polMaster);
                    if (processed) {
                        successCount++;
                        successfullyProcessed.add(polMaster.getPolNo());
                    } else {
                        failureCount++;
                    }
                } catch (Exception e) {
                    logError("Error processing policy " + polMaster.getPolNo() + ": " + e.getMessage(), e);
                    failureCount++;
                }
            }
            
            // Mark successfully processed policies
            for (String polNo : successfullyProcessed) {
                for (PolMaster polMaster : unprocessedPolicies) {
                    if (polMaster.getPolNo().equals(polNo)) {
                        tracker.markPolicyAsProcessed(polMaster.getPolNo(), 
                            polMaster.getRenCnt(), polMaster.getEndtCnt(), today);
                        break;
                    }
                }
            }
            
            result.setSuccessCount(successCount);
            result.setFailureCount(failureCount);
            result.setSuccess(true);
            result.setMessage(String.format("Scheduled job completed for %s: %d successful, %d failed", 
                today, successCount, failureCount));
            
        } catch (Exception e) {
            logError("Scheduled job failed: " + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("Scheduled job failed: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        }
        
        return result;
    }
    
    /**
     * Find policies with today's transaction date using your specific query
     */
    private List<PolMaster> findTodayPolicies(EntityManager em, LocalDate today) {
        try {
            // Convert LocalDate to java.util.Date for the query
            Date todayDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            // Use your exact JPQL query
            String jpql = "SELECT p FROM PolMaster p JOIN AcctMaster a " +
                "ON a.branch = p.branch AND a.trantype1 = p.acct1TranType1 AND a.docno = p.acct1DocNo " +
                "AND a.acctno = p.acctNo1 AND a.polno = p.polNo AND a.rencnt = p.renCnt AND a.endtcnt = p.endtCnt " +
                "WHERE p.tranRel = 1 AND p.department = 'M' AND p.polStat IN ('IF', 'RE') " +
                "AND p.tranDate = :today AND (" +
                "a.bal = 0 OR (a.bal IS NOT NULL AND a.netamt IS NOT NULL AND a.bal <> a.netamt)) " +
                "ORDER BY p.tranDate DESC";
            
            return em.createQuery(jpql, PolMaster.class)
                .setParameter("today", todayDate)
                .getResultList();
            
        } catch (Exception e) {
            logError("Error finding today's policies: " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Filter out already processed policies for today
     */
    private List<PolMaster> filterUnprocessedPolicies(List<PolMaster> allPolicies, LocalDate today) {
        List<PolMaster> unprocessed = new ArrayList<>();
        
        for (PolMaster policy : allPolicies) {
            if (!tracker.isPolicyProcessed(policy.getPolNo(), 
                    policy.getRenCnt(), policy.getEndtCnt(), today)) {
                unprocessed.add(policy);
            } else {
                logDebug("Skipping already processed policy for today: " + policy.getPolNo());
            }
        }
        
        return unprocessed;
    }
    
    /**
     * Process a single policy
     */
    private boolean processSinglePolicy(EntityManager em, PolMaster polMaster) {
        try {
            // Check if policy should be skipped
            boolean skipPolicy = dataService.shouldSkipPolicy(em, polMaster.getPolNo(), polMaster.getRenCnt());
            if (skipPolicy) {
                logInfo("Skipping policy " + polMaster.getPolNo() + " (marked for skip)");
                return false;
            }
            
            // Get all vehicles for this policy
            List<PolMtrVeh> vehicles = dataService.findVehicles(em, polMaster, null);
            
            if (vehicles == null || vehicles.isEmpty()) {
                logWarning("No vehicles found for policy " + polMaster.getPolNo());
                return false;
            }
            
            boolean anySuccess = false;
            
            for (PolMtrVeh vehicle : vehicles) {
                // Get risks for this vehicle
                List<PolRisk> risks = dataService.findRisks(em, polMaster, vehicle);
                
                if (risks == null || risks.isEmpty()) {
                    logWarning("No risks found for policy " + polMaster.getPolNo() + " vehicle " + vehicle.getVehRegNo());
                    continue;
                }
                
                for (PolRisk risk : risks) {
                    // Get policy item
                    PolItem item = dataService.findItem(em, polMaster, vehicle);
                    if (item == null) {
                        logWarning("No item found for policy " + polMaster.getPolNo());
                        continue;
                    }
                    
                    // Get item benefits
                    List<PolItemBen> itemBens = dataService.findItemBenefits(em, polMaster, vehicle);
                    
                    // Get intermediaries and clients
                    List<Xmm600> intermediaries = dataService.findIntermediaries(em, polMaster);
                    List<Xmm600> clients = dataService.findClients(em, polMaster);
                    
                    // Validate the record
                    boolean isValid = validationService.validateRecord(polMaster, risk, vehicle, 
                        itemBens, clients, intermediaries);
                    
                    if (isValid) {
                        try {
                            // Build JSON
                            String json = jsonBuilder.buildPolicyJson(polMaster, risk, vehicle, item,itemBens);
                            
                            // Import to portal
                            boolean importSuccess = importService.sendToPortalScheduled(json, polMaster.getPolNo());
                            
                            if (importSuccess) {
                                anySuccess = true;
                                logInfo("Successfully imported policy " + polMaster.getPolNo() + 
                                    " for vehicle " + vehicle.getVehRegNo());
                            } else {
                                logWarning("Failed to import policy " + polMaster.getPolNo());
                            }
                        } catch (Exception e) {
                            logError("Error building/importing JSON for policy " + polMaster.getPolNo() + 
                                ": " + e.getMessage(), e);
                        }
                    } else {
                        logWarning("Policy " + polMaster.getPolNo() + " failed validation");
                    }
                }
            }
            
            return anySuccess;
            
        } catch (Exception e) {
            logError("Error processing policy " + polMaster.getPolNo() + ": " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Reprocess policies for a specific date (manual intervention)
     */
    public ImportResult reprocessDatePolicies(LocalDate date) {
        // Reset tracking for this date
        tracker.resetDateTracking(date);
        
        // Now process as if it's a fresh date
        return processDatePolicies(date);
    }
    
    /**
     * Process policies for a specific date (not just today)
     */
    private ImportResult processDatePolicies(LocalDate date) {
        // Similar to processCurrentDatePolicies but with specified date
        // Implementation would be similar but using the specified date instead of today
        // ... implementation omitted for brevity
        return null;
    }
    
    private void logInfo(String message) {
        if (logging != null) {
            logging.setMessage("[SCHEDULED JOB] " + message);
           
        }
        System.out.println("[SCHEDULED JOB] " + message);
    }
    
    private void logWarning(String message) {
        if (logging != null) {
            logging.setMessage("[SCHEDULED JOB WARNING] " + message);
            
        }
        System.out.println("[SCHEDULED JOB WARNING] " + message);
    }
    
    private void logError(String message, Throwable throwable) {
        if (logging != null) {
            logging.setMessage("[SCHEDULED JOB ERROR] " + message);
            
        }
        System.out.println("[SCHEDULED JOB ERROR] " + message);
        if (throwable != null) {
            throwable.printStackTrace();
        }
    }
    
    private void logDebug(String message) {
        if (logging != null) {
            logging.setMessage("[SCHEDULED JOB DEBUG] " + message);
           
        }
        System.out.println("[SCHEDULED JOB DEBUG] " + message);
    }
}