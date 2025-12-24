package com.mid.app.xmm600.resource;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mid.app.xmm600.model.Xmm600;

public class Xmm600JsonConverter {

	public JsonElement convertToJsonElement(final Xmm600 xmm600) {

		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("number", "");
		jsonObject.addProperty("nic_registration_number", "NIC/AGM/" + xmm600.getClientNo());
		jsonObject.addProperty("first_name",
				xmm600.getFirstName().isEmpty() ? xmm600.getName1() : xmm600.getFirstName());
		jsonObject.addProperty("last_name", xmm600.getName1() + " " + xmm600.getName2());
		jsonObject.addProperty("other_names", xmm600.getName3());
		jsonObject.addProperty("phone",
				(xmm600.getTelno7().isEmpty() || xmm600.getTelno7().length() < 10) ? "xxx" + xmm600.getClientNo()
						: xmm600.getTelno7());
		jsonObject.addProperty("email",
				"");
		jsonObject.addProperty("reference", xmm600.getClientNo());
		jsonObject.addProperty("intermediary_type_code", "01");
		jsonObject.addProperty("branch_code", xmm600.getHomeBrn().isEmpty() ? "HO" : xmm600.getHomeBrn());

		return jsonObject;
	}

	JsonElement convertToJsonElement(final List<Xmm600> entities) {
		final JsonArray jsonArray = new JsonArray();

		for (final Xmm600 entity : entities) {
			jsonArray.add(convertToJsonElement(entity));
		}

		return jsonArray;
	}

}
