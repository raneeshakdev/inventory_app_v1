package com.svym.inventory.service.exception;

public class DataNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3352693265302962788L;

	public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
