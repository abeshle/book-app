package com.project.bookapp.dto.book;

import com.project.bookapp.model.Book;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookDtoWithoutCategoryIds {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;

    public BookDtoWithoutCategoryIds(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.isbn = book.getIsbn();
        this.price = book.getPrice();
        this.description = book.getDescription();
        this.coverImage = book.getCoverImage();
    }
}
