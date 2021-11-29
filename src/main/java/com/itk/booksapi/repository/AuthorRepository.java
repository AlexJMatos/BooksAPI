package com.itk.booksapi.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.itk.booksapi.model.Author;

public interface AuthorRepository extends PagingAndSortingRepository<Author, Long>, JpaSpecificationExecutor<Author> {

	@Query(value = "DELETE FROM isbn_authors WHERE author_id = ?1", nativeQuery = true)
	@Modifying
	public void deleteIsbnRowsFromAuthorId(long authorId);
}
