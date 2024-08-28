FROM maven:3.9.9-amazoncorretto-17-al2023

LABEL maintainer="Mauro Pereira <mauropereira1096@gmail.com>"

WORKDIR /app

COPY . .

RUN mvn package

EXPOSE 8080

CMD [ "java", "-jar", "target/jwt-0.0.1-SNAPSHOT.jar" ]