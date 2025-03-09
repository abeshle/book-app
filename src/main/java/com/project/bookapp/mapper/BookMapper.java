package com.project.bookapp.mapper;

import com.project.bookapp.config.MapperConfig;
import com.project.bookapp.dto.book.BookDto;
import com.project.bookapp.dto.book.CreateBookRequestDto;
import com.project.bookapp.dto.book.UpdateBookRequestDto;
import com.project.bookapp.model.Book;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto bookDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBookFromDto(UpdateBookRequestDto dto, @MappingTarget Book entity);
}
