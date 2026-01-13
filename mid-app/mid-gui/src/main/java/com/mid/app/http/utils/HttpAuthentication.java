package com.mid.app.http.utils;

import java.io.InputStream;
import java.util.Properties;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpAuthentication {



	public static CloseableHttpResponse getPostPolicyToPortalResponse(final String json) {

	    try {
	        final InputStream is =
	                CloseableHttpResponse.class.getResourceAsStream(System.getProperty("PropFile"));

	        final Properties prop = new Properties();
	        prop.load(is);

	        String baseUrl = prop.getProperty("url.base");
	        String postIndividualPolicyPath = prop.getProperty("url.PostIndividualPolicy");
	        String apiKey = prop.getProperty("url.ApiKey");

	        final CloseableHttpClient client = HttpClients.createDefault();
	        String apiUrl = baseUrl + postIndividualPolicyPath;

	        HttpPost httpPost = new HttpPost(apiUrl);

	        // Headers
	        httpPost.setHeader("Accept", "application/json");
	        httpPost.setHeader("Content-Type", "application/json");
	        httpPost.setHeader("Authorization", "x-api-key " + apiKey);

	      
	        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
	        httpPost.setEntity(entity);

	        // Debug (strongly recommended)
	        System.out.println("POST URL: " + apiUrl);
	        System.out.println("REQUEST BODY: " + json);


	        return client.execute(httpPost);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}




}
