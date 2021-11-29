package com.itk.booksapi.controller.exceptionhandler;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.itk.booksapi.dto.error.ErrorDTO;
import com.itk.booksapi.errors.util.ErrorsHandlerUtil;
import com.itk.booksapi.exception.ElementNotFoundException;
import com.itk.booksapi.exception.IsbnNotUniqueException;

/* Controller to handle all exceptions that may occur when using the API 
 * It handles custom exception like ElementNotFoundException and IsbnNotUniqueException 
 * It handles validation from the JSON Body entered using the MethodArgumentNotValidException 
 * It handles all other general exception */

@ControllerAdvice
public class AppControllerExceptionHandler {

	// Exception handler if the element is not found
	@ExceptionHandler
	public ResponseEntity<ErrorDTO> handleElementNotFoundException(ElementNotFoundException ex) {
		
		// Create a book or author error response
		ErrorDTO errorResponse = new ErrorDTO();

		// Setting the values for the JSON error response
		errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
		errorResponse.setMessage(ex.getMessage());
		errorResponse.setTimeStamp(System.currentTimeMillis());

		// Return the response
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleException(MethodArgumentNotValidException ex) {

		// Create a general error response
		ErrorDTO errorResponse = new ErrorDTO();

		// Setting the values for the JSON error response
		errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		errorResponse.setMessage(ErrorsHandlerUtil.getConciseErrorMessage(ex.getFieldErrors()));
		errorResponse.setTimeStamp(System.currentTimeMillis());

		// Return the response
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleException(MethodArgumentTypeMismatchException ex) {

		// Create a general error response
		ErrorDTO errorResponse = new ErrorDTO();

		// Setting the values for the JSON error response
		errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		errorResponse.setMessage(ex.getMessage());
		errorResponse.setTimeStamp(System.currentTimeMillis());

		// Return the response
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleException(ConstraintViolationException ex) {

		// Create a general error response
		ErrorDTO errorResponse = new ErrorDTO();

		// Setting the values for the JSON error response
		errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		errorResponse.setMessage(ErrorsHandlerUtil.getConciseErrorMessage(ex.getMessage()));
		errorResponse.setTimeStamp(System.currentTimeMillis());

		// Return the response
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(IsbnNotUniqueException.class)
	public ResponseEntity<ErrorDTO> handleIsbnNotUniqueException(IsbnNotUniqueException ex) {
		
		// Create a Book error response
		ErrorDTO errorResponse = new ErrorDTO();

		// Setting the values for the JSON error response
		errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		errorResponse.setMessage(ex.getMessage());
		errorResponse.setTimeStamp(System.currentTimeMillis());

		// Return the response
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(Exception ex) {

		// Create a general error response
		ErrorDTO errorResponse = new ErrorDTO();

		// Setting the values for the JSON error response
		errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		errorResponse.setMessage(ErrorsHandlerUtil.getConciseErrorMessage(ex.getMessage()));
		errorResponse.setTimeStamp(System.currentTimeMillis());

		// Return the response
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
}
