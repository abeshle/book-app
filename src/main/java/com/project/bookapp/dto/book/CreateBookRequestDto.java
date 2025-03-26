package com.project.bookapp.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    private Long id;
    @NotBlank
    @Size(min = 2, max = 255, message = "Title must be between 2 and 255 characters")
    private String title;
    @NotBlank
    @Size(min = 2, max = 100, message = "Author name must be between 2 and 100 characters")
    private String author;
    @NotBlank
    @Pattern(regexp = "^(97[89])?[-]?\\d{9}[-]?([0-9X])$", message = "Invalid ISBN format")
    private String isbn;
    @NotNull
    @PositiveOrZero(message = "Price must be non-negative")
    private BigDecimal price;
    @Size(max = 1000, message = "Description should not exceed 1000 characters")
    private String description;
    @Pattern(regexp = "^(https?|ftp)://.*$", message = "Invalid URL format for cover image")
    private String coverImage;
    @NotBlank
    private List<Long> categoryIds;
}
