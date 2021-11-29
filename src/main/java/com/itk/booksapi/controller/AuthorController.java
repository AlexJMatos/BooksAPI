package com.itk.booksapi.controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;

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

import com.itk.booksapi.dto.AuthorDTO;
import com.itk.booksapi.dto.PagedResult;
import com.itk.booksapi.dto.error.ErrorDTO;
import com.itk.booksapi.dto.request.AuthorRequest;
import com.itk.booksapi.service.AuthorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@Validated
@RequestMapping("/authors")
public class AuthorController {

	private AuthorService authorService;

	@Autowired
	public AuthorController(AuthorService authorService) {
		this.authorService = authorService;
	}

	@GetMapping
	@Operation(summary = "Get all authors or search authors by request parameter", responses = {
			@ApiResponse(description = "Authors found successfully", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedResult.class))),
			@ApiResponse(description = "Author not found", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))) }, parameters = {
					@Parameter(name = "search", required = false, description = "A request parameter to search in name and last name of the authors"),
					@Parameter(name = "size", required = false, description = "The size of the page"),
					@Parameter(name = "sort", required = false, description = "The request parameter to sort by id, name or last name"),
					@Parameter(name = "pageNumber", required = false, description = "The page to show in the json response") })
	public ResponseEntity<Object> findAll(@RequestParam(name = "search", required = false) String search,
			Pageable pageable) {
		return new ResponseEntity<>(authorService.findAll(search, pageable), HttpStatus.OK);
	}

	/* 7. Get author by ID */
	@GetMapping("/{authorId}")
	@Operation(summary = "Get the author with the ID specified in the path variable", responses = {
			@ApiResponse(description = "Author found successfully", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthorDTO.class))),
			@ApiResponse(description = "Author not found", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))) }, parameters = {
					@Parameter(name = "authorId", required = true, description = "The ID greater than 0 for the author to search") })
	public ResponseEntity<Object> findById(@PathVariable("authorId") @Min(1) long authorId) {
		AuthorDTO author = authorService.findById(authorId);
		return ResponseEntity.ok(author);
	}

	/* 8. Add an author */
	@PostMapping
	@Operation(summary = "Add a new author to the database", responses = {
			@ApiResponse(description = "Author created successfully", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthorDTO.class))),
			@ApiResponse(description = "Missing JSON fields", responseCode = "400", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))) })
	public ResponseEntity<Object> addAuthor(@RequestBody @Valid AuthorRequest authorRequest) {
		return new ResponseEntity<>(authorService.save(authorRequest), HttpStatus.CREATED);
	}

	/* 9. Update an author */
	@PutMapping("/{authorId}")
	@Operation(summary = "Update an author in the JSON body", responses = {
			@ApiResponse(description = "Author updated successfully", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthorDTO.class))),
			@ApiResponse(description = "Author with ID entered not found", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(description = "Missing JSON fields", responseCode = "400", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))) }, parameters = {
					@Parameter(name = "authorId", required = true, description = "The ID from the author to be updated") })
	public ResponseEntity<Object> updateAuthor(@PathVariable("authorId") @Min(1) long authorId,
			@RequestBody @Valid AuthorRequest authorRequest) {
		return new ResponseEntity<>(authorService.update(authorId, authorRequest), HttpStatus.OK);
	}

	/* 10. Delete an author */
	@DeleteMapping("/{authorId}")
	@Operation(summary = "Delete an author from the database", responses = {
			@ApiResponse(description = "Author deleted successfully", responseCode = "204"),
			@ApiResponse(description = "Author with given ID not found", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))) }, parameters = {
					@Parameter(name = "authorId", required = true, description = "The ID from the author to be deleted") })
	public ResponseEntity<Object> deleteAuthorById(@PathVariable("authorId") @Min(1) long authorId) {
		authorService.deleteById(authorId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
