package com.project.bookapp.repository.item;

import com.project.bookapp.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT c FROM CartItem c WHERE c.id "
            + "= :cartItemId AND c.shoppingCart.id = :shoppingCartId")
    Optional<CartItem> findByIdAndShoppingCartId(
            @Param("cartItemId") Long cartItemId,
            @Param("shoppingCartId") Long shoppingCartId);
}
