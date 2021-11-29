package com.itk.booksapi.service;

import org.springframework.data.domain.Pageable;

import com.itk.booksapi.dto.BookDTO;
import com.itk.booksapi.dto.PagedResult;
import com.itk.booksapi.dto.SearchBookDTO;
import com.itk.booksapi.dto.request.BookAddRequest;
import com.itk.booksapi.dto.request.BookUpdateRequest;

public interface BookService {
	public PagedResult<SearchBookDTO> findAll(String search, Pageable pageable);

	public BookDTO findById(String isbn);

	public BookDTO save(BookAddRequest bookRequest);

	public BookDTO update(BookUpdateRequest bookRequest);

	public void deleteById(String isbn);
}
