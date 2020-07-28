package net.powerbi.microsoft.auth;

public class PowerBIAuthException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public PowerBIAuthException() {
		super();
	}

	public PowerBIAuthException(String message, Throwable cause) {
		super(message, cause);
	}

	public PowerBIAuthException(String message) {
		super(message);
	}

	public PowerBIAuthException(Throwable cause) {
		super(cause);
	}
	
}
