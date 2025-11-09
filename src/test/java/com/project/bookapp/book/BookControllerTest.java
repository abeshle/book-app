package com.project.bookapp.book;

import com.fasterxml.jackson.databind.JsonNode;
import com.project.bookapp.dto.book.BookDto;
import com.project.bookapp.dto.book.CreateBookRequestDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    public static final double UPDATED_BOOK_PRICE = 999.99;
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/add-categories.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/add-three-books.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/remove-all-books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/remove-all-categories.sql")
            );
        }
    }

    @Test
    @DisplayName("Create a new Book")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/remove-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto requestDto = getTestBookDto();
        BookDto expected = getBookDto(requestDto);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult mvcResult = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        BookDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                BookDto.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Get all books")
    @WithMockUser(username = "user", roles = {"USER"})
//    @Sql(scripts = {"classpath:database/add-categories.sql",
//            "classpath:database/add-three-books.sql"},
//            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(scripts = {"classpath:database/remove-all-books.sql",
//            "classpath:database/remove-all-categories.sql"},
//            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_GivenBooksInCatalog_ShouldReturnAllBooks() throws Exception {
        List<BookDto> expected = new ArrayList<>();
        expected.add(getTheFirstBook());
        expected.add(getTheSecondBook());
        expected.add(getTheThirdBook());

        MvcResult mvcResult = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        JsonNode root = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        BookDto[] actual = objectMapper.treeToValue(root.get("content"), BookDto[].class);

        Assertions.assertEquals(3, actual.length);
        EqualsBuilder.reflectionEquals(expected, Arrays.stream(actual).toList(), "id","description","coverImage","categoryIds");

    }

    @Test
    @DisplayName("Find book by id")
    @WithMockUser(username = "user", roles = {"USER"})
//    @Sql(scripts = {"classpath:database/add-categories.sql",
//            "classpath:database/add-three-books.sql"},
//            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(scripts = {"classpath:database/remove-all-books.sql",
//            "classpath:database/remove-all-categories.sql"},
//            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_GivenExistingBookId_ShouldReturnBook() throws Exception {
        BookDto expected1 = getTheFirstBook();
        MvcResult mvcResult = mockMvc.perform(
                        get("/books/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual1 = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                BookDto.class
        );
        Assertions.assertNotNull(actual1);
        EqualsBuilder.reflectionEquals(expected1, actual1);
        BookDto expected3 = getTheThirdBook();
        mvcResult = mockMvc.perform(
                        get("/books/3")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual3 = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                BookDto.class
        );
        Assertions.assertNotNull(actual3);
        EqualsBuilder.reflectionEquals(expected3, actual3);
    }

    @Test
    @DisplayName("Delete book by id")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {"classpath:database/add-categories.sql",
            "classpath:database/add-three-books.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/remove-all-books.sql",
            "classpath:database/remove-all-categories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_DeleteExistedBook_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        delete("/books/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("Update book by id")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    @Sql(scripts = {"classpath:database/add-categories.sql",
//            "classpath:database/add-three-books.sql"},
//            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(scripts = {"classpath:database/remove-all-books.sql",
//            "classpath:database/remove-all-categories.sql"},
//            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBook_UpdateExistedBook_Success() throws Exception {
        CreateBookRequestDto updateBookRequestDto = getUpdateBookRequestDto();
        BookDto expected = getBookDtoFromUpdatedBookRequestDto(updateBookRequestDto);
        String jsonRequest = objectMapper.writeValueAsString(updateBookRequestDto);
        MvcResult mvcResult = mockMvc.perform(
                        put("/books/1")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                BookDto.class
        );
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    private BookDto getBookDtoFromUpdatedBookRequestDto(CreateBookRequestDto updateBookRequestDto) {
        return new BookDto()
                .setId(1L)
                .setTitle(updateBookRequestDto.getTitle())
                .setAuthor(updateBookRequestDto.getAuthor())
                .setIsbn(updateBookRequestDto.getIsbn())
                .setPrice(BigDecimal.valueOf(UPDATED_BOOK_PRICE))
                .setDescription(updateBookRequestDto.getDescription())
                .setCoverImage(updateBookRequestDto.getCoverImage());
    }

    private CreateBookRequestDto getUpdateBookRequestDto() {
        CreateBookRequestDto updateBookRequestDto = new CreateBookRequestDto();
        updateBookRequestDto.setId(1L)
                .setTitle("Test Book 1")
                .setAuthor("One")
                .setPrice(BigDecimal.valueOf(10.10))
                .setIsbn("978-0000000009")
                .setDescription("Description")
                .setCoverImage("https://example.com/cover.jpg")
                .setCategoryIds(List.of(1L));
        return updateBookRequestDto;
    }

    private BookDto getTheFirstBook() {
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

    private BookDto getTheSecondBook() {
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

    private BookDto getTheThirdBook() {
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

    private BookDto getBookDto(CreateBookRequestDto requestDto) {
        return new BookDto()
                .setId(1L)
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setDescription(requestDto.getDescription())
                .setCoverImage(requestDto.getCoverImage());
    }
//
    private CreateBookRequestDto getTestBookDto() {
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




