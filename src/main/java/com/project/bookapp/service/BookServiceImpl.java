package com.project.bookapp.service;

import com.project.bookapp.dto.book.BookDto;
import com.project.bookapp.dto.book.BookDtoWithoutCategoryIds;
import com.project.bookapp.dto.book.BookSearchParametersDto;
import com.project.bookapp.dto.book.CreateBookRequestDto;
import com.project.bookapp.dto.book.UpdateBookRequestDto;
import com.project.bookapp.exceptions.EntityNotFoundException;
import com.project.bookapp.mapper.BookMapper;
import com.project.bookapp.model.Book;
import com.project.bookapp.repository.book.BookRepository;
import com.project.bookapp.repository.book.BookSpecificationBuilder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto book) {
        return bookMapper.toDto(bookRepository.save(bookMapper.toModel(book)));
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookDto getById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toDto).orElseThrow(() ->
                new EntityNotFoundException("Entity with id " + id + " not found"));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateBook(Long id, UpdateBookRequestDto bookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Entity with id " + id + " not found"));

        bookMapper.updateBookFromDto(bookDto, book);
        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    public Page<BookDto> search(BookSearchParametersDto params, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(bookSpecification, pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Pageable pageable, Long id) {
        return bookRepository.findAllByCategoryId(pageable, id)
                .stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
