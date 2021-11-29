package com.itk.booksapi.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/* The add author request INSERTS a new row 
 * The ID information is not needed because the
 * ID is auto incremental and unique */
public class AuthorRequest {

	@NotNull(message = "Name must not be null")
	@Size(min = 1, max = 50, message = "Name must be between 1 and 50 chars")
	private String name;

	@NotNull(message = "Last name must not be null")
	@Size(min = 1, max = 50, message = "Last name must be between 1 and 50 chars")
	private String lastName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
