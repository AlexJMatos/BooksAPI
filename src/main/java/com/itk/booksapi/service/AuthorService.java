package com.itk.booksapi.service;

import org.springframework.data.domain.Pageable;

import com.itk.booksapi.dto.AuthorDTO;
import com.itk.booksapi.dto.PagedResult;
import com.itk.booksapi.dto.request.AuthorRequest;

public interface AuthorService {
	public PagedResult<AuthorDTO> findAll(String search, Pageable pageable);

	public AuthorDTO findById(long id);

	public AuthorDTO save(AuthorRequest authorRequest);
	
	public AuthorDTO update(long id ,AuthorRequest authorRequest);

	public void deleteById(long id);
}
