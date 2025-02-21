package com.project.bookapp.mapper;

import com.project.bookapp.config.MapperConfig;
import com.project.bookapp.dto.BookDto;
import com.project.bookapp.dto.CreateBookRequestDto;
import com.project.bookapp.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto bookDto);
}
