package com.mid.app.politemben.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "pub.PolItemBen")
@IdClass(PolItemBenId.class)
public class PolItemBen implements Serializable {

	private static final long serialVersionUID = -4040422753231309375L;

	@Id
	String polNo = "";
	@Id
	Integer renCnt = new Integer(0);
	@Id
	Integer endtCnt = new Integer(0);
	@Id
	Integer riskGrp = new Integer(0);
	@Id
	Integer riskNo = new Integer(0);
	@Id
	Integer itemNo = new Integer(0);
	@Id
	Integer seqNo = new Integer(0);
	@Id
	String benCode = "";
	String benDesc = "";

	BigDecimal premRatePct = new BigDecimal(0);
	BigDecimal benGAP = new BigDecimal(0);
	BigDecimal discLdg1 = new BigDecimal(0);
	BigDecimal discLdg2 = new BigDecimal(0);
	BigDecimal discLdg3 = new BigDecimal(0);
	BigDecimal premDue = new BigDecimal(0);

	public PolItemBen() {

	}

	public PolItemBen(final String benCode, final String polNo, final String benDesc, final BigDecimal premRatePct,
			final BigDecimal benGAP,
			final BigDecimal discLdg1, final BigDecimal discLdg2, final BigDecimal discLdg3, final BigDecimal premDue,
			final Integer renCnt,
			final Integer endtCnt, final Integer riskGrp, final Integer riskNo, final Integer itemNo) {

		this.benCode = benCode;
		this.polNo = polNo;
		this.benDesc = benDesc;
		this.premRatePct = premRatePct;
		this.benGAP = benGAP;
		this.discLdg1 = discLdg1;
		this.discLdg2 = discLdg2;
		this.discLdg3 = discLdg3;
		this.premDue = premDue;
		this.renCnt = renCnt;
		this.endtCnt = endtCnt;
		this.riskGrp = riskGrp;
		this.riskNo = riskNo;
		this.itemNo = itemNo;
	}

	public String getBenCode() {
		return benCode;
	}

	public void setBenCode(final String benCode) {
		this.benCode = benCode;
	}

	public String getPolNo() {
		return polNo;
	}

	public void setPolNo(final String polNo) {
		this.polNo = polNo;
	}

	public String getBenDesc() {
		return benDesc;
	}

	public void setBenDesc(final String benDesc) {
		this.benDesc = benDesc;
	}

	public BigDecimal getPremRatePct() {
		return premRatePct;
	}

	public void setPremRatePct(final BigDecimal premRatePct) {
		this.premRatePct = premRatePct;
	}

	public BigDecimal getBenGAP() {
		return benGAP;
	}

	public void setBenGAP(final BigDecimal benGAP) {
		this.benGAP = benGAP;
	}

	public BigDecimal getDiscLdg1() {
		return discLdg1;
	}

	public void setDiscLdg1(final BigDecimal discLdg1) {
		this.discLdg1 = discLdg1;
	}

	public BigDecimal getDiscLdg2() {
		return discLdg2;
	}

	public void setDiscLdg2(final BigDecimal discLdg2) {
		this.discLdg2 = discLdg2;
	}

	public BigDecimal getDiscLdg3() {
		return discLdg3;
	}

	public void setDiscLdg3(final BigDecimal discLdg3) {
		this.discLdg3 = discLdg3;
	}

	public BigDecimal getPremDue() {
		return premDue;
	}

	public void setPremDue(final BigDecimal premDue) {
		this.premDue = premDue;
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

	public Integer getItemNo() {
		return itemNo;
	}

	public void setItemNo(final Integer itemNo) {
		this.itemNo = itemNo;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(final Integer seqNo) {
		this.seqNo = seqNo;
	}

}
