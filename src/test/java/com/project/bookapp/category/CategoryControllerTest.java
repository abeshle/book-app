package com.project.bookapp.category;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bookapp.dto.category.CategoryRequestDto;
import com.project.bookapp.dto.category.CategoryResponseDto;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;
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
                    new ClassPathResource("database/add-categories.sql")
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
                    new ClassPathResource("database/remove-all-categories.sql")
            );
        }
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts =
            "classpath:database/remove-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ShouldReturnCreatedCategory() throws Exception {
        CategoryRequestDto categoryDto = new CategoryRequestDto();
        categoryDto.setName("Horror");
        CategoryResponseDto expected = new CategoryResponseDto();
        expected.setName(categoryDto.getName());

        String jsonRequest = objectMapper.writeValueAsString(categoryDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                        .content(jsonRequest)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryResponseDto.class);

        Assertions.assertNotNull(actual);

        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(roles = "USER")
    @Test
    @DisplayName("Get all categories")
    void getAll_ShouldReturnCategories() throws Exception {
        List<CategoryResponseDto> expected = new ArrayList<>();
        expected.add(new CategoryResponseDto().setId(1L)
                .setName("Adventure"));
        expected.add(new CategoryResponseDto().setId(2L)
                .setName("Science Fiction"));
        expected.add(new CategoryResponseDto().setId(3L)
                .setName("Computer Science"));



        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsByteArray());
        CategoryResponseDto[] actual = objectMapper.readValue(
                root.get("content").traverse(), CategoryResponseDto[].class);

        Assertions.assertEquals(3, actual.length);

        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(roles = "USER")
    @Test
    @DisplayName("Get category by ID")
    @Sql(scripts = "classpath:database/add-one-category.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/remove-all-categories.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCategoryById_validId_success() throws Exception {
        Long categoryId = 1L;
        CategoryResponseDto expected = new CategoryResponseDto()
                .setId(categoryId)
                .setName("Adventure");


        MvcResult result = mockMvc.perform(get("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryResponseDto.class);

        Assertions.assertNotNull(actual);

        Assertions.assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update category")
    @Sql(scripts = "classpath:database/add-one-category.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/remove-all-categories.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategory_validRequestDto_success() throws Exception {
        Long categoryId = 1L;
        CategoryRequestDto updateRequestDto = new CategoryRequestDto();
        updateRequestDto.setName("Updated Fiction");

        CategoryResponseDto expected = new CategoryResponseDto()
                .setId(categoryId)
                .setName(updateRequestDto.getName());


        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);

        MvcResult result = mockMvc.perform(put("/categories/{id}", categoryId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryResponseDto.class);

        Assertions.assertNotNull(actual);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteCategory_ShouldReturnNoContent() throws Exception {
        Long categoryId = 1L;


        mockMvc.perform(delete("/categories/{id}", categoryId)
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());


        mockMvc.perform(get("/categories/{id}", categoryId)
                        .with(user("user").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
//for mentor review
}
