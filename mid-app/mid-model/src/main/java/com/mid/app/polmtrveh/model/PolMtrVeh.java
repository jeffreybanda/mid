package com.mid.app.polmtrveh.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "pub.PolMtrVeh")
@IdClass(PolMtrVehId.class)
public class PolMtrVeh implements Serializable {

	private static final long serialVersionUID = -5261085511021773458L;

	@Id
	private String polNo;
	@Id
	private Integer renCnt;
	@Id
	private Integer endtCnt;
	@Id
	private String vehRegNo;

	private Integer riskGrp;

	private Integer riskNo;

	private Integer itemNo;

	private String vehMake;

	private String modelDesc;

	private String vehBody;

	private String colour;

	private String chassisNo;

	private String yrManu;

	private Integer engineCC;

	private BigDecimal tPPrem;

	private Integer noSeats;

	private Integer mileage;

	private String coverType;

	private String cocNo;

	private String ownName;

	private String certRef;
	
	private String tariff;
	
	private String vehUsg;
	
	private Integer regYr;
	
	private Integer regMth;

	public PolMtrVeh() {

	}

	public Integer getItemNo() {
		return itemNo;
	}

	public void setItemNo(final Integer itemNo) {
		this.itemNo = itemNo;
	}

	public String getVehBody() {
		return vehBody;
	}

	public void setVehBody(final String vehBody) {
		this.vehBody = vehBody;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(final String colour) {
		this.colour = colour;
	}

	public String getVehRegNo() {
		return vehRegNo;
	}

	public void setVehRegNo(final String vehRegNo) {
		this.vehRegNo = vehRegNo;
	}

	public String getVehMake() {
		return vehMake;
	}

	public void setVehMake(final String vehMake) {
		this.vehMake = vehMake;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public void setChassisNo(final String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public String getYrManu() {
		return yrManu;
	}

	public void setYrManu(final String yrManu) {
		this.yrManu = yrManu;
	}

	public Integer getEngineCC() {
		return engineCC;
	}

	public void setEngineCC(final Integer engineCC) {
		this.engineCC = engineCC;
	}

	public BigDecimal gettPPrem() {
		return tPPrem;
	}

	public void settPPrem(final BigDecimal tPPrem) {
		this.tPPrem = tPPrem;
	}

	public String getPolNo() {
		return polNo;
	}

	public void setPolNo(final String polNo) {
		this.polNo = polNo;
	}

	public Integer getRenCnt() {
		return renCnt;
	}

	public void setRenCnt(final Integer renCnt) {
		this.renCnt = renCnt;
	}

	public Integer getEndtCnt() {
		return endtCnt;
	}

	public void setEndtCnt(final Integer endtCnt) {
		this.endtCnt = endtCnt;
	}

	public Integer getRiskGrp() {
		return riskGrp;
	}

	public void setRiskGrp(final Integer riskGrp) {
		this.riskGrp = riskGrp;
	}

	public Integer getRiskNo() {
		return riskNo;
	}

	public void setRiskNo(final Integer riskNo) {
		this.riskNo = riskNo;
	}

	public String getModelDesc() {
		return modelDesc;
	}

	public void setModelDesc(final String modelDesc) {
		this.modelDesc = modelDesc;
	}

	public Integer getNoSeats() {
		return noSeats;
	}

	public void setNoSeats(final Integer noSeats) {
		this.noSeats = noSeats;
	}

	public Integer getMileage() {
		return mileage;
	}

	public void setMileage(final Integer mileage) {
		this.mileage = mileage;
	}

	public String getCoverType() {
		return coverType;
	}

	public void setCoverType(final String coverType) {
		this.coverType = coverType;
	}

	public String getCocNo() {
		return cocNo;
	}

	public void setCocNo(final String cocNo) {
		this.cocNo = cocNo;
	}

	public String getOwnName() {
		return ownName;
	}

	public void setOwnName(final String ownName) {
		this.ownName = ownName;
	}

	public String getCertRef() {
		return certRef;
	}

	public void setCertRef(final String certRef) {
		this.certRef = certRef;
	}
	
	

	public String getTariff() {
		return tariff;
	}

	public void setTariff(String tariff) {
		this.tariff = tariff;
	}
	
	

	public String getVehUsg() {
		return vehUsg;
	}

	public void setVehUsg(String vehUsg) {
		this.vehUsg = vehUsg;
	}
	
	

	public Integer getRegYr() {
		return regYr;
	}

	public void setRegYr(Integer regYr) {
		this.regYr = regYr;
	}

	public Integer getRegMth() {
		return regMth;
	}

	public void setRegMth(Integer regMth) {
		this.regMth = regMth;
	}

	@Override
	public String toString() {
		return "PolMtrVeh [vehRegNo=" + vehRegNo + ", vehMake=" + vehMake + ", chassisNo=" + chassisNo + ", yrManu="
				+ yrManu + ", engineCC=" + engineCC + ", tPPrem=" + tPPrem + "]";
	}

}
