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

            if (categoryRepo.count() == 0) {
                List<Category> categories = List.of(
                        Category.builder().name("Informatique").description("Ordinateurs et accessoires").icon("laptop").active(true).createdAt(LocalDateTime.now()).build(),
                        Category.builder().name("Téléphonie").description("Smartphones et accessoires").icon("phone").active(true).createdAt(LocalDateTime.now()).build(),
                        Category.builder().name("Audio").description("Casques et écouteurs").icon("headphones").active(true).createdAt(LocalDateTime.now()).build(),
                        Category.builder().name("TV & Vidéo").description("Téléviseurs et écrans").icon("video").active(true).createdAt(LocalDateTime.now()).build(),
                        Category.builder().name("Jeux Vidéo").description("Consoles et gaming").icon("game").active(true).createdAt(LocalDateTime.now()).build()
                );
                categoryRepo.saveAll(categories);
            }

            if (productRepo.count() == 0) {

                Map<String, Category> categoriesMap = new HashMap<>();
                categoryRepo.findAll().forEach(cat -> categoriesMap.put(cat.getName(), cat));

                List<Product> products = List.of(

                        Product.builder()
                                .name("MacBook Pro M3")
                                .description("Le MacBook Pro M3 est un ordinateur portable haut de gamme conçu pour les professionnels. Grâce à sa puce Apple M3 ultra performante, il offre une rapidité exceptionnelle pour le développement, le design et le montage vidéo. Son écran Retina garantit une qualité d’image incroyable et une autonomie longue durée.")
                                .price(new BigDecimal("2499.99"))
                                .quantity(15)
                                .category(categoriesMap.get("Informatique"))
                                .imageUrl("https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400")
                                .specifications("14 pouces Retina, M3, 8GB RAM, 512GB SSD")
                                .status("ACTIVE")
                                .rating(4.5)
                                .reviewCount(28)
                                .createdAt(LocalDateTime.now())
                                .build(),

                        Product.builder()
                                .name("iPhone 15 Pro")
                                .description("L’iPhone 15 Pro est un smartphone premium avec des performances exceptionnelles grâce à la puce A17 Pro. Il dispose d’un appareil photo 48 MP pour des photos professionnelles, d’un écran Super Retina XDR et d’un design élégant en titane.")
                                .price(new BigDecimal("1199.99"))
                                .quantity(42)
                                .category(categoriesMap.get("Téléphonie"))
                                .imageUrl("https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=400")
                                .specifications("6.1 pouces, A17 Pro, 128GB")
                                .status("ACTIVE")
                                .rating(4.7)
                                .reviewCount(156)
                                .createdAt(LocalDateTime.now())
                                .build(),

                        Product.builder()
                                .name("Samsung Galaxy S24")
                                .description("Le Samsung Galaxy S24 est un smartphone Android puissant avec un écran AMOLED lumineux et un appareil photo haute résolution. Idéal pour les utilisateurs exigeants, il combine performance, autonomie et design moderne.")
                                .price(new BigDecimal("899.99"))
                                .quantity(30)
                                .category(categoriesMap.get("Téléphonie"))
                                .imageUrl("https://images.unsplash.com/photo-1705585173299-3d7c8c0c6d9e?w=400")
                                .specifications("6.2 pouces AMOLED, 256GB")
                                .status("ACTIVE")
                                .rating(4.6)
                                .reviewCount(89)
                                .createdAt(LocalDateTime.now())
                                .build(),

                        Product.builder()
                                .name("Sony WH-1000XM5")
                                .description("Le casque Sony WH-1000XM5 offre une expérience audio immersive avec une réduction de bruit active parmi les meilleures du marché. Idéal pour les voyages, le travail ou la musique, avec une autonomie allant jusqu’à 30 heures.")
                                .price(new BigDecimal("379.99"))
                                .quantity(58)
                                .category(categoriesMap.get("Audio"))
                                .imageUrl("https://images.unsplash.com/photo-1583394838336-acd977736f90?w=400")
                                .specifications("Réduction de bruit, Bluetooth, 30h autonomie")
                                .status("ACTIVE")
                                .rating(4.8)
                                .reviewCount(342)
                                .createdAt(LocalDateTime.now())
                                .build(),

                        Product.builder()
                                .name("iPad Air")
                                .description("L’iPad Air est une tablette puissante et légère équipée de la puce M1. Parfaite pour le travail, les études ou le divertissement, elle offre une fluidité exceptionnelle et une compatibilité avec l’Apple Pencil.")
                                .price(new BigDecimal("699.99"))
                                .quantity(22)
                                .category(categoriesMap.get("Informatique"))
                                .imageUrl("https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=400")
                                .specifications("10.9 pouces, M1, 64GB")
                                .status("ACTIVE")
                                .rating(4.5)
                                .reviewCount(67)
                                .createdAt(LocalDateTime.now())
                                .build(),

                        Product.builder()
                                .name("AirPods Pro 2")
                                .description("Les AirPods Pro 2 offrent un son de haute qualité avec réduction de bruit active et mode transparence. Ils sont parfaits pour les appels, la musique et les déplacements grâce à leur confort et leur autonomie.")
                                .price(new BigDecimal("279.99"))
                                .quantity(75)
                                .category(categoriesMap.get("Audio"))
                                .imageUrl("https://images.unsplash.com/photo-1606220838315-056192d5e927?w=400")
                                .specifications("Son spatial, réduction de bruit")
                                .status("ACTIVE")
                                .rating(4.7)
                                .reviewCount(512)
                                .createdAt(LocalDateTime.now())
                                .build(),

                        Product.builder()
                                .name("Dell XPS 15")
                                .description("Le Dell XPS 15 est un ordinateur portable haut de gamme avec un écran OLED 4K impressionnant. Idéal pour les développeurs et créatifs, il offre puissance, élégance et performance graphique.")
                                .price(new BigDecimal("1899.99"))
                                .quantity(8)
                                .category(categoriesMap.get("Informatique"))
                                .imageUrl("https://images.unsplash.com/photo-1593642702821-c8da6771f0c6?w=400")
                                .specifications("15.6 pouces, i9, 32GB RAM")
                                .status("ACTIVE")
                                .rating(4.6)
                                .reviewCount(95)
                                .createdAt(LocalDateTime.now())
                                .build(),

                        Product.builder()
                                .name("LG OLED 55")
                                .description("Téléviseur LG OLED 55 pouces avec une qualité d’image exceptionnelle, des noirs profonds et un contraste parfait. Idéal pour le cinéma à domicile et le gaming en 4K.")
                                .price(new BigDecimal("1299.99"))
                                .quantity(5)
                                .category(categoriesMap.get("TV & Vidéo"))
                                .imageUrl("https://images.unsplash.com/photo-1593784991095-a205069470b6?w=400")
                                .specifications("OLED 4K, 144Hz")
                                .status("ACTIVE")
                                .rating(4.8)
                                .reviewCount(234)
                                .createdAt(LocalDateTime.now())
                                .build(),

                        Product.builder()
                                .name("PlayStation 5")
                                .description("La PlayStation 5 est une console de nouvelle génération offrant des graphismes en 4K, des temps de chargement ultra rapides et une immersion totale grâce au ray tracing.")
                                .price(new BigDecimal("499.99"))
                                .quantity(12)
                                .category(categoriesMap.get("Jeux Vidéo"))
                                .imageUrl("https://images.unsplash.com/photo-1606813907291-d86efa9b94db?w=400")
                                .specifications("4K, SSD rapide")
                                .status("ACTIVE")
                                .rating(4.7)
                                .reviewCount(287)
                                .createdAt(LocalDateTime.now())
                                .build(),

                        Product.builder()
                                .name("Xbox Series X")
                                .description("La Xbox Series X est une console puissante avec des performances exceptionnelles, idéale pour le gaming en 4K. Profitez d’une fluidité incroyable et du Game Pass pour accéder à des centaines de jeux.")
                                .price(new BigDecimal("499.99"))
                                .quantity(9)
                                .category(categoriesMap.get("Jeux Vidéo"))
                                .imageUrl("https://images.unsplash.com/photo-1621259182978-fbf93132d53d?w=400")
                                .specifications("4K, 1TB SSD")
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