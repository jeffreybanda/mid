package com.mid.app.xmm023.resource;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mid.app.xmm023.model.Xmm023;

public class Xmm023JsonConverter {

	public JsonElement convertToJsonElement(final Xmm023 xmm023) {

		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("branch", xmm023.getBranch());
		jsonObject.addProperty("branchDesc", xmm023.getBranchDesc());

		return jsonObject;
	}

	JsonElement convertToJsonElement(final List<Xmm023> entities) {
		final JsonArray jsonArray = new JsonArray();

		for (final Xmm023 entity : entities) {
			jsonArray.add(convertToJsonElement(entity));
		}

		return jsonArray;
	}

}
