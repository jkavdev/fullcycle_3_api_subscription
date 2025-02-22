FROM eclipse-temurin:21.0.2_13-jre-alpine

# copiando o jar gerado para a imagem
COPY build/libs/*.jar /opt/app/application.jar

# criando grupo e um usuario, para que nao seja executado criado um container com o usuario root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# rodando jar da aplicacao
CMD java -jar /opt/app/application.jar