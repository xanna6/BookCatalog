package com.apiotrowska.databaseservice.filter;

import com.apiotrowska.databaseservice.entity.Book;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class BookSpecificationBuilder {

    private final List<BookFilter> filters;

    public BookSpecificationBuilder(List<BookFilter> filters) {
        this.filters = filters;
    }

    public final BookSpecificationBuilder with(String key, Object value){
        filters.add(new BookFilter(key, value));
        return this;
    }

    public final BookSpecificationBuilder with(BookFilter bookFilter){
        filters.add(bookFilter);
        return this;
    }

    public Specification<Book> build(){
        if(filters == null || filters.size() == 0){
            return null;
        }

        Specification<Book> result =
                new BookSpecification(filters.get(0));
        for (int idx = 1; idx < filters.size(); idx++){
            BookFilter criteria = filters.get(idx);
            result =   Specification.where(result).and(new BookSpecification(criteria));
        }
        return result;
    }
}
