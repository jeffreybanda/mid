package com.mid.app.common.json;

import java.util.List;

import javax.ejb.Local;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

@Local
public interface EntityJsonConverter<T> {

	T convertFrom(final String json);

	JsonElement convertToJsonElement(final T entity);

	default JsonElement convertToJsonElement(final List<T> entities) {
		final JsonArray jsonArray = new JsonArray();

		for (final T entity : entities) {
			jsonArray.add(convertToJsonElement(entity));
		}

		return jsonArray;
	}

}
