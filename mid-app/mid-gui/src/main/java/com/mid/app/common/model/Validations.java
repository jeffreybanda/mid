package com.mid.app.common.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.commons.validator.GenericValidator;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mid.app.politemben.model.PolItemBen;
import com.mid.app.polmaster.model.PolMaster;
import com.mid.app.polmtrveh.model.PolMtrVeh;
import com.mid.app.polrisk.model.PolRisk;
import com.mid.app.utils.LoggingEngine;
import com.mid.app.utils.MapCreator;
import com.mid.app.xmm600.model.Xmm600;

public class Validations {
	
	public static boolean recordIsValidated(final PolMaster polMaster, final PolRisk polRisk, final PolMtrVeh mtrVeh,
			final List<PolItemBen> itemBens, final List<Xmm600> xmm600ClientList,
			final List<Xmm600> xmm600IntermediaryList,LoggingEngine logging) {

		// return true;

		JsonObject jsonObject = new JsonObject();
		String result = "";
		boolean isValid = true;
		//InputStream inputStream;
		Properties propSchedule = new Properties();
		boolean foundScheduleCode = false;
		final InputStream inputStream = MapCreator.class.getResourceAsStream(System.getProperty("PropFile"));
	//	inputStream = MapCreator.class.getClassLoader().getResourceAsStream("milleniumLiveConfig.properties");
		propSchedule = new Properties();

		try {
			propSchedule.load(inputStream);
		} catch (IOException e1) {

			e1.printStackTrace();
		}
		
		@SuppressWarnings("unchecked")
		Enumeration<String> enums = (Enumeration<String>) propSchedule.propertyNames();

		while (enums.hasMoreElements() && (!foundScheduleCode)) {
			String key = enums.nextElement();
			
			
			if (mtrVeh.getCertRef().replace(" ", "").replace("  ", "").replace("   ", "").trim()
					.equalsIgnoreCase(key)) {
				foundScheduleCode = true;
				
			}

		}
		
		if(!foundScheduleCode) {
			logging.setMessage(" Certificate reference Not mapped For :" + mtrVeh.getPolNo() +  " OV2 CERT REF : " + mtrVeh.getCertRef());
			isValid = false;
		}

		if (itemBens.isEmpty()) {
			jsonObject.addProperty("NoItemBenefitsIdentification",
					polMaster.getPolNo() + " " + mtrVeh.getVehRegNo());
			jsonObject.addProperty("NoItemBenefitsDescription", "Record Has No Item Benefits");
			logging.setMessage(polMaster.getPolNo() + " " + mtrVeh.getVehRegNo() + " Record Has No Item Benefits ");
			isValid = false;
		}

		for (final Xmm600 xmm6002 : xmm600ClientList) {

			if (polRisk.getComDate() == null) {
				jsonObject.addProperty("riskCommenceDateIdentification",
						polMaster.getPolNo() + " " + mtrVeh.getVehRegNo());
				jsonObject.addProperty("riskCommenceDateDescription", "Risk Commence Date is Null");
				logging.setMessage(polMaster.getPolNo() + " " + mtrVeh.getVehRegNo() + " Risk Commence Date is Null ");
				isValid = false;
			}

			if (polRisk.getExpiryDate() == null) {
				jsonObject.addProperty("riskExpiryDateIdentification",
						polMaster.getPolNo() + " " + mtrVeh.getVehRegNo());
				jsonObject.addProperty("riskExpiryDateDescription", "Risk Expiry Date is Null");
				logging.setMessage(polMaster.getPolNo() + " " + mtrVeh.getVehRegNo() + " Risk Expiry Date is Null ");
				isValid = false;
			}

			if (xmm6002.getName1().isEmpty()) {
				logging.setMessage( xmm6002.getName1().toString() + " "
						+ xmm6002.getTelno7().toString() + " Client name is Null ");
				return false;
				
			}

			if (xmm6002.getBirthday() == null) {

				jsonObject.addProperty("clientBirthdayIdentification",
						xmm6002.getClientNo() + " " + polMaster.getPolNo() + "  " + xmm6002.getName1());
				jsonObject.addProperty("clientBirthDayDescription", "Birthday is Null");
				logging.setMessage( xmm6002.getName1().toString() + "  "
						+ xmm6002.getTelno7().toString() + " Birthday is Null ");
				isValid = false;
			}

			if (xmm6002.getBirthday() != null) {
				if (!GenericValidator.isDate(xmm6002.getBirthday().toString().substring(0, 10), "yyyy-MM-dd", true)) {

					jsonObject.addProperty("invalidBirthdayIdentification",
							xmm6002.getClientNo() + " " + polMaster.getPolNo() + "  " + xmm6002.getName1().toString()
									+ " " + xmm6002.getBirthday().toString().substring(0, 10));
					jsonObject.addProperty("Invalid BirthdayDescription", "Invalid Birthday");
					logging.setMessage( xmm6002.getName1().toString() + "  "
							+ xmm6002.getTelno7().toString() + " Invalid Birthday ");
					isValid = false;
				}
			}

			if (xmm6002.getTelno7() == null || xmm6002.getTelno7().isEmpty() || xmm6002.getTelno7().length() < 10) {

				jsonObject.addProperty("invalidClientTelephoneIdentification",
						xmm6002.getClientNo() + " " + polMaster.getPolNo() + "  " + xmm6002.getName1().toString() + " "
								+ xmm6002.getTelno7().toString());
				jsonObject.addProperty("invalidClientTelephoneDescription", "Invalid Client Telephone");
				logging.setMessage(xmm6002.getClientNo().toString() + "  " + xmm6002.getName1().toString() + "  "
						+ xmm6002.getTelno7().toString() + " Invalid Client Telephone ");
				isValid = false;
			}

			if (mtrVeh.getColour() == null || mtrVeh.getColour().isEmpty()) {
				jsonObject.addProperty("motorColorIdentification",
						polMaster.getPolNo() + " " + xmm6002.getName1().toString() + " " + mtrVeh.getVehRegNo());
				jsonObject.addProperty("motorColoreDescription", "No Motor Colour");
				logging.setMessage(polMaster.getPolNo() + "  " + mtrVeh.getVehRegNo() + " No Motor Colour ");
				isValid = false;
			}

			if (mtrVeh.getNoSeats() == null || mtrVeh.getNoSeats() == 0) {
				jsonObject.addProperty("numberOfSeatsIdentification",
						polMaster.getPolNo() + " " + xmm6002.getName1().toString() + " " + mtrVeh.getVehRegNo());
				jsonObject.addProperty("numberOfSeatDescription", "Number of Seats should be greater than Zero!!");
				logging.setMessage(polMaster.getPolNo() + "  " + mtrVeh.getVehRegNo() + " Number of Seats should be greater than Zero!! ");
				isValid = false;
			}
		}

		result = new Gson().toJson(jsonObject);
		System.out.println(result);

		if (isValid) {

			return true;
		} else {
			return false;
		}

	}


}
