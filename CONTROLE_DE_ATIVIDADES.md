# Controle de Atividades

## Objetivo

Desenvolver um microsservico em Java 25 com Spring Boot para encurtamento de URLs. A aplicacao deve receber uma URL original, validar se ela e valida, gerar um codigo curto, persistir o relacionamento no Redis e redirecionar o usuario para a URL original quando a URL encurtada for acessada pelo navegador.

## Premissas Tecnicas

- Linguagem: Java 25.
- Framework: Spring Boot.
- Banco de dados/cache: Redis.
- Build sugerido: Maven.
- Empacotamento: Dockerfile para deploy.
- Automacao local: Makefile.
- Arquitetura: camadas com controller, service, repository, entity/model, DTOs, config e exception handling.

## Fluxo de Trabalho

- [ ] Ao concluir cada fase do controle de atividades, realizar um commit com uma mensagem clara.
- [ ] Apos cada commit de fase concluida, executar o push para o repositorio GitHub.

## Atividades

### 1. Inicializacao do Projeto

- [x] Criar o projeto Spring Boot com Java 25.
- [x] Definir ferramenta de build: Maven.
- [x] Adicionar dependencias principais:
  - [x] Spring Web.
  - [x] Spring Data Redis.
  - [x] Bean Validation.
  - [x] Lombok, se o projeto optar por usa-lo.
  - [x] Spring Boot Test.
- [x] Configurar estrutura base de pacotes:
  - [x] `controller`
  - [x] `service`
  - [x] `repository`
  - [x] `entity`
  - [x] `dto`
  - [x] `config`
  - [x] `exception`

### 2. Modelagem e Contratos

- [ ] Criar entidade/modelo para representar a URL encurtada.
- [ ] Definir campos minimos:
  - [ ] Codigo curto.
  - [ ] URL original.
  - [ ] Data de criacao.
  - [ ] Data de expiracao, se houver regra de TTL.
- [ ] Criar DTO de requisicao para receber a URL original.
- [ ] Criar DTO de resposta com:
  - [ ] URL original.
  - [ ] Codigo curto.
  - [ ] URL encurtada completa.
- [ ] Definir padrao de resposta de erro da API.

### 3. Validacao da URL Original

- [ ] Validar se o campo da URL foi informado.
- [ ] Validar se a URL possui formato valido.
- [ ] Validar protocolos permitidos, preferencialmente `http` e `https`.
- [ ] Tratar URLs invalidas com resposta HTTP adequada, como `400 Bad Request`.
- [ ] Definir se URLs repetidas geram novo codigo ou reutilizam codigo existente.

### 4. Geracao da URL Encurtada

- [ ] Implementar estrategia de geracao do codigo curto.
- [ ] Garantir tamanho adequado para o codigo curto.
- [ ] Evitar colisoes verificando se o codigo ja existe no Redis.
- [ ] Definir quantidade maxima de tentativas em caso de colisao.
- [ ] Definir base URL configuravel para montar a URL encurtada completa.

### 5. Persistencia com Redis

- [ ] Configurar conexao com Redis no `application.yml` ou `application.properties`.
- [ ] Criar repository para gravar e buscar URLs no Redis.
- [ ] Definir formato das chaves no Redis, por exemplo `short-url:{code}`.
- [ ] Implementar gravacao do relacionamento codigo curto -> URL original.
- [ ] Implementar busca da URL original pelo codigo curto.
- [ ] Avaliar uso de TTL para expiracao das URLs encurtadas.

### 6. Endpoints da API

- [ ] Criar endpoint para encurtar URL.
- [ ] Definir contrato sugerido:
  - [ ] `POST /api/v1/urls`
  - [ ] Body: `{ "url": "https://exemplo.com" }`
  - [ ] Response: `{ "originalUrl": "...", "shortCode": "...", "shortUrl": "..." }`
- [ ] Criar endpoint de redirecionamento.
- [ ] Definir contrato sugerido:
  - [ ] `GET /{shortCode}`
  - [ ] Resposta: redirecionamento HTTP `302 Found` ou `301 Moved Permanently`.
- [ ] Retornar `404 Not Found` quando o codigo curto nao existir.

### 7. Tratamento de Erros

- [ ] Criar excecoes de dominio, como:
  - [ ] `InvalidUrlException`
  - [ ] `ShortUrlNotFoundException`
  - [ ] `ShortCodeGenerationException`
- [ ] Criar `ControllerAdvice` global para padronizar erros.
- [ ] Mapear erros de validacao para `400 Bad Request`.
- [ ] Mapear codigo inexistente para `404 Not Found`.
- [ ] Evitar exposicao de stack trace na resposta da API.

### 8. Configuracoes da Aplicacao

- [ ] Criar configuracao para Redis.
- [ ] Criar configuracao para base URL da aplicacao.
- [ ] Criar configuracao para tamanho do codigo curto.
- [ ] Criar configuracao para TTL, caso aplicavel.
- [ ] Separar configuracoes por ambiente quando necessario.
- [ ] Documentar variaveis de ambiente esperadas.

### 9. Testes

- [ ] Criar testes unitarios para o service de encurtamento.
- [ ] Testar validacao de URLs invalidas.
- [ ] Testar geracao de codigo curto.
- [ ] Testar tratamento de colisao de codigo.
- [ ] Testar busca de URL existente.
- [ ] Testar comportamento para codigo inexistente.
- [ ] Criar testes de controller com MockMvc ou WebTestClient.
- [ ] Avaliar uso de Testcontainers para testes com Redis real.

### 10. Docker e Deploy

- [ ] Criar `Dockerfile` para build e execucao da aplicacao.
- [ ] Usar imagem compativel com Java 25.
- [ ] Configurar porta exposta da aplicacao.
- [ ] Permitir configuracao via variaveis de ambiente.
- [ ] Avaliar criacao de `docker-compose.yml` para subir aplicacao e Redis localmente.
- [ ] Garantir que a imagem rode sem depender do ambiente local.

### 11. Makefile

- [ ] Criar `Makefile` com comandos padronizados.
- [ ] Incluir comandos sugeridos:
  - [ ] `make build`
  - [ ] `make test`
  - [ ] `make run`
  - [ ] `make docker-build`
  - [ ] `make docker-run`
  - [ ] `make compose-up`
  - [ ] `make compose-down`
- [ ] Documentar os comandos no README.

### 12. Documentacao

- [ ] Criar `README.md` com descricao do projeto.
- [ ] Documentar requisitos locais:
  - [ ] Java 25.
  - [ ] Maven ou Gradle.
  - [ ] Docker.
  - [ ] Redis, se executar sem Docker.
- [ ] Documentar como executar localmente.
- [ ] Documentar como executar com Docker.
- [ ] Documentar endpoints da API.
- [ ] Incluir exemplos de requisicao e resposta.
- [ ] Documentar variaveis de ambiente.

### 13. Qualidade e Padroes

- [ ] Garantir separacao clara entre controller, service e repository.
- [ ] Evitar regras de negocio no controller.
- [ ] Usar injecao de dependencias por construtor.
- [ ] Usar nomes claros para classes, metodos e pacotes.
- [ ] Padronizar responses e exceptions.
- [ ] Avaliar uso de logs estruturados para operacoes importantes.
- [ ] Garantir que dados sensiveis nao sejam logados.

## Criterios de Aceite

- [ ] A aplicacao aceita uma URL valida e retorna uma URL encurtada.
- [ ] A aplicacao rejeita URLs invalidas com erro `400`.
- [ ] A URL encurtada redireciona corretamente para a URL original.
- [ ] Codigos inexistentes retornam erro `404`.
- [ ] Os dados sao persistidos no Redis.
- [ ] O projeto possui Dockerfile funcional.
- [ ] O projeto possui Makefile com comandos principais.
- [ ] O projeto possui testes automatizados para os fluxos principais.
- [ ] O projeto possui README com instrucoes de uso.

## Decisoes Pendentes

- [ ] Definir se URLs encurtadas terao expiracao.
- [ ] Definir se a mesma URL original deve sempre retornar o mesmo codigo.
- [ ] Definir tamanho padrao do codigo curto.
- [ ] Definir dominio/base URL usado em producao.
- [ ] Definir se o redirecionamento sera `301` ou `302`.
- [ ] Definir se havera endpoint para consultar estatisticas de acesso.
