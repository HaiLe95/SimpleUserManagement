FROM openjdk:11

WORKDIR .

COPY . .

EXPOSE 8080

CMD java -jar target/scala-2.13/SimpleUserManagement.jar