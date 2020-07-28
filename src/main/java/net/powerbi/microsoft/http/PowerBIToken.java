package net.powerbi.microsoft.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

public class PowerBIToken {

	private String accessToken;
	
	private String tokenType;
	
	private long expiresIn;
	
	private long timestamp;
	
	private List<String> scope = new ArrayList<>();
	
	private String tenant;
	
	private int status;
	
	private int result;
	
	private String error;
	
	private String message;
	
	private String url;
	
	private String username;
	
	private String password;
	
	private Map<String, Object> extra = new HashMap<String, Object>();
	
	private Map<String, String> parameters = new HashMap<String, String>();
	
	private Map<String, String> headers = new HashMap<String, String>();
	
	public PowerBIToken(String url, String username, String password, Map<String, String> headers, Map<String, String> parameters) throws HttpException, IOException {
		this.url = url;
		this.username = username;
		this.password = password;
		this.parameters = parameters;
		this.headers = headers;
		request();
	}

	@SuppressWarnings("unchecked")
	private void request() throws IOException, HttpException {
		PostMethod httpPost = new PostMethod(url);

		httpPost.addParameter("username", username);
		httpPost.addParameter("password", password);
		if(parameters != null) {
			for(Entry<String, String> entry : this.parameters.entrySet()) {
				httpPost.addParameter(entry.getKey(), entry.getValue());
			}
		}
		if(headers != null) {
			for(Entry<String, String> entry : this.headers.entrySet()) {
				httpPost.addRequestHeader(entry.getKey(), entry.getValue());
			}
		}

		
		HttpClient httpClient = new HttpClient();
		this.result = httpClient.executeMethod(httpPost);

		PowerBIRespHttp resp = new PowerBIRespHttp(result, httpPost);
		Map<String, Object> mapResp = resp.getContentAsMap();
		String accessToken = (String) mapResp.get("access_token");
		if(accessToken != null) {
			this.setAccessToken(accessToken);
			this.setTenant((String) mapResp.get("tenant"));
			this.setTokenType((String) mapResp.get("token_type"));
			this.setExpiresIn(Integer.valueOf(mapResp.get("expires_in").toString()));
			this.timestamp = System.currentTimeMillis();
			String scope = (String) mapResp.get("scope");
			if(scope != null) {
				String [] scopeData = scope.split(" ");
				for (int i = 0; i < scopeData.length; i++) {
					this.scope.add(scopeData[i]);
				}
			}
			if(mapResp.containsKey("extra")) {
				this.extra.putAll((Map<? extends String, ? extends Object>) mapResp.get("extra"));
			}
		}else {
			this.setError((String) mapResp.get("error"));
			this.setStatus(resp.getCode());
			this.setMessage((String) mapResp.get("message"));
		}

		System.out.println(httpPost.getResponseBodyAsString());
		httpPost.releaseConnection();
	}
	
	public int getSecondsRemaining() {
		int elapsed = (int) (System.currentTimeMillis() - this.timestamp) / 1000;
		return (int) (this.expiresIn - elapsed);
	}
	
	public void validate() throws HttpException, IOException {
		System.out.printf("Remaining: %d\n", getSecondsRemaining());
		if(getSecondsRemaining() < 30) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			request();
			System.out.println("new token .... " + accessToken);
			
		}
		
		if(error != null) {
			request();
		}
	}
	

	public String getAccessToken() {
		return accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public List<String> getScope() {
		return scope;
	}

	public String getTenant() {
		return tenant;
	}

	void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

	void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	void setScope(List<String> scope) {
		this.scope = scope;
	}

	void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public int getStatus() {
		return status;
	}

	public String getError() {
		return error;
	}

	public String getMessage() {
		return message;
	}

	void setStatus(int status) {
		this.status = status;
	}

	void setError(String error) {
		this.error = error;
	}

	void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getExtra() {
		return extra;
	}

	
}
