package com.mid.app.politem.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "pub.PolItem")
@IdClass(PolItemId.class)
public class PolItem implements Serializable {

	private static final long serialVersionUID = 7692351378607231338L;
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

	String txtRefNo = "";
	String uOM1 = "";
	String uOM2 = "";
	String uOM3 = "";
	String uOM4 = "";
	String uOM5 = "";
	String uOM6 = "";
	String uOM7 = "";
	BigDecimal uOM1Val = new BigDecimal(0);
	BigDecimal uOM2Val = new BigDecimal(0);
	BigDecimal uOM3Val = new BigDecimal(0);
	BigDecimal uOM4Val = new BigDecimal(0);
	BigDecimal uOM5Val = new BigDecimal(0);
	BigDecimal uOM6Val = new BigDecimal(0);
	BigDecimal uOM7Val = new BigDecimal(0);
	BigDecimal discLdg1Pct = new BigDecimal(0);
	BigDecimal discLdg2Pct = new BigDecimal(0);
	BigDecimal discLdg3Pct = new BigDecimal(0);

	public PolItem() {

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

	public Integer getItemNo() {
		return itemNo;
	}

	public void setItemNo(final Integer itemNo) {
		this.itemNo = itemNo;
	}

	public String getTxtRefNo() {
		return txtRefNo;
	}

	public void setTxtRefNo(final String txtRefNo) {
		this.txtRefNo = txtRefNo;
	}

	public String getuOM1() {
		return uOM1;
	}

	public void setuOM1(final String uOM1) {
		this.uOM1 = uOM1;
	}

	public String getuOM2() {
		return uOM2;
	}

	public void setuOM2(final String uOM2) {
		this.uOM2 = uOM2;
	}

	public String getuOM3() {
		return uOM3;
	}

	public void setuOM3(final String uOM3) {
		this.uOM3 = uOM3;
	}

	public String getuOM4() {
		return uOM4;
	}

	public void setuOM4(final String uOM4) {
		this.uOM4 = uOM4;
	}

	public String getuOM5() {
		return uOM5;
	}

	public void setuOM5(final String uOM5) {
		this.uOM5 = uOM5;
	}

	public String getuOM6() {
		return uOM6;
	}

	public void setuOM6(final String uOM6) {
		this.uOM6 = uOM6;
	}

	public String getuOM7() {
		return uOM7;
	}

	public void setuOM7(final String uOM7) {
		this.uOM7 = uOM7;
	}

	public BigDecimal getuOM1Val() {
		return uOM1Val;
	}

	public void setuOM1Val(final BigDecimal uOM1Val) {
		this.uOM1Val = uOM1Val;
	}

	public BigDecimal getuOM2Val() {
		return uOM2Val;
	}

	public void setuOM2Val(final BigDecimal uOM2Val) {
		this.uOM2Val = uOM2Val;
	}

	public BigDecimal getuOM3Val() {
		return uOM3Val;
	}

	public void setuOM3Val(final BigDecimal uOM3Val) {
		this.uOM3Val = uOM3Val;
	}

	public BigDecimal getuOM4Val() {
		return uOM4Val;
	}

	public void setuOM4Val(final BigDecimal uOM4Val) {
		this.uOM4Val = uOM4Val;
	}

	public BigDecimal getuOM5Val() {
		return uOM5Val;
	}

	public void setuOM5Val(final BigDecimal uOM5Val) {
		this.uOM5Val = uOM5Val;
	}

	public BigDecimal getuOM6Val() {
		return uOM6Val;
	}

	public void setuOM6Val(final BigDecimal uOM6Val) {
		this.uOM6Val = uOM6Val;
	}

	public BigDecimal getuOM7Val() {
		return uOM7Val;
	}

	public void setuOM7Val(final BigDecimal uOM7Val) {
		this.uOM7Val = uOM7Val;
	}

	public BigDecimal getDiscLdg1Pct() {
		return discLdg1Pct;
	}

	public void setDiscLdg1Pct(final BigDecimal discLdg1Pct) {
		this.discLdg1Pct = discLdg1Pct;
	}

	public BigDecimal getDiscLdg2Pct() {
		return discLdg2Pct;
	}

	public void setDiscLdg2Pct(final BigDecimal discLdg2Pct) {
		this.discLdg2Pct = discLdg2Pct;
	}

	public BigDecimal getDiscLdg3Pct() {
		return discLdg3Pct;
	}

	public void setDiscLdg3Pct(final BigDecimal discLdg3Pct) {
		this.discLdg3Pct = discLdg3Pct;
	}

}
