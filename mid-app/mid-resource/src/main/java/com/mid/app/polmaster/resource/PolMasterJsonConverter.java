package com.mid.app.polmaster.resource;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mid.app.polmaster.model.PolMaster;

@ApplicationScoped
public class PolMasterJsonConverter {

	public JsonElement convertToJsonElement(final PolMaster polMaster) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("number", "");
		jsonObject.addProperty("first_name",
				polMaster.getFirstName().isEmpty() ? polMaster.getInsdName1() : polMaster.getFirstName());
		jsonObject.addProperty("last_name", polMaster.getInsdName1() + " " + polMaster.getInsdName2());
		jsonObject.addProperty("other_names", polMaster.getInsdName3());
		jsonObject.addProperty("email", "");
		jsonObject.addProperty("phone", "xxx" + polMaster.getInsured());
		jsonObject.addProperty("digital_address", "");
		jsonObject.addProperty("postal_address", polMaster.getInsdAddr1() + " " + polMaster.getInsdAddr2() + " "
				+ polMaster.getInsdAddr3() + " " + polMaster.getInsdAddr4());
		jsonObject.addProperty("residential_address", polMaster.getInsdAddr1() + " " + polMaster.getInsdAddr2() + " "
				+ polMaster.getInsdAddr3() + " " + polMaster.getInsdAddr4());
		jsonObject.addProperty("tin", "");
		jsonObject.addProperty("occupation", "");
		jsonObject.addProperty("id_number", "");
		jsonObject.addProperty("national_id", "");
		jsonObject.addProperty("reference", polMaster.getPolNo() + polMaster.getRenCnt() + polMaster.getEndtCnt());
		jsonObject.addProperty("branch_code", polMaster.getBranch());
		jsonObject.addProperty("customer_type_code", "01");
		jsonObject.addProperty("id_card_type_code", "02");
		return jsonObject;
	}

	public JsonElement convertToJsonElement(final List<PolMaster> entities) {
		final JsonArray jsonArray = new JsonArray();

		for (final PolMaster entity : entities) {
			jsonArray.add(convertToJsonElement(entity));
		}

		return jsonArray;
	}

}