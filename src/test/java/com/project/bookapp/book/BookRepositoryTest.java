package com.project.bookapp.book;

import com.project.bookapp.model.Book;
import com.project.bookapp.model.Category;
import com.project.bookapp.repository.book.BookRepository;
import com.project.bookapp.repository.category.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("""
            Find all paginated books by category id
            """)
    @Sql(scripts = {
            "classpath:database/remove-from-books-categories.sql",
            "classpath:database/remove-test-category-from-table.sql",
            "classpath:database/remove-test-book-from-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategories_Id_shouldReturnBooksForGivenCategoryId() {
        Category category = new Category();
        category.setName("Test Category");
        category = categoryRepository.save(category);

        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("123-456-7890");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setCategories(Set.of(category));
        bookRepository.save(book);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Book> result = bookRepository.findAllByCategories_Id(pageable, category.getId());

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals("Test Book", result.getContent().get(0).getTitle());
        Assertions.assertEquals("Test Author", result.getContent().get(0).getAuthor());
        Assertions.assertEquals("123-456-7890", result.getContent().get(0).getIsbn());
    }
}
