# 🛍️ Product CRUD — Spring Boot + Angular + Ant Design

Application CRUD complète de gestion de produits, construite avec les meilleures pratiques.

---

## 🏗️ Architecture

```
product-crud/
├── backend/          # Spring Boot 3 REST API
│   ├── src/main/java/com/example/productcrud/
│   │   ├── config/           # CORS, Data initializer
│   │   ├── controller/       # REST Controllers
│   │   ├── dto/              # Request/Response DTOs
│   │   ├── entity/           # JPA Entities
│   │   ├── exception/        # Custom exceptions + Global handler
│   │   ├── mapper/           # MapStruct mappers
│   │   ├── repository/       # Spring Data JPA
│   │   └── service/          # Business logic (interface + impl)
│   └── pom.xml
│
└── frontend/         # Angular 17 + Ant Design
    └── src/app/
        ├── core/
        │   ├── models/        # TypeScript interfaces
        │   ├── services/      # HTTP services
        │   └── interceptors/  # HTTP error interceptor
        ├── features/
        │   └── products/
        │       ├── components/ # Réutilisables (ProductForm)
        │       └── pages/      # Pages (ProductsPage)
        └── shared/
            └── pipes/          # Custom pipes Angular
```

---

## ✅ Bonnes pratiques appliquées

### Backend
- **Architecture en couches** : Controller → Service (interface/impl) → Repository
- **DTOs** séparés (Request/Response) pour ne pas exposer les entités
- **MapStruct** pour le mapping entité ↔ DTO sans code boilerplate
- **Validation** des DTOs avec `@Valid`, `@NotBlank`, `@Min`, etc.
- **Gestion d'exceptions centralisée** avec `@RestControllerAdvice`
- **Wrapper ApiResponse<T>** cohérent pour toutes les réponses
- **Pagination + filtres** côté serveur
- **Transactions** `@Transactional(readOnly = true)` par défaut
- **Logging** avec SLF4J / Lombok `@Slf4j`
- **OpenAPI/Swagger** auto-générée sur `/swagger-ui.html`

### Frontend
- **Standalone Components** (Angular 17)
- **Lazy loading** des routes
- **Service** dédié avec typage fort TypeScript
- **Reactive Forms** avec validation
- **HTTP Interceptor** pour la gestion centralisée des erreurs
- **Debounce** sur la recherche (400ms)
- **Pipes** réutilisables pour la logique de présentation
- **Séparation** composants/pages/services/models

---

## 🚀 Démarrage rapide

### Prérequis
- Java 17+
- Maven 3.9+
- Node.js 20+
- Angular CLI 17+

### Backend
```bash
cd backend
mvn spring-boot:run
```
API disponible sur `http://localhost:8080`
Swagger UI sur `http://localhost:8080/swagger-ui.html`
H2 Console sur `http://localhost:8080/h2-console`

### Frontend
```bash
cd frontend
npm install
ng serve
```
Application disponible sur `http://localhost:4200`

### Docker (optionnel)
```bash
docker-compose up --build
```

---

## 📡 API Endpoints

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | `/api/v1/products` | Liste paginée avec filtres |
| GET | `/api/v1/products/{id}` | Détail d'un produit |
| POST | `/api/v1/products` | Créer un produit |
| PUT | `/api/v1/products/{id}` | Modifier un produit |
| DELETE | `/api/v1/products/{id}` | Supprimer un produit |

### Paramètres de liste
```
GET /api/v1/products?search=iphone&category=Téléphonie&status=ACTIVE&page=0&size=10&sortBy=price&sortDir=asc
```

---

## 📦 Stack Technique

| Couche | Technologie |
|--------|-------------|
| Backend | Spring Boot 3.2, Spring Data JPA |
| Base de données | H2 (dev), compatible PostgreSQL |
| Mapping | MapStruct 1.5 |
| Validation | Jakarta Validation |
| Documentation | SpringDoc OpenAPI 3 |
| Frontend | Angular 17 (Standalone) |
| UI | Ant Design (ng-zorro-antd 17) |
| HTTP | HttpClient + Interceptors |
| Forms | Reactive Forms |
| Build | Maven, Angular CLI |
| Conteneurs | Docker, Docker Compose |
| CI/CD | GitHub Actions, Docker Hub |

---

## 🔄 CI/CD avec GitHub Actions & Docker Hub

Ce projet inclut un pipeline CI/CD automatisé qui:
- ✅ Build et teste le backend (Maven)
- ✅ Build et teste le frontend (npm)
- ✅ Construit les images Docker
- ✅ Les pousse automatiquement vers Docker Hub

### Configuration rapide

1. **Créez un Personal Access Token sur Docker Hub**
   - Settings → Security → New Access Token
   - Permissions: `Read, Write, Delete`
   - Copiez le token

2. **Configurez les secrets GitHub**
   - Allez dans votre repo GitHub
   - Settings → Secrets and variables → Actions
   - Ajoutez 2 secrets:
     - `DOCKERHUB_USERNAME` → votre pseudo Docker Hub
     - `DOCKERHUB_TOKEN` → le token généré

3. **Poussez le code**
   ```bash
   git add .github/workflows/ci.yml
   git commit -m "Add CI/CD pipeline"
   git push origin main
   ```

4. **Vérifiez le workflow**
   - Allez dans l'onglet "Actions" de votre repo
   - Sélectionnez le workflow pour voir les détails

### Tester localement

```bash
# Validez que tout build correctement avant de pousser
./validate-ci.sh
```

### Pour plus de détails
👉 Consultez [GITHUB_ACTIONS_SETUP.md](./GITHUB_ACTIONS_SETUP.md)

---

## 📚 Documentation complète

- [Configuration CI/CD détaillée](./GITHUB_ACTIONS_SETUP.md)
- [Guide Docker Hub](./CI_CD_SETUP.md)
- [API Documentation](http://localhost:8080/swagger-ui.html) (après démarrage)

---

## 📝 Licence

MIT
