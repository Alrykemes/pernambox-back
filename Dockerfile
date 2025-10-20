# Etapa 1: Build da aplicação
FROM eclipse-temurin:21-jdk AS build

# Define diretório de trabalho
WORKDIR /app

# Copia os arquivos do projeto
COPY . .

# Caso use Maven:
RUN ./mvnw clean package -DskipTests

# Etapa 2: Imagem final (somente runtime)
FROM eclipse-temurin:21-jre-alpine

# Define diretório de trabalho
WORKDIR /app

# Copia o JAR gerado do estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Define variáveis de ambiente recomendadas
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando de inicialização
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]