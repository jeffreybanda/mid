package com.mid.app.acctmaster.model;

import java.io.Serializable;

public class AcctMasterId implements Serializable {

	private static final long serialVersionUID = -7925879299666661161L;

	private String acctno;
	private String branch;
	private Integer acctyr;
	private Integer acctmth;
	private String trantype1;
	private String docno;

	public AcctMasterId() {
		// TODO Auto-generated constructor stub
	}

	public AcctMasterId(String acctno, String branch, Integer acctyr, Integer acctmth, String trantype1, String docno) {
		super();
		this.acctno = acctno;
		this.branch = branch;
		this.acctyr = acctyr;
		this.acctmth = acctmth;
		this.trantype1 = trantype1;
		this.docno = docno;
	}

	public String getAcctno() {
		return acctno;
	}

	public void setAcctno(String acctno) {
		this.acctno = acctno;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public Integer getAcctyr() {
		return acctyr;
	}

	public void setAcctyr(Integer acctyr) {
		this.acctyr = acctyr;
	}

	public Integer getAcctmth() {
		return acctmth;
	}

	public void setAcctmth(Integer acctmth) {
		this.acctmth = acctmth;
	}

	public String getTrantype1() {
		return trantype1;
	}

	public void setTrantype1(String trantype1) {
		this.trantype1 = trantype1;
	}

	public String getDocno() {
		return docno;
	}

	public void setDocno(String docno) {
		this.docno = docno;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acctmth == null) ? 0 : acctmth.hashCode());
		result = prime * result + ((acctno == null) ? 0 : acctno.hashCode());
		result = prime * result + ((acctyr == null) ? 0 : acctyr.hashCode());
		result = prime * result + ((branch == null) ? 0 : branch.hashCode());
		result = prime * result + ((docno == null) ? 0 : docno.hashCode());
		result = prime * result + ((trantype1 == null) ? 0 : trantype1.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AcctMasterId other = (AcctMasterId) obj;
		if (acctmth == null) {
			if (other.acctmth != null)
				return false;
		} else if (!acctmth.equals(other.acctmth))
			return false;
		if (acctno == null) {
			if (other.acctno != null)
				return false;
		} else if (!acctno.equals(other.acctno))
			return false;
		if (acctyr == null) {
			if (other.acctyr != null)
				return false;
		} else if (!acctyr.equals(other.acctyr))
			return false;
		if (branch == null) {
			if (other.branch != null)
				return false;
		} else if (!branch.equals(other.branch))
			return false;
		if (docno == null) {
			if (other.docno != null)
				return false;
		} else if (!docno.equals(other.docno))
			return false;
		if (trantype1 == null) {
			if (other.trantype1 != null)
				return false;
		} else if (!trantype1.equals(other.trantype1))
			return false;
		return true;
	}

}
