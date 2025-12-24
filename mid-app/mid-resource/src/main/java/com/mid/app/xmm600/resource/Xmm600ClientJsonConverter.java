package com.mid.app.xmm600.resource;

import java.util.List;

import org.apache.commons.validator.GenericValidator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mid.app.polmaster.model.PolMaster;
import com.mid.app.xmm600.model.Xmm600;

public class Xmm600ClientJsonConverter {

	public JsonElement convertToJsonElement(final Xmm600 xmm600, final PolMaster polMaster) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("number", "");
		jsonObject.addProperty("first_name",
				xmm600.getFirstName().isEmpty() ? xmm600.getName1() : xmm600.getFirstName());
		jsonObject.addProperty("last_name", xmm600.getName1() + " " + xmm600.getName2());
		jsonObject.addProperty("other_names", xmm600.getName3());
		if(xmm600.getBirthday() != null) {
			if (!GenericValidator.isDate(xmm600.getBirthday().toString().substring(0, 10), "yyyy-MM-dd", true)) {

				jsonObject.addProperty("date_of_birth","1980-01-01");
			}else {
									
			jsonObject.addProperty("date_of_birth", xmm600.getBirthday().toString().substring(0, 10));
			}
		}else {
			jsonObject.addProperty("date_of_birth","1980-01-01");
		}
		jsonObject.addProperty("email", "");
		jsonObject.addProperty("phone",
				(xmm600.getTelno7().isEmpty() || xmm600.getTelno7().length() < 10) ? "xxx" + xmm600.getClientNo()
						: xmm600.getTelno7());
		jsonObject.addProperty("digital_address", "");
		jsonObject.addProperty("digital_address", "");
		jsonObject.addProperty("postal_address", xmm600.getAddr1() + " " + xmm600.getAddr2() + " "
				+ xmm600.getAddr3() + " " + xmm600.getAddr4());
		jsonObject.addProperty("residential_address", xmm600.getAddr1() + " " + xmm600.getAddr2() + " "
				+ xmm600.getAddr3() + " " + xmm600.getAddr4());
		jsonObject.addProperty("tin", "");
		jsonObject.addProperty("occupation", "");
		jsonObject.addProperty("id_number", "");
		jsonObject.addProperty("national_id",  xmm600.getClientType() + xmm600.getClientNo());
		jsonObject.addProperty("reference", xmm600.getClientType() + xmm600.getClientNo());
		jsonObject.addProperty("branch_code", polMaster.getBranch());
		jsonObject.addProperty("customer_type_code", "01");
		jsonObject.addProperty("id_card_type_code", "02");
		return jsonObject;
	}

	JsonElement convertToJsonElement(final List<Xmm600> entities) {
		final JsonArray jsonArray = new JsonArray();

		for (final Xmm600 entity : entities) {
			jsonArray.add(convertToJsonElement(entity));
		}

		return jsonArray;
	}

	private JsonElement convertToJsonElement(final Xmm600 entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
