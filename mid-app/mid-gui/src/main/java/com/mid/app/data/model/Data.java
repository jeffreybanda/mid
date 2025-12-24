package com.mid.app.data.model;

import com.mid.app.customerdata.model.CustomerData;
import com.mid.app.policydata.model.PolicyData;
import com.mid.app.vehicledata.model.VehicleData;

public class Data {

	private String branchCode;
	private String productCode;
	private String intermediaryCode;
	private String subintermediaryCode;
	private String riskTypeCode;
	
	private VehicleData vehicleData;
	private CustomerData customerData;
	private PolicyData policyData;
	
	public Data() {
		
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getIntermediaryCode() {
		return intermediaryCode;
	}

	public void setIntermediaryCode(String intermediaryCode) {
		this.intermediaryCode = intermediaryCode;
	}

	public String getSubintermediaryCode() {
		return subintermediaryCode;
	}

	public void setSubintermediaryCode(String subintermediaryCode) {
		this.subintermediaryCode = subintermediaryCode;
	}

	public String getRiskTypeCode() {
		return riskTypeCode;
	}

	public void setRiskTypeCode(String riskTypeCode) {
		this.riskTypeCode = riskTypeCode;
	}

	public VehicleData getVehicleData() {
		return vehicleData;
	}

	public void setVehicleData(VehicleData vehicleData) {
		this.vehicleData = vehicleData;
	}

	public CustomerData getCustomerData() {
		return customerData;
	}

	public void setCustomerData(CustomerData customerData) {
		this.customerData = customerData;
	}

	public PolicyData getPolicyData() {
		return policyData;
	}

	public void setPolicyData(PolicyData policyData) {
		this.policyData = policyData;
	}
	
	

}
