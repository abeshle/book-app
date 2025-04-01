package com.project.bookapp.repository.order;

import com.project.bookapp.model.Order;
import com.project.bookapp.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUser(User user);
}
