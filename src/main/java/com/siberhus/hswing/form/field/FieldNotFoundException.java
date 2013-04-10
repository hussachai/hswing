package com.siberhus.hswing.form.field;

public class FieldNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public FieldNotFoundException() {
		super();
	}

	public FieldNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public FieldNotFoundException(String message) {
		super(message);
	}

	public FieldNotFoundException(Throwable cause) {
		super(cause);
	}
	
	
}
