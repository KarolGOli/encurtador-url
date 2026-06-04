# Encurtador URL

API em Spring Boot para um encurtador de URLs.

## Tecnologias

- Java 25
- Spring Boot 4
- Maven Wrapper
- Redis
- Testcontainers

## Como executar

Clone o repositório e execute a aplicação com o Maven Wrapper:

```bash
./mvnw spring-boot:run
```

No Windows:

```bat
mvnw.cmd spring-boot:run
```

A aplicação usa o nome `encurtador-url` configurado em `src/main/resources/application.properties`.

## Testes

Os testes usam Testcontainers para iniciar um container Redis automaticamente. Para executar:

```bash
./mvnw test
```

No Windows:

```bat
mvnw.cmd test
```

## Estrutura

```text
src/main/java/desafio/encurtador_url
src/main/resources
src/test/java/desafio/encurtador_url
```

## Licença

Este projeto ainda nao possui uma licenca definida.
