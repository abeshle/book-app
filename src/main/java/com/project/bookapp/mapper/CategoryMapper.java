package com.project.bookapp.mapper;

import com.project.bookapp.config.MapperConfig;
import com.project.bookapp.dto.category.CategoryRequestDto;
import com.project.bookapp.dto.category.CategoryResponseDto;
import com.project.bookapp.model.Category;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    Category toEntity(CategoryRequestDto categoryDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBookFromDto(CategoryRequestDto dto, @MappingTarget Category entity);
}
