package com.project.bookapp.repository.book;

import com.project.bookapp.dto.book.BookSearchParametersDto;
import com.project.bookapp.model.Book;
import com.project.bookapp.repository.SpecificationBuilder;
import com.project.bookapp.repository.SpecificationProviderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    @Autowired
    private SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.author() != null && searchParameters.author().length() > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(BookSearchParametersDto.AUTHOR)
                    .getSpecification(searchParameters.author()));
        }
        if (searchParameters.isbn() != null && searchParameters.isbn().length() > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(BookSearchParametersDto.ISBN)
                    .getSpecification(searchParameters.isbn()));
        }
        if (searchParameters.title() != null && searchParameters.title().length() > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(BookSearchParametersDto.TITLE)
                    .getSpecification(searchParameters.title()));
        }

        return spec;
    }
}
