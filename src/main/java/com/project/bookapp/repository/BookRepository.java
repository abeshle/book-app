package com.project.bookapp.repository;

import com.project.bookapp.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

}
