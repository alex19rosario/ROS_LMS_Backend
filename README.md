# Spring Boot Authorization and Authentication Setup

This README file outlines the steps to set up a Spring Boot project with authentication and authorization using an Oracle database and JWT.

---

## 1. Database Setup

### Tables Creation

Use the following Oracle SQL script to create the necessary tables:

```sql
CREATE TABLE AUTHORITY_TYPE (
    TYPE_DESCRIPTION NVARCHAR2(128) CONSTRAINT AUTHORITY_TYPE_UK UNIQUE
);

CREATE TABLE USERS (
    USERNAME NVARCHAR2(128) CONSTRAINT USER_ID_PK PRIMARY KEY,
    PASSWORD NVARCHAR2(128) NOT NULL,
    ENABLED CHAR(1) CONSTRAINT USER_ENABLED_CK CHECK (ENABLED IN ('Y','N')) NOT NULL
);

CREATE TABLE AUTHORITIES (
    USERNAME NVARCHAR2(128) NOT NULL,
    AUTHORITY NVARCHAR2(128) NOT NULL
);

ALTER TABLE AUTHORITIES ADD CONSTRAINT AUTHORITIES_UNIQUE UNIQUE (USERNAME, AUTHORITY);
ALTER TABLE AUTHORITIES ADD CONSTRAINT AUTHORITIES_USERNAME_FK FOREIGN KEY (USERNAME) REFERENCES USERS (USERNAME) ENABLE;
ALTER TABLE AUTHORITIES ADD CONSTRAINT AUTHORITIES_AUTHORITY_FK FOREIGN KEY (AUTHORITY) REFERENCES AUTHORITY_TYPE (TYPE_DESCRIPTION);
```

### Dummy Data

Insert the following dummy data for testing:

```sql
-- Delete existing records
DELETE FROM AUTHORITIES;
DELETE FROM USERS;
DELETE FROM AUTHORITY_TYPE;

-- Insert Authority Types
INSERT INTO AUTHORITY_TYPE (TYPE_DESCRIPTION) VALUES ('ROLE_ADMIN');
INSERT INTO AUTHORITY_TYPE (TYPE_DESCRIPTION) VALUES ('ROLE_STAFF');
INSERT INTO AUTHORITY_TYPE (TYPE_DESCRIPTION) VALUES ('ROLE_MEMBER');

-- Insert Users
INSERT INTO USERS (USERNAME, PASSWORD, ENABLED) VALUES ('carlos', '{noop}testpassword', 'Y');
INSERT INTO USERS (USERNAME, PASSWORD, ENABLED) VALUES ('mary', '{noop}testpassword', 'Y');
INSERT INTO USERS (USERNAME, PASSWORD, ENABLED) VALUES ('susan', '{noop}testpassword', 'Y');

-- Insert User Roles
INSERT INTO AUTHORITIES (USERNAME, AUTHORITY) VALUES ('carlos', 'ROLE_ADMIN');
INSERT INTO AUTHORITIES (USERNAME, AUTHORITY) VALUES ('carlos', 'ROLE_STAFF');
INSERT INTO AUTHORITIES (USERNAME, AUTHORITY) VALUES ('carlos', 'ROLE_MEMBER');
INSERT INTO AUTHORITIES (USERNAME, AUTHORITY) VALUES ('mary', 'ROLE_STAFF');
INSERT INTO AUTHORITIES (USERNAME, AUTHORITY) VALUES ('mary', 'ROLE_MEMBER');
INSERT INTO AUTHORITIES (USERNAME, AUTHORITY) VALUES ('susan', 'ROLE_MEMBER');

-- Commit changes
COMMIT;

-- Verify data
SELECT * FROM AUTHORITY_TYPE;
SELECT * FROM USERS;
SELECT * FROM AUTHORITIES;
```

---

## 2. Configure Database Connection

Add the following properties to `application.properties`:

```properties
spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/YOUR_SERVICE
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

---

## 3. Private and Public Key Setup

### Generate RSA Keys

Run the following commands in the `src/main/resources/certs` directory:

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

This completes the basic setup for authentication and authorization in your Spring Boot application. Follow these steps to configure your application for secure access and testing.

