package com.mindvault.mymemory.security;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mindvault.mymemory.entities.Category;
import com.mindvault.mymemory.repositories.CategoryRepository;
@Configuration
public class SeedCategory {

    @Bean
    CommandLineRunner initCategories(CategoryRepository categoryRepositories) {
        return args -> {

            String[] defaultCategories = {"Travel", "Food", "Work", "Personal", "Fitness"};

            for (String name : defaultCategories) {
                categoryRepositories.findByName(name)
                        .orElseGet(() -> categoryRepositories.save(new Category(null, name)));
            }

            System.out.println("Categories seeded!");
        };
    }
}
