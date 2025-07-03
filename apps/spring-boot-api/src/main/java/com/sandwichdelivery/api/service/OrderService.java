package com.sandwichdelivery.api.service;

import com.sandwichdelivery.api.dto.OrderDto;
import com.sandwichdelivery.api.dto.SandwichDto;
import com.sandwichdelivery.api.entity.Order;
import com.sandwichdelivery.api.entity.Sandwich;
import com.sandwichdelivery.api.entity.Topping;
import com.sandwichdelivery.api.entity.User;
import com.sandwichdelivery.api.repository.OrderRepository;
import com.sandwichdelivery.api.repository.SandwichRepository;
import com.sandwichdelivery.api.repository.ToppingRepository;
import com.sandwichdelivery.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final SandwichRepository sandwichRepository;
    private final ToppingRepository toppingRepository;

    public OrderService(OrderRepository orderRepository, 
                       UserRepository userRepository,
                       SandwichRepository sandwichRepository,
                       ToppingRepository toppingRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.sandwichRepository = sandwichRepository;
        this.toppingRepository = toppingRepository;
    }

    public OrderDto getCurrentBasket(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Order basket = orderRepository.findByUserAndStatus(user, Order.OrderStatus.BASKET)
                .orElseGet(() -> {
                    Order newOrder = new Order();
                    newOrder.setUser(user);
                    return orderRepository.save(newOrder);
                });
        
        return convertToDto(basket);
    }

    public OrderDto addSandwichToBasket(String userEmail, SandwichDto sandwichDto) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Order basket = orderRepository.findByUserAndStatus(user, Order.OrderStatus.BASKET)
                .orElseGet(() -> {
                    Order newOrder = new Order();
                    newOrder.setUser(user);
                    return orderRepository.save(newOrder);
                });
        
        if (sandwichDto.getToppingIds() != null && sandwichDto.getToppingIds().size() > 4) {
            throw new RuntimeException("Maximum 4 toppings allowed");
        }

        Map<Long, Long> toppingCounts = new HashMap<>();
        if (sandwichDto.getToppingIds() != null) {
            for (Long toppingId : sandwichDto.getToppingIds()) {
                toppingCounts.put(toppingId, toppingCounts.getOrDefault(toppingId, 0L) + 1);
                if (toppingCounts.get(toppingId) > 2) {
                    throw new RuntimeException("Maximum 2 of each topping type allowed");
                }
            }
        }
        
        Sandwich sandwich = new Sandwich();
        sandwich.setType(sandwichDto.getType());
        
        if (sandwichDto.getToppingIds() != null && !sandwichDto.getToppingIds().isEmpty()) {
            Set<Topping> toppings = new HashSet<>(toppingRepository.findAllById(sandwichDto.getToppingIds()));
            sandwich.setToppings(toppings);
        }
        
        sandwich = sandwichRepository.save(sandwich);
        basket.addSandwich(sandwich);
        orderRepository.save(basket);
        
        return convertToDto(basket);
    }

    public OrderDto payOrder(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Order basket = orderRepository.findByUserAndStatus(user, Order.OrderStatus.BASKET)
                .orElseThrow(() -> new RuntimeException("No active basket found"));
        
        if (basket.getSandwiches().isEmpty()) {
            throw new RuntimeException("Cannot pay for empty basket");
        }
        
        basket.setStatus(Order.OrderStatus.PAID);
        basket.setPaidAt(LocalDateTime.now());
        orderRepository.save(basket);
        
        return convertToDto(basket);
    }

    public List<OrderDto> getUserOrders(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Order> orders = orderRepository.findByUserAndStatusIn(user, 
                Arrays.asList(Order.OrderStatus.PAID, Order.OrderStatus.DELIVERED));
        
        return orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setPaidAt(order.getPaidAt());
        dto.setTotalPrice(order.getTotalPrice());
        
        List<SandwichDto> sandwichDtos = order.getSandwiches().stream()
                .map(this::convertSandwichToDto)
                .collect(Collectors.toList());
        dto.setSandwiches(sandwichDtos);
        
        return dto;
    }

    private SandwichDto convertSandwichToDto(Sandwich sandwich) {
        SandwichDto dto = new SandwichDto();
        dto.setId(sandwich.getId());
        dto.setType(sandwich.getType());
        dto.setTotalPrice(sandwich.calculateTotalPrice());
        
        List<Long> toppingIds = sandwich.getToppings().stream()
                .map(Topping::getId)
                .collect(Collectors.toList());
        dto.setToppingIds(toppingIds);
        
        return dto;
    }
}