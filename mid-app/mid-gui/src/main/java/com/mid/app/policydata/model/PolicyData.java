package com.mid.app.policydata.model;

import java.math.BigDecimal;
import java.util.Date;

public class PolicyData {
	
	private Date startDate;
    private Date expiryDate;
    private BigDecimal sumInsured;
    private BigDecimal grossPremium;
    private String type; // Can be: NEW_BUSINESS, RENEWAL, ENDORSEMENT, CANCELLATION
    private Date transactionDate;
    private boolean excessBought;
    private int noClaimDiscount; // Percentage (0-100)
    private int earnedNoClaimDiscount; // Percentage (0-100)
    private String calculationType; // Can be: FULL_YEAR, SHORT_RATE, PRO_
    
    
    
    public PolicyData() {
    	
    }



	public Date getStartDate() {
		return startDate;
	}



	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}



	public Date getExpiryDate() {
		return expiryDate;
	}



	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}



	public BigDecimal getSumInsured() {
		return sumInsured;
	}



	public void setSumInsured(BigDecimal sumInsured) {
		this.sumInsured = sumInsured;
	}



	public BigDecimal getGrossPremium() {
		return grossPremium;
	}



	public void setGrossPremium(BigDecimal grossPremium) {
		this.grossPremium = grossPremium;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public Date getTransactionDate() {
		return transactionDate;
	}



	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}



	public boolean isExcessBought() {
		return excessBought;
	}



	public void setExcessBought(boolean excessBought) {
		this.excessBought = excessBought;
	}



	public int getNoClaimDiscount() {
		return noClaimDiscount;
	}



	public void setNoClaimDiscount(int noClaimDiscount) {
		this.noClaimDiscount = noClaimDiscount;
	}



	public int getEarnedNoClaimDiscount() {
		return earnedNoClaimDiscount;
	}



	public void setEarnedNoClaimDiscount(int earnedNoClaimDiscount) {
		this.earnedNoClaimDiscount = earnedNoClaimDiscount;
	}



	public String getCalculationType() {
		return calculationType;
	}



	public void setCalculationType(String calculationType) {
		this.calculationType = calculationType;
	}
    
    

}
