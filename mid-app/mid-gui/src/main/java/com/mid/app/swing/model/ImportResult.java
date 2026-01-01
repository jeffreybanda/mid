
package com.mid.app.swing.model;

public class ImportResult {
    private boolean success;
    private String message;
    private int successCount;
    private int failureCount;
    private int totalProcessed;
    
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