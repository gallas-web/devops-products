package com.example.productcrud.config;

import com.example.productcrud.entity.Product;
import com.example.productcrud.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    CommandLineRunner initData(ProductRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.saveAll(List.of(
                    Product.builder().name("MacBook Pro M3").description("Ordinateur portable Apple 14 pouces").price(new BigDecimal("2499.99")).quantity(15).category("Informatique").status("ACTIVE").build(),
                    Product.builder().name("iPhone 15 Pro").description("Smartphone Apple haut de gamme").price(new BigDecimal("1199.99")).quantity(42).category("Téléphonie").status("ACTIVE").build(),
                    Product.builder().name("Samsung Galaxy S24").description("Smartphone Android premium").price(new BigDecimal("899.99")).quantity(30).category("Téléphonie").status("ACTIVE").build(),
                    Product.builder().name("Sony WH-1000XM5").description("Casque audio à réduction de bruit").price(new BigDecimal("379.99")).quantity(58).category("Audio").status("ACTIVE").build(),
                    Product.builder().name("iPad Air").description("Tablette Apple avec puce M1").price(new BigDecimal("699.99")).quantity(22).category("Informatique").status("ACTIVE").build(),
                    Product.builder().name("AirPods Pro 2").description("Écouteurs sans fil à réduction de bruit").price(new BigDecimal("279.99")).quantity(75).category("Audio").status("ACTIVE").build(),
                    Product.builder().name("Dell XPS 15").description("Laptop professionnel haute performance").price(new BigDecimal("1899.99")).quantity(8).category("Informatique").status("INACTIVE").build(),
                    Product.builder().name("LG OLED 55''").description("Télévision OLED 4K 55 pouces").price(new BigDecimal("1299.99")).quantity(5).category("TV & Vidéo").status("ACTIVE").build()
                ));
            }
        };
    }
}
