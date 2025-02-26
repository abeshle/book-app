package com.project.bookapp.service;

import com.project.bookapp.dto.BookDto;
import com.project.bookapp.dto.CreateBookRequestDto;
import com.project.bookapp.exceptions.EntityNotFoundException;
import com.project.bookapp.mapper.BookMapper;
import com.project.bookapp.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

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
}
