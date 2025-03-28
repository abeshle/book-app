package com.project.bookapp.mapper;

import com.project.bookapp.config.MapperConfig;
import com.project.bookapp.dto.item.CartItemRequestDto;
import com.project.bookapp.dto.item.CartItemResponseDto;
import com.project.bookapp.model.CartItem;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItemResponseDto toDto(CartItem cartItem);

    CartItem toEntity(CartItemRequestDto cartItemRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBookFromDto(CartItemRequestDto dto, @MappingTarget CartItem entity);
}
