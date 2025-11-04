package com.project.bookapp.book;

import static com.project.bookapp.book.TestUtil.getBookDto;
import static com.project.bookapp.book.TestUtil.getBookDtoFromUpdatedBookRequestDto;
import static com.project.bookapp.book.TestUtil.getTestBookDto;
import static com.project.bookapp.book.TestUtil.getTheFirstBook;
import static com.project.bookapp.book.TestUtil.getTheSecondBook;
import static com.project.bookapp.book.TestUtil.getTheThirdBook;
import static com.project.bookapp.book.TestUtil.getUpdateBookRequestDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bookapp.dto.book.BookDto;
import com.project.bookapp.dto.book.BookDtoWithoutCategoryIds;
import com.project.bookapp.dto.book.CreateBookRequestDto;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
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
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(reflectionEquals(expected, actual, "id","categoryIds"));
    }

    @Test
    @DisplayName("Get all books")
    @WithMockUser(username = "user", roles = {"USER"})
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

        assertEquals(3, actual.length);
        for (int i = 0; i < expected.size(); i++) {
            assertTrue(reflectionEquals(expected.get(i), actual[i],
                    "id", "description", "coverImage", "categoryIds"));
        }
    }

    @Test
    @DisplayName("Find book by id")
    @WithMockUser(username = "user", roles = {"USER"})
    void findById_GivenExistingBookId_ShouldReturnBook() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/books/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        JsonNode root = objectMapper.readTree(response);
        JsonNode content = root.get("content");

        assertNotNull(content);
        assertTrue(content.isArray());
        assertTrue(content.size() > 0);

        BookDtoWithoutCategoryIds actualFirst = objectMapper.treeToValue(content.get(0),
                BookDtoWithoutCategoryIds.class);

        BookDto expectedFirst = getTheFirstBook();

        assertNotNull(actualFirst);
        assertEquals(expectedFirst.getTitle(), actualFirst.getTitle());
        assertEquals(expectedFirst.getAuthor(), actualFirst.getAuthor());
        assertEquals(expectedFirst.getIsbn(), actualFirst.getIsbn());

        mvcResult = mockMvc.perform(get("/books/3")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        response = mvcResult.getResponse().getContentAsString();

        root = objectMapper.readTree(response);
        content = root.get("content");

        assertNotNull(content);
        assertTrue(content.isArray());
        assertTrue(content.size() > 0);

        BookDtoWithoutCategoryIds actualThird =
                objectMapper.treeToValue(content.get(0), BookDtoWithoutCategoryIds.class);
        BookDto expectedThird = getTheThirdBook();

        assertNotNull(actualThird);
        assertEquals(expectedThird.getTitle(), actualThird.getTitle());
        assertEquals(expectedThird.getAuthor(), actualThird.getAuthor());
        assertEquals(expectedThird.getIsbn(), actualThird.getIsbn());
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
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("Update book by id")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
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
        assertNotNull(actual);
        assertTrue(reflectionEquals(expected, actual,"categoryIds"));
    }
}
