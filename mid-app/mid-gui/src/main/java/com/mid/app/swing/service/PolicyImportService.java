package com.mid.app.swing.service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import com.mid.app.common.model.HttpCode;
import com.mid.app.http.utils.HttpAuthentication;
import com.mid.app.politem.model.PolItem;
import com.mid.app.politemben.model.PolItemBen;
import com.mid.app.polmaster.model.PolMaster;
import com.mid.app.polmtrveh.model.PolMtrVeh;
import com.mid.app.polrisk.model.PolRisk;
import com.mid.app.swing.model.ImportCriteria;
import com.mid.app.swing.model.ImportResult;
import com.mid.app.utils.LoggingEngine;

public class PolicyImportService {
    
    private final MotorPolicyJsonBuilder jsonBuilder;
    private final PolicyValidationService validationService;
    private final PolicyDataService dataService;
    private final LoggingEngine logging;
    
    public PolicyImportService(MotorPolicyJsonBuilder jsonBuilder,
                              PolicyValidationService validationService,
                              PolicyDataService dataService) {
        this.jsonBuilder = jsonBuilder;
        this.validationService = validationService;
        this.dataService = dataService;
        this.logging = LoggingEngine.getInstance();
    }
    
    public ImportResult importPolicies(ImportCriteria criteria) {
        ImportResult result = new ImportResult();
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        
        EntityManagerFactory emf = null;
        EntityManager em = null;
        
        try {
            emf = Persistence.createEntityManagerFactory("midPU");
            em = emf.createEntityManager();
            
            List<PolMaster> polMasters = dataService.findPolMasters(em, criteria);
            
            if (polMasters == null || polMasters.isEmpty()) {
                result.setMessage("No policies found for the given criteria");
                result.setSuccess(false);
                return result;
            }
            
            for (PolMaster polMaster : polMasters) {
                boolean skipPolicy = dataService.shouldSkipPolicy(em, 
                    polMaster.getPolNo(), polMaster.getRenCnt());
                
                if (skipPolicy) {
                    continue;
                }
                
                List<PolMtrVeh> vehicles = dataService.findVehicles(em, polMaster, 
                    criteria.getVehicleRegNo());
                
                for (PolMtrVeh vehicle : vehicles) {
                    List<PolRisk> risks = dataService.findRisks(em, polMaster, vehicle);
                    
                    for (PolRisk risk : risks) {
                        PolItem item = dataService.findItem(em, polMaster, vehicle);
                        if (item == null) {
                            if (logging != null) {
                                logging.setMessage("No item found for policy " + 
                                    polMaster.getPolNo());
                            }
                            failureCount.incrementAndGet();
                            continue;
                        }
                        
                        List<PolItemBen> itemBens = dataService.findItemBenefits(em, 
                            polMaster, vehicle);
                        
                        // Build JSON
                        String json = jsonBuilder.buildPolicyJson(polMaster, risk, vehicle,
                            item, itemBens);
                        
                        logging.setMessage("Generated payload for policy: " + 
                            polMaster.getPolNo());
                        
                        // Try import
                        boolean importSuccess = sendToPortal(json, polMaster.getPolNo());
                        
                        if (importSuccess) {
                            successCount.incrementAndGet();
                        } else {
                            failureCount.incrementAndGet();
                            // Store failed policy for potential retry
                            result.addFailedPolicy(polMaster.getPolNo(), json, 
                                "API submission failed");
                        }
                    }
                }
            }
            
            result.setTotalProcessed(successCount.get() + failureCount.get());
            result.setSuccessCount(successCount.get());
            result.setFailureCount(failureCount.get());
            result.setSuccess(failureCount.get() == 0);
            result.setMessage(String.format("Import completed: %d successful, %d failed", 
                successCount.get(), failureCount.get()));
            
        } catch (Exception e) {
            if (logging != null) {
                logging.setMessage("Policy import failed: " + e.getMessage());
            }
            result.setSuccess(false);
            result.setMessage("Import failed: " + e.getMessage());
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
    
    private boolean sendToPortal(String json, String policyNumber) 
    	    throws AuthenticationException, IOException {
    	    
    	    CloseableHttpResponse response = null;
    	    try {
    	        System.out.println("Importing policy: " + policyNumber);
    	        
    	        response = HttpAuthentication.getPostPolicyToPortalResponse(json);
    	        HttpEntity body = response.getEntity();
    	        StatusLine statusLine = response.getStatusLine();
    	        String content = EntityUtils.toString(body);
    	        
    	        if (statusLine.getStatusCode() == HttpCode.CREATED.getCode()|| statusLine.getStatusCode() == HttpCode.OK.getCode()) {
    	            if (logging != null) {
    	                logging.setMessage("Successfully imported policy: " + policyNumber);
    	            }
    	            return true;
    	        } else {
    	            if (logging != null) {
    	                logging.setMessage("Failed to import policy " + policyNumber + ": " + content);
    	            }
    	            
    	            // Store the failed JSON (could save to file or keep in memory)
    	            storeFailedJsonLocally(policyNumber, json, content);
    	            
    	            return false;
    	        }
    	    } catch (IOException e) {
    	        if (logging != null) {
    	            logging.setMessage("IO error importing policy " + policyNumber + ": " + e.getMessage());
    	        }
    	        
    	        // Store the failed JSON even on IO exception
    	        storeFailedJsonLocally(policyNumber, json, e.getMessage());
    	        
    	        return false;
    	    } finally {
    	        if (response != null) {
    	            try {
    	                response.close();
    	            } catch (IOException e) {
    	                // Ignore
    	            }
    	        }
    	    }
    	}

    
    
    private void storeFailedJsonLocally(String policyNumber, String json, String error) {
        try {
            String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "failed_" + policyNumber + "_" + timestamp + ".json";
            java.nio.file.Path dir = java.nio.file.Paths.get("failed_submissions");
            java.nio.file.Files.createDirectories(dir);
            
            String fullRecord = "// ERROR: " + error + "\n" +
                               "// POLICY: " + policyNumber + "\n" +
                               "// TIMESTAMP: " + timestamp + "\n" +
                               json;
            
            java.nio.file.Files.write(dir.resolve(fileName), fullRecord.getBytes());
        } catch (Exception e) {
            // Log but don't crash
            System.err.println("Could not save failed JSON: " + e.getMessage());
        }
    }

    

    public boolean sendToPortalScheduled(String json, String policyNumber) 
            throws AuthenticationException, IOException {
        
        CloseableHttpResponse response = null;
        try {
            response = HttpAuthentication.getPostPolicyToPortalResponse(json);
            HttpEntity body = response.getEntity();
            StatusLine statusLine = response.getStatusLine();
            String content = EntityUtils.toString(body);
            
            if (statusLine.getStatusCode() == HttpCode.OK.getCode()) {
                logInfo("Successfully imported policy: " + policyNumber);
                return true;
            } else {
                logError("Failed to import policy " + policyNumber + ": " + content);
                return false;
            }
        } catch (IOException e) {
            logError("IO error importing policy " + policyNumber + ": " + e.getMessage());
            return false;
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }
    
    private void logInfo(String message) {
        if (logging != null) {
            logging.setMessage(message);
           
        }
    }
    
    private void logError(String message) {
        if (logging != null) {
            logging.setMessage(message);
            
        }
    }
    
    
    
    public boolean retryPolicySubmission(String policyNumber, String json) {
        try {
            return sendToPortal(json, policyNumber);
        } catch (Exception e) {
            if (logging != null) {
                logging.setMessage("Retry failed for policy " + policyNumber + ": " + e.getMessage());
            }
            return false;
        }
    }
}