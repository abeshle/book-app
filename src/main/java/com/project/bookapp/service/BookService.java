package com.project.bookapp.service;

import com.project.bookapp.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();

}
