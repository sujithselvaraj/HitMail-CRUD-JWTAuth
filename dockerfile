# Use a base image with Java installed
FROM openjdk:11-jre-slim
# Copy the application JAR file into the container
COPY target/MailCrud-0.0.1-SNAPSHOT.jar .
EXPOSE 8083
# Specify the command to run your application
CMD ["java", "-jar", "MailCrud-0.0.1-SNAPSHOT.jar"]
