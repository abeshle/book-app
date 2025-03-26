package com.project.bookapp.mapper;

import com.project.bookapp.config.MapperConfig;
import com.project.bookapp.dto.book.BookDto;
import com.project.bookapp.dto.book.BookDtoWithoutCategoryIds;
import com.project.bookapp.dto.book.CreateBookRequestDto;
import com.project.bookapp.dto.book.UpdateBookRequestDto;
import com.project.bookapp.model.Book;
import com.project.bookapp.model.Category;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto bookDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBookFromDto(UpdateBookRequestDto dto, @MappingTarget Book entity);

    @Mapping(target = "id", ignore = true)
    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            List<Long> categoryIds = book.getCategories()
                    .stream()
                    .map(Category::getId)
                    .toList();
            bookDto.setCategoryIds(categoryIds);
        }
    }
}
