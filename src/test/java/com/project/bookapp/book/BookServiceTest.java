package com.project.bookapp.book;

import com.project.bookapp.dto.book.*;
import com.project.bookapp.exceptions.EntityNotFoundException;
import com.project.bookapp.mapper.BookMapper;
import com.project.bookapp.model.Book;
import com.project.bookapp.repository.book.BookRepository;
import com.project.bookapp.repository.book.BookSpecificationBuilder;
import com.project.bookapp.service.BookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Save book")
    public void save_shouldReturnBookDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Test");

        Book book = new Book();
        book.setTitle("Test");

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setTitle("Test Book");

        BookDto expectedDto = new BookDto();
        expectedDto.setId(1L);
        expectedDto.setTitle("Test Book");

        Mockito.when(bookMapper.toModel(requestDto)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(savedBook);
        Mockito.when(bookMapper.toDto(savedBook)).thenReturn(expectedDto);

        BookDto result = bookService.save(requestDto);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getTitle(), result.getTitle());

        verify(bookMapper).toModel(requestDto);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(savedBook);
    }

    @Test
    @DisplayName("Find all books")
    public void findAll_shouldReturnAllBooksDto() {
        Pageable pageable = PageRequest.of(0, 2);

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");

        List<Book> books = List.of(book1, book2);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        BookDto dto1 = new BookDto();
        dto1.setId(1L);
        dto1.setTitle("Book 1");

        BookDto dto2 = new BookDto();
        dto2.setId(2L);
        dto2.setTitle("Book 2");

        Mockito.when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        Mockito.when(bookMapper.toDto(book1)).thenReturn(dto1);
        Mockito.when(bookMapper.toDto(book2)).thenReturn(dto2);

        Page<BookDto> result = bookService.findAll(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(dto1, result.getContent().get(0));
        assertEquals(dto2, result.getContent().get(1));

        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toDto(book1);
        verify(bookMapper).toDto(book2);
    }

    @Test
    @DisplayName(" Verify the correct book was returned when book exists")
    public void getBookById_withValidId_shouldReturnValidBook() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Test");
        book.setPrice(BigDecimal.valueOf(12.99));

        BookDto expectedDto = new BookDto();
        expectedDto.setId(bookId);
        expectedDto.setTitle("Test");
        expectedDto.setPrice(BigDecimal.valueOf(12.99));

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        Mockito.when(bookMapper.toDto(book)).thenReturn(expectedDto);

        BookDto actualDto = bookService.getById(bookId);

        assertEquals(expectedDto.getId(), actualDto.getId());
        assertEquals(expectedDto.getTitle(), actualDto.getTitle());
        assertEquals(expectedDto.getPrice(), actualDto.getPrice());

        verify(bookRepository).findById(bookId);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Throw exception when book doesn't exist")
    public void getBookById_withNonExistingId_shouldThrowException() {
        Long bookId = 11L;

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> bookService.getById(bookId)
        );

        String expected = "Entity with id " + bookId + " not found";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        verify(bookRepository).findById(bookId);
    }

    @Test
    @DisplayName("Verify that the book is deleted")
    public void deleteBook_shouldDeleteBook() {
        Long bookId = 11L;

        Mockito.doNothing().when(bookRepository).deleteById(bookId);

        bookService.deleteById(bookId);

        verify(bookRepository).deleteById(bookId);
    }

    @Test
    @DisplayName("Verify that the book is updated when book exists")
    public void updateBook_withValidId_shouldReturnUpdatedBook() {
        Long bookId = 1L;

        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setTitle("Old Title");
        existingBook.setPrice(BigDecimal.valueOf(10.99));

        UpdateBookRequestDto updateDto = new UpdateBookRequestDto();
        updateDto.setTitle("Updated Title");
        updateDto.setPrice(BigDecimal.valueOf(15.99));

        Book updatedBook = new Book();
        updatedBook.setId(bookId);
        updatedBook.setTitle("Updated Title");
        updatedBook.setPrice(BigDecimal.valueOf(15.99));

        BookDto expectedDto = new BookDto();
        expectedDto.setId(bookId);
        expectedDto.setTitle("Updated Title");
        expectedDto.setPrice(BigDecimal.valueOf(15.99));

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        Mockito.doAnswer(invocation -> {
            Book bookToUpdate = invocation.getArgument(1);
            bookToUpdate.setTitle(updateDto.getTitle());
            bookToUpdate.setPrice(updateDto.getPrice());
            return null;
        }).when(bookMapper).updateBookFromDto(updateDto, existingBook);
        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(updatedBook);
        Mockito.when(bookMapper.toDto(Mockito.any(Book.class))).thenReturn(expectedDto);

        BookDto result = bookService.updateBook(bookId, updateDto);

        assertEquals(expectedDto, result);

        verify(bookRepository).findById(bookId);
        verify(bookMapper).updateBookFromDto(updateDto, existingBook);
        verify(bookRepository).save(Mockito.any(Book.class));
        verify(bookMapper).toDto(Mockito.any(Book.class));
    }

    @Test
    @DisplayName("Should return paginated books based on search parameters")
    public void search_withValidParams_shouldReturnPaginatedBooks() {
        String searchTitle = "Some Book";
        String searchAuthor = "John Doe";
        String searchIsbn = "123456789";

        BookSearchParametersDto searchParams = new BookSearchParametersDto(searchTitle, searchAuthor, searchIsbn);

        Pageable pageable = PageRequest.of(0, 10);

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Some Book");
        book.setAuthor("John Doe");
        book.setIsbn("123456789");
        book.setPrice(BigDecimal.valueOf(15));

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Some Book");
        bookDto.setAuthor("John Doe");
        bookDto.setIsbn("123456789");
        bookDto.setPrice(BigDecimal.valueOf(15));

        Page<Book> bookPage = new PageImpl<>(Arrays.asList(book));
        Page<BookDto> bookDtoPage = new PageImpl<>(Arrays.asList(bookDto));

        Mockito.when(bookSpecificationBuilder.build(searchParams)).thenReturn(Specification.where(null));
        Mockito.when(bookRepository.findAll(Mockito.any(Specification.class),
                Mockito.any(Pageable.class))).thenReturn(bookPage);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> result = bookService.search(searchParams, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Some Book", result.getContent().get(0).getTitle());
        assertEquals("John Doe", result.getContent().get(0).getAuthor());
        assertEquals("123456789", result.getContent().get(0).getIsbn());

        verify(bookSpecificationBuilder).build(searchParams);
        verify(bookRepository).findAll(Mockito.any(Specification.class), Mockito.eq(pageable));
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Should return books for a given category ID")
    public void getBooksByCategoryId_withValidCategoryId_shouldReturnBooks() {
        // Arrange
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Sample Book");

        BookDtoWithoutCategoryIds bookDtoWithoutCategories = new BookDtoWithoutCategoryIds();
        bookDtoWithoutCategories.setId(1L);
        bookDtoWithoutCategories.setTitle("Sample Book");

        Page<Book> bookPage = new PageImpl<>(Collections.singletonList(book));
        Page<BookDtoWithoutCategoryIds> bookDtoPage = new PageImpl<>(Collections.singletonList(bookDtoWithoutCategories));

        Mockito.when(bookRepository.findAllByCategories_Id(Mockito.eq(pageable), Mockito.eq(categoryId))).thenReturn(bookPage);
        Mockito.when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDtoWithoutCategories);

        Page<BookDtoWithoutCategoryIds> result = bookService.getBooksByCategoryId(pageable, categoryId);

        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Book", result.getContent().get(0).getTitle());

        verify(bookRepository).findAllByCategories_Id(pageable, categoryId);
        verify(bookMapper).toDtoWithoutCategories(book);
    }
}
