package com.project.bookapp.service;

import com.project.bookapp.dto.book.BookDto;
import com.project.bookapp.dto.book.BookDtoWithoutCategoryIds;
import com.project.bookapp.dto.book.BookSearchParametersDto;
import com.project.bookapp.dto.book.CreateBookRequestDto;
import com.project.bookapp.dto.book.UpdateBookRequestDto;
import com.project.bookapp.model.Book;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    Page<BookDto> findAll(Pageable pageable);

    BookDtoWithoutCategoryIds getById(Long id);

    void deleteById(Long id);

    BookDto updateBook(Long id, UpdateBookRequestDto bookDto);

    Page<BookDto> search(BookSearchParametersDto params, Pageable pageable);

    List<Book> getBooksByCategoryId(Long categoryId);
}
