# 📦 E-Commerce Order Service

## 🚀 Overview

This project is a **Spring Boot E-Commerce Order Service** that provides essential APIs for managing **users, products, carts, and orders** with authentication & authorization. It is designed with **Dockerized deployment**, **role-based security**, and **monitoring via Spring Boot Actuator**. The project is intended as a **demo for backend and DevOps skills**.

## ✨ Features

* **Authentication & Authorization** with JWT and role-based access (`USER`, `ADMIN`)
* **User Management**: register, login, update profile, admin manages users
* **Product Management**: CRUD products (admin only)
* **Cart Management**: add/remove/update items, view cart
* **Order Processing**: checkout, manage orders
* **Security**: Spring Security + JWT + Role-based access
* **Monitoring**: Spring Boot Actuator (health, metrics, env, info)

## 🛠️ Tech Stack

* **Backend**: Spring Boot 3, Spring Security, JWT, JPA/Hibernate
* **Database**: MySQL 8
* **Build Tool**: Maven
* **Containerization**: Docker & Docker Compose
* **Monitoring**: Spring Boot Actuator
* **Deployment**: VPS with Docker Hub integration

## 📂 Branches

* `main`: stable branch
* `dev`: active development branch (all source code is here)

## ⚙️ Setup & Run

### 1️⃣ Clone repository

```bash
git clone https://github.com/yourusername/E-Commerce-Order-Service.git
cd E-Commerce-Order-Service
git checkout dev
```

### 2️⃣ Build Docker Image

```bash
docker compose build
docker compose up -d
```

### 3️⃣ Tag & Push to Docker Hub

```bash
docker tag e-commerce-order-service-app:latest trimanh51/ecommerce-app:latest
docker push trimanh51/ecommerce-app:latest
```

### 4️⃣ Deploy on VPS

* Install Docker & Docker Compose on VPS
* Create a `docker-compose.yml` file:

```yaml
services:
  db:
    image: mysql:8.0
    container_name: mysql-db
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: secret
      MYSQL_DATABASE: mydb
      MYSQL_USER: admin
      MYSQL_PASSWORD: secret
    ports:
      - "3306:3306"
    volumes:
      - mysqldata:/var/lib/mysql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-psecret"]
      interval: 10s
      timeout: 5s
      retries: 5

  app:
    image: trimanh51/ecommerce-app:latest
    container_name: ecommerce-app
    restart: always
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/mydb
      SPRING_DATASOURCE_USERNAME: admin
      SPRING_DATASOURCE_PASSWORD: secret
      JWT_SIGNERKEY: "PP224JWPVK74T+sJANCMZSpFHjfuN3wRFBoe0jFJkHOM0S0944Fwvy+BrS/tVd0a"
    depends_on:
      db:
        condition: service_healthy

volumes:
  mysqldata:
```

* Run deployment:

```bash
docker compose up -d
```

* Access API docs: 👉 `http://<VPS_PUBLIC_IP>:8080/api/swagger-ui/index.html`

## 📊 Monitoring

Monitoring is enabled with **Spring Boot Actuator**. Available endpoints:

* `/actuator/health` → app health status
* `/actuator/metrics` → metrics (JVM, system, HTTP requests, DB connections, etc.)
* `/actuator/env` → environment properties
* `/actuator/info` → app info

🔒 **Note**: In real production, these endpoints should be secured and restricted. For demo purposes, they are exposed.

## ⚠️ Credentials Notice

The credentials (DB username, password, JWT key) in this repo are **for demo purposes only**. Do not reuse them in production. In a real project, you should use:

* Environment variables
* Docker secrets
* `.env` files (excluded from Git)

## 📝 License

This project is for demonstration purposes.

## 👤 Author

Created by Tran Manh Tri

---

**Happy Coding!** 🚀