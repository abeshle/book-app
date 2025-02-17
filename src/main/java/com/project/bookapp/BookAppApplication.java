package com.project.bookapp;

import com.project.bookapp.model.Book;
import com.project.bookapp.service.BookService;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(BookService bookService) {
        return args -> {
            Book book = new Book();
            book.setTitle("The Alchemist");
            book.setAuthor("Paulo Coelho");
            book.setIsbn("978-5-17-138753-2");
            book.setPrice(BigDecimal.valueOf(22));

            bookService.save(book);

        };
    }
}
