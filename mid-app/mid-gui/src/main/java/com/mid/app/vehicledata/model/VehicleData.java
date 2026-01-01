package com.mid.app.vehicledata.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VehicleData {

	@JsonProperty("registrationNumber")
	private String registrationNumber;

	@JsonProperty("chassisNumber")
	private String chassisNumber;

	@JsonProperty("make")
	private String make;

	@JsonProperty("model")
	private String model;

	@JsonProperty("manufacturingYear")
	private Integer manufacturingYear;

	@JsonProperty("registrationYear")
	private Integer registrationYear;

	@JsonProperty("vehicleColour")
	private String vehicleColour;

	@JsonProperty("bodyType")
	private String bodyType;

	@JsonProperty("seatingCapacity")
	private Integer seatingCapacity;

	@JsonProperty("cubicCapacity")
	private Integer cubicCapacity;

	@JsonProperty("vehicleUsage")
	private String vehicleUsage;

	@JsonProperty("fuelType")
	private String fuelType;

	@JsonProperty("vehicleValue")
	private BigDecimal vehicleValue;

	@JsonProperty("verified")
	private Boolean verified;

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
