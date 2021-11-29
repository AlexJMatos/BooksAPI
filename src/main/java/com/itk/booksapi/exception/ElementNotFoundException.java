package com.itk.booksapi.exception;

public class ElementNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public ElementNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ElementNotFoundException(String message) {
		super(message);
	}

	public ElementNotFoundException(Throwable cause) {
		super(cause);
	}
}
