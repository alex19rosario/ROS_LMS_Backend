# Authentication and Authorization with JWT

This document provides a summary of the implementation of authentication and authorization using JSON Web Tokens (JWT) and guidance on setting up the application locally and in production.

---

## **Overview**

This project implements authentication and authorization mechanisms to secure the application. It leverages:

- **JWT (JSON Web Tokens)**: For generating, validating, and authenticating API tokens.
- **Spring Security**: To secure endpoints and manage user roles and permissions.

---

## **Features**

- Login endpoint to generate JWT tokens.
- Role-based access control (RBAC) for securing API endpoints.
- Token validation for requests to protected routes.
- Unit and integration tests for the authentication flow.

---

## **Getting Started**

### **Prerequisites**

Ensure the following are installed on your system:

- Java 21 or higher
- Maven 3.8+
- Oracle (or your chosen database)

### **Environment Setup**

1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo-url.git
   cd your-project-name
   ```

2. Create the necessary configuration files:

   - Copy the provided template:
     ```bash
     cp src/main/resources/application.properties.template src/main/resources/application.properties
     ```

   - Open `src/main/resources/application.properties` and update the values:
     ```properties
     spring.datasource.url=YOUR_DB_URL
     spring.datasource.username=YOUR_DB_USERNAME
     spring.datasource.password=YOUR_DB_PASSWORD
     jwt.private-key=YOUR_PRIVATE_KEY_PATH
     jwt.public-key=YOUR_PUBLIC_KEY_PATH
     ```

3. Generate public and private keys for JWT:
   ```bash
   openssl genrsa -out private.pem 2048
   openssl rsa -in private.pem -pubout -out public.pem
   ```

4. Place the `private.pem` and `public.pem` files in a secure directory and update the file paths in `application.properties`.

5. Add sensitive files to `.gitignore`:
   ```
   # Ignore sensitive files
   src/main/resources/application.properties
   src/main/resources/application-test.properties
   private.pem
   public.pem
   ```

---

## **Running the Application**

### **Development Mode**

1. Start the database server.
2. Run the application:
   ```bash
   mvn spring-boot:run
   ```
3. Access the application at `http://localhost:8080`.

### **Testing**

Run unit and integration tests:
```bash
mvn test
```

---

## **Endpoints**

### **Login**
- **URL**: `/api/login`
- **Method**: `POST`
- **Headers**: `Content-Type: application/json`
- **Request Body**:
  ```json
  {
      "username": "your_username",
      "password": "your_password"
  }
  ```
- **Response**:
  ```json
  {
      "token": "jwt-token"
  }
  ```

### **Secured Endpoints**
All other endpoints require a valid JWT in the `Authorization` header:
```
Authorization: Bearer <jwt-token>
```

---

## **Production Setup**

1. **Environment Variables:**
   - Use environment variables to configure sensitive information.

2. **Secure Key Management:**
   - Use a secrets manager (e.g., AWS Secrets Manager, Azure Key Vault) for managing private/public keys and database credentials.

3. **Deployment:**
   - Package the application using Maven:
     ```bash
     mvn clean package
     ```
   - Deploy the generated JAR file.

---

## **Known Issues and Limitations**

- Ensure JWT expiration times are configured properly to balance security and usability.
- Periodically rotate private/public keys to enhance security.

---

## **Contributing**

If you would like to contribute to this project:

1. Fork the repository.
2. Create a new feature branch:
   ```bash
   git checkout -b feature/your-feature
   ```
3. Commit your changes and push to the branch.
4. Create a pull request.

---

## **Contact**

For any questions or issues, please contact the maintainer at [your-email@example.com].

