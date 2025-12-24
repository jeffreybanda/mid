package com.mid.app.http.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.mid.app.common.model.HttpCode;

public class HttpAuthentication {

//	public static CloseableHttpResponse getPostPolicyToPortalResponse(final String json) {
//		Properties prop = new Properties();
//
//		try (InputStream is = CloseableHttpResponse.class.getResourceAsStream(System.getProperty("PropFile"));
//				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//				CloseableHttpClient client = HttpClients.createDefault()) {
//
//			// Load properties from the file
//			// prop.load(reader);
//
//			// Retrieve the API site URL from the properties
//			// String apiSite = prop.getProperty("url.policies");
//			String apiSite = "https://api.midv2.nicgh.com/api/policies";
//			// Create the HttpPost request
//			HttpPost httpPost = new HttpPost(apiSite);
//
//			// Set the JSON entity as the request body
//			StringEntity entity = new StringEntity(json);
//			httpPost.setEntity(entity);
//
//			// Set headers for API key, Bearer token, and content types
//			httpPost.setHeader("x-api-key", "41e5e8cf3b3441b198ebf5723425a156");
//			httpPost.setHeader("Authorization", "Bearer " + ConnectionUtils.token); // Add Bearer token
//			httpPost.setHeader("Accept", "application/json");
//			httpPost.setHeader("Content-type", "application/json");
//
//			// Execute the HTTP request and return the response
//			return client.execute(httpPost);
//
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		// Return null if an exception occurred
//		return null;
//	}

	public static CloseableHttpResponse getPostPolicyToPortalResponse(final String json) {

		try {
			final InputStream is = CloseableHttpResponse.class.getResourceAsStream(System.getProperty("PropFile"));

			final Properties prop = new Properties();
			prop.load(is);

			String baseUrl = prop.getProperty("url.base");
			String postIndividualPolicyPath = prop.getProperty("url.PostIndividualPolicy");
			String apiKey = prop.getProperty("url.ApiKey");

			prop.load(is);

			final CloseableHttpClient client = HttpClients.createDefault();
			String apiUrl = baseUrl + postIndividualPolicyPath;

			// DEBUG: Print what we're sending

			final HttpPost httpPost = new HttpPost(apiUrl.toString());

			// Set headers - choose ONE of these options based on your API docs
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			// Use this exact format:
			httpPost.setHeader("Authorization", "x-api-key " + apiKey);

			// SSL setup
			SSLUtilities.trustAllHostnames();
			SSLUtilities.trustAllHttpsCertificates();

			return client.execute(httpPost);

		} catch (final UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Failed to Post Policy");
			e.printStackTrace();
			return null;
		} catch (final IOException e) {
			JOptionPane.showMessageDialog(null, "Error! Contact Your Admin");
			e.printStackTrace();
			return null;
		}
	}

	public static CloseableHttpResponse getPostBranchToPortalResponse(final String json) {

		try {

			final InputStream is = CloseableHttpResponse.class.getResourceAsStream(System.getProperty("PropFile"));
			final BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			final Properties prop = new Properties();
			final String apiSite;
			final String userName;
			final String passWord;
			final String token;

			prop.load(is);

			final CloseableHttpClient client = HttpClients.createDefault();

			apiSite = prop.getProperty("url");

			userName = prop.getProperty("url.UserName");
			passWord = prop.getProperty("url.Password");
			token = prop.getProperty("url.Token");

			final HttpPost httpPost = new HttpPost(apiSite.toString());
			final UsernamePasswordCredentials creds = new UsernamePasswordCredentials(userName, passWord);

			httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));

			final StringEntity entity = new StringEntity(json);
			httpPost.setEntity(entity);

			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("token", token);
			httpPost.setHeader("Accept", "*/*");
			httpPost.setHeader("Content-type", "application/json");
			SSLUtilities.trustAllHostnames();
			SSLUtilities.trustAllHttpsCertificates();
			return client.execute(httpPost);
		} catch (final MalformedURLException e) {

			e.printStackTrace();
			return null;

		} catch (final AuthenticationException e) {

			e.printStackTrace();

			return null;

		} catch (final ClientProtocolException e) {

			e.printStackTrace();

			return null;
		} catch (final IOException e) {

			e.printStackTrace();
			return null;
		}

	}

	public static String getMIDBranches() throws AuthenticationException {

		final InputStream is = CloseableHttpResponse.class.getResourceAsStream(System.getProperty("PropFile"));
		final BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		final Properties prop = new Properties();

		String apiSite;
		final String userName;
		final String passWord;
		final String token;

		try {
			prop.load(is);

			// apiSite = prop.getProperty("url.BranchTest");
			apiSite = prop.getProperty("url.Branch");
			// apiSite = prop.getProperty("url.Live");

			userName = prop.getProperty("url.UserName");
			passWord = prop.getProperty("url.Password");
			token = prop.getProperty("url.Token");

			final CloseableHttpClient client = HttpClients.createDefault();

			final HttpGet httpGet = new HttpGet(apiSite.toString());

			final UsernamePasswordCredentials creds = new UsernamePasswordCredentials(userName, passWord);
			httpGet.addHeader(new BasicScheme().authenticate(creds, httpGet, null));

			httpGet.setHeader("Accept", "application/json");
			httpGet.setHeader("token", token);

			httpGet.setHeader("Accept", "*/*");
			httpGet.setHeader("Content-type", "application/json");
			SSLUtilities.trustAllHostnames();
			SSLUtilities.trustAllHttpsCertificates();

			final CloseableHttpResponse response = client.execute(httpGet);
			final HttpEntity body = response.getEntity();
			final StatusLine statusLine = response.getStatusLine();
			final String content = EntityUtils.toString(body);
			System.out.println(response.getStatusLine());

			if (statusLine.getStatusCode() == HttpCode.OK.getCode()) {

				return body.toString();
			} else {

				return "";
			}

		} catch (final IOException e) {

			e.printStackTrace();

			return "";
		}

	}

}
