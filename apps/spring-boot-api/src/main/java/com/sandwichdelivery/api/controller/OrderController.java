package com.sandwichdelivery.api.controller;

import com.sandwichdelivery.api.dto.OrderDto;
import com.sandwichdelivery.api.dto.SandwichDto;
import com.sandwichdelivery.api.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/basket")
    public ResponseEntity<OrderDto> getCurrentBasket(Authentication authentication) {
        OrderDto basket = orderService.getCurrentBasket(authentication.getName());
        return ResponseEntity.ok(basket);
    }

    @PostMapping("/basket/sandwich")
    public ResponseEntity<OrderDto> addSandwichToBasket(
            Authentication authentication,
            @Valid @RequestBody SandwichDto sandwichDto) {
        OrderDto updatedBasket = orderService.addSandwichToBasket(authentication.getName(), sandwichDto);
        return ResponseEntity.ok(updatedBasket);
    }

    @PostMapping("/pay")
    public ResponseEntity<OrderDto> payOrder(Authentication authentication) {
        OrderDto paidOrder = orderService.payOrder(authentication.getName());
        return ResponseEntity.ok(paidOrder);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getUserOrders(Authentication authentication) {
        List<OrderDto> orders = orderService.getUserOrders(authentication.getName());
        return ResponseEntity.ok(orders);
    }
}