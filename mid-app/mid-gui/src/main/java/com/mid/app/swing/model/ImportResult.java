
package com.mid.app.swing.model;

import java.util.ArrayList;
import java.util.List;

public class ImportResult {
    private boolean success;
    private String message;
    private int successCount;
    private int failureCount;
    private int totalProcessed;
    private List<FailedPolicy> failedPolicies = new ArrayList<>();
    // Default constructor
    public ImportResult() {}
    
    // Constructor for success
    public ImportResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    // Full constructor
    public ImportResult(boolean success, String message, int successCount, int failureCount) {
        this.success = success;
        this.message = message;
        this.successCount = successCount;
        this.failureCount = failureCount;
        this.totalProcessed = successCount + failureCount;
    }
    
    public void addFailedPolicy(String policyNumber, String json, String error) {
        failedPolicies.add(new FailedPolicy(policyNumber, json, error));
    }
    
    public List<FailedPolicy> getFailedPolicies() {
        return failedPolicies;
    }
    
    public void setFailedPolicies(List<FailedPolicy> failedPolicies) {
        this.failedPolicies = failedPolicies;
    }
    
    public static class FailedPolicy {
        private String policyNumber;
        private String json;
        private String error;
        
        public FailedPolicy(String policyNumber, String json, String error) {
            this.policyNumber = policyNumber;
            this.json = json;
            this.error = error;
        }
        
        // Getters and setters
        public String getPolicyNumber() { return policyNumber; }
        public String getJson() { return json; }
        public String getError() { return error; }
        
        public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }
        public void setJson(String json) { this.json = json; }
        public void setError(String error) { this.error = error; }
    }
    
    // Getters and setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public int getSuccessCount() {
        return successCount;
    }
    
    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
        this.totalProcessed = this.successCount + this.failureCount;
    }
    
    public int getFailureCount() {
        return failureCount;
    }
    
    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
        this.totalProcessed = this.successCount + this.failureCount;
    }
    
    public int getTotalProcessed() {
        return totalProcessed;
    }
    
    public void setTotalProcessed(int totalProcessed) {
        this.totalProcessed = totalProcessed;
    }
}