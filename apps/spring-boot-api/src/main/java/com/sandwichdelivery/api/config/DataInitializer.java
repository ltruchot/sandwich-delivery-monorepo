package com.sandwichdelivery.api.config;

import com.sandwichdelivery.api.entity.Topping;
import com.sandwichdelivery.api.repository.ToppingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DataInitializer {

    @Autowired
    private ToppingRepository toppingRepository;

    @Bean
    @Transactional
    CommandLineRunner initDatabase() {
        return args -> {
            System.out.println("Starting database initialization...");
            
            try {
                long count = toppingRepository.count();
                System.out.println("Current topping count: " + count);
                
                if (count == 0) {
                    System.out.println("Initializing toppings...");
                    
                    toppingRepository.save(new Topping("salad"));
                    toppingRepository.save(new Topping("tomato"));
                    toppingRepository.save(new Topping("onion"));
                    toppingRepository.save(new Topping("cheese"));
                    
                    System.out.println("Successfully initialized 4 toppings");
                } else {
                    System.out.println("Toppings already exist, skipping initialization");
                }
            } catch (Exception e) {
                System.err.println("Error during database initialization: " + e.getMessage());
                e.printStackTrace();
            }
            
            System.out.println("Database initialization completed.");
        };
    }
}