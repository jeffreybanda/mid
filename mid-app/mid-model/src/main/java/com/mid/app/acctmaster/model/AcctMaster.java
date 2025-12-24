package com.mid.app.acctmaster.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "pub.AcctMaster")
@IdClass(AcctMasterId.class)
public class AcctMaster implements Serializable {

	private static final long serialVersionUID = -2623799716916778471L;

	@Id
	private String acctno;
	@Id
	private String branch;
	@Id
	private Integer acctyr;
	@Id
	private Integer acctmth;
	@Id
	private String trantype1;
	@Id
	private String docno;

	private String trantype2;

	private String polno;
	private Integer rencnt;
	private Integer endtcnt;

//	private String doctype;
	private String docName;

	private Integer instalno;
	private String stmtref1;
	private Date duedate;
	private Date trandate;
	private String bankno;
//	private String payer;

	private String progId;
	private String paytInType;
	private String payChqNo;
	private String drawerName;
	private String curr;

	private BigDecimal netamtloc;
	private BigDecimal netamt;
	private BigDecimal bal;

	public AcctMaster() {
	}

	public String getAcctno() {
		return acctno;
	}

	public void setAcctno(final String acctno) {
		this.acctno = acctno;
	}

	public String getPolno() {
		return polno;
	}

	public void setPolno(final String polno) {
		this.polno = polno;
	}

	public Integer getRencnt() {
		return rencnt;
	}

	public void setRencnt(final Integer rencnt) {
		this.rencnt = rencnt;
	}

	public Integer getEndtcnt() {
		return endtcnt;
	}

	public void setEndtcnt(final Integer endtcnt) {
		this.endtcnt = endtcnt;
	}

	public Integer getInstalno() {
		return instalno;
	}

	public void setInstalno(final Integer instalno) {
		this.instalno = instalno;
	}

	public Date getDuedate() {
		return duedate;
	}

	public void setDuedate(final Date duedate) {
		this.duedate = duedate;
	}

	public Date getTrandate() {
		return trandate;
	}

	public void setTrandate(final Date trandate) {
		this.trandate = trandate;
	}

	public String getDocno() {
		return docno;
	}

	public void setDocno(final String docno) {
		this.docno = docno;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(final String branch) {
		this.branch = branch;
	}

	public Integer getAcctyr() {
		return acctyr;
	}

	public void setAcctyr(final Integer acctyr) {
		this.acctyr = acctyr;
	}

	public Integer getInstalNo() {
		return instalno;
	}

	public void setInstalNo(final Integer instalno) {
		this.instalno = instalno;
	}

	public Integer getAcctmth() {
		return acctmth;
	}

	public void setAcctmth(final Integer acctmth) {
		this.acctmth = acctmth;
	}

	public String getStmtref1() {
		return stmtref1;
	}

	public void setDueDate(final Date duedate) {
		this.duedate = duedate;
	}

	public Date getDueDate() {
		return duedate;
	}

	public void setStmtref1(final String stmtref1) {
		this.stmtref1 = stmtref1;
	}

	public void setTranDate(final Date trandate) {
		this.trandate = trandate;
	}

	public Date getTranDate() {
		return trandate;
	}

	public String getBankno() {
		return bankno;
	}

	public void setBankno(String bankno) {
		this.bankno = bankno;
	}

	public String getTrantype1() {
		return trantype1;
	}

	public void setTrantype1(String trantype1) {
		this.trantype1 = trantype1;
	}

//	public String getDoctype() {
//		return doctype;
//	}
//
//	public void setDoctype(String doctype) {
//		this.doctype = doctype;
//	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

//	public String getPayer() {
//		return payer;
//	}
//
//	public void setPayer(String payer) {
//		this.payer = payer;
//	}

	public String getProgId() {
		return progId;
	}

	public void setProgId(String progId) {
		this.progId = progId;
	}

	public String getPaytInType() {
		return paytInType;
	}

	public void setPaytInType(String paytInType) {
		this.paytInType = paytInType;
	}

	public String getPayChqNo() {
		return payChqNo;
	}

	public void setPayChqNo(String payChqNo) {
		this.payChqNo = payChqNo;
	}

	public String getDrawerName() {
		return drawerName;
	}

	public void setDrawerName(String drawerName) {
		this.drawerName = drawerName;
	}

	public String getCurr() {
		return curr;
	}

	public void setCurr(String curr) {
		this.curr = curr;
	}

	public BigDecimal getNetamtloc() {
		return netamtloc;
	}

	public void setNetamtloc(BigDecimal netamtloc) {
		this.netamtloc = netamtloc;
	}

	public String getTrantype2() {
		return trantype2;
	}

	public void setTrantype2(String trantype2) {
		this.trantype2 = trantype2;
	}

	public BigDecimal getNetamt() {
		return netamt;
	}

	public void setNetamt(BigDecimal netamt) {
		this.netamt = netamt;
	}

	public BigDecimal getBal() {
		return bal;
	}

	public void setBal(BigDecimal bal) {
		this.bal = bal;
	}

}
