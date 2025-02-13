package org.telosys.eclipse.plugin.core.telosys;

public class TelosysApiException extends Exception  {
	
	private static final long serialVersionUID = 1L;
	

    public TelosysApiException(String message) {
        super(message);
    }

	public TelosysApiException(String message, Throwable cause) {
		super(message, cause);
	}

}
