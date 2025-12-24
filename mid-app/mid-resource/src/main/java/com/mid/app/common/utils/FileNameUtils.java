package com.mid.app.common.utils;

public class FileNameUtils {
	private static final String PATH_REQUEST = "/request/";
	private static final String PATH_RESPONSE = "/response/";

	private FileNameUtils() {
	}

	public static String getPathFileRequest(final String mainFolder, final String fileName) {
		return mainFolder + PATH_REQUEST + fileName;
	}

	public static String getPathFileResponse(final String mainFolder, final String fileName) {
		return mainFolder + PATH_RESPONSE + fileName;
	}

}