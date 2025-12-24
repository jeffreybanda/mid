package com.mid.app.vehicledata.model;

import java.math.BigDecimal;

public class VehicleData {
	
private String 	registrationNumber;
private String    chassisNumber;
private String    make;
private String    model;
private Integer    manufacturingYear;
private Integer    registrationYear;
private String    vehicleColour;
private String   bodyType;
private Integer    seatingCapacity;
private Integer    cubicCapacity;
private String    vehicleUsage;
private String    fuelType;
private BigDecimal    vehicleValue;
private boolean    verified;


public VehicleData() {
	
}


public String getRegistrationNumber() {
	return registrationNumber;
}


public void setRegistrationNumber(String registrationNumber) {
	this.registrationNumber = registrationNumber;
}


public String getChassisNumber() {
	return chassisNumber;
}


public void setChassisNumber(String chassisNumber) {
	this.chassisNumber = chassisNumber;
}


public String getMake() {
	return make;
}


public void setMake(String make) {
	this.make = make;
}


public String getModel() {
	return model;
}


public void setModel(String model) {
	this.model = model;
}


public Integer getManufacturingYear() {
	return manufacturingYear;
}


public void setManufacturingYear(Integer manufacturingYear) {
	this.manufacturingYear = manufacturingYear;
}


public Integer getRegistrationYear() {
	return registrationYear;
}


public void setRegistrationYear(Integer registrationYear) {
	this.registrationYear = registrationYear;
}


public String getVehicleColour() {
	return vehicleColour;
}


public void setVehicleColour(String vehicleColour) {
	this.vehicleColour = vehicleColour;
}


public String getBodyType() {
	return bodyType;
}


public void setBodyType(String bodyType) {
	this.bodyType = bodyType;
}


public Integer getSeatingCapacity() {
	return seatingCapacity;
}


public void setSeatingCapacity(Integer seatingCapacity) {
	this.seatingCapacity = seatingCapacity;
}


public Integer getCubicCapacity() {
	return cubicCapacity;
}


public void setCubicCapacity(Integer cubicCapacity) {
	this.cubicCapacity = cubicCapacity;
}


public String getVehicleUsage() {
	return vehicleUsage;
}


public void setVehicleUsage(String vehicleUsage) {
	this.vehicleUsage = vehicleUsage;
}


public String getFuelType() {
	return fuelType;
}


public void setFuelType(String fuelType) {
	this.fuelType = fuelType;
}


public BigDecimal getVehicleValue() {
	return vehicleValue;
}


public void setVehicleValue(BigDecimal vehicleValue) {
	this.vehicleValue = vehicleValue;
}


public boolean isVerified() {
	return verified;
}


public void setVerified(boolean verified) {
	this.verified = verified;
}


}
