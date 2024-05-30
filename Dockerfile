# Iimagem base do Maven com OpenJDK 17 para construir a aplicação
FROM maven:3.8.1-openjdk-17 AS builder

#Diretório de trabalho
WORKDIR /app

# Copie  pom.xml e o código-fonte para o contêiner
COPY pom.xml /app
COPY src /app/src

# Compile a aplicação
RUN mvn clean package

# Imagem para rodar a aplicação
FROM openjdk:17-jdk-slim

# Diretório de trabalho
WORKDIR /app

# Copie o JAR construído para o contêiner
COPY --from=builder /app/target/"api-transferencia-0.0.1-SNAPSHOT.jar" /app/"api-transferencia-0.0.1-SNAPSHOT.jar"

# Exponha a porta 80
EXPOSE 8083

# Comando para rodar a aplicação
CMD ["java", "-jar", "api-transferencia-0.0.1-SNAPSHOT.jar"]
