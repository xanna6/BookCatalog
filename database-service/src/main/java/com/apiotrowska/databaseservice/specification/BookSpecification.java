package com.apiotrowska.databaseservice.specification;

import com.apiotrowska.common.model.BookFilter;
import com.apiotrowska.databaseservice.entity.Book;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification implements Specification<Book> {

    private final BookFilter bookFilter;

    public BookSpecification(BookFilter bookFilter) {
        this.bookFilter = bookFilter;
    }

    @Override
    public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        String strToSearch = bookFilter.getValue()
                .toString().toLowerCase();

        return criteriaBuilder.like(criteriaBuilder.lower(root
                        .get(bookFilter.getFilterKey())),
                "%" + strToSearch + "%");
    }
}
