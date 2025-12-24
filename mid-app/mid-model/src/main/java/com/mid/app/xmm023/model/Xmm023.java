package com.mid.app.xmm023.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pub.Xmm023")
public class Xmm023 {

	@Id
	String branch;

	String branchDesc;

	public Xmm023() {

	}

	public Xmm023(final String branch, final String branchDesc) {

		this.branch = branch;
		this.branchDesc = branchDesc;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(final String branch) {
		this.branch = branch;
	}

	public String getBranchDesc() {
		return branchDesc;
	}

	public void setBranchDesc(final String branchDesc) {
		this.branchDesc = branchDesc;
	}

}
