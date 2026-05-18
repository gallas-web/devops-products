package com.example.productcrud.config;

import com.example.productcrud.entity.Category;
import com.example.productcrud.entity.Product;
import com.example.productcrud.repository.CategoryRepository;
import com.example.productcrud.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    CommandLineRunner initData(ProductRepository productRepo, CategoryRepository categoryRepo) {
        return args -> {
            // Initialiser les catégories
            if (categoryRepo.count() == 0) {
                List<Category> categories = List.of(
                        Category.builder()
                                .name("Informatique")
                                .description("Ordinateurs portables, ordinateurs de bureau, et accessoires informatiques")
                                .icon("laptop")
                                .active(true)
                                .createdAt(LocalDateTime.now())
                                .build(),
                        Category.builder()
                                .name("Téléphonie")
                                .description("Smartphones, téléphones, et accessoires de téléphonie")
                                .icon("phone")
                                .active(true)
                                .createdAt(LocalDateTime.now())
                                .build(),
                        Category.builder()
                                .name("Audio")
                                .description("Casques, écouteurs, et systèmes audio")
                                .icon("headphones")
                                .active(true)
                                .createdAt(LocalDateTime.now())
                                .build(),
                        Category.builder()
                                .name("TV & Vidéo")
                                .description("Téléviseurs, écrans, et équipements vidéo")
                                .icon("video")
                                .active(true)
                                .createdAt(LocalDateTime.now())
                                .build(),
                        Category.builder()
                                .name("Jeux Vidéo")
                                .description("Consoles de jeux, jeux vidéo, et accessoires de gaming")
                                .icon("game")
                                .active(true)
                                .createdAt(LocalDateTime.now())
                                .build(),
                        Category.builder()
                                .name("Accessoires")
                                .description("Câbles, adaptateurs, étuis, et autres accessoires")
                                .icon("plug")
                                .active(true)
                                .createdAt(LocalDateTime.now())
                                .build()
                );
                categoryRepo.saveAll(categories);
            }

            // Initialiser les produits
            if (productRepo.count() == 0) {
                Map<String, Category> categoriesMap = new HashMap<>();
                categoryRepo.findAll().forEach(cat -> categoriesMap.put(cat.getName(), cat));

                List<Product> products = List.of(
                        Product.builder()
                                .name("MacBook Pro M3")
                                .description("Ordinateur portable Apple 14 pouces avec puce M3, 8GB RAM, 512GB SSD. Parfait pour les professionnels.")
                                .price(new BigDecimal("2499.99"))
                                .quantity(15)
                                .category(categoriesMap.get("Informatique"))
                                .imageUrl("https://via.placeholder.com/400x300?text=MacBook+Pro+M3")
                                .specifications("14 pouces Retina, M3, 8GB, 512GB, GPU 8-core")
                                .status("ACTIVE")
                                .rating(4.5)
                                .reviewCount(28)
                                .createdAt(LocalDateTime.now())
                                .build(),
                        Product.builder()
                                .name("iPhone 15 Pro")
                                .description("Smartphone Apple dernière génération avec caméra de 48MP, processeur A17 Pro, écran Super Retina XDR.")
                                .price(new BigDecimal("1199.99"))
                                .quantity(42)
                                .category(categoriesMap.get("Téléphonie"))
                                .imageUrl("https://via.placeholder.com/400x300?text=iPhone+15+Pro")
                                .specifications("6.1 pouces, A17 Pro, 128GB, 48MP Camera")
                                .status("ACTIVE")
                                .rating(4.7)
                                .reviewCount(156)
                                .createdAt(LocalDateTime.now())
                                .build(),
                        Product.builder()
                                .name("Samsung Galaxy S24")
                                .description("Smartphone Android premium avec processeur Snapdragon 8 Gen 3, caméra 200MP, écran AMOLED.")
                                .price(new BigDecimal("899.99"))
                                .quantity(30)
                                .category(categoriesMap.get("Téléphonie"))
                                .imageUrl("https://via.placeholder.com/400x300?text=Samsung+Galaxy+S24")
                                .specifications("6.2 pouces AMOLED, Snapdragon 8 Gen 3, 256GB, 200MP")
                                .status("ACTIVE")
                                .rating(4.6)
                                .reviewCount(89)
                                .createdAt(LocalDateTime.now())
                                .build(),
                        Product.builder()
                                .name("Sony WH-1000XM5")
                                .description("Casque audio premium avec réduction de bruit active, autonomie 30 heures, connexion multi-appareils.")
                                .price(new BigDecimal("379.99"))
                                .quantity(58)
                                .category(categoriesMap.get("Audio"))
                                .imageUrl("https://via.placeholder.com/400x300?text=Sony+WH-1000XM5")
                                .specifications("Réduction bruit, 30h autonomie, Bluetooth 5.3")
                                .status("ACTIVE")
                                .rating(4.8)
                                .reviewCount(342)
                                .createdAt(LocalDateTime.now())
                                .build(),
                        Product.builder()
                                .name("iPad Air")
                                .description("Tablette Apple puissante avec puce M1, écran Liquid Retina, compatible Apple Pencil.")
                                .price(new BigDecimal("699.99"))
                                .quantity(22)
                                .category(categoriesMap.get("Informatique"))
                                .imageUrl("https://via.placeholder.com/400x300?text=iPad+Air")
                                .specifications("10.9 pouces Liquid Retina, M1, 64GB, iPadOS 17")
                                .status("ACTIVE")
                                .rating(4.5)
                                .reviewCount(67)
                                .createdAt(LocalDateTime.now())
                                .build(),
                        Product.builder()
                                .name("AirPods Pro 2")
                                .description("Écouteurs sans fil avec réduction de bruit active, isolement adaptatif, son spatial.")
                                .price(new BigDecimal("279.99"))
                                .quantity(75)
                                .category(categoriesMap.get("Audio"))
                                .imageUrl("https://via.placeholder.com/400x300?text=AirPods+Pro+2")
                                .specifications("Réduction bruit, Son spatial, 6h autonomie")
                                .status("ACTIVE")
                                .rating(4.7)
                                .reviewCount(512)
                                .createdAt(LocalDateTime.now())
                                .build(),
                        Product.builder()
                                .name("Dell XPS 15")
                                .description("Laptop professionnel haute performance avec écran 4K OLED, Intel i9, RTX 4070.")
                                .price(new BigDecimal("1899.99"))
                                .quantity(8)
                                .category(categoriesMap.get("Informatique"))
                                .imageUrl("https://via.placeholder.com/400x300?text=Dell+XPS+15")
                                .specifications("15.6 OLED 4K, i9, RTX 4070, 32GB, 1TB SSD")
                                .status("ACTIVE")
                                .rating(4.6)
                                .reviewCount(95)
                                .createdAt(LocalDateTime.now())
                                .build(),
                        Product.builder()
                                .name("LG OLED 55''")
                                .description("Télévision OLED 4K 55 pouces avec processeur IA, 144Hz, HDMI 2.1, gaming optimisé.")
                                .price(new BigDecimal("1299.99"))
                                .quantity(5)
                                .category(categoriesMap.get("TV & Vidéo"))
                                .imageUrl("https://via.placeholder.com/400x300?text=LG+OLED+55")
                                .specifications("55 pouces OLED 4K, 144Hz, IA Upscaling, HDMI 2.1")
                                .status("ACTIVE")
                                .rating(4.8)
                                .reviewCount(234)
                                .createdAt(LocalDateTime.now())
                                .build(),
                        Product.builder()
                                .name("PlayStation 5")
                                .description("Console de jeu de nouvelle génération avec drive SSD ultra-rapide, ray tracing, 4K 120fps.")
                                .price(new BigDecimal("499.99"))
                                .quantity(12)
                                .category(categoriesMap.get("Jeux Vidéo"))
                                .imageUrl("https://via.placeholder.com/400x300?text=PlayStation+5")
                                .specifications("Ryzen 8-core, RDNA 2, 825GB SSD, 4K 120fps")
                                .status("ACTIVE")
                                .rating(4.7)
                                .reviewCount(287)
                                .createdAt(LocalDateTime.now())
                                .build(),
                        Product.builder()
                                .name("Xbox Series X")
                                .description("Console gaming puissante avec 12 TFLOPS, 1TB SSD, support 4K 120fps, Game Pass Ultimate.")
                                .price(new BigDecimal("499.99"))
                                .quantity(9)
                                .category(categoriesMap.get("Jeux Vidéo"))
                                .imageUrl("https://via.placeholder.com/400x300?text=Xbox+Series+X")
                                .specifications("Zen 3 8-core, RDNA 2, 1TB SSD, 4K 120fps")
                                .status("ACTIVE")
                                .rating(4.6)
                                .reviewCount(198)
                                .createdAt(LocalDateTime.now())
                                .build()
                );
                productRepo.saveAll(products);
            }
        };
    }
}

