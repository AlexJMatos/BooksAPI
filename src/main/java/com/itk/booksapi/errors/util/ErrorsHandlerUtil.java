package com.itk.booksapi.errors.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.validation.FieldError;


public class ErrorsHandlerUtil {
	
	public static String getConciseErrorMessage(List<FieldError> fieldErrors) {
		StringBuilder sb = new StringBuilder();
		for (FieldError error : fieldErrors) {
			sb.append(error.getDefaultMessage());
			sb.append(" - ");
		}
		sb.delete(sb.length() - 3, sb.length() - 1);
		return sb.toString().trim();
	}
	
	public static String getConciseErrorMessage(String result) {
		String[] temp = result.split(":");
		if (temp.length > 1) {
			String message = IntStream.range(0, temp.length).filter(i -> i % 2 != 0).mapToObj(i -> temp[i])
					.collect(Collectors.joining(" - "));
			return message.trim();
		} else {
			return result;
		}
	}
}
