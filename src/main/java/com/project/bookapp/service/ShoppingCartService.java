package com.project.bookapp.service;

import com.project.bookapp.dto.cart.ShoppingCartResponseDto;
import com.project.bookapp.dto.item.CartItemRequestDto;
import com.project.bookapp.model.ShoppingCart;
import com.project.bookapp.model.User;

public interface ShoppingCartService {
    ShoppingCartResponseDto save(User user, CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto findById(Long id);

    ShoppingCart createNewShoppingCart(User user);

    ShoppingCartResponseDto update(Long cartItemId, CartItemRequestDto cartItemRequestDto, Long id);

    void deleteById(Long cartItemId, Long userId);
}
