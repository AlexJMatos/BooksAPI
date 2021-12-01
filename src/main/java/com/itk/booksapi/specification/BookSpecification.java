package com.itk.booksapi.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.itk.booksapi.model.Book;

public class BookSpecification implements Specification<Book> {
	private static final long serialVersionUID = 1L;
	private SearchCriteria searchCriteria;

	public BookSpecification(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Override
	public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		if (searchCriteria.getOperation().equals("~")) {
			return criteriaBuilder.like(root.get(searchCriteria.getKey()),
					"%" + searchCriteria.getValue().toString() + "%");
		} else if (searchCriteria.getOperation().equals("@")) {
			return criteriaBuilder.like(root.join("authors").get(searchCriteria.getKey()),
					"%" + searchCriteria.getValue().toString() + "%");
		}
		return null;
	}
}
