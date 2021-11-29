package com.itk.booksapi.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/* Custom request for adding a book, all book information 
 * is needed and also the ID from the author that the book
 * is associated with */
public class BookUpdateRequest {
	
	// Not required in the body. Added to the request using the path variable
	@Size(min = 13, max = 13, message = "ISBN should be 13 digits")
	private String isbn;

	@NotNull(message = "Title cannot be null")
	@Size(min = 1, max = 50, message = "The title must be between 1 and 50 characters")
	private String title;

	@NotNull(message = "Editorial cannot be null")
	@Size(min = 1, max = 50, message = "The editoral must be between 1 and 50 characters")
	private String editorial;

	@NotNull(message = "Edition number cannot be null")
	private Integer editionNumber;

	@NotNull(message = "Copyright year cannot be null")
	private Integer copyright;

	// Optional: In case the we mistake the authors of the book. 
	// If it is given, it will update the book and overwrite the isbn_authors table with the new authors - books relationship
	// If not given, then simply updates the book 
	private List<Long> authorsId;

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEditorial() {
		return editorial;
	}

	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}

	public int getEditionNumber() {
		return editionNumber;
	}

	public void setEditionNumber(int editionNumber) {
		this.editionNumber = editionNumber;
	}

	public int getCopyright() {
		return copyright;
	}

	public void setCopyright(int copyright) {
		this.copyright = copyright;
	}

	public List<Long> getAuthorsId() {
		return authorsId;
	}

	public void setAuthorsId(List<Long> authorsId) {
		this.authorsId = authorsId;
	}
}
