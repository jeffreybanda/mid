package com.mid.app.customerdata.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerData {
	
	 @JsonProperty("isActive")
     private Boolean isActive;
     
     @JsonProperty("type")
     private String type;
     
     @JsonProperty("title")
     private String title;
     
     @JsonProperty("firstName")
     private String firstName;
     
     @JsonProperty("lastName")
     private String lastName;
     
     @JsonProperty("gender")
     private String gender;
     
     @JsonProperty("dateOfBirth")
     private String dateOfBirth;
     
     @JsonProperty("nationality")
     private String nationality;
     
     @JsonProperty("ghanaCardNumber")
     private String ghanaCardNumber;
     
     @JsonProperty("email")
     private String email;
     
     @JsonProperty("phoneNumber")
     private String phoneNumber;
     
     @JsonProperty("digitalAddress")
     private String digitalAddress;
     
     @JsonProperty("residentialAddress")
     private String residentialAddress;
     
     @JsonProperty("occupation")
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


	

	public Boolean getIsActive() {
		return isActive;
	}


	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}


	public String getDateOfBirth() {
		return dateOfBirth;
	}


	public void setDateOfBirth(String dateOfBirth) {
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
