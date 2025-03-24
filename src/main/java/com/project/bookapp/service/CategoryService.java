package com.project.bookapp.service;

import com.project.bookapp.dto.category.CategoryDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    List<CategoryDto> findAll();

    CategoryDto getById(Long id);

    CategoryDto save(CategoryDto categoryDto);

    CategoryDto update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);

}
