# ---------- Build stage ----------
FROM maven:3.9.7-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copy pom and download dependencies first
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# ---------- Run stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# COPY jar from the correct build stage
COPY --from=build /workspace/target/*.jar app.jar

# Expose port
EXPOSE 8081

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
