package com.mid.app.common.utils;

import java.io.InputStream;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.mid.app.common.json.JsonReader;

public class JsonFileUtils {
	public static final String BASE_JSON_DIR = "json/";

	private JsonFileUtils() {
	}

	public static String readJsonFile(final String relativePath) {
		final InputStream is = JsonFileUtils.class.getClassLoader().getResourceAsStream(BASE_JSON_DIR + relativePath);
		try (Scanner s = new Scanner(is)) {
			return s.useDelimiter("\\A").hasNext() ? s.next() : "";
		}
	}

	public static Long getIdFromJson(final String json) {
		final JsonObject jsonObject = JsonReader.readAsJsonObject(json);
		return JsonReader.getLongOrNull(jsonObject, "id");
	}

}