package com.mid.app.politemben.resource;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mid.app.politemben.model.PolItemBen;

public class PolItemJsonConverter {

	public JsonElement convertToJsonElement(final PolItemBen polItemBen) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("loadlng_type_code", polItemBen.getBenCode());
		jsonObject.addProperty("Rate", polItemBen.getPremRatePct());
		jsonObject.addProperty("Amount", polItemBen.getPremDue());

		return jsonObject;
	}

	public JsonElement convertToJsonElement(final List<PolItemBen> entities) {
		final JsonArray jsonArray = new JsonArray();

		for (final PolItemBen entity : entities) {
			jsonArray.add(convertToJsonElement(entity));
		}

		return jsonArray;
	}

}
