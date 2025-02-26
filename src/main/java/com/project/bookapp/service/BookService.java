package com.project.bookapp.service;

import com.project.bookapp.dto.BookDto;
import com.project.bookapp.dto.BookSearchParametersDto;
import com.project.bookapp.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    List<BookDto> findAll();

    BookDto getById(Long id);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto params);
}
