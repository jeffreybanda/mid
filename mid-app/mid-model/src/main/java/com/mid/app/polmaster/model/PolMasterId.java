package com.mid.app.polmaster.model;

import java.io.Serializable;

public class PolMasterId implements Serializable {

	private static final long serialVersionUID = -2615905038776322530L;

	private String polNo;

	private Integer renCnt;

	private Integer endtCnt;

	public PolMasterId() {

	}

	public PolMasterId(final String polNo, final Integer renCnt, final Integer endtCnt) {

		this.polNo = polNo;
		this.renCnt = renCnt;
		this.endtCnt = endtCnt;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endtCnt == null) ? 0 : endtCnt.hashCode());
		result = prime * result + ((polNo == null) ? 0 : polNo.hashCode());
		result = prime * result + ((renCnt == null) ? 0 : renCnt.hashCode());
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
		final PolMasterId other = (PolMasterId) obj;
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
		return true;
	}

}
