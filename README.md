# 📚 Bookify

**Bookify** is a web application for managing a book library with support for users, book and rent management.

---

## 🚀 Features

- **Book Management**: add, edit, delete books.
- **User Management**: registration, login (Two roles with different permits provided - ```ADMIN``` and ```USER```).
- **Rent Actions**: rent and return books, view rents.
- **Authentication**: JWT-based authentication.
- **REST API** for frontend integration.

---

## 🛠 Technologies

- **Java 17**
- **Spring Boot 3**
- **Spring Security + JWT**
- **Hibernate / JPA**
- **PostgreSQL**
- **Maven**
- **Lombok**
- **Validation**
- **JUnit 5 + Mockito**
- **Slf4j**
- **Jacoco**

---

## 📦 Installation & Running

1. **Clone the repository**

```bash
git clone https://github.com/YaroslavTsvyk/Bookify.git
cd Bookify
```

2. **Create `application.properties` file in src/main/resources/ using `application.properties.example`**
- Create a PostgreSQL database, e.g., bookify_db.
- Set your database credentials:
- Set your JWT Secret (must have size >= 256 bits)

```bash
spring.application.name=Bookify

jwt.secret=YOUR_SECRET_HERE

spring.sql.init.platform=postgres
spring.datasource.url=jdbc:mysql://localhost:5432/bookify_db
spring.jpa.database=POSTGRESQL
spring.datasource.username=YOUR_DATASOURCE_USERNAME
spring.datasource.password=YOUR_DATASOURCE_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.sql.init.mode=always
```

3. **Run the application**
```bash
mvn spring-boot:run
```

- The app will be available at: http://localhost:8080.

---

## 🧩 Project Structure

```bash
src/
├─ main/
│  ├─ java/com/example/bookify/
│  │  ├─ configuration/                   # Security and JwtFilter configs
│  │  ├─ controller/                      # REST controllers
│  │  ├─ dto/                             # DTOs (Request and Response)
│  │  │   └─ mapper/                      # Mappers for DTOs transformation
│  │  ├─ exception/                       # GlobalExceptionHandler and custom exceptions
│  │  ├─ model/                           # Entities
│  │  ├─ repository/                      # Database repositories
│  │  └─ service/                         # Business logic
│  └─ resources/
│     ├─ application.properties.example   # Template for creating application.properties file
│     ├─ data.sql                         # Initial data 
│     └─ schema.sql                       # Initial tables creation
├─ test/
│  └─ java/com/example/bookify/
│     └─ service/                         # Unit-tests for bussiness logic
```

---

## 🔑 Authentication
Bookify uses JWT for user authentication:
- User login returns a token.
- Token is used to access protected resources.
- Include the token in the header: Authorization: Bearer <token>.

## ⚡ Future Improvements
- Support favorites functionality
- Frontend integration (React / Angular).
- Dockerization
- User Reviews & Ratings

## 📬 Postman Collection
A Postman collection is provided in the repository to test all Bookify API endpoints.

### Steps to Run

1. **Open Postman**  
   Download and install Postman if you don’t have it: [https://www.postman.com/downloads/](https://www.postman.com/downloads/)

2. **Import the Collection**  
   - Navigate to the Postman app.  
   - Click `File > Import` or the `Import` button in the top-left corner.  
   - Choose the Postman collection JSON file from the repository, e.g.:  
     ```
     Bookify.postman_collection.json
     ```
   - Click `Import`.

3. **Set Environment Variable for JWT Token** 
   Create a new environment in Postman with the following variable: `{{jwt_token}}`. Authentication requests contain post-response script that sets `jwt_token` variable with received token.
   
4. **Run Requests**  
    - Select the imported collection.
    - Run all (both from folders ADMIN and USER) requests sequentially with previously set order in original file `Bookify.postman_collection.json`.  
    - Make sure the Spring Boot application is running locally before sending requests.

## 📊 Viewing Code Coverage with JaCoCo

Bookify uses **JaCoCo** to generate code coverage reports for unit tests.

### Steps to Generate and View the Report

1. **Run Tests with Maven**

```bash
mvn clean test -Dtest=*Test
```
- This will run all unit tests in the project without setting up SpringBoot context and generate JaCoCo coverage data with HTML report.

2. **Open the Report**
- Open the `index.html` file in your browser to view the code coverage.
- The report shows:
    - Lines covered
    - Branches covered
    - Coverage per class and package

### Using this report, you can see which parts of the code are tested and identify untested areas. Provided unit-tests cover practically 100% of bussiness logic code

## 📝 License
MIT License © 2025 Yaroslav Tsvyk
