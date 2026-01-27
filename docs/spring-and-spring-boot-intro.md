# Lesson 01: Introduction to Spring Framework and Spring Boot

This document provides a summary of the first lesson on Spring Framework and Spring Boot.

## Core Concepts of Spring Framework

### Dependency Injection (DI) and `ApplicationContext`

The core of the Spring Framework is its Inversion of Control (IoC) container. We manage our application's components (called "beans") through the `ApplicationContext`. Instead of creating objects manually, we let Spring create and manage them for us.

### `@Component` and Bean Scanning

-   **`@Component`**: This is a generic stereotype annotation. Any class annotated with `@Component` will be picked up by Spring's component scanning mechanism and registered as a bean in the `ApplicationContext`.
-   **Singleton by Default**: By default, Spring creates beans as singletons. This means that only one instance of the bean is created and shared across the entire application.

### Injecting Dependencies

There are two primary ways to inject dependencies into a bean:

1.  **Constructor Injection**: This is the recommended approach. Dependencies are provided as arguments to the class's constructor.

    ```java
    @Component
    public class MyService {
        private final MyRepository repository;

        public MyService(MyRepository repository) {
            this.repository = repository;
        }
    }
    ```

2.  **Field Injection with `@Autowired`**: Dependencies are injected directly into the fields of a class. While simpler, it's generally not recommended for required dependencies as it can make testing more difficult.

    ```java
    @Component
    public class MyService {
        @Autowired
        private MyRepository repository;
    }
    ```

### Component Scanning and Configuration

-   **`@SpringBootApplication`**: In a Spring Boot application, the class with this annotation triggers component scanning. By default, it scans the package of the annotated class and all its sub-packages.
-   **`@ComponentScan`**: If you need to scan for components in packages outside of the default scope, you can use the `@ComponentScan` annotation to specify additional packages to scan.

### Handling Multiple Beans of the Same Type

When you have multiple implementations of the same interface, Spring needs help to decide which one to inject.

-   **`@Primary`**: You can mark one of the implementations as the primary one. If no other qualifier is specified, this bean will be chosen.
-   **`@Qualifier("beanName")`**: You can give each implementation a unique name and use `@Qualifier` at the injection point to specify which one you want.

## Spring Boot for Web Applications

### `spring-boot-starter-web`

To build a web application, we add the `spring-boot-starter-web` dependency to our `build.gradle` file. This starter includes all the necessary dependencies for building a web application, including an embedded web server (Tomcat by default).

### Creating a REST API

-   **`@RestController`**: This annotation is a combination of `@Controller` and `@ResponseBody`. It marks a class as a request handler and ensures that the return value of the methods are serialized into the response body (e.g., as JSON).
-   **`@RequestMapping("/api/v1")`**: This annotation can be used at the class level to define a base path for all the request mappings within the controller.
-   **`@GetMapping`, `@PostMapping`, etc.**: These annotations are used to map HTTP requests to specific handler methods for each HTTP verb (GET, POST, PUT, DELETE, etc.).

## Testing in Spring Boot

-   **`@SpringBootTest`**: This annotation is used for integration tests. It loads the full `ApplicationContext` so you can test the interaction between different components of your application.

## Bonus: Project Lombok

Lombok is a Java library that helps to reduce boilerplate code.

-   **`@Getter`**: Generates getter methods for all fields.
-   **`@ToString`**: Generates a `toString()` method.
-   **`@AllArgsConstructor`**: Generates a constructor with all fields as arguments.

To use Lombok, you need to add it as a plugin in your `build.gradle` file and also install the Lombok plugin in your IDE.

## Useful Links

-   **Spring Initializr**: [https://start.spring.io/](https://start.spring.io/) - A quickstart generator for new Spring Boot projects.
-   **Project Lombok**: [https://projectlombok.org/](https://projectlombok.org/) - Official website for Project Lombok.
