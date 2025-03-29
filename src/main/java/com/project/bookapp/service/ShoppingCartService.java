package com.project.bookapp.service;

import com.project.bookapp.dto.cart.ShoppingCartResponseDto;
import com.project.bookapp.dto.item.CartItemRequestDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto save(Long id, CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto findById(Long id);

    ShoppingCartResponseDto update(Long cartItemId, CartItemRequestDto cartItemRequestDto, Long id);

    void deleteById(Long cartItemId, Long userId);
}
