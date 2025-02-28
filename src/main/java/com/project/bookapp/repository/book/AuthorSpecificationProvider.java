package com.project.bookapp.repository.book;

import com.project.bookapp.dto.BookSearchParametersDto;
import com.project.bookapp.model.Book;
import com.project.bookapp.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return BookSearchParametersDto.AUTHOR;
    }

    public Specification<Book> getSpecification(String param) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root
                                .get(BookSearchParametersDto.AUTHOR)),
                        "%" + param.toLowerCase() + "%");
    }
}
