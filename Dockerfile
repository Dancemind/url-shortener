FROM openjdk:11-jdk

COPY . .

RUN ./mvnw clean install -DskipTests

RUN cp ./target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]