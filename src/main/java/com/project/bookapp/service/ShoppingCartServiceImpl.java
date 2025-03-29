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
import com.project.bookapp.repository.user.UserRepository;
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
    private final UserRepository userRepository;

    @Override
    public ShoppingCartResponseDto save(Long id, CartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id)
                .orElseGet(() -> createNewShoppingCart(id));

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

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        shoppingCart.addCartItem(savedCartItem);

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

    private ShoppingCart createNewShoppingCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()
                        -> new EntityNotFoundException("User with id " + userId + " not found"));
        ShoppingCart newCart = new ShoppingCart();
        newCart.setUser(user);
        return shoppingCartRepository.save(newCart);
    }

    @Override
    public ShoppingCartResponseDto update(
            Long cartItemId,
            CartItemRequestDto cartItemRequestDto,
            Long id) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(()
                        -> new EntityNotFoundException("Cart item not found with ID: "
                        + cartItemId));

        ShoppingCart shoppingCart = shoppingCartRepository.findById(id)
                .orElseThrow(()
                        -> new EntityNotFoundException("Shopping cart not found for user ID: "
                        + id));

        if (!cartItem.getShoppingCart().getId().equals(shoppingCart.getId())) {
            throw new IllegalArgumentException(
                    "Cart item does not belong to the authenticated user");
        }

        if (cartItemRequestDto.getBookId() != null
                && !cartItem.getBook().getId().equals(cartItemRequestDto.getBookId())) {
            Book book = bookRepository.findById(cartItemRequestDto.getBookId())
                    .orElseThrow(()
                            -> new EntityNotFoundException("Book not found with ID: "
                            + cartItemRequestDto.getBookId()));
            cartItem.setBook(book);
        }

        cartItem.setQuantity(cartItemRequestDto.getQuantity());

        cartItemRepository.save(cartItem);

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void deleteById(Long cartItemId, Long userId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found with ID: "
                        + cartItemId));

        ShoppingCart shoppingCart = shoppingCartRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user ID: " + userId));

        if (!cartItem.getShoppingCart().getId().equals(shoppingCart.getId())) {
            throw new IllegalArgumentException(
                    "Cart item does not belong to the authenticated user");
        }

        cartItemRepository.delete(cartItem);
    }
}
