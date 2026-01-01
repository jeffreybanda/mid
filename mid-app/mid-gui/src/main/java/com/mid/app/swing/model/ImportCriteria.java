
package com.mid.app.swing.model;

import java.util.Date;

public class ImportCriteria {
    private String policyNumber;
    private String vehicleRegNo;
    private Date startDate;
    private Date endDate;
    
    // Default constructor
    public ImportCriteria() {}
    
    // Constructor with parameters
    public ImportCriteria(String policyNumber, String vehicleRegNo, Date startDate, Date endDate) {
        this.policyNumber = policyNumber;
        this.vehicleRegNo = vehicleRegNo;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    // Getters and setters
    public String getPolicyNumber() {
        return policyNumber;
    }
    
    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }
    
    public String getVehicleRegNo() {
        return vehicleRegNo;
    }
    
    public void setVehicleRegNo(String vehicleRegNo) {
        this.vehicleRegNo = vehicleRegNo;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    // Helper method to check if criteria is empty
    public boolean isEmpty() {
        return (policyNumber == null || policyNumber.trim().isEmpty()) &&
               (vehicleRegNo == null || vehicleRegNo.trim().isEmpty()) &&
               startDate == null && endDate == null;
    }
}