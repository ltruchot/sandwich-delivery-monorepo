package com.sandwichdelivery.api.dto;

import com.sandwichdelivery.api.entity.Sandwich;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public class SandwichDto {
    private Long id;
    
    @NotNull
    private Sandwich.SandwichType type;
    
    @Size(max = 4, message = "Maximum 4 different toppings allowed")
    private List<Long> toppingIds;
    
    private BigDecimal totalPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sandwich.SandwichType getType() {
        return type;
    }

    public void setType(Sandwich.SandwichType type) {
        this.type = type;
    }

    public List<Long> getToppingIds() {
        return toppingIds;
    }

    public void setToppingIds(List<Long> toppingIds) {
        this.toppingIds = toppingIds;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}