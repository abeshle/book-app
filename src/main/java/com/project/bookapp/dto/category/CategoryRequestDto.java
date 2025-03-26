package com.project.bookapp.dto.category;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotEmpty
    @Size(max = 50)
    private String name;
    private String description;
}
