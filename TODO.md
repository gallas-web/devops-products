# TODO - Product CRUD (complétion)

## Étape 1 — Backend Security / JWT
- [x] Configurer Spring Security (SecurityFilterChain) + règles d’accès (permitAll vs authenticated)
- [x] Brancher JwtAuthenticationFilter dans la chaîne
- [x] Autoriser swagger + endpoints publics

## Étape 2 — Backend: corriger la récupération de l’utilisateur courant
- [x] Remplacer les placeholders `return 1L` dans ReviewController, CartController, OrderController
- [x] Récupérer l’utilisateur depuis SecurityContext (email) via UserRepository

## Étape 3 — Validation
- [x] `mvn test` backend
- [x] `ng build` frontend
- [ ] `npm run lint` frontend (script absent dans package.json)
- [ ] `docker-compose up --build` et test rapide endpoints
