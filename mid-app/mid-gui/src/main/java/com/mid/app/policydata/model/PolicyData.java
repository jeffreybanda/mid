package com.mid.app.policydata.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PolicyData {
	
	
	@JsonProperty("startDate")
	private Date startDate;
	
	@JsonProperty("expiryDate")
    private Date expiryDate;
	
	@JsonProperty("sumInsured")
    private BigDecimal sumInsured;
	
	@JsonProperty("grossPremium")
    private BigDecimal grossPremium;
	
	@JsonProperty("type")
    private String type; // Can be: NEW_BUSINESS, RENEWAL, ENDORSEMENT, CANCELLATION
	
	@JsonProperty("transactionDate")
    private Date transactionDate;
	
	@JsonProperty("excessBought")
    private boolean excessBought;
	
	@JsonProperty("noClaimDiscount")
    private int noClaimDiscount; // Percentage (0-100)
	
	@JsonProperty("earnedNoClaimDiscount")
    private int earnedNoClaimDiscount; // Percentage (0-100)
	
	@JsonProperty("calculationType")
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
