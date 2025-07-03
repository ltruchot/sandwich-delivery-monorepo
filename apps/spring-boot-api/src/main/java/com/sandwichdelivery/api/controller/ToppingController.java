package com.sandwichdelivery.api.controller;

import com.sandwichdelivery.api.dto.ToppingDto;
import com.sandwichdelivery.api.entity.Topping;
import com.sandwichdelivery.api.repository.ToppingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/toppings")
public class ToppingController {

    private final ToppingRepository toppingRepository;

    public ToppingController(ToppingRepository toppingRepository) {
        this.toppingRepository = toppingRepository;
    }

    @GetMapping
    public ResponseEntity<List<ToppingDto>> getAllToppings() {
        List<ToppingDto> toppings = toppingRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(toppings);
    }

    private ToppingDto convertToDto(Topping topping) {
        return new ToppingDto(topping.getId(), topping.getName(), topping.getPrice());
    }
}