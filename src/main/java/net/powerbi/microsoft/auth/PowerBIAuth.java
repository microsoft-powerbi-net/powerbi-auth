package net.powerbi.microsoft.auth;


public class PowerBIAuth {

	private String url;
	
	private String clientId;
	
	private String grantType;
	
	private String username;
	
	private String password;
	
	private String scope;
	
	private String resource;
	
	private String reportsByGroup;
	
	private String groups;
	
	private String generateToken;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getReportsByGroup() {
		return reportsByGroup;
	}

	public void setReportsByGroup(String reportsByGroup) {
		this.reportsByGroup = reportsByGroup;
	}

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

	public String getGenerateToken() {
		return generateToken;
	}

	public void setGenerateToken(String generateToken) {
		this.generateToken = generateToken;
	}
	
}
