package com.mid.app.xmm600.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pub.Xmm600")
public class Xmm600 implements Serializable {

	private static final long serialVersionUID = 7919306161422100854L;

	@Id
	private String clientNo;

	private String clientType;

	private String firstName;

	private String name1;

	private String name2;

	private String name3;

	private Date birthday;

	private String icno;

	private String email;

	private String telNo1;

	private String addr1;

	private String addr2;

	private String addr3;

	private String addr4;

	private String homeBrn;

	private String telno1;

	private String telno2;

	private String telno3;

	private String telno4;

	private String telno5;

	private String telno6;

	private String telno7;

	private String titleName;

	private String gender;

	public Xmm600() {

	}

	public Xmm600(String clientNo, String clientType, String firstName, String name1, String name2, String name3,
			Date birthday, String icno, String email, String telNo1, String addr1, String addr2, String addr3,
			String addr4, String homeBrn, String telno12, String telno2, String telno3, String telno4, String telno5,
			String telno6, String telno7, String titleName, String gender) {
		super();
		this.clientNo = clientNo;
		this.clientType = clientType;
		this.firstName = firstName;
		this.name1 = name1;
		this.name2 = name2;
		this.name3 = name3;
		this.birthday = birthday;
		this.icno = icno;
		this.email = email;
		this.telNo1 = telNo1;
		this.addr1 = addr1;
		this.addr2 = addr2;
		this.addr3 = addr3;
		this.addr4 = addr4;
		this.homeBrn = homeBrn;
		telno1 = telno12;
		this.telno2 = telno2;
		this.telno3 = telno3;
		this.telno4 = telno4;
		this.telno5 = telno5;
		this.telno6 = telno6;
		this.telno7 = telno7;
		this.titleName = titleName;
		this.gender = gender;
	}

	public String getClientNo() {
		return clientNo;
	}

	public void setClientNo(final String clientNo) {
		this.clientNo = clientNo;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getName1() {
		return name1;
	}

	public void setName1(final String name1) {
		this.name1 = name1;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(final String name2) {
		this.name2 = name2;
	}

	public String getName3() {
		return name3;
	}

	public void setName3(final String name3) {
		this.name3 = name3;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getIcno() {
		return icno;
	}

	public void setIcno(String icno) {
		this.icno = icno;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getTelNo1() {
		return telNo1;
	}

	public void setTelNo1(final String telNo1) {
		this.telNo1 = telNo1;
	}

	public String getAddr1() {
		return addr1;
	}

	public void setAddr1(final String addr1) {
		this.addr1 = addr1;
	}

	public String getAddr2() {
		return addr2;
	}

	public void setAddr2(final String addr2) {
		this.addr2 = addr2;
	}

	public String getAddr3() {
		return addr3;
	}

	public void setAddr3(final String addr3) {
		this.addr3 = addr3;
	}

	public String getAddr4() {
		return addr4;
	}

	public void setAddr4(final String addr4) {
		this.addr4 = addr4;
	}

	public String getHomeBrn() {
		return homeBrn;
	}

	public void setHomeBrn(final String homeBrn) {
		this.homeBrn = homeBrn;
	}

	public String getTelno1() {
		return telno1;
	}

	public void setTelno1(final String telno1) {
		this.telno1 = telno1;
	}

	public String getTelno2() {
		return telno2;
	}

	public void setTelno2(final String telno2) {
		this.telno2 = telno2;
	}

	public String getTelno3() {
		return telno3;
	}

	public void setTelno3(final String telno3) {
		this.telno3 = telno3;
	}

	public String getTelno4() {
		return telno4;
	}

	public void setTelno4(final String telno4) {
		this.telno4 = telno4;
	}

	public String getTelno5() {
		return telno5;
	}

	public void setTelno5(final String telno5) {
		this.telno5 = telno5;
	}

	public String getTelno6() {
		return telno6;
	}

	public void setTelno6(final String telno6) {
		this.telno6 = telno6;
	}

	public String getTelno7() {
		return telno7;
	}

	public void setTelno7(final String telno7) {
		this.telno7 = telno7;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(final String clientType) {
		this.clientType = clientType;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
