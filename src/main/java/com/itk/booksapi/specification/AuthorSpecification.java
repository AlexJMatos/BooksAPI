package com.itk.booksapi.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.itk.booksapi.model.Author;

public class AuthorSpecification implements Specification<Author> {
	private static final long serialVersionUID = 1L;
	private SearchCriteria searchCriteria;

	public AuthorSpecification(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Override
	public Predicate toPredicate(Root<Author> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		if (searchCriteria.getOperation().equals("~")) {
			return criteriaBuilder.like(root.get(searchCriteria.getKey()),
					"%" + searchCriteria.getValue().toString() + "%");
		}
		return null;
	}

}
