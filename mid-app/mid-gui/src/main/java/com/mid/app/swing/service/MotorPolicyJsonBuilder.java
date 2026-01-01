package com.mid.app.swing.service;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.mid.app.politem.model.PolItem;
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
import java.util.Optional;
import java.util.Properties;

public class MotorPolicyJsonBuilder {
    
    private static final DateTimeFormatter MID_DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter INPUT_DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    
    private final Properties scheduleProperties;
    private final Gson gson;
    
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
    
    public String buildPolicyJson(PolMaster polMaster, PolRisk polRisk, 
                                  PolMtrVeh polMtrVeh, PolItem polItem) {
        
        JsonObject root = new JsonObject();
        JsonObject data = new JsonObject();
        
        String productCode = getCoverType(polMtrVeh);
        String scheduleCode = getScheduleCode(polMtrVeh.getCertRef());
        
        // Basic policy data
        data.addProperty("branchCode", polMaster.getBranch());
        data.addProperty("productCode", productCode);
        data.addProperty("intermediaryCode", polMaster.getAcctNo1());
        
        addPropertyIfNotEmpty(data, "subIntermediaryCode", null);
      //  data.addProperty("subintermediaryCode", "null");
        data.addProperty("riskTypeCode", scheduleCode);
        data.addProperty("companyAssignedPolicyNumber", polMaster.getPolNo());
        
        // Vehicle data
        JsonObject vehicleData = createVehicleData(polMtrVeh, polItem);
        data.add("vehicleData", vehicleData);
        
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
        
        JsonObject customerData = createCustomerData(polMaster, client);
        data.add("customerData", customerData);
        
        // Policy data
        JsonObject policyData = createPolicyData(polMaster, polRisk, polItem);
        data.add("policyData", policyData);
        
        root.add("data", data);
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
        customerData.addProperty("type", "INDIVIDUAL");
      //  customerData.addProperty("title", getTitle(xmm600));
        
        addPropertyIfNotEmpty(customerData, "title", 
    		    xmm600 != null ? xmm600.getTitleName() : null);
        customerData.addProperty("firstName", resolveFirstName(polMaster));
        customerData.addProperty("lastName", polMaster.getInsdName1());
        customerData.addProperty("gender", getGender(xmm600));
        customerData.addProperty("dateOfBirth", convertToMIDDate(xmm600 != null ? 
            xmm600.getBirthday().toString() : null));
        customerData.addProperty("nationality", "GH");
        customerData.addProperty("ghanaCardNumber", xmm600.getIcno()
        		);
        
        String emailValue = xmm600 != null && xmm600.getEmail() != null && !xmm600.getEmail().isEmpty() 
        	    ? xmm600.getEmail() 
        	    : null;

        	if (emailValue != null) {
        	    customerData.addProperty("email", emailValue);
        	} else {
        	    customerData.add("email", JsonNull.INSTANCE);
        	}
        
        
//        customerData.addProperty("email", xmm600 != null && xmm600.getEmail() != null && !xmm600.getEmail().isEmpty()? 
//            xmm600.getEmail() : null);
        	addPropertyIfNotEmpty(customerData, "phoneNumber", 
        		    xmm600 != null ? xmm600.getTelno7() : null);

        		addPropertyIfNotEmpty(customerData, "digitalAddress", null); 

        		addPropertyIfNotEmpty(customerData, "residentialAddress", 
        		    polMaster != null ? polMaster.getInsdAddr1() : null);
        if (polMaster != null && 
        	    polMaster.getOccupation() != null && 
        	    !polMaster.getOccupation().trim().isEmpty()) {
        	    
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
        
        BigDecimal sumInsured = polItem.getuOM1Val()
            .divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP);
        policyData.addProperty("sumInsured", sumInsured);
        
        BigDecimal grossPremium = polRisk.getTotGap()
                .divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP);
        
        policyData.addProperty("grossPremium", grossPremium.add(getStickerFee()));
        policyData.addProperty("type", "NEW_BUSINESS");
        policyData.addProperty("transactionDate", convertToMIDDate(polMaster.getTranDate().toString()));
        policyData.addProperty("excessBought", false);
        policyData.addProperty("noClaimDiscount", 0);
        policyData.addProperty("earnedNoClaimDiscount", 0);
        policyData.addProperty("calculationType", "FULL_YEAR");
        
        return policyData;
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
            case "C": return "MTCOMP";
            case "F": return "MTPFT";
            default: return "MTP";
        }
    }
    
    private String getGender(Xmm600 xmm600) {
        if (xmm600 == null || xmm600.getGender() == null) {
            return null;
        }
        switch (xmm600.getGender()) {
            case "M": return "MALE";
            case "F": return "FEMALE";
            default: return null;
        }
    }
    
    private String getTitle(Xmm600 xmm600) {
        if (xmm600 == null || xmm600.getTitleName() == null || 
            xmm600.getTitleName().isEmpty()) {
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