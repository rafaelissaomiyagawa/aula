# Introduction to Data Persistence and Docker in Spring Boot

This document provides a summary of concepts related to data persistence with JPA/Hibernate, JDBC Template, and using Docker for database management in a Spring Boot application.

## JPA and Hibernate for Object-Relational Mapping

JPA, the Java Persistence API, is a standard Java specification that provides a way to map Java objects to relational database tables, a concept known as Object-Relational Mapping (ORM). Hibernate is one of the most popular and powerful implementations of the JPA specification. Using JPA abstracts away much of the boilerplate code required for database interactions.

### How to Map an Entity using `@Entity`

To make a Java class persistent, you need to mark it as an entity. This is done by annotating the class with `@Entity`. This annotation tells the JPA provider (like Hibernate) that this class is mapped to a table in the database. You can use the `@Table` annotation to specify the exact table name.

The `@Id` annotation is used to declare the primary key for the entity.

**Example:** Below, the `Conta` class is mapped to a database table named `AUL_CONTA`.

```java
package com.example.jpa.jdbc;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "AUL_CONTA")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    private String nome;
    private Integer valor;
    private String tipoConta;
    // ... constructors and methods
}
```

### How to Create a JPARepository

Spring Data JPA makes it incredibly easy to create repositories. A repository is a design pattern used to manage data access. By simply creating an interface that extends `JpaRepository<EntityType, IdType>`, Spring Data will automatically provide implementations for common CRUD (Create, Read, Update, Delete) operations.

**Example:** The `JPAContaRepository` interface below gets all standard persistence operations for the `Conta` entity, which has a primary key of type `Long`.

```java
package com.example.jpa.jdbc.infra;

import jdbc.jpa.br.com.youready.curso.spring.boot.Conta;
import jdbc.jpa.br.com.youready.curso.spring.boot.ContaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface JPAContaRepository extends JpaRepository<Conta, Long>, ContaRepository {
}
```

### How to Make a Projection Using Records

Often, you don't need to fetch all the columns for an entity from the database. Spring Data Projections allow you to select only a subset of columns, which can improve performance. Java `record` classes are a perfect fit for defining the structure of these projections because they are simple, immutable data carriers.

**Example:** If you only need to retrieve the `nome` from a `Pessoa` entity, you can define a `PessoaSomenteComNome` record.

```java
package com.example.jpa.jdbc;

public record PessoaSomenteComNome(String nome) {
}
```
You can then create a query method in your repository that returns this projection. Spring Data will automatically implement the query to fetch only the required column.

### Important JPA Properties

You can configure JPA and Hibernate behavior in the `application.properties` file.

```properties
# JPA and Hibernate settings
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

-   **`spring.jpa.hibernate.ddl-auto`**: This property is extremely important. It controls how Hibernate interacts with the database schema.
    -   `update`: Hibernate attempts to update the schema to match the entities. This is useful during development but can be risky.
    -   **`none`**: Hibernate does not make any changes to the database schema. **This is the recommended and safest setting for production environments and integration tests.**

## A Look at JDBC Template

For situations where you need more direct control over your SQL or want to avoid the overhead of an ORM, Spring provides the `JdbcTemplate`. It simplifies the use of JDBC by handling resource management (like opening/closing connections) and exception translation.

### How to do "Similar Things" with JDBC Template

You can implement the same repository pattern using `JdbcTemplate`. This involves writing the SQL queries manually for each method, giving you full control over the database interaction.

**Example:** Here is a snippet from `JDBCTemplateContaRepository`, showing how `findById` can be implemented using `JdbcTemplate`.

```java
@Repository("jdbc")
public class JDBCTemplateContaRepository implements ContaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Conta> findById(Long id) {
        String sql = "SELECT id, valor FROM aul_conta WHERE id = ?";
        try {
            Conta conta = jdbcTemplate.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<>(Conta.class),
                    id
            );
            return Optional.ofNullable(conta);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    // ... other methods like save, update, etc.
}
```

## PostgreSQL and Docker Integration

Docker allows us to run applications and services in isolated containers. Docker Compose is a tool for defining and running multi-container applications, which is perfect for setting up a local development environment with a database.

### How to Create a `compose.yaml` for PostgreSQL

The `compose.yml` file is a YAML file used to configure your application's services. The following example defines a service named `db` that runs a PostgreSQL container.

```yaml
services:
  db:
    image: postgres:17.5
    container_name: 'aula-db'
    environment:
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      POSTGRES_DB: ${POSTGRES_DB}
    ports:
      - "0.0.0.0:5432:5432"
    volumes:
      - aula-db-data:/var/lib/postgresql/data
    restart: unless-stopped

volumes:
  aula-db-data:
```
The environment variables (like `${POSTGRES_USER}`) are typically read from a `.env` file in the project's root directory.

### How to Run and Access the Database
-   **To start the database**, run the command `docker compose up` in your terminal.
-   **To access the database using `psql`**, you can execute a command inside the running container:
    ```bash
    docker exec -it <container_name> psql -U <username> -d <dbname>
    ```

## Spring Boot Docker Compose Integration

Starting with Spring Boot 3.1, there is a special integration that simplifies local development with Docker Compose.

-   **No Need for `application.properties`**: When you run your application with `./gradlew bootRun`, Spring Boot automatically detects your `compose.yml` file, starts the containers, and configures the `DataSource` to connect to the database. You no longer need to specify `spring.datasource.url` etc., for local development runs.
-   **Changing File Path**: If your compose file is not in the root directory, you can specify its location in `application.properties`: `spring.docker.compose.file=./docker/compose.yml`
-   **Integration Test Caveat**: This automatic integration **only works for the `bootRun` command**. When you run integration tests (e.g., via `./gradlew test`), this mechanism is not active. For tests, you must configure the database connection separately, for instance, by using Testcontainers.
