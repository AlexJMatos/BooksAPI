package com.itk.booksapi.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itk.booksapi.dto.BookDTO;
import com.itk.booksapi.dto.PagedResult;
import com.itk.booksapi.dto.error.ErrorDTO;
import com.itk.booksapi.dto.request.BookAddRequest;
import com.itk.booksapi.dto.request.BookUpdateRequest;
import com.itk.booksapi.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@Validated
@RequestMapping("/books")
public class BookController {
	private BookService bookService;

	@Autowired
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping
	@Operation(summary = "Get all books or search an specific book based on isbn, title, editorial, author name and last name", responses = {
			@ApiResponse(description = "Books found successfully", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedResult.class))),
			@ApiResponse(description = "No book found with search criteria.", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))) }, parameters = {
					@Parameter(name = "search", required = false, description = "A request parameter to search in isbn, title, editorial from the book and name or last name from the authors"),
					@Parameter(name = "pageable", required = false, description = "The request query for sorting, pageSize, pageNumber, offset and paged", content = @Content(schema = @Schema(implementation = Pageable.class)))})
	public ResponseEntity<Object> findAll(@RequestParam(name = "search", required = false) String search,
			Pageable pageable) {
		return new ResponseEntity<>(bookService.findAll(search, pageable), HttpStatus.OK);
	}

	/* 3. Get an book by ISBN */
	@GetMapping("/{bookIsbn}")
	@Operation(summary = "Get the book with the ISBN specified in the path variable", responses = {
			@ApiResponse(description = "Book found successfully", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class))),
			@ApiResponse(description = "Book not found", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(description = "ISBN must be 13 digits", responseCode = "400", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))) }, parameters = {
					@Parameter(name = "bookIsbn", required = true, description = "The 13 digits number to identify the book") })
	public ResponseEntity<Object> findById(
			@PathVariable("bookIsbn") @NotNull @Size(min = 13, max = 13, message = "ISBN must be 13 digits") @Pattern(regexp = "^(0|[1-9][0-9]*)$") String bookIsbn) {
		BookDTO foundBook = bookService.findById(bookIsbn);
		return ResponseEntity.ok(foundBook);
	}

	/* 4. Add a book */
	@PostMapping
	@Operation(summary = "Add a new book to the database", responses = {
			@ApiResponse(description = "Book created successfully", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class))),
			@ApiResponse(description = "Missing JSON fields. Bad request", responseCode = "400", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))) })
	public ResponseEntity<Object> addBook(@RequestBody @Valid BookAddRequest bookRequest) {
		return new ResponseEntity<>(bookService.save(bookRequest), HttpStatus.CREATED);
	}

	/* 5. Update a book */
	@PutMapping("/{bookIsbn}")
	@Operation(summary = "Update an book in the JSON body", responses = {
			@ApiResponse(description = "Book updated successfully", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class))),
			@ApiResponse(description = "Book not found", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(description = "Missing JSON fields", responseCode = "400", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))) }, parameters = {
					@Parameter(name = "bookIsbn", required = true, description = "The ISBN from the book to be updated") })
	public ResponseEntity<Object> updateBook(
			@PathVariable("bookIsbn") @NotNull @Size(min = 13, max = 13, message = "ISBN must be 13 digits") @Pattern(regexp = "^(0|[1-9][0-9]*)$") String bookIsbn,
			@RequestBody @Valid BookUpdateRequest bookRequest) {
		bookRequest.setIsbn(bookIsbn);
		return new ResponseEntity<>(bookService.update(bookRequest), HttpStatus.OK);
	}

	/* 6. Delete a book */
	@DeleteMapping("/{bookIsbn}")
	@Operation(summary = "Delete an book from the database", responses = {
			@ApiResponse(description = "Book deleted successfully", responseCode = "204"),
			@ApiResponse(description = "Book with given ISBN not found", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(description = "ISBN must be 13 digits", responseCode = "400", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))) }, parameters = {
					@Parameter(name = "bookIsbn", required = true, description = "The Book ISBN to be deleted") })
	public ResponseEntity<Object> deleteBook(
			@PathVariable("bookIsbn") @NotNull @Size(min = 13, max = 13, message = "ISBN must be 13 digits") @Pattern(regexp = "^(0|[1-9][0-9]*)$") String bookIsbn) {
		bookService.deleteById(bookIsbn);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}