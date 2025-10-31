package com.project.bookapp.book;

import com.project.bookapp.dto.book.BookDto;
import com.project.bookapp.dto.book.CreateBookRequestDto;

import java.math.BigDecimal;
import java.util.List;

public class TestUtil {
    public static final double UPDATED_BOOK_PRICE = 999.99;

    public static BookDto getBookDtoFromUpdatedBookRequestDto(CreateBookRequestDto updateBookRequestDto) {
        return new BookDto()
                .setId(1L)
                .setTitle(updateBookRequestDto.getTitle())
                .setAuthor(updateBookRequestDto.getAuthor())
                .setIsbn(updateBookRequestDto.getIsbn())
                .setPrice(BigDecimal.valueOf(UPDATED_BOOK_PRICE))
                .setDescription(updateBookRequestDto.getDescription())
                .setCoverImage(updateBookRequestDto.getCoverImage());
    }

    public static  CreateBookRequestDto getUpdateBookRequestDto() {
        CreateBookRequestDto updateBookRequestDto = new CreateBookRequestDto();
        updateBookRequestDto.setId(1L)
                .setTitle("Test Book 1")
                .setAuthor("One")
                .setPrice(BigDecimal.valueOf(UPDATED_BOOK_PRICE))
                .setIsbn("978-0000000009")
                .setDescription("Description")
                .setCoverImage("https://example.com/cover.jpg")
                .setCategoryIds(List.of(1L));
        return updateBookRequestDto;
    }

    public static  BookDto getTheFirstBook() {
        return new BookDto()
                .setId(1L)
                .setTitle("Test Book 1")
                .setAuthor("One")
                .setPrice(BigDecimal.valueOf(10.1))
                .setIsbn("978-0000000001")
                .setDescription("Description")
                .setCoverImage("https://example.com/cover.jpg")
                .setCategoryIds(List.of(1L));

    }

    public static  BookDto getTheSecondBook() {
        return new BookDto()
                .setId(2L)
                .setTitle("Test Book 2")
                .setAuthor("Two")
                .setPrice(BigDecimal.valueOf(10.1))
                .setIsbn("978-0000000002")
                .setDescription("Description")
                .setCoverImage("https://example.com/cover.jpg")
                .setCategoryIds(List.of(1L));

    }

    public static  BookDto getTheThirdBook() {
        return new BookDto()
                .setId(3L)
                .setTitle("Test Book 3")
                .setAuthor("Three")
                .setPrice(BigDecimal.valueOf(10.1))
                .setIsbn("978-0000000003")
                .setDescription("Description")
                .setCoverImage("https://example.com/cover.jpg")
                .setCategoryIds(List.of(1L));

    }

    public static  BookDto getBookDto(CreateBookRequestDto requestDto) {
        return new BookDto()
                .setId(1L)
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setDescription(requestDto.getDescription())
                .setCoverImage(requestDto.getCoverImage());
    }

    public static  CreateBookRequestDto getTestBookDto() {
        return new CreateBookRequestDto()
                .setId(1L)
                .setTitle("Test Book")
                .setAuthor("One")
                .setPrice(BigDecimal.valueOf(10.1))
                .setIsbn("978-0000000001")
                .setDescription("Description")
                .setCoverImage("https://example.com/cover.jpg")
                .setCategoryIds(List.of(1L));
    }
}
