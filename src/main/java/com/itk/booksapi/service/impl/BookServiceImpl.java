package com.itk.booksapi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itk.booksapi.dto.AuthorDTO;
import com.itk.booksapi.dto.BookDTO;
import com.itk.booksapi.dto.PagedResult;
import com.itk.booksapi.dto.SearchBookDTO;
import com.itk.booksapi.dto.request.BookAddRequest;
import com.itk.booksapi.dto.request.BookUpdateRequest;
import com.itk.booksapi.exception.ElementNotFoundException;
import com.itk.booksapi.exception.IsbnNotUniqueException;
import com.itk.booksapi.model.Author;
import com.itk.booksapi.model.Book;
import com.itk.booksapi.repository.AuthorRepository;
import com.itk.booksapi.repository.BookRepository;
import com.itk.booksapi.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	private BookRepository bookRepository;
	private AuthorRepository authorRepository;

	@Autowired
	public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
		this.bookRepository = bookRepository;
		this.authorRepository = authorRepository;
	}

	@Override
	public PagedResult<SearchBookDTO> findAll(String search, Pageable pageable) {
		Page<Book> pageBooks = null;

		if (search != null) {
			pageBooks = bookRepository.searchBooks(search, pageable);
		} else {
			pageBooks = bookRepository.findAll(pageable);
		}
		
		if (pageBooks.isEmpty()) {
			throw new ElementNotFoundException("No Book found with given search parameter");
		}

		List<SearchBookDTO> listBooks = new ArrayList<>();
		for (Book book : pageBooks.getContent()) {
			SearchBookDTO bookDTO = new SearchBookDTO();
			bookDTO.setIsbn(book.getIsbn());
			bookDTO.setTitle(book.getTitle());
			bookDTO.setEditorial(book.getEditorial());
			bookDTO.setEditionNumber(book.getEditionNumber());
			bookDTO.setCopyright(book.getCopyright());
			List<AuthorDTO> authorDTOs = new ArrayList<>();
			for (Author author : book.getAuthors()) {
				AuthorDTO authorDTO = new AuthorDTO();
				authorDTO.setId(author.getId());
				authorDTO.setName(author.getName());
				authorDTO.setLastName(author.getLastName());
				authorDTOs.add(authorDTO);
			}
			bookDTO.setAuthors(authorDTOs);
			listBooks.add(bookDTO);
		}
		PagedResult<SearchBookDTO> booksDTO = new PagedResult<>();
		booksDTO.setPageNumber(pageBooks.getNumber());
		booksDTO.setPageSize(pageBooks.getSize());
		booksDTO.setTotalElements(pageBooks.getTotalElements());
		booksDTO.setTotalPages(pageBooks.getTotalPages());
		booksDTO.setData(listBooks);
		return booksDTO;
	}

	@Override
	public BookDTO findById(String isbn) {
		Optional<Book> result = bookRepository.findById(isbn);
		Book book = null;
		BookDTO bookDTO = new BookDTO();
		if (result.isPresent()) {
			book = result.get();
			bookDTO.setIsbn(book.getIsbn());
			bookDTO.setTitle(book.getTitle());
			bookDTO.setEditorial(book.getEditorial());
			bookDTO.setEditionNumber(book.getEditionNumber());
			bookDTO.setCopyright(book.getCopyright());
			return bookDTO;
		} else {
			throw new ElementNotFoundException("Book with ISBN - " + isbn + " not found");
		}
	}

	@Override
	@Transactional(rollbackOn = IsbnNotUniqueException.class)
	public BookDTO save(BookAddRequest bookRequest) {
		BookDTO bookResponse = new BookDTO();
		boolean uniqueIsbn = false;
		boolean authorExists = false;

		// Check if the ISBN is unique
		if (!bookRepository.existsById(bookRequest.getIsbn())) {
			uniqueIsbn = true;
		} else {
			throw new IsbnNotUniqueException("Book with ISBN " + bookRequest.getIsbn() + " already registered.");
		}

		// Check if all the authors id exist
		for (Long authorId : bookRequest.getAuthorsId()) {
			if (!authorRepository.existsById(authorId)) {
				throw new ElementNotFoundException("Associated author with ID " + authorId + " not found.");
			}
		}
		authorExists = true;

		// Add the book
		if (uniqueIsbn && authorExists) {
			Book book = new Book();
			book.setIsbn(bookRequest.getIsbn());
			book.setTitle(bookRequest.getTitle());
			book.setEditorial(bookRequest.getEditorial());
			book.setEditionNumber(bookRequest.getEditionNumber());
			book.setCopyright(bookRequest.getCopyright());
			book = bookRepository.save(book);

			// Add the authorId - ISBN to the IsbnAuthors table
			for (Long id : bookRequest.getAuthorsId()) {
				bookRepository.addIsbnAuthorRow(id, book.getIsbn());
			}

			// Return the DTO response
			bookResponse.setIsbn(book.getIsbn());
			bookResponse.setTitle(book.getTitle());
			bookResponse.setEditorial(book.getEditorial());
			bookResponse.setEditionNumber(book.getEditionNumber());
			bookResponse.setCopyright(book.getCopyright());
		}
		return bookResponse;
	}

	@Override
	@Transactional(rollbackOn = ElementNotFoundException.class)
	public BookDTO update(BookUpdateRequest bookRequest) {
		BookDTO bookResponse = new BookDTO();
		boolean uniqueIsbn = false;
		boolean authorExists = false;

		// Check if the ISBN is unique
		if (bookRepository.existsById(bookRequest.getIsbn())) {
			uniqueIsbn = true;
		} else {
			throw new ElementNotFoundException("Cannot update. Book with ISBN " + bookRequest.getIsbn() + " not found");
		}

		// Check if all the authors id exist
		if (bookRequest.getAuthorsId() != null) {
			for (Long authorId : bookRequest.getAuthorsId()) {
				if (!authorRepository.existsById(authorId)) {
					throw new ElementNotFoundException("Associated author with ID " + authorId + " not found.");
				}
			}
		}
		authorExists = true;

		// Add the book
		if (uniqueIsbn && authorExists) {
			Book book = new Book();
			book.setIsbn(bookRequest.getIsbn());
			book.setTitle(bookRequest.getTitle());
			book.setEditorial(bookRequest.getEditorial());
			book.setEditionNumber(bookRequest.getEditionNumber());
			book.setCopyright(bookRequest.getCopyright());
			book = bookRepository.save(book);

			// Add the authorId - ISBN to the IsbnAuthors table
			if (bookRequest.getAuthorsId() != null) {
				bookRepository.deleteIsbnAuthorRowFromIsbn(bookRequest.getIsbn());
				for (Long id : bookRequest.getAuthorsId()) {
					bookRepository.addIsbnAuthorRow(id, book.getIsbn());
				}
			}

			// Return the DTO response
			bookResponse.setIsbn(book.getIsbn());
			bookResponse.setTitle(book.getTitle());
			bookResponse.setEditorial(book.getEditorial());
			bookResponse.setEditionNumber(book.getEditionNumber());
			bookResponse.setCopyright(book.getCopyright());
		}
		return bookResponse;
	}

	@Override
	@Transactional(rollbackOn = ElementNotFoundException.class)
	public void deleteById(String isbn) {
		// Check if the ISBN exits
		if (bookRepository.existsById(isbn)) {

			// Delete the row from the ISBN - Authors Table
			bookRepository.deleteIsbnAuthorRowFromIsbn(isbn);

			// Delete the row from the books Table
			bookRepository.deleteById(isbn);

		} else {
			throw new ElementNotFoundException("Book with ISBN " + isbn + " not found");
		}
	}
}
