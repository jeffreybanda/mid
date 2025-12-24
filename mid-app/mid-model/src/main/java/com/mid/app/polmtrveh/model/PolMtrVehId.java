package com.mid.app.polmtrveh.model;

import java.io.Serializable;

public class PolMtrVehId implements Serializable {

	private static final long serialVersionUID = 5267619812160396044L;

	public PolMtrVehId() {

	}

	private String polNo;
	private Integer renCnt;
	private Integer endtCnt;
	private String vehRegNo;

	public PolMtrVehId(final String polNo, final Integer renCnt, final Integer endtCnt, final String vehRegNo) {

		this.polNo = polNo;
		this.renCnt = renCnt;
		this.endtCnt = endtCnt;
		this.vehRegNo = vehRegNo;
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

	public String getVehRegNo() {
		return vehRegNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endtCnt == null) ? 0 : endtCnt.hashCode());
		result = prime * result + ((polNo == null) ? 0 : polNo.hashCode());
		result = prime * result + ((renCnt == null) ? 0 : renCnt.hashCode());
		result = prime * result + ((vehRegNo == null) ? 0 : vehRegNo.hashCode());
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
		final PolMtrVehId other = (PolMtrVehId) obj;
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
		if (vehRegNo == null) {
			if (other.vehRegNo != null)
				return false;
		} else if (!vehRegNo.equals(other.vehRegNo))
			return false;
		return true;
	}

}
