package com.sandwichdelivery.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sandwiches")
public class Sandwich {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SandwichType type;

    @ManyToMany
    @JoinTable(
        name = "sandwich_toppings",
        joinColumns = @JoinColumn(name = "sandwich_id"),
        inverseJoinColumns = @JoinColumn(name = "topping_id")
    )
    private Set<Topping> toppings = new HashSet<>();

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public enum SandwichType {
        HAM, VEGETARIAN
    }

    public Sandwich() {
        this.price = new BigDecimal("5.00");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SandwichType getType() {
        return type;
    }

    public void setType(SandwichType type) {
        this.type = type;
    }

    public Set<Topping> getToppings() {
        return toppings;
    }

    public void setToppings(Set<Topping> toppings) {
        this.toppings = toppings;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public BigDecimal calculateTotalPrice() {
        BigDecimal toppingPrice = toppings.stream()
            .map(Topping::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return price.add(toppingPrice);
    }
}