package com.mid.app.common.json;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mid.app.politemben.model.PolItemBen;
import com.mid.app.politemben.resource.PolItemJsonConverter;
import com.mid.app.polmaster.model.PolMaster;
import com.mid.app.polmaster.resource.PolMasterJsonConverter;
import com.mid.app.xmm023.model.Xmm023;
import com.mid.app.xmm023.resource.Xmm023JsonConverter;
import com.mid.app.xmm600.model.Xmm600;
import com.mid.app.xmm600.resource.Xmm600ClientJsonConverter;
import com.mid.app.xmm600.resource.Xmm600JsonConverter;

public final class JsonWriter {

	static Xmm023JsonConverter xmm023JsonConverter;
	static Xmm600JsonConverter xmm600JsonConverter;
	static Xmm600ClientJsonConverter xmm600ClientJsonConverter;
	static PolMasterJsonConverter polMasterJsonConverter;
	static PolItemJsonConverter polItemJsonConverter;

	private JsonWriter() {
	}

	public static String writeToString(final Object object) {
		if (object == null) {
			return "";
		}

		return new Gson().toJson(object);
	}

	public static String writeJsonPolicyRecord(final Map<String, Object> map, final Xmm600 xmm600,
			final PolMaster polMaster, final List<PolItemBen> itemBens, final List<Xmm600> xmm600ClientList) {

		final JsonObject jsonObject = new JsonObject();

		final JsonObject jsonRoot = new JsonObject();

		xmm600JsonConverter = new Xmm600JsonConverter();

		polMasterJsonConverter = new PolMasterJsonConverter();

		polItemJsonConverter = new PolItemJsonConverter();

		xmm600ClientJsonConverter = new Xmm600ClientJsonConverter();

		for (final Entry<String, Object> mapEntry : map.entrySet()) {
			if (mapEntry.getKey() != "loadings") {
				System.out.println(mapEntry.getKey());
				jsonObject.addProperty(mapEntry.getKey(), (String) mapEntry.getValue());
			}

//			if (mapEntry.getKey() == "customer") {
//				for (final Xmm600 xmm6002 : xmm600ClientList) {
//					jsonObject.add("customer", xmm600ClientJsonConverter.convertToJsonElement(xmm6002, polMaster));
//				}
//
//			}

//			if (mapEntry.getKey() == "intermediary") {
//
//				jsonObject.add("intermediary", xmm600JsonConverter.convertToJsonElement(xmm600));
//			}

			if (mapEntry.getKey() == "loadings") {
				jsonObject.add("loadings", new JsonArray());
			}

//			if (mapEntry.getKey() == "discounts") {
//				jsonObject.add("discounts", new JsonArray());
//			}

		}

		// jsonRoot.add("motor_policy", jsonObject);

		return new Gson().toJson(jsonObject);

	}

	public static String writeJsonBranchRecord(final Xmm023 xmm023) {

		final JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("code", xmm023.getBranch());
		jsonObject.addProperty("name", xmm023.getBranchDesc());
		return new Gson().toJson(jsonObject);

	}

}