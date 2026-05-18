# 🛍️ E-Commerce Application - Matériel Électronique

Une application e-commerce complète pour la vente de matériel électronique, construite avec Spring Boot 3.2 et Angular 17.

## 📋 Architecture

### Backend (Spring Boot)
- **Framework** : Spring Boot 3.2.0 avec Java 17
- **Base de données** : PostgreSQL
- **Authentification** : JWT (JSON Web Tokens)
- **API** : REST avec Swagger/OpenAPI
- **Build** : Maven 3.8+

### Frontend (Angular)
- **Framework** : Angular 17
- **UI Components** : ng-zorro-antd 17
- **Build** : @angular/cli 17

### Intégration
- **Docker** : Support Docker pour backend et frontend
- **Kubernetes** : Manifests Kubernetes pour déploiement
- **Docker Compose** : Orchestration des services localement

## 🏗️ Structure de Base de Données

### Entités Principales

#### 1. **User** (Utilisateurs)
- ID, Email (unique), Mot de passe hashé
- Informations personnelles (prénom, nom, téléphone)
- Adresse de livraison (adresse, ville, code postal, pays)
- Rôle (USER, ADMIN)
- Statut (actif/inactif)
- Relations : 1 Cart, N Orders, N Reviews

#### 2. **Category** (Catégories de produits)
- ID, Nom, Description
- Icône
- Statut (actif/inactif)
- Catégories initialisées :
  - Informatique (ordinateurs, laptops, etc.)
  - Téléphonie (smartphones, téléphones)
  - Audio (casques, écouteurs, enceintes)
  - TV & Vidéo (téléviseurs, écrans)
  - Jeux Vidéo (consoles, jeux)
  - Accessoires (câbles, adaptateurs, étuis)

#### 3. **Product** (Produits)
- ID, Nom, Description, Prix
- Quantité en stock
- Catégorie (relation N:1)
- URL Image, Spécifications techniques
- Évaluation moyenne, Nombre d'avis
- Statut (ACTIVE, INACTIVE)
- Relations : N CartItems, N OrderItems, N Reviews

#### 4. **Cart** (Panier)
- ID, Utilisateur (relation 1:1)
- Prix total du panier
- Relations : N CartItems

#### 5. **CartItem** (Articles du panier)
- ID, Panier (N:1), Produit (N:1)
- Quantité, Prix unitaire
- Date d'ajout

#### 6. **Order** (Commandes)
- ID, Numéro de commande unique
- Utilisateur (N:1)
- Articles commandés
- Prix total, Frais de port, Taxes
- Statut (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED, REFUNDED)
- Statut de paiement (PENDING, PAID, FAILED, REFUNDED)
- Adresse de livraison
- Numéro de suivi
- Relations : N OrderItems

#### 7. **OrderItem** (Articles d'une commande)
- ID, Commande (N:1), Produit (N:1)
- Quantité, Prix unitaire, Nom du produit

#### 8. **Review** (Avis produits)
- ID, Produit (N:1), Utilisateur (N:1)
- Note (1-5), Titre, Commentaire
- Vérification (acheté ou non)
- Relations : Utilisateur unique par produit

#### 9. **Payment** (Paiements)
- ID, Commande (1:1)
- Montant, Méthode (CREDIT_CARD, DEBIT_CARD, PAYPAL, BANK_TRANSFER)
- Statut (PENDING, COMPLETED, FAILED, REFUNDED)
- Transaction ID

## 🔐 API Endpoints

### Authentification (`/api/v1/auth`)
```
POST   /register              - Inscription nouvel utilisateur
POST   /login                 - Connexion utilisateur
GET    /me                    - Utilisateur actuel
POST   /logout                - Déconnexion
```

### Produits (`/api/v1/products`)
```
GET    /                      - Lister les produits (avec filtres, pagination)
GET    /{id}                  - Détails d'un produit + avis
POST   /                      - Créer un produit (ADMIN)
PUT    /{id}                  - Mettre à jour un produit (ADMIN)
DELETE /{id}                  - Supprimer un produit (ADMIN)
```

### Catégories (`/api/v1/categories`)
```
GET    /                      - Lister toutes les catégories
GET    /active                - Lister les catégories actives
GET    /{id}                  - Détails d'une catégorie
POST   /                      - Créer une catégorie (ADMIN)
PUT    /{id}                  - Mettre à jour une catégorie (ADMIN)
DELETE /{id}                  - Supprimer une catégorie (ADMIN)
```

### Panier (`/api/v1/cart`)
```
GET    /                      - Voir le panier
POST   /add                   - Ajouter un produit au panier
PUT    /update                - Mettre à jour la quantité
DELETE /item/{id}             - Supprimer un article
DELETE /clear                 - Vider le panier
```

### Commandes (`/api/v1/orders`)
```
POST   /                      - Créer une commande
GET    /                      - Lister mes commandes
GET    /{id}                  - Détails d'une commande
PUT    /{id}/status/{status}  - Mettre à jour le statut (ADMIN)
DELETE /{id}                  - Annuler une commande
```

### Avis (`/api/v1/reviews`)
```
GET    /product/{id}          - Lister les avis d'un produit
POST   /product/{id}          - Ajouter un avis
PUT    /{id}                  - Mettre à jour un avis
DELETE /{id}                  - Supprimer un avis
```

### Utilisateurs (`/api/v1/users`)
```
GET    /{id}                  - Détails utilisateur
GET    /email/{email}         - Utilisateur par email
PUT    /{id}                  - Mettre à jour profil
DELETE /{id}                  - Supprimer compte
```

## 🔑 Configuration & Secrets

### Variables d'Environnement
```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=productdb
DB_USER=postgres
DB_PASSWORD=postgres

# JWT
JWT_SECRET=votre_clé_secrète_jwt
JWT_EXPIRATION=86400000  # 24 heures en ms
```

### Authentification JWT
- Token expiration: 24h (configurable)
- Header: `Authorization: Bearer <token>`
- Algorithme: HS512
- Refresh token: À implémenter

## 🚀 Démarrage Local

### Prérequis
- Java 17+
- Maven 3.8+
- Node.js 18+
- PostgreSQL 14+
- Docker & Docker Compose (optionnel)

### Avec Docker Compose
```bash
docker-compose up
```

### Manuellement

**Backend :**
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

**Frontend :**
```bash
cd frontend
npm install
ng serve --open
```

API sera disponible sur `http://localhost:8080`  
Frontend sur `http://localhost:4200`

## 📦 Dépendances Principales

### Backend
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-security
- jjwt (JWT)
- spring-boot-starter-validation
- mapstruct (Mapping Entity <-> DTO)
- lombok (Boilerplate)
- springdoc-openapi (Swagger/OpenAPI)
- postgresql-driver
- prometheus-micrometer (Metrics)

### Frontend
- @angular/core, @angular/router, @angular/forms
- ng-zorro-antd (UI Components)
- rxjs (Reactive Programming)
- typescript 5.2+

## 📝 Données d'Exemple Initialisées

### Catégories
- ✅ Informatique (15 produits)
- ✅ Téléphonie (8 produits)
- ✅ Audio (12 produits)
- ✅ TV & Vidéo (5 produits)
- ✅ Jeux Vidéo (8 produits)
- ✅ Accessoires

### Produits Exemple
- MacBook Pro M3 - 2499.99€
- iPhone 15 Pro - 1199.99€
- Samsung Galaxy S24 - 899.99€
- Sony WH-1000XM5 - 379.99€
- (et 6 autres...)

## 🔄 Flux E-Commerce Typique

1. **Inscription/Connexion** → JWT Token
2. **Parcourir produits** → Filtres, pagination, recherche
3. **Consulter détails** → Description, specs, avis, évaluations
4. **Ajouter au panier** → Gestion quantités
5. **Passer commande** → Adresse livraison, méthode paiement
6. **Suivi commande** → Statut, tracking
7. **Évaluation produit** → Avis et notation

## 🛡️ Sécurité

- ✅ Authentification JWT
- ✅ Mots de passe hashés (BCrypt)
- ✅ CORS configuré
- ✅ Validation des données (Jakarta Validation)
- ✅ Roles (USER, ADMIN)
- À implémenter:
  - HTTPS/TLS en production
  - Rate limiting
  - Refresh tokens
  - 2FA (Two-Factor Authentication)

## 📊 Monitoring & Observabilité

- Actuator endpoints: `/actuator/*`
- Prometheus metrics: `/actuator/prometheus`
- Logs centralisés (à configurer)
- Jaeger tracing (à implémenter)

## 📱 Frontend Components (À créer)

- [x] ProductList + Filters
- [ ] ProductDetail (+ Reviews)
- [ ] Cart Page
- [ ] Checkout Form
- [ ] Order History
- [ ] User Profile
- [ ] Auth Pages (Login, Register)
- [ ] Admin Dashboard (Products, Orders, Users)

## 🔮 Fonctionnalités Futures

- [ ] Wishlist (Favoris)
- [ ] Coupon & Promotion codes
- [ ] Multi-langue (i18n)
- [ ] Paiement en ligne (Stripe/PayPal)
- [ ] Email notifications
- [ ] SMS notifications
- [ ] Système de points de fidélité
- [ ] Recommandations IA
- [ ] Live chat support
- [ ] Mobile app (React Native)

## 📚 Documentation API

API documentation available at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 🤝 Contribution

Les contributions sont bienvenues ! Veuillez créer une branche feature et soumettre une pull request.

## 📄 Licence

MIT License - Libre d'utilisation

## 📞 Support

Pour toute question ou besoin de support, veuillez ouvrir une issue.

---

**Dernière mise à jour** : Mai 2026
