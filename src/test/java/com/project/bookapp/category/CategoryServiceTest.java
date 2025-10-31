package com.project.bookapp.category;

import com.project.bookapp.dto.category.CategoryRequestDto;
import com.project.bookapp.dto.category.CategoryResponseDto;
import com.project.bookapp.exceptions.EntityNotFoundException;
import com.project.bookapp.mapper.CategoryMapper;
import com.project.bookapp.model.Category;
import com.project.bookapp.repository.category.CategoryRepository;
import com.project.bookapp.service.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("""
    Should return all paginated books
    """)
    public void findAll_shouldReturnAllBooksDto() {
        Pageable pageable = PageRequest.of(0, 2);

        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Fiction");

        Category category2 = new Category();
        category2.setId(2L);
        category1.setName("Fantasy");

        List<Category> categories = List.of(category1, category2);
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, categories.size());

        CategoryResponseDto dto1 = new CategoryResponseDto();
        dto1.setId(1L);
        dto1.setName("Fiction");

        CategoryResponseDto dto2 = new CategoryResponseDto();
        dto2.setId(2L);
        dto2.setName("Fantasy");

        Mockito.when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        Mockito.when(categoryMapper.toDto(category1)).thenReturn(dto1);
        Mockito.when(categoryMapper.toDto(category2)).thenReturn(dto2);

        Page<CategoryResponseDto> result = categoryService.findAll(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(dto1, result.getContent().get(0));
        assertEquals(dto2, result.getContent().get(1));

        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toDto(category1);
        verify(categoryMapper).toDto(category2);
    }

    @Test
    @DisplayName("""
                    Verify the correct category was returned when book exists
            """)
    void getById_withValidId_shouldReturnValidCategory() {
        Long id = 1L;
        Category category = new Category();
        category.setId(id);
        category.setName("Fantasy");

        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(id);
        dto.setName("Fantasy");

        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        Mockito.when(categoryMapper.toDto(category)).thenReturn(dto);

        CategoryResponseDto result = categoryService.getById(id);

        assertEquals(dto, result);

        verify(categoryRepository).findById(id);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Throw exception when category doesn't exist")
    void getById_withNonExistingId_shouldThrowException() {
        Long id = 1L;
        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.getById(id));

        verify(categoryRepository).findById(id);
        verify(categoryMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Verify that the category is saved")
    void save_shouldReturnSavedCategoryDto() {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Fiction");

        Category category = new Category();
        category.setName("Fiction");

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Fiction");

        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Fiction");

        Mockito.when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        Mockito.when(categoryRepository.save(category)).thenReturn(savedCategory);
        Mockito.when(categoryMapper.toDto(savedCategory)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.save(requestDto);

        assertEquals(responseDto, result);

        verify(categoryMapper).toEntity(requestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(savedCategory);
    }

    @Test
    @DisplayName("Verify that the category is updated when category exists")
    void update_withValidId_shouldReturnUpdatedCategory() {
        Long id = 1L;

        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Updated Name");

        Category existingCategory = new Category();
        existingCategory.setId(id);
        existingCategory.setName("Old Name");

        Category updatedCategory = new Category();
        updatedCategory.setId(id);
        updatedCategory.setName("Updated Name");

        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setId(id);
        responseDto.setName("Updated Name");

        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));
        Mockito.doAnswer(invocation -> {
            CategoryRequestDto dtoArg = invocation.getArgument(0);
            Category cat = invocation.getArgument(1);
            cat.setName(dtoArg.getName());
            return null;
        }).when(categoryMapper).updateBookFromDto(requestDto, existingCategory);

        Mockito.when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);
        Mockito.when(categoryMapper.toDto(existingCategory)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.update(id, requestDto);

        assertEquals("Updated Name", result.getName());

        verify(categoryRepository).findById(id);
        verify(categoryMapper).updateBookFromDto(requestDto, existingCategory);
        verify(categoryRepository).save(existingCategory);
        verify(categoryMapper).toDto(existingCategory);
    }

    @Test
    @DisplayName("Verify that the book is deleted")
    public void deleteCategory_shouldDeleteCategory() {
        Long id = 11L;

        Mockito.doNothing().when(categoryRepository).deleteById(id);

        categoryService.deleteById(id);

        verify(categoryRepository).deleteById(id);
    }
}
