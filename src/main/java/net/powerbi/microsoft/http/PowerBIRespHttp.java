package net.powerbi.microsoft.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;

import org.apache.commons.httpclient.HttpMethodBase;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PowerBIRespHttp {

	private int code;
	
	private HttpMethodBase httpMethodBase;
	
	private Exception exception;
	
	private String contentAsString;
	
	public PowerBIRespHttp(int code, HttpMethodBase httpMethodBase) {
		this.code = code;
		this.httpMethodBase = httpMethodBase;
	}
	
	public void updateContentAsString() throws IOException {
		this.contentAsString = httpMethodBase.getResponseBodyAsString();
	}

	public PowerBIRespHttp(Exception exception) {
		super();
		this.exception = exception;
	}

	public int getCode() {
		return code;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getContentAsMap() throws IOException{
		updateContentAsString();
		String json = getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, Map.class);
	}

	public String getContentAsString() throws IOException {
		return this.contentAsString;
	}

	public InputStream getContentAsStream() throws IOException {
		return httpMethodBase.getResponseBodyAsStream();
	}

	public byte[] getContentAsByteArray() throws IOException {
		return httpMethodBase.getResponseBody();
	}

	public String getContentAsBase64String() throws IOException {
		return Base64.getEncoder().encodeToString(httpMethodBase.getResponseBody());
	}

	public Exception getException() {
		return exception;
	}
}
