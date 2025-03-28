package com.project.bookapp.service;

import com.project.bookapp.dto.item.CartItemRequestDto;
import com.project.bookapp.dto.item.CartItemResponseDto;

public interface CartItemService {
    CartItemResponseDto update(Long id, CartItemRequestDto cartItemRequestDto);

    void deleteById(Long id);
}
