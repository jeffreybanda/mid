package com.mid.app.politem.model;

import java.io.Serializable;

public class PolItemId implements Serializable {

	private static final long serialVersionUID = -1019773731821842247L;
	String polNo = "";
	Integer renCnt = new Integer(0);
	Integer endtCnt = new Integer(0);
	Integer riskGrp = new Integer(0);
	Integer riskNo = new Integer(0);
	Integer itemNo = new Integer(0);

	public PolItemId() {
	}

	public PolItemId(final String polNo, final Integer renCnt, final Integer endtCnt, final Integer riskGrp,
			final Integer riskNo, final Integer itemNo) {

		this.polNo = polNo;
		this.renCnt = renCnt;
		this.endtCnt = endtCnt;
		this.riskGrp = riskGrp;
		this.riskNo = riskNo;
		this.itemNo = itemNo;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endtCnt == null) ? 0 : endtCnt.hashCode());
		result = prime * result + ((itemNo == null) ? 0 : itemNo.hashCode());
		result = prime * result + ((polNo == null) ? 0 : polNo.hashCode());
		result = prime * result + ((renCnt == null) ? 0 : renCnt.hashCode());
		result = prime * result + ((riskGrp == null) ? 0 : riskGrp.hashCode());
		result = prime * result + ((riskNo == null) ? 0 : riskNo.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PolItemId other = (PolItemId) obj;
		if (endtCnt == null) {
			if (other.endtCnt != null)
				return false;
		} else if (!endtCnt.equals(other.endtCnt))
			return false;
		if (itemNo == null) {
			if (other.itemNo != null)
				return false;
		} else if (!itemNo.equals(other.itemNo))
			return false;
		if (polNo == null) {
			if (other.polNo != null)
				return false;
		} else if (!polNo.equals(other.polNo))
			return false;
		if (renCnt == null) {
			if (other.renCnt != null)
				return false;
		} else if (!renCnt.equals(other.renCnt))
			return false;
		if (riskGrp == null) {
			if (other.riskGrp != null)
				return false;
		} else if (!riskGrp.equals(other.riskGrp))
			return false;
		if (riskNo == null) {
			if (other.riskNo != null)
				return false;
		} else if (!riskNo.equals(other.riskNo))
			return false;
		return true;
	}

}
