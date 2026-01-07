package com.mid.app.swing.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.mid.app.politem.model.PolItem;
import com.mid.app.politemben.model.PolItemBen;
import com.mid.app.polmaster.model.PolMaster;
import com.mid.app.polmtrveh.model.PolMtrVeh;
import com.mid.app.polrisk.model.PolRisk;
import com.mid.app.xmm600.model.Xmm600;
import com.mid.app.xmm600.repository.Xmm600Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class MotorPolicyJsonBuilder {

	private static final DateTimeFormatter MID_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter INPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

	private final Properties scheduleProperties;
	private Gson gson;

	public MotorPolicyJsonBuilder() {
		this.gson = new Gson();
		this.scheduleProperties = loadScheduleProperties();
	}

	private Properties loadScheduleProperties() {
		Properties props = new Properties();
		try (InputStream is = getClass().getResourceAsStream("/milleniumLiveConfig.properties")) {
			if (is != null) {
				props.load(is);
			}
		} catch (IOException e) {
			System.err.println("Failed to load schedule properties: " + e.getMessage());
		}
		return props;
	}

	public String buildPolicyJson(PolMaster polMaster, PolRisk polRisk, PolMtrVeh polMtrVeh, PolItem polItem,
			List<PolItemBen> itemBens) {

		JsonObject root = new JsonObject();
		JsonObject data = new JsonObject();

		System.out.println("Basic policy data");
		// Basic policy data
		data.addProperty("branchCode", polMaster.getBranch());

		data.addProperty("intermediaryCode", polMaster.getAcctNo1());
		// data.add("intermediaryCode", null);

		addPropertyIfNotEmpty(data, "subIntermediaryCode", null);
		data.addProperty("currencyCode", polMaster.getBillCurr());
		data.addProperty("isActive", true);
		data.addProperty("isFleet", false);
		data.addProperty("fleetSize", 1);
		data.addProperty("fleetDiscount", 0.0);

		BigDecimal grossPremium = polRisk.getTotGap().divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP);

		data.addProperty("grossPremium", grossPremium.add(getStickerFee()));

		addPropertyIfNotEmpty(data, "notes", null);
		// data.add("notes", null);

		data.addProperty("startDate", convertToMIDDate(polMaster.getComDate().toString()));
		data.addProperty("expiryDate", convertToMIDDate(polMaster.getExpiryDate().toString()));

		data.addProperty("transactionDate", convertToMIDDate(polMaster.getTranDate().toString()));
		data.addProperty("paymentDate", convertToMIDDate(polMaster.getTranDate().toString()));

		addPropertyIfNotEmpty(data, "paymentMode", null);
		// data.add("paymentMode", null);
		addPropertyIfNotEmpty(data, "specialTerms", null);
		// data.add("specialTerms", null);
		data.addProperty("umbrellaLimit", BigDecimal.ZERO);

		data.addProperty("companyAssignedPolicyNumber", polMaster.getPolNo());

		System.out.println(" Finished Basic policy data");

		// Customer data
		Xmm600 client = null;
		EntityManagerFactory emf = null;
		EntityManager em = null;

		try {
			emf = Persistence.createEntityManagerFactory("midPU");
			em = emf.createEntityManager();

			Xmm600Repository xmm600Repo = new Xmm600Repository();
			xmm600Repo.em = em;
			Optional<Xmm600> optClient = xmm600Repo.findClient(polMaster.getInsured());
			client = optClient.orElse(null);

		} catch (Exception e) {
			System.err.println("Error fetching client data: " + e.getMessage());
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
			if (emf != null && emf.isOpen()) {
				emf.close();
			}
		}
		System.out.println("  Basic customer data");
		JsonObject customerData = createCustomerData(polMaster, client);
		data.add("customerData", customerData);
		System.out.println(" Finished customer data");
		// Policy data
//        JsonObject policyData = createPolicyData(polMaster, polRisk, polItem);
//        data.add("policyData", policyData);
//        

		System.out.println(" Create Vehicles");
		JsonArray vehicles = createVehicles(polMaster, polRisk, polItem, polMtrVeh, itemBens);
		data.add("vehicles", vehicles);
		System.out.println(" Finished Vehicles");

		System.out.println(" Finished Basic policy data");

		root.add("data", data);

		System.out.println("root" + root);
		gson = new GsonBuilder().serializeNulls().create();

		return gson.toJson(root);
	}

	private JsonObject createVehicleData(PolMtrVeh polMtrVeh, PolItem polItem) {
		JsonObject vehicleData = new JsonObject();

		vehicleData.addProperty("registrationNumber", polMtrVeh.getVehRegNo());
		vehicleData.addProperty("chassisNumber", polMtrVeh.getChassisNo());
		vehicleData.addProperty("make", polMtrVeh.getVehMake());
		vehicleData.addProperty("model", polMtrVeh.getModelDesc());

		try {
			vehicleData.addProperty("manufacturingYear", Integer.parseInt(polMtrVeh.getYrManu()));
		} catch (NumberFormatException e) {
			vehicleData.addProperty("manufacturingYear", 0);
		}

		vehicleData.addProperty("registrationYear", polMtrVeh.getRegYr());
		vehicleData.addProperty("vehicleColour", polMtrVeh.getColour());
		vehicleData.addProperty("bodyType", polMtrVeh.getVehBody());
		vehicleData.addProperty("seatingCapacity", polMtrVeh.getNoSeats());
		vehicleData.addProperty("cubicCapacity", polMtrVeh.getEngineCC());
		vehicleData.addProperty("vehicleUsage", polMtrVeh.getVehUsg());

		addPropertyIfNotEmpty(vehicleData, "fuelType", null);
		// vehicleData.addProperty("fuelType", "NA");
		vehicleData.addProperty("vehicleValue", polItem.getuOM1Val());
		vehicleData.addProperty("verified", true);

		return vehicleData;
	}

	private JsonObject createCustomerData(PolMaster polMaster, Xmm600 xmm600) {
		JsonObject customerData = new JsonObject();

		customerData.addProperty("isActive", true);

		String gender = getGender(xmm600);

		String emailValue = xmm600 != null && xmm600.getEmail() != null && !xmm600.getEmail().isEmpty()
				? xmm600.getEmail()
				: null;

		if (gender == null || gender.isEmpty()) {
			customerData.addProperty("type", "CORPORATE");
			customerData.addProperty("companyName", polMaster.getInsdName1());
			customerData.addProperty("tinNumber", xmm600.getIcno());
			customerData.addProperty("registeredAddress", xmm600.getAddr1());
			// customerData.addProperty "businessSector": null,
			addPropertyIfNotEmpty(customerData, "businessSector", null);
			customerData.addProperty("companySize", 5);
			customerData.addProperty("yearEstablished", 1988);
			customerData.addProperty("contactPerson", xmm600.getName1());
			addPropertyIfNotEmpty(customerData, "contactPersonPhone", xmm600 != null ? xmm600.getTelno7() : null);
			customerData.addProperty("contactPersonEmail", emailValue);
		} else {
			customerData.addProperty("type", "INDIVIDUAL");
			addPropertyIfNotEmpty(customerData, "title", xmm600 != null ? xmm600.getTitleName() : null);
			customerData.addProperty("firstName", resolveFirstName(polMaster));
			addPropertyIfNotEmpty(customerData, "otherNames", null);
			// customerData.add("otherNames", null);
			customerData.addProperty("lastName", polMaster.getInsdName1());
			customerData.addProperty("gender", getGender(xmm600));
			customerData.addProperty("dateOfBirth",
					convertToMIDDate(xmm600 != null ? xmm600.getBirthday().toString() : null));
			customerData.addProperty("nationality", "GH");
			customerData.addProperty("ghanaCardNumber", xmm600.getIcno());

			addPropertyIfNotEmpty(customerData, "levelOfEducation", null);
			// customerData.add("levelOfEducation", null);
			addPropertyIfNotEmpty(customerData, "maritalStatus", null);
			// customerData.add("maritalStatus", null);
		}

		// customerData.addProperty("type", "INDIVIDUAL");
		// customerData.addProperty("title", getTitle(xmm600));

		if (emailValue != null) {
			customerData.addProperty("email", emailValue);
		} else {
			customerData.add("email", JsonNull.INSTANCE);
		}

//        customerData.addProperty("email", xmm600 != null && xmm600.getEmail() != null && !xmm600.getEmail().isEmpty()? 
//            xmm600.getEmail() : null);
		addPropertyIfNotEmpty(customerData, "phoneNumber", xmm600 != null ? xmm600.getTelno7() : null);

		addPropertyIfNotEmpty(customerData, "digitalAddress", null);

		addPropertyIfNotEmpty(customerData, "residentialAddress", polMaster != null ? polMaster.getInsdAddr1() : null);
		if (polMaster != null && polMaster.getOccupation() != null && !polMaster.getOccupation().trim().isEmpty()) {

			customerData.addProperty("occupation", polMaster.getOccupation().trim());
		} else {
			customerData.add("occupation", JsonNull.INSTANCE);
		}

		return customerData;
	}

	private JsonObject createPolicyData(PolMaster polMaster, PolRisk polRisk, PolItem polItem) {
		JsonObject policyData = new JsonObject();

		policyData.addProperty("startDate", convertToMIDDate(polMaster.getComDate().toString()));
		policyData.addProperty("expiryDate", convertToMIDDate(polMaster.getExpiryDate().toString()));

		BigDecimal sumInsured = polItem.getuOM1Val().divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP);
		policyData.addProperty("sumInsured", sumInsured);

		BigDecimal grossPremium = polRisk.getTotGap().divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP);

		policyData.addProperty("grossPremium", grossPremium.add(getStickerFee()));
		policyData.addProperty("type", "NEW_BUSINESS");
		policyData.addProperty("transactionDate", convertToMIDDate(polMaster.getTranDate().toString()));
		policyData.addProperty("excessBought", false);
		policyData.addProperty("noClaimDiscount", 0);
		policyData.addProperty("earnedNoClaimDiscount", 0);
		policyData.addProperty("calculationType", "FULL_YEAR");

		return policyData;
	}

	private JsonArray createVehicles(PolMaster polMaster, PolRisk polRisk, PolItem polItem, PolMtrVeh polMtrVeh,
			List<PolItemBen> itemBens) {

		JsonArray vehicles = new JsonArray();
		JsonObject vehicle = new JsonObject();

		// --- Vehicle details ---
		vehicle.addProperty("registrationNumber", polMtrVeh.getVehRegNo());
		vehicle.addProperty("make", polMtrVeh.getVehMake());
		vehicle.addProperty("model", polMtrVeh.getModelDesc());

		try {
			vehicle.addProperty("manufacturingYear", Integer.parseInt(polMtrVeh.getYrManu()));
		} catch (NumberFormatException e) {
			vehicle.addProperty("manufacturingYear", 0);
		}
		vehicle.addProperty("registrationYear", polMtrVeh.getRegYr());
		vehicle.addProperty("chassisNumber", polMtrVeh.getChassisNo());
		vehicle.addProperty("vehicleColour", polMtrVeh.getColour());
		vehicle.addProperty("bodyType", polMtrVeh.getVehBody());
		addPropertyIfNotEmpty(vehicle, "fuelType", null);
		vehicle.addProperty("vehicleUsage", polMtrVeh.getVehUsg());
		vehicle.addProperty("seatingCapacity", polMtrVeh.getNoSeats());
		addPropertyIfNotEmpty(vehicle, "grossWeight", null);
		// vehicle.add("grossWeight", null);
		addPropertyIfNotEmpty(vehicle, "vehicleMileage", null);
		// vehicle.add("vehicleMileage", null);
		vehicle.addProperty("vehicleValue", polItem.getuOM1Val());
		vehicle.addProperty("cubicCapacity", polMtrVeh.getEngineCC());
		vehicle.addProperty("partsAvailability", false);
		vehicle.addProperty("verified", false);

		// --- Product / risk ---
		String productCode = getCoverType(polMtrVeh);
		String scheduleCode = getScheduleCode(polMtrVeh.getCertRef());

		// Basic policy data

		vehicle.addProperty("productCode", productCode);
		vehicle.addProperty("riskTypeCode", scheduleCode);
		vehicle.addProperty("type", "NEW_BUSINESS");

		// --- Financials (moved from policyData) ---
		BigDecimal sumInsured = polItem.getuOM1Val().divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP);
		vehicle.addProperty("sumInsured", sumInsured);

		BigDecimal grossPremium = polRisk.getTotGap().divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP)
				.add(getStickerFee());
		vehicle.addProperty("grossPremium", grossPremium);

		// --- Discounts / limits ---
		vehicle.addProperty("excessBought", false);
		vehicle.addProperty("extraTppdl", BigDecimal.ZERO);
		vehicle.addProperty("fleetDiscount", BigDecimal.ZERO);
		vehicle.addProperty("umbrellaLimit", BigDecimal.ZERO);

		
		Integer noClaimDiscount = 0;
		BigDecimal earnedNoClaimDsicount = BigDecimal.ZERO;
		for (PolItemBen itemBen : itemBens) {
			
			if(itemBen.getBenCode().equalsIgnoreCase("NCD") || itemBen.getBenCode().equalsIgnoreCase("NCB")) {
				
				noClaimDiscount = polMtrVeh.getNcbPct().intValue();
				earnedNoClaimDsicount = itemBen.getPremDue();
				
			}

		}

		vehicle.addProperty("noClaimDiscount", noClaimDiscount);
		vehicle.addProperty("earnedNoClaimDiscount", earnedNoClaimDsicount.abs());
		vehicle.addProperty("calculationType", "FULL_YEAR");

		vehicles.add(vehicle);
		return vehicles;
	}

	private String resolveFirstName(PolMaster polMaster) {
		if (polMaster.getInsdName2() != null && !polMaster.getInsdName2().trim().isEmpty()) {
			return polMaster.getInsdName2();
		}

		String insdName1 = polMaster.getInsdName1();
		if (insdName1 != null && !insdName1.trim().isEmpty()) {
			return insdName1.trim().split("\\s+")[0];
		}

		return "";
	}

	private String getScheduleCode(String certRef) {
		if (certRef != null && scheduleProperties.containsKey(certRef)) {
			return scheduleProperties.getProperty(certRef);
		}
		return "PRIVATE_INDIVIDUAL_X1";
	}

	private BigDecimal getStickerFee() {
		String value = scheduleProperties.getProperty("stickerFee");
		return value != null ? new BigDecimal(value) : BigDecimal.ZERO;
	}

	private String getCoverType(PolMtrVeh mtrVeh) {
		if (mtrVeh.getCoverType() == null) {
			return "MTP";
		}
		switch (mtrVeh.getCoverType()) {
		case "C":
			return "MTCOMP";
		case "F":
			return "MTPFT";
		default:
			return "MTP";
		}
	}

	private String getGender(Xmm600 xmm600) {
		if (xmm600 == null || xmm600.getGender() == null) {
			return null;
		}
		switch (xmm600.getGender()) {
		case "M":
			return "MALE";
		case "F":
			return "FEMALE";
		default:
			return null;
		}
	}

	private String getTitle(Xmm600 xmm600) {
		if (xmm600 == null || xmm600.getTitleName() == null || xmm600.getTitleName().isEmpty()) {
			return "NA";
		}
		return xmm600.getTitleName();
	}

	private String convertToMIDDate(String input) {
		if (input == null || input.isEmpty()) {
			return null;
		}

		try {
			LocalDateTime dateTime = LocalDateTime.parse(input, INPUT_DATE_FORMATTER);
			return dateTime.format(MID_DATE_FORMATTER);
		} catch (Exception e) {
			return null;
		}
	}

	private void addPropertyIfNotEmpty(JsonObject json, String key, String value) {
		if (value != null && !value.trim().isEmpty()) {
			json.addProperty(key, value.trim());
		} else {
			json.add(key, JsonNull.INSTANCE);
		}
	}

}