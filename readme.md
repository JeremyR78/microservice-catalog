[![coverage report](https://gitlab.local.com/jeremy/microservice-catalog/badges/master/coverage.svg)](https://gitlab.local.com/jeremy/microservice-catalog/-/commits/master)
[![pipeline status](https://gitlab.local.com/jeremy/microservice-catalog/badges/master/pipeline.svg)](https://gitlab.local.com/jeremy/microservice-catalog/-/commits/master)

# Microservice Catalogue

Gestion d'un catalogue de produits pour une plateforme d'e-commerce from scratch.

Objectifs :

Le service doit gérer les fonctionnalités de base d'un catalogue d'e-commerce telles que : 
- gestion de differentes monnaies
- gestion de taxes
- gestion des propriétés produits
- gestion des différents types d'utilisateurs (B2C, B2B)
- gestion asynchrone des données entre une base SQL (MySQL) à NoSQL (Elasticsearch)
- des tests unitaires et d'intégrations (Gitlab-ci)
- configuration complète via une API REST


# Techno utilisées

- Spring boot 
- Mysql
- Elasticsearch
- keycloak ( authentification à venir )
- junit 
- testcontainers
- docker
- kubernetes ( à faire )
