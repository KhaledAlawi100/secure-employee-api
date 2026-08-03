# ==========================
# Build Stage
# ==========================
FROM maven:3.9.11-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

# ==========================
# Runtime Stage
# ==========================
FROM eclipse-temurin:21-jre

RUN addgroup --system spring && adduser --system spring --ingroup spring

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]