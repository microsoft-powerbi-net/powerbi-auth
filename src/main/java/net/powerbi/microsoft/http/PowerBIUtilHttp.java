package net.powerbi.microsoft.http;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;



public class PowerBIUtilHttp {

	private static boolean hasProxy;
	
	private static boolean hasCredentialProxy;
	
	private static String proxyHost;
	private static int proxyPort;
	private static String proxyUser;
	private static String proxyPass;
	
	static {
		Protocol easyhttps = new Protocol("https", (ProtocolSocketFactory)new PowerBISSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", easyhttps);
	}
	
	public static void setHttpProxy(String host, int port, String user, String pass) {
		hasProxy = true;
		proxyHost = host;
		proxyPort = port;
		
		if(user != null && pass != null && !user.isEmpty()) {
			proxyUser = user;
			proxyPass = pass;
			hasCredentialProxy = true;
		}
	}

	public static String getAuthHeaderBase64(String clientId, String clientSecret) {
		String auth = clientId + ":" + clientSecret;
		byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
		String authHeader = "Basic " + new String(encodedAuth);
		return authHeader;
	}
	
	public static PowerBIRespHttp sendPut(String url, Map<String, String> headers, String contentType, String body, String charset) {
		try {
			StringRequestEntity stringEntity = new StringRequestEntity(body, contentType, charset);

			PutMethod httpPost = new PutMethod(url);
			try {
				httpPost.setRequestEntity(stringEntity);

				for(Entry<String, String> entry: headers.entrySet()) {
					httpPost.addRequestHeader(entry.getKey(), entry.getValue());
				}
				HttpClient httpClient = new HttpClient();
				configProxy(httpClient);
				int result = httpClient.executeMethod(httpPost);

				PowerBIRespHttp resp = new PowerBIRespHttp(result, httpPost);
				resp.updateContentAsString();
				return resp;
			} finally {
				httpPost.releaseConnection();
			}

		} catch (IOException e) {
			return new PowerBIRespHttp(e);
		}
	}

	public static PowerBIRespHttp sendPost(String url, Map<String, String> headers, Map<String, String> parameters, String contentType, String body, String charset) {
		try {
			StringRequestEntity stringEntity = new StringRequestEntity(body, contentType, charset);

			PostMethod httpPost = new PostMethod(url);
			try {

				httpPost.setRequestEntity(stringEntity);

				for(Entry<String, String> entry: parameters.entrySet()) {
					httpPost.addParameter(entry.getKey(), entry.getValue());
				}

				for(Entry<String, String> entry: headers.entrySet()) {
					httpPost.addRequestHeader(entry.getKey(), entry.getValue());
				}
				HttpClient httpClient = new HttpClient();
				configProxy(httpClient);
				int result = httpClient.executeMethod(httpPost);

				PowerBIRespHttp resp = new PowerBIRespHttp(result, httpPost);
				resp.updateContentAsString();
				return resp;
			}finally {
				httpPost.releaseConnection();
			}


		} catch (Exception e) {
			return new PowerBIRespHttp(e);
		}
	}

	public static PowerBIToken oauth(String url, String username, String password, Map<String, String> headers, Map<String, String> parameters) {
		try {
			return new PowerBIToken(url, username, password, headers, parameters);
		} catch (Exception e) {
			return null;
		}
	}

	public static PowerBIRespHttp sendGet(String url, Map<String, String> headers, String charset) {
		try {
			GetMethod httpGet = new GetMethod(url);

			for(Entry<String, String> entry: headers.entrySet()) {
				httpGet.addRequestHeader(entry.getKey(), entry.getValue());
			}
			HttpClient httpClient = new HttpClient();
			configProxy(httpClient);

			try {
				int result = httpClient.executeMethod(httpGet);

				PowerBIRespHttp resp = new PowerBIRespHttp(result, httpGet);
				resp.updateContentAsString();
				return resp;
			} finally {
				httpGet.releaseConnection();
			}
		} catch (Exception e) {
			return new PowerBIRespHttp(e);
		}
	}

	private static void configProxy(HttpClient httpClient) {
		if(hasProxy) {
			HostConfiguration config = httpClient.getHostConfiguration();
			config.setProxy(proxyHost, proxyPort);
			if(hasCredentialProxy) {
				Credentials credentials = new UsernamePasswordCredentials(proxyUser, proxyPass);
				AuthScope authScope = new AuthScope(proxyHost, proxyPort);
				httpClient.getState().setProxyCredentials(authScope, credentials);					
			}
		}
	}

}
