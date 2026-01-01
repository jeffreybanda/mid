package com.mid.app.swing.service;

import com.google.gson.JsonObject;
import com.mid.app.politemben.model.PolItemBen;
import com.mid.app.polmaster.model.PolMaster;
import com.mid.app.polmtrveh.model.PolMtrVeh;
import com.mid.app.polrisk.model.PolRisk;
import com.mid.app.xmm600.model.Xmm600;
import org.apache.commons.validator.GenericValidator;

import java.util.List;

public class PolicyValidationService {
    
    public boolean validateRecord(PolMaster polMaster, PolRisk polRisk, 
                                 PolMtrVeh mtrVeh, List<PolItemBen> itemBens,
                                 List<Xmm600> clients, List<Xmm600> intermediaries) {
        
        if (itemBens == null || itemBens.isEmpty()) {
            return false;
        }
        
        if (polRisk.getComDate() == null || polRisk.getExpiryDate() == null) {
            return false;
        }
        
        if (clients == null || clients.isEmpty()) {
            return false;
        }
        
        for (Xmm600 client : clients) {
            if (!validateClient(client, polMaster)) {
                return false;
            }
        }
        
        if (!validateVehicle(mtrVeh, polMaster)) {
            return false;
        }
        
        return true;
    }
    
    private boolean validateClient(Xmm600 client, PolMaster polMaster) {
        if (client == null || client.getName1() == null || client.getName1().isEmpty()) {
            return false;
        }
        
        if (client.getBirthday() == null) {
            return false;
        }
        
        if (!GenericValidator.isDate(client.getBirthday().toString().substring(0, 10), 
                                    "yyyy-MM-dd", true)) {
            return false;
        }
        
        if (client.getTelno7() == null || client.getTelno7().isEmpty() || 
            client.getTelno7().length() < 10) {
            return false;
        }
        
        return true;
    }
    
    private boolean validateVehicle(PolMtrVeh vehicle, PolMaster polMaster) {
        if (vehicle.getColour() == null || vehicle.getColour().isEmpty()) {
            return false;
        }
        
        if (vehicle.getNoSeats() == null || vehicle.getNoSeats() <= 0) {
            return false;
        }
        
        return true;
    }
    
    public JsonObject createValidationJson(PolMaster polMaster, PolRisk polRisk,
                                          PolMtrVeh mtrVeh, Xmm600 client) {
        JsonObject jsonObject = new JsonObject();
        
        jsonObject.addProperty("policyNumber", polMaster.getPolNo());
        
        if (polRisk.getComDate() == null) {
            jsonObject.addProperty("riskCommenceDateIdentification", polMaster.getPolNo());
            jsonObject.addProperty("riskCommenceDateDescription", "Risk Commence Date is Null");
        }
        
        if (polRisk.getExpiryDate() == null) {
            jsonObject.addProperty("riskExpiryDateIdentification", polMaster.getPolNo());
            jsonObject.addProperty("riskExpiryDateDescription", "Risk Expiry Date is Null");
        }
        
        if (client != null) {
            validateClientForJson(jsonObject, client, polMaster);
        }
        
        validateVehicleForJson(jsonObject, mtrVeh, polMaster, client);
        
        return jsonObject;
    }
    
    private void validateClientForJson(JsonObject jsonObject, Xmm600 client, PolMaster polMaster) {
        if (client.getName1().isEmpty()) {
            jsonObject.addProperty("clientNameIdentification", 
                client.getClientNo() + " " + polMaster.getPolNo());
            jsonObject.addProperty("clientNameDescription", "Client Name is Empty");
        }
        
        if (client.getBirthday() == null) {
            jsonObject.addProperty("clientBirthdayIdentification",
                client.getClientNo() + " " + polMaster.getPolNo() + "  " + client.getName1());
            jsonObject.addProperty("clientBirthDayDescription", "Birthday is Null");
        } else if (!GenericValidator.isDate(client.getBirthday().toString().substring(0, 10), 
                                           "yyyy-MM-dd", true)) {
            jsonObject.addProperty("invalidBirthdayIdentification",
                client.getClientNo() + " " + polMaster.getPolNo() + "  " + client.getName1() + " "
                    + client.getBirthday().toString().substring(0, 10));
            jsonObject.addProperty("Invalid BirthdayDescription", "Invalid Birthday");
        }
        
        if (client.getTelno7() == null || client.getTelno7().isEmpty() || 
            client.getTelno7().length() < 10) {
            jsonObject.addProperty("invalidClientTelephoneIdentification", 
                client.getClientNo() + " " + polMaster.getPolNo() + "  " + client.getName1() + " " 
                    + client.getTelno7());
            jsonObject.addProperty("invalidClientTelephoneDescription", "Invalid Client Telephone");
        }
    }
    
    private void validateVehicleForJson(JsonObject jsonObject, PolMtrVeh vehicle, 
                                       PolMaster polMaster, Xmm600 client) {
        if (vehicle.getColour() == null || vehicle.getColour().isEmpty()) {
            String clientName = client != null ? client.getName1() : "Unknown";
            jsonObject.addProperty("motorColorIdentification",
                polMaster.getPolNo() + " " + clientName + " " + vehicle.getVehRegNo());
            jsonObject.addProperty("motorColoreDescription", "No Motor Colour");
        }
        
        if (vehicle.getNoSeats() == null || vehicle.getNoSeats() == 0) {
            String clientName = client != null ? client.getName1() : "Unknown";
            jsonObject.addProperty("numberOfSeatsIdentification",
                polMaster.getPolNo() + " " + clientName + " " + vehicle.getVehRegNo());
            jsonObject.addProperty("numberOfSeatDescription", 
                "Number of Seats should be greater than Zero!!");
        }
    }
}