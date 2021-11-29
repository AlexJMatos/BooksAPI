package com.itk.booksapi.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "books")
public class Book {
	// Define fields
	@Id
	@Column(name = "isbn")
	@NotNull(message = "ISBN cannot be null")
	@Size(min = 13, max = 13, message = "ISBN should be 13 digits")
	private String isbn;
	
	@Column(name = "title")
	@NotNull(message = "Title cannot be null")
	@Size(min = 1, max = 50, message = "The title must not exceed 50 characters")
	private String title;
	
	@Column(name = "editorial")
	@NotNull(message = "Editorial cannot be null")
	@Size(min = 1, max = 50, message = "The editoral must not exceed 50 characters")
	private String editorial;
	
	@Column(name = "edition_number")
	@NotNull(message = "Edition number cannot be null")
	private int editionNumber;
	
	@Column(name = "copyright")
	@NotNull(message = "Copyright year cannot be null")
	private int copyright;
	
	@ElementCollection
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "isbn_authors", joinColumns = {@JoinColumn(name = "isbn")}, 
	inverseJoinColumns = {@JoinColumn(name = "author_id")})
	private Set<Author> authors = new HashSet<>();
	
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

	public Set<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}
}