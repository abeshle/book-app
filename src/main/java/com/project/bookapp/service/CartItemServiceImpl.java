package com.project.bookapp.service;

import com.project.bookapp.dto.item.CartItemRequestDto;
import com.project.bookapp.dto.item.CartItemResponseDto;
import com.project.bookapp.exceptions.EntityNotFoundException;
import com.project.bookapp.mapper.CartItemMapper;
import com.project.bookapp.model.CartItem;
import com.project.bookapp.repository.item.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public CartItemResponseDto update(Long id, CartItemRequestDto cartItemRequestDto) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Entity with id " + id + " not found"));
        cartItemMapper.updateBookFromDto(cartItemRequestDto, cartItem);
        cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }
}
