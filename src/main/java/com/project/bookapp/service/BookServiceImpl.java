package com.project.bookapp.service;

import com.project.bookapp.dto.BookDto;
import com.project.bookapp.dto.BookSearchParametersDto;
import com.project.bookapp.dto.CreateBookRequestDto;
import com.project.bookapp.dto.UpdateBookRequestDto;
import com.project.bookapp.exceptions.EntityNotFoundException;
import com.project.bookapp.mapper.BookMapper;
import com.project.bookapp.model.Book;
import com.project.bookapp.repository.book.BookRepository;
import com.project.bookapp.repository.book.BookSpecificationBuilder;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Entity with id " + id + " not found")));
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

        Optional.ofNullable(bookDto.getTitle()).ifPresent(book::setTitle);
        Optional.ofNullable(bookDto.getAuthor()).ifPresent(book::setAuthor);
        Optional.ofNullable(bookDto.getIsbn()).ifPresent(book::setIsbn);
        Optional.ofNullable(bookDto.getPrice()).ifPresent(book::setPrice);
        Optional.ofNullable(bookDto.getDescription()).ifPresent(book::setDescription);
        Optional.ofNullable(bookDto.getCoverImage()).ifPresent(book::setCoverImage);

        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto params) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
