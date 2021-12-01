package com.itk.booksapi.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.itk.booksapi.model.Book;

public interface BookRepository extends PagingAndSortingRepository<Book, String>, JpaSpecificationExecutor<Book> {
	@Query(value = "INSERT INTO isbn_authors (author_id, isbn) VALUES (?1, ?2); ", nativeQuery = true)
	@Modifying
	public void addIsbnAuthorRow(long authorId, String isbn);

	@Query(value = "DELETE FROM isbn_authors WHERE isbn = ?1", nativeQuery = true)
	@Modifying
	public void deleteIsbnAuthorRowFromIsbn(String isbn);
}
