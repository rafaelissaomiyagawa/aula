# Stage 1: Build the application
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY . .
# Build and ensure layered jar support
RUN ./gradlew bootJar --no-daemon

# Stage 2: Extract layers
FROM eclipse-temurin:21-jre-alpine AS extractor
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Stage 3: Final Production Image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Best Practice: Run as non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy layers from extractor stage
COPY --from=extractor /app/dependencies/ ./
COPY --from=extractor /app/spring-boot-loader/ ./
COPY --from=extractor /app/snapshot-dependencies/ ./
COPY --from=extractor /app/application/ ./

# Expose port
EXPOSE 8080

# Use Entrypoint for better signal handling
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
