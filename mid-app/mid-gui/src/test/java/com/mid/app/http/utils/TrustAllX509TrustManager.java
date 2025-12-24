package com.mid.app.http.utils;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class TrustAllX509TrustManager implements X509TrustManager {
	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}

	@Override
	public void checkClientTrusted(final java.security.cert.X509Certificate[] certs,
			final String authType) {
	}

	@Override
	public void checkServerTrusted(final java.security.cert.X509Certificate[] certs,
			final String authType) {
	}

}
