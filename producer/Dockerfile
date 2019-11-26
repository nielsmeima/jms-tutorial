FROM maven:3-jdk-14 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM gcr.io/distroless/java
COPY --from=build /usr/src/app/target/producer-1.0-SNAPSHOT.jar /usr/app/producer-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/usr/app/producer-1.0-SNAPSHOT.jar"]