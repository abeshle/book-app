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
    private final BookRepository bookRepository;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartResponseDto save(User user, CartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId())
                .orElseGet(() -> createNewShoppingCart(user));

        Book book = bookRepository.findById(cartItemRequestDto.getBookId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Book not found with ID: "
                                + cartItemRequestDto.getBookId()));

        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(cartItemRequestDto.getBookId()))
                .findFirst()
                .orElseGet(() -> {
                    CartItem newItem = cartItemMapper.toEntity(cartItemRequestDto);
                    newItem.setShoppingCart(shoppingCart);
                    newItem.setBook(book);
                    return newItem;
                });

        cartItem.setQuantity(cartItem.getQuantity() + cartItemRequestDto.getQuantity());

        cartItemRepository.save(cartItem);
        shoppingCart.addCartItem(cartItem);

        shoppingCartRepository.save(shoppingCart);

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto findById(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Shopping cart with id " + id + " not found"));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCart createNewShoppingCart(User user) {
        ShoppingCart newCart = new ShoppingCart();
        newCart.setUser(user);
        return shoppingCartRepository.save(newCart);
    }

    @Override
    public ShoppingCartResponseDto update(
            Long cartItemId,
            CartItemRequestDto cartItemRequestDto,
            Long userId) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Shopping cart not found for user with id " + userId));
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId, cart.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Cart item with id "
                                + cartItemId + " not found in shopping cart " + cart.getId()));

        cartItem.setQuantity(cartItemRequestDto.getQuantity());
        cartItemRepository.save(cartItem);

        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public void deleteById(Long cartItemId, Long userId) {

        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found with ID: " + cartItemId + " for user ID: " + userId));

        cartItemRepository.delete(cartItem);
    }
}
