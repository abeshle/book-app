package com.project.bookapp.repository.book;

import com.project.bookapp.exceptions.SpecificationProviderNotFoundException;
import com.project.bookapp.model.Book;
import com.project.bookapp.repository.SpecificationProvider;
import com.project.bookapp.repository.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new SpecificationProviderNotFoundException("Cant find correct "
                        + "specification provider for key " + key));
    }
}
