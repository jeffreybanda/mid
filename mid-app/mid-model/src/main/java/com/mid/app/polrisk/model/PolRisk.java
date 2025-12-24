package com.mid.app.polrisk.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "pub.PolRisk")
@IdClass(PolRiskId.class)
public class PolRisk implements Serializable {

	private static final long serialVersionUID = 7329595848154055635L;

	@Id
	private String polNo;
	@Id
	private Integer renCnt;
	@Id
	private Integer endtCnt;
	@Id
	private Integer riskGrp;
	@Id
	private Integer riskNo;

	private Date comDate;

	private Date expiryDate;

	private BigDecimal si = new BigDecimal(0.0D);

	private String siCurr;

	private BigDecimal siExrate = new BigDecimal(0.0D);

	private BigDecimal totGap = new BigDecimal(0.0D);

	private BigDecimal premDue = new BigDecimal(0.0D);

	private BigDecimal stamp = new BigDecimal(0.0D);

	private BigDecimal fee = new BigDecimal(0.0D);

	private BigDecimal tax = new BigDecimal(0.0D);

	private String riskUppTxt = "";

	@Column(name = "class")
	private String businessClass;

	public PolRisk() {

	}

	public PolRisk(final String polNo, final Integer renCnt, final Integer endtCnt, final Integer riskGrp,
			final Integer riskNo, final Date comDate,
			final Date expiryDate, final BigDecimal si, final String siCurr, final BigDecimal siExrate,
			final BigDecimal totGap, final BigDecimal premDue,
			final BigDecimal stamp, final BigDecimal fee, final BigDecimal tax, final String riskUppTxt,
			final String businessClass) {

		this.polNo = polNo;
		this.renCnt = renCnt;
		this.endtCnt = endtCnt;
		this.riskGrp = riskGrp;
		this.riskNo = riskNo;
		this.comDate = comDate;
		this.expiryDate = expiryDate;
		this.si = si;
		this.siCurr = siCurr;
		this.siExrate = siExrate;
		this.totGap = totGap;
		this.premDue = premDue;
		this.stamp = stamp;
		this.fee = fee;
		this.tax = tax;
		this.riskUppTxt = riskUppTxt;
		this.businessClass = businessClass;
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

	public Date getComDate() {
		return comDate;
	}

	public void setComDate(final Date comDate) {
		this.comDate = comDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(final Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public BigDecimal getSi() {
		return si;
	}

	public void setSi(final BigDecimal si) {
		this.si = si;
	}

	public String getSiCurr() {
		return siCurr;
	}

	public void setSiCurr(final String siCurr) {
		this.siCurr = siCurr;
	}

	public BigDecimal getSiExrate() {
		return siExrate;
	}

	public void setSiExrate(final BigDecimal siExrate) {
		this.siExrate = siExrate;
	}

	public BigDecimal getTotGap() {
		return totGap;
	}

	public void setTotGap(final BigDecimal totGap) {
		this.totGap = totGap;
	}

	public BigDecimal getPremDue() {
		return premDue;
	}

	public void setPremDue(final BigDecimal premDue) {
		this.premDue = premDue;
	}

	public BigDecimal getStamp() {
		return stamp;
	}

	public void setStamp(final BigDecimal stamp) {
		this.stamp = stamp;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(final BigDecimal fee) {
		this.fee = fee;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(final BigDecimal tax) {
		this.tax = tax;
	}

	public String getBusinessClass() {
		return businessClass;
	}

	public void setBusinessClass(final String businessClass) {
		this.businessClass = businessClass;
	}

	public String getRiskUppTxt() {
		return riskUppTxt;
	}

	public void setRiskUppTxt(final String riskUppTxt) {
		this.riskUppTxt = riskUppTxt;
	}

}
