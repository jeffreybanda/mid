package com.mid.app.http.utils;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyAgreement;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class TestHttp {

	public void whenPostRequestWithAuthorizationUsingHttpClient_thenCorrect()
			throws ClientProtocolException, IOException, AuthenticationException

	{
		final CloseableHttpClient client = HttpClients.createDefault();

		final HttpGet httpGet = new HttpGet("https://api.nicmid.com/apiv1/");
		// final UsernamePasswordCredentials creds = new UsernamePasswordCredentials("PRIMEINSAPI",
		// "KINqVawp");
		final UsernamePasswordCredentials creds = new UsernamePasswordCredentials("millennium", "vuPtzNns");
		httpGet.addHeader(new BasicScheme().authenticate(creds, httpGet, null));

		httpGet.setHeader("Accept", "application/json");
		httpGet.setHeader("token", "E8rZHBDF38xuQobQWeYD7tL2");
		// httpGet.setHeader("token", "ibWgwpjQEZ1RW8G5318EN3eG");
		httpGet.setHeader("Accept", "*/*");
		httpGet.setHeader("Content-type", "application/json");

		final CloseableHttpResponse response = client.execute(httpGet);
		assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
		client.close();
	}

	@SuppressWarnings("restriction")

	public void testDiffieHellMan() {

		KeyAgreement ka;
		java.security.Security.addProvider(new com.sun.crypto.provider.SunJCE());
		try {
			ka = KeyAgreement.getInstance("DiffieHellman");
			System.out.println(ka);
		} catch (final NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("restriction")

	public void whenPostJsonUsingHttpClient_thenCorrect()
			throws ClientProtocolException, IOException, AuthenticationException, KeyManagementException,
			NoSuchAlgorithmException, KeyStoreException {

		// java.security.Security.addProvider(new com.sun.crypto.provider.SunJCE());

		final CloseableHttpClient client = HttpClients.createDefault();
		;

		final HttpGet httpPost = new HttpGet("https://api.nicmid.com/apiv1/body_types");
		final UsernamePasswordCredentials creds = new UsernamePasswordCredentials("PRIMEINSAPI", "KINqVawp");
		httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));

		// final String json = "";
		// final StringEntity entity = new StringEntity(json);
		// httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("token", "ibWgwpjQEZ1RW8G5318EN3eG");
		httpPost.setHeader("Accept", "*/*");
		httpPost.setHeader("Content-type", "application/json");

		final CloseableHttpResponse response = client.execute(httpPost);
		assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
		client.close();
	}
}
