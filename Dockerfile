FROM openjdk:8-jdk-alpine
ADD target/api-test-0.0.1-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -Dspring.data.mongodb.uri=mongodb://mongo/apitest -Djava.security.egd=file:/dev/./urandom -jar /app.jar
