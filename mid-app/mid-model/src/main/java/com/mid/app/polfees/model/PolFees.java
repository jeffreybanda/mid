package com.mid.app.polfees.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.mid.app.polmaster.model.PolMasterId;

@Entity
@Table(name = "pub.PolMaster")
@IdClass(PolMasterId.class)
public class PolFees {
	@Id
	private String polNo = "";
	@Id
	private Integer endtCnt = new Integer(0);
	@Id
	private Integer renCnt = new Integer(0);
	private BigDecimal polFee = new BigDecimal(0);
	private BigDecimal polStamp = new BigDecimal(0);
	private BigDecimal polTax = new BigDecimal(0);

	public PolFees() {

	}

	public String getPolNo() {
		return polNo;
	}

	public void setPolNo(final String polNo) {
		this.polNo = polNo;
	}

	public Integer getEndtCnt() {
		return endtCnt;
	}

	public void setEndtCnt(final Integer endtCnt) {
		this.endtCnt = endtCnt;
	}

	public Integer getRenCnt() {
		return renCnt;
	}

	public void setRenCnt(final Integer renCnt) {
		this.renCnt = renCnt;
	}

	public BigDecimal getPolFee() {
		return polFee;
	}

	public void setPolFee(final BigDecimal polFee) {
		this.polFee = polFee;
	}

	public BigDecimal getPolStamp() {
		return polStamp;
	}

	public void setPolStamp(final BigDecimal polStamp) {
		this.polStamp = polStamp;
	}

	public BigDecimal getPolTax() {
		return polTax;
	}

	public void setPolTax(final BigDecimal polTax) {
		this.polTax = polTax;
	}

}
