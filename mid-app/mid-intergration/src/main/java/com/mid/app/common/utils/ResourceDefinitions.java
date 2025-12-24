package com.mid.app.common.utils;

public enum ResourceDefinitions {

	CATEGORY("categories");

	private String resourceName;

	private ResourceDefinitions(final String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceName() {
		return resourceName;
	}

}
