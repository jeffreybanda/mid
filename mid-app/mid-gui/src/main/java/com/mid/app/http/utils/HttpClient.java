package com.mid.app.http.utils;

import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;

public class HttpClient {

	private final String user;
	private final String password;

	public HttpClient(final String user, final String password) {
		this.user = user;
		this.password = password;
	}

	public int sendRquestWithAuthHeader(final String url) throws IOException {
		HttpURLConnection connection = null;
		try {
			connection = createConnection(url);
			connection.setRequestProperty("Authorization", createBasicAuthHeaderValue());

			return connection.getResponseCode();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public int sendRquestWithAuthenticator(final String url) throws IOException {
		setAuthenticator();

		HttpURLConnection connection = null;
		try {
			connection = createConnection(url);
			return connection.getResponseCode();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private HttpURLConnection createConnection(final String urlString)
			throws MalformedURLException, IOException, ProtocolException {
		final URL url = new URL(String.format(urlString));
		final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		return connection;
	}

	private String createBasicAuthHeaderValue() {
		final String auth = user + ":" + password;
		final byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
		final String authHeaderValue = "Basic " + new String(encodedAuth);
		return authHeaderValue;
	}

	private void setAuthenticator() {
		Authenticator.setDefault(new BasicAuthenticator());
	}

	private final class BasicAuthenticator extends Authenticator {
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(user, password.toCharArray());
		}
	}
}
