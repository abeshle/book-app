package com.project.bookapp.service;

import com.project.bookapp.dto.cart.ShoppingCartResponseDto;
import com.project.bookapp.dto.item.CartItemRequestDto;
import com.project.bookapp.exceptions.EntityNotFoundException;
import com.project.bookapp.mapper.CartItemMapper;
import com.project.bookapp.mapper.ShoppingCartMapper;
import com.project.bookapp.model.Book;
import com.project.bookapp.model.CartItem;
import com.project.bookapp.model.ShoppingCart;
import com.project.bookapp.model.User;
import com.project.bookapp.repository.book.BookRepository;
import com.project.bookapp.repository.cart.ShoppingCartRepository;
import com.project.bookapp.repository.item.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserService userService;
    private final BookRepository bookRepository;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartResponseDto save(CartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = getShoppingCartModel();

        Book book = bookRepository.findById(cartItemRequestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by ID"));

        CartItem cartItem = cartItemMapper.toEntity(cartItemRequestDto);
        cartItem.setShoppingCart(shoppingCart);

        CartItem savedCartItem = cartItemRepository.save(cartItem);

        shoppingCart.addCartItem(savedCartItem);

        shoppingCartRepository.save(shoppingCart);

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto findById(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Entity with id " + id + " not found"));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    private ShoppingCart getShoppingCartModel() {
        User user = userService.getUser();
        return shoppingCartRepository.findById(user.getId()).get();
    }
}
