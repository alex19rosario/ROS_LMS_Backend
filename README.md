# This is a Spring Boot Rest API for a Library Management System

This README file outlines the steps to set up a Spring Boot project with authentication and authorization using an MySQL database and JWT.

---

## 1. Database Setup

This project uses a MySQL database running inside a Docker container. To create the database and corresponding tables, run the following command:

```
docker compose up -d
```



## 2. Configure Database Connection

Add the following properties to `application.properties` in `infrastructure/src/main/resources/`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ros-lms-db
spring.datasource.username=root
spring.datasource.password=test123

spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.properties.hibernate.connection.characterEncoding=utf-8
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true
```

---

## 3. Private and Public Key Setup

### Generate RSA Keys

Run the following commands in the `infrastructure/src/main/resources/certs` directory:

1. Generate a key pair:
   ```bash
   openssl genrsa -out keypair.pem 2048
   ```
2. Extract the public key:
   ```bash
   openssl rsa -in keypair.pem -pubout -out public.pem
   ```
3. Extract the private key:
   ```bash
   openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem
   ```
4. Remove the `keypair.pem` file as it is no longer needed:
   ```bash
   rm keypair.pem
   ```

### Configure Keys

Add the following properties to `application.properties`:

```properties
rsa.private-key=classpath:certs/private.pem
rsa.public-key=classpath:certs/public.pem
```

---

