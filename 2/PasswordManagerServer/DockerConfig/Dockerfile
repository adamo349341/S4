FROM openjdk:24-jdk

WORKDIR /app

COPY PasswordManagerServer.jar app.jar
COPY keystore/ ./keystore/

EXPOSE 8443

CMD ["java", "-Djavax.net.ssl.keyStore=/DockerConfig/keystore/password_manager_server.keystore", "-Djavax.net.ssl.keyStorePassword=123456", "-jar", "app.jar"]
