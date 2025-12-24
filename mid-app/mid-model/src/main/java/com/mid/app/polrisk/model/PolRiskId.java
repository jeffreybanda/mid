package com.mid.app.polrisk.model;

import java.io.Serializable;

public class PolRiskId implements Serializable {

	private static final long serialVersionUID = -8044532295148377342L;

	private String polNo;

	private Integer renCnt;

	private Integer endtCnt;

	private Integer riskGrp;

	private Integer riskNo;

	public PolRiskId() {

	}

	public PolRiskId(final String polNo, final Integer renCnt, final Integer endtCnt, final Integer riskGrp,
			final Integer riskNo) {

		this.polNo = polNo;
		this.renCnt = renCnt;
		this.endtCnt = endtCnt;
		this.riskGrp = riskGrp;
		this.riskNo = riskNo;
	}

	public String getPolNo() {
		return polNo;
	}

	public Integer getRenCnt() {
		return renCnt;
	}

	public Integer getEndtCnt() {
		return endtCnt;
	}

	public Integer getRiskGrp() {
		return riskGrp;
	}

	public Integer getRiskNo() {
		return riskNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endtCnt == null) ? 0 : endtCnt.hashCode());
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
		final PolRiskId other = (PolRiskId) obj;
		if (endtCnt == null) {
			if (other.endtCnt != null)
				return false;
		} else if (!endtCnt.equals(other.endtCnt))
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
