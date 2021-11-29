package com.itk.booksapi.exception;

public class IsbnNotUniqueException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public IsbnNotUniqueException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public IsbnNotUniqueException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public IsbnNotUniqueException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
