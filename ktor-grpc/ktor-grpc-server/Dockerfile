FROM openjdk:11
ADD target/ktor-grpc-server-0.0.1-jar-with-dependencies.jar app.jar

RUN mkdir app \
    && cd app \
    && jar -xf ../app.jar

EXPOSE 8080
EXPOSE 7777

CMD java -cp app io.github.hsedjame.ktor.grpc.server.ApplicationKt

