package com.mid.app.polmaster.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "pub.PolMaster")
@IdClass(PolMasterId.class)
public class PolMaster implements Serializable {

	private static final long serialVersionUID = 8645064061771955171L;

	@Id
	private String polNo;
	@Id
	private Integer renCnt;
	@Id
	private Integer endtCnt;

	private BigDecimal billCurrRate;

	private Date comDate;

	private Date expiryDate;

	private Date tranDate;

	private BigDecimal coinsPct;

	private Boolean tranRel;

	private String insured;

	private String firstName;

	private String insdName1;

	private String insdName2;

	private String insdName3;

	private String insdAddr1;

	private String insdAddr2;

	private String insdAddr3;

	private String insdAddr4;

	private String branch;

	private String agent;

	private String billCurr;

	private String department;

	private String polStat;

	private String acct1TranType1;

	private String acct1DocNo;

	private String acctNo1;

	private BigDecimal polStamp = new BigDecimal(0.0D);

	private BigDecimal polFee = new BigDecimal(0.0D);

	private BigDecimal polTax = new BigDecimal(0.0D);
	
	private String titleName;
	
	private String occupation ;

	public PolMaster() {

	}

	public String getPolNo() {
		return polNo;
	}

	

	
	public PolMaster(String polNo, Integer renCnt, Integer endtCnt, BigDecimal billCurrRate, Date comDate,
			Date expiryDate, Date tranDate, BigDecimal coinsPct, Boolean tranRel, String insured, String firstName,
			String insdName1, String insdName2, String insdName3, String insdAddr1, String insdAddr2, String insdAddr3,
			String insdAddr4, String branch, String agent, String billCurr, String department, String polStat,
			String acct1TranType1, String acct1DocNo, String acctNo1, BigDecimal polStamp, BigDecimal polFee,
			BigDecimal polTax, String titleName, String occupation) {
		super();
		this.polNo = polNo;
		this.renCnt = renCnt;
		this.endtCnt = endtCnt;
		this.billCurrRate = billCurrRate;
		this.comDate = comDate;
		this.expiryDate = expiryDate;
		this.tranDate = tranDate;
		this.coinsPct = coinsPct;
		this.tranRel = tranRel;
		this.insured = insured;
		this.firstName = firstName;
		this.insdName1 = insdName1;
		this.insdName2 = insdName2;
		this.insdName3 = insdName3;
		this.insdAddr1 = insdAddr1;
		this.insdAddr2 = insdAddr2;
		this.insdAddr3 = insdAddr3;
		this.insdAddr4 = insdAddr4;
		this.branch = branch;
		this.agent = agent;
		this.billCurr = billCurr;
		this.department = department;
		this.polStat = polStat;
		this.acct1TranType1 = acct1TranType1;
		this.acct1DocNo = acct1DocNo;
		this.acctNo1 = acctNo1;
		this.polStamp = polStamp;
		this.polFee = polFee;
		this.polTax = polTax;
		this.titleName = titleName;
		this.occupation = occupation;
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

	public BigDecimal getBillCurrRate() {
		return billCurrRate;
	}

	public void setBillCurrRate(final BigDecimal billCurrRate) {
		this.billCurrRate = billCurrRate;
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

	public BigDecimal getCoinsPct() {
		return coinsPct;
	}

	public void setCoinsPct(final BigDecimal coinsPct) {
		this.coinsPct = coinsPct;
	}

	public Boolean getTranRel() {
		return tranRel;
	}

	public void setTranRel(final Boolean tranRel) {
		this.tranRel = tranRel;
	}

	public String getInsured() {
		return insured;
	}

	public void setInsured(final String insured) {
		this.insured = insured;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getInsdName1() {
		return insdName1;
	}

	public void setInsdName1(final String insdName1) {
		this.insdName1 = insdName1;
	}

	public String getInsdName2() {
		return insdName2;
	}

	public void setInsdName2(final String insdName2) {
		this.insdName2 = insdName2;
	}

	public String getInsdName3() {
		return insdName3;
	}

	public void setInsdName3(final String insdName3) {
		this.insdName3 = insdName3;
	}

	public String getInsdAddr1() {
		return insdAddr1;
	}

	public void setInsdAddr1(final String insdAddr1) {
		this.insdAddr1 = insdAddr1;
	}

	public String getInsdAddr2() {
		return insdAddr2;
	}

	public void setInsdAddr2(final String insdAddr2) {
		this.insdAddr2 = insdAddr2;
	}

	public String getInsdAddr3() {
		return insdAddr3;
	}

	public void setInsdAddr3(final String insdAddr3) {
		this.insdAddr3 = insdAddr3;
	}

	public String getInsdAddr4() {
		return insdAddr4;
	}

	public void setInsdAddr4(final String insdAddr4) {
		this.insdAddr4 = insdAddr4;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(final String branch) {
		this.branch = branch;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(final String agent) {
		this.agent = agent;
	}

	public String getBillCurr() {
		return billCurr;
	}

	public void setBillCurr(final String billCurr) {
		this.billCurr = billCurr;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(final String department) {
		this.department = department;
	}

	public Date getTranDate() {
		return tranDate;
	}

	public void setTranDate(final Date tranDate) {
		this.tranDate = tranDate;
	}

	public String getPolStat() {
		return polStat;
	}

	public void setPolStat(final String polStat) {
		this.polStat = polStat;
	}

	public BigDecimal getPolStamp() {
		return polStamp;
	}

	public void setPolStamp(final BigDecimal polStamp) {
		this.polStamp = polStamp;
	}

	public BigDecimal getPolFee() {
		return polFee;
	}

	public void setPolFee(final BigDecimal polFee) {
		this.polFee = polFee;
	}

	public BigDecimal getPolTax() {
		return polTax;
	}

	public void setPolTax(final BigDecimal polTax) {
		this.polTax = polTax;
	}

	public String getAcct1TranType1() {
		return acct1TranType1;
	}

	public void setAcct1TranType1(String acct1TranType1) {
		this.acct1TranType1 = acct1TranType1;
	}

	public String getAcct1DocNo() {
		return acct1DocNo;
	}

	public void setAcct1DocNo(String acct1DocNo) {
		this.acct1DocNo = acct1DocNo;
	}

	public String getAcctNo1() {
		return acctNo1;
	}

	public void setAcctNo1(String acctNo1) {
		this.acctNo1 = acctNo1;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	
	

}
