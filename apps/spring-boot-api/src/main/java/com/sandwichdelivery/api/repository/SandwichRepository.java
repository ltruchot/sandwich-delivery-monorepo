package com.sandwichdelivery.api.repository;

import com.sandwichdelivery.api.entity.Sandwich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SandwichRepository extends JpaRepository<Sandwich, Long> {
}