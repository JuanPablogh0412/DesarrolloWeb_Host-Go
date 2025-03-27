FROM openjdk:17

COPY . /app

WORKDIR /app

RUN chmod +x ./mvnw && ./mvnw clean package -DskipTests --batch-mode


CMD ["java", "-jar", "target/host_go-0.0.1-SNAPSHOT.jar"]