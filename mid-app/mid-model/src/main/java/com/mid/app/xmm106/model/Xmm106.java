package com.mid.app.xmm106.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pub.Xmm106")
public class Xmm106 implements Serializable {

	private static final long serialVersionUID = -7459492714078146033L;

	@Id
	String tariff;
	String benCod;
	@Column(name = "class")
	String businessClass;
	String cvrType;
	Date effDate;
	BigDecimal pctIncBaseAP = new BigDecimal(0.0D);

	public Xmm106() {
	}

	public Xmm106(final String tariff, final String benCod, final String businessClass, final String cvrType,
			final Date effDate,
			final BigDecimal pctIncBaseAP) {

		this.tariff = tariff;
		this.benCod = benCod;
		this.businessClass = businessClass;
		this.cvrType = cvrType;
		this.effDate = effDate;
		this.pctIncBaseAP = pctIncBaseAP;
	}

	public String getTariff() {
		return tariff;
	}

	public void setTariff(final String tariff) {
		this.tariff = tariff;
	}

	public String getBenCod() {
		return benCod;
	}

	public void setBenCod(final String benCod) {
		this.benCod = benCod;
	}

	public String getBusinessClass() {
		return businessClass;
	}

	public void setBusinessClass(final String businessClass) {
		this.businessClass = businessClass;
	}

	public String getCvrType() {
		return cvrType;
	}

	public void setCvrType(final String cvrType) {
		this.cvrType = cvrType;
	}

	public Date getEffDate() {
		return effDate;
	}

	public void setEffDate(final Date effDate) {
		this.effDate = effDate;
	}

	public BigDecimal getPctIncBaseAP() {
		return pctIncBaseAP;
	}

	public void setPctIncBaseAP(final BigDecimal pctIncBaseAP) {
		this.pctIncBaseAP = pctIncBaseAP;
	}

}
