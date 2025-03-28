package com.project.bookapp.mapper;

import com.project.bookapp.config.MapperConfig;
import com.project.bookapp.dto.cart.ShoppingCartRequestDto;
import com.project.bookapp.dto.cart.ShoppingCartResponseDto;
import com.project.bookapp.model.ShoppingCart;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);

    ShoppingCart toEntity(ShoppingCartRequestDto shoppingCartRequestDto);
}
