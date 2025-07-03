package com.sandwichdelivery.api.repository;

import com.sandwichdelivery.api.entity.Order;
import com.sandwichdelivery.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    Optional<Order> findByUserAndStatus(User user, Order.OrderStatus status);
    List<Order> findByUserAndStatusIn(User user, List<Order.OrderStatus> statuses);
}