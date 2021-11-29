package com.itk.booksapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	@Query(value = "SELECT books.* FROM (( isbn_authors INNER JOIN books ON books.isbn = isbn_authors.isbn) "
			+ "INNER JOIN authors ON authors.id = isbn_authors.author_id) WHERE authors.name LIKE %?1% OR authors.last_name LIKE %?1% OR books.isbn LIKE %?1% OR books.title LIKE %?1% OR books.editorial LIKE %?1%", nativeQuery = true)
	public Page<Book> searchBooks(String search, Pageable pageable);
}
