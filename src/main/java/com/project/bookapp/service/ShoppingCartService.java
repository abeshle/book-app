package com.project.bookapp.service;

import com.project.bookapp.dto.cart.ShoppingCartResponseDto;
import com.project.bookapp.dto.item.CartItemRequestDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto save(CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto findById(Long id);
}
