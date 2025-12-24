
package com.mid.app.http.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.mid.app.common.model.HttpCode;
import com.mid.app.utils.ConfigurationClass;

public class ConnectionUtils {

	static String token;
	static LocalDateTime creationDate;
	static LocalDateTime expiryDate;

	private static boolean isTokenExpired() {
		if (expiryDate == null || LocalDateTime.now().isAfter(expiryDate)) {
			return true;
		}
		return false;
	}

	private static boolean generateNewToken() {
		try {
			final CloseableHttpClient client = HttpClients.createDefault();
			final HttpPost httpPost = new HttpPost("https://api.midv2.nicgh.com/api/Auth/GetToken");

			// Set headers
			httpPost.setHeader("x-api-key", "41e5e8cf3b3441b198ebf5723425a156");
			httpPost.setHeader("Content-type", "application/json");

			// Set body
			JSONObject json = new JSONObject();
			json.put("userName", "jbanda@insurefin.com");
			json.put("password", "@M1llenn1uMInsurance");

			StringEntity entity = new StringEntity(json.toString());
			httpPost.setEntity(entity);

			// Execute POST request
			final CloseableHttpResponse response = client.execute(httpPost);
			String responseBody = EntityUtils.toString(response.getEntity());

			// Parse response
			JSONObject responseJson = new JSONObject(responseBody);
			token = responseJson.getString("access_token");

			long creationTime = responseJson.getLong("creation_Time");
			long expirationTime = responseJson.getLong("expiration_Time");

			// Convert Unix timestamps to LocalDateTime
			creationDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(creationTime), ZoneId.systemDefault());
			expiryDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(expirationTime), ZoneId.systemDefault());

			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

//	public static boolean checkConnectionToPortal() throws AuthenticationException {
//
//		try {
//			final InputStream is = ConnectionUtils.class.getResourceAsStream(System.getProperty("PropFile"));
//			final Properties prop = new Properties();
//
//			String apiSite;
//
//			prop.load(is);
//
//			apiSite = prop.getProperty("url");
//
//			if (isTokenExpired()) {
//				// Generate a new token if the current one is expired
//				if (!generateNewToken()) {
//					JOptionPane.showMessageDialog(null, "Failed to generate new token.");
//					return false;
//				}
//			}
//
//			if (token != null) {
//				JOptionPane.showMessageDialog(null, "Welcome To MDI Portal");
//				return true;
//			} else {
//				JOptionPane.showMessageDialog(null, "Connection To MDI Portal Failed");
//				return false;
//			}
//
//		} catch (final UnknownHostException e) {
//			JOptionPane.showMessageDialog(null, "Portal is OffLine! Contact Your Admin");
//			e.printStackTrace();
//			return false;
//		} catch (final IOException e) {
//			JOptionPane.showMessageDialog(null, "Error! Contact Your Admin");
//			e.printStackTrace();
//			return false;
//		}
//
//	}

	public static boolean checkConnectionToPortal() throws AuthenticationException {
	    try {
	        final InputStream is = ConnectionUtils.class.getResourceAsStream(System.getProperty("PropFile"));
	        final Properties prop = new Properties();
	        
	        prop.load(is);
	        
	        String baseUrl = prop.getProperty("url.base");
	        String healthCheckPath = prop.getProperty("url.healthcheck");
	        String apiKey = prop.getProperty("url.ApiKey");
	        
	        // Construct URL
	        String apiUrl = baseUrl + healthCheckPath;
	        
	        // DEBUG: Print what we're sending
	     
	        final CloseableHttpClient client = HttpClients.createDefault();
	        final HttpGet httpGet = new HttpGet(apiUrl);
	        
	        // Set headers - choose ONE of these options based on your API docs
	        httpGet.setHeader("Accept", "application/json");
	        httpGet.setHeader("Content-type", "application/json");
	        
	     

	        // Use this exact format:
	        httpGet.setHeader("Authorization", "x-api-key " + apiKey);

	    
	        // SSL setup
	        SSLUtilities.trustAllHostnames();
	        SSLUtilities.trustAllHttpsCertificates();
	        
	        final CloseableHttpResponse response = client.execute(httpGet);
	        
	        // Print full response details
	        System.out.println("Response Status: " + response.getStatusLine());
	        System.out.println("Response Headers:");
	        for (Header header : response.getAllHeaders()) {
	            System.out.println("  " + header.getName() + ": " + header.getValue());
	        }
	        
	        final HttpEntity body = response.getEntity();
	        final StatusLine statusLine = response.getStatusLine();
	        final String content = EntityUtils.toString(body);
	        
	        System.out.println("Response Body: " + content);
	        
	        if (statusLine.getStatusCode() == HttpCode.OK.getCode()) {
	            JOptionPane.showMessageDialog(null, "Welcome To MDI Portal");
	            return true;
	        } else {
	            String message = "Connection Failed: " + statusLine.getStatusCode() + " - " + statusLine.getReasonPhrase();
	            JOptionPane.showMessageDialog(null, message);
	            return false;
	        }
	        
	    } catch (final UnknownHostException e) {
	        JOptionPane.showMessageDialog(null, "Portal is OffLine! Contact Your Admin");
	        e.printStackTrace();
	        return false;
	    } catch (final IOException e) {
	        JOptionPane.showMessageDialog(null, "Error! Contact Your Admin");
	        e.printStackTrace();
	        return false;
	    }
	}

	public static boolean checkJobConnectionToPortal() throws AuthenticationException {

		try {
			final InputStream is = ConfigurationClass.class.getResourceAsStream(System.getProperty("PropFile"));
			final BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			final Properties prop = new Properties();

			final CloseableHttpClient client = HttpClients.createDefault();

			String apiSite;
			final String userName;
			final String passWord;
			final String token;

			prop.load(is);

			apiSite = prop.getProperty("url");
			// apiSite = prop.getProperty("url.Test");

			userName = prop.getProperty("url.UserName");
			passWord = prop.getProperty("url.Password");
			token = prop.getProperty("url.Token");

			final HttpGet httpGet = new HttpGet(apiSite.toString());
			final UsernamePasswordCredentials creds = new UsernamePasswordCredentials(userName,
					passWord);
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
			System.out.println(response.getStatusLine() + " " + content.toString());

			if (statusLine.getStatusCode() == HttpCode.OK.getCode()) {
				System.out.println("Welcome To MDI Portal");
				return true;
			} else {
				System.out.println("Connection To MDI Portal Failed");
				return false;
			}

		} catch (final UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Portal is OffLine! Contact Your Admin");
			e.printStackTrace();
			return false;
		} catch (final IOException e) {
			JOptionPane.showMessageDialog(null, "Error! Contact Your Admin");
			e.printStackTrace();
			return false;
		}

	}

}
