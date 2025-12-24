package com.mid.app.customerdata.model;

import java.util.Date;

public class CustomerData {
	
	private boolean isActive;
    private String type;
    private String title;
    private String firstName;
    private String lastName;
    private String gender;
    private Date dateOfBirth;
    private String nationality; 
    private String ghanaCardNumber;
    private String email;
    private String phoneNumber;
    private String digitalAddress;
    private String residentialAddress;
    private String occupation;
    
    
    public CustomerData() {
    	
    }


	public boolean isActive() {
		return isActive;
	}


	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public Date getDateOfBirth() {
		return dateOfBirth;
	}


	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}


	public String getNationality() {
		return nationality;
	}


	public void setNationality(String nationality) {
		this.nationality = nationality;
	}


	public String getGhanaCardNumber() {
		return ghanaCardNumber;
	}


	public void setGhanaCardNumber(String ghanaCardNumber) {
		this.ghanaCardNumber = ghanaCardNumber;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getDigitalAddress() {
		return digitalAddress;
	}


	public void setDigitalAddress(String digitalAddress) {
		this.digitalAddress = digitalAddress;
	}


	public String getResidentialAddress() {
		return residentialAddress;
	}


	public void setResidentialAddress(String residentialAddress) {
		this.residentialAddress = residentialAddress;
	}


	public String getOccupation() {
		return occupation;
	}


	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
    
    

}
