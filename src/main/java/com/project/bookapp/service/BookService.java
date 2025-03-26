package com.project.bookapp.service;

import com.project.bookapp.dto.book.BookDto;
import com.project.bookapp.dto.book.BookDtoWithoutCategoryIds;
import com.project.bookapp.dto.book.BookSearchParametersDto;
import com.project.bookapp.dto.book.CreateBookRequestDto;
import com.project.bookapp.dto.book.UpdateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    Page<BookDto> findAll(Pageable pageable);

    BookDto getById(Long id);

    void deleteById(Long id);

    BookDto updateBook(Long id, UpdateBookRequestDto bookDto);

    Page<BookDto> search(BookSearchParametersDto params, Pageable pageable);

    Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(Pageable pageable, Long id);
}
