package com.itk.booksapi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.itk.booksapi.dto.AuthorDTO;
import com.itk.booksapi.dto.PagedResult;
import com.itk.booksapi.dto.request.AuthorRequest;
import com.itk.booksapi.exception.ElementNotFoundException;
import com.itk.booksapi.model.Author;
import com.itk.booksapi.model.Author_;
import com.itk.booksapi.repository.AuthorRepository;
import com.itk.booksapi.service.AuthorService;
import com.itk.booksapi.specification.AuthorSpecification;
import com.itk.booksapi.specification.SearchCriteria;

@Service
public class AuthorServiceImpl implements AuthorService {

	private AuthorRepository authorRepository;

	@Autowired
	public AuthorServiceImpl(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}

	@Override
	public PagedResult<AuthorDTO> findAll(String search, Pageable pageable) {
		/* Create an specification for authors */
		Specification<Author> authorSpecification = Specification.where(null);
		
		/* Search the parameter in the name and last name of the author */
		if (search != null) {
			Specification<Author> nameSpecification = Specification
					.where(new AuthorSpecification(new SearchCriteria(Author_.NAME, "~", search)));
			authorSpecification = authorSpecification.or(nameSpecification);

			Specification<Author> lastNameSpecification = Specification
					.where(new AuthorSpecification(new SearchCriteria(Author_.LAST_NAME, "~", search)));
			authorSpecification = authorSpecification.or(lastNameSpecification);
		}

		/* Find all authors with the given specification */
		Page<Author> pageAuthors = authorRepository.findAll(authorSpecification, pageable);
		
		/* Throw an exception in case no author was found */
		if (pageAuthors.isEmpty()) {
			throw new ElementNotFoundException("No author found with given search parameter");
		}
		
		/* Create the PagedResult DTO for the response */
		List<AuthorDTO> listAuthors = new ArrayList<>();
		for (Author author : pageAuthors.getContent()) {
			AuthorDTO authorDTO = new AuthorDTO();
			authorDTO.setId(author.getId());
			authorDTO.setName(author.getName());
			authorDTO.setLastName(author.getLastName());
			listAuthors.add(authorDTO);
		}
		PagedResult<AuthorDTO> authorsDTO = new PagedResult<>();
		authorsDTO.setPageNumber(pageAuthors.getNumber());
		authorsDTO.setPageSize(pageAuthors.getSize());
		authorsDTO.setTotalElements(pageAuthors.getTotalElements());
		authorsDTO.setTotalPages(pageAuthors.getTotalPages());
		authorsDTO.setData(listAuthors);
		return authorsDTO;
	}

	@Override
	public AuthorDTO findById(long id) {
		/* Find the author */
		Optional<Author> result = authorRepository.findById(id);
		Author author = null;
		AuthorDTO authorDTO = new AuthorDTO();
		
		/* If present then create the DTO and return the object */
		if (result.isPresent()) {
			author = result.get();
			authorDTO.setId(author.getId());
			authorDTO.setName(author.getName());
			authorDTO.setLastName(author.getLastName());
			return authorDTO;
		} else { // Otherwise, throw an exception
			throw new ElementNotFoundException("Author with ID - " + id + " not found");
		}
	}

	@Override
	public AuthorDTO save(AuthorRequest authorAddRequest) {
		/* Set the values for the new author and save*/
		Author author = new Author();
		author.setName(authorAddRequest.getName());
		author.setLastName(authorAddRequest.getLastName());
		author = authorRepository.save(author);

		// Return the Author DTO as a response 
		AuthorDTO authorDTO = new AuthorDTO();
		authorDTO.setId(author.getId());
		authorDTO.setName(author.getName());
		authorDTO.setLastName(author.getLastName());

		return authorDTO;
	}

	@Override
	@Transactional(rollbackOn = ElementNotFoundException.class)
	public void deleteById(long id) {
		// Check if the author id exists
		if (authorRepository.existsById(id)) {
			// Delete the ISBN records from the isbn_authors table
			authorRepository.deleteIsbnRowsFromAuthorId(id);

			// Delete the author
			authorRepository.deleteById(id);
		} else { // If no author found then throw an exception
			throw new ElementNotFoundException("Author with ID - " + id + " not found");
		}		
	}

	@Override
	public AuthorDTO update(long id, AuthorRequest authorRequest) {
		// If the author already exists, then it is possible to do an update
		// Else, throw the NoElementFoundException inside the findById method
		// In case the user tries to update an author with no id defined
		if (authorRepository.existsById(id)) {
			Author author = new Author();
			author.setId(id);
			author.setName(authorRequest.getName());
			author.setLastName(authorRequest.getLastName());
			author = authorRepository.save(author);

			// Return the Author DTO
			AuthorDTO authorDTO = new AuthorDTO();
			authorDTO.setId(author.getId());
			authorDTO.setName(author.getName());
			authorDTO.setLastName(author.getLastName());

			return authorDTO;
		}
		else {
			throw new ElementNotFoundException("Author with ID - " + id + " not found");
		}
	}
}
