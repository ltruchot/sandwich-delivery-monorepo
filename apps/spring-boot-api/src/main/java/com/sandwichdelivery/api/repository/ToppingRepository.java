package com.sandwichdelivery.api.repository;

import com.sandwichdelivery.api.entity.Topping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToppingRepository extends JpaRepository<Topping, Long> {
    Optional<Topping> findByName(String name);
}