# ⛈️ PowerCastService

**FIAP Global Solution × PowerCast**

---

## ✏️ Descrição

O **PowerCastService** é uma aplicação backend desenvolvida em Java com Spring Boot que integra diversos módulos para:

- **Gerenciamento de usuários** (cadastro, autenticação, autorização via JWT, CRUD de usuários).
- **Consulta de dados meteorológicos** utilizando a API do OpenWeatherMap.
- **Registro e consulta de falhas** em bairros específicos (armazenamento em banco PostgreSQL via JPA/Hibernate).
- **Predição de risco** para cada bairro, combinando informações de clima e quantidade de falhas recentes.
- **Exposição de um endpoint SOAP** simples que acessa o serviço público de cálculo (Calculator SOAP Service).
- **Persistência em banco de dados PostgreSQL** (configurado via Docker Compose).
- **Segurança** via Spring Security, protegendo endpoints sensíveis e expondo somente operações permitidas a usuários autenticados.
- **Documentação de API** via Swagger, com todos os endpoints sendo documentados.

O serviço foi desenvolvido como parte do projeto Global Solution da FIAP, visando demonstrar uma solução full-stack de backend que engloba consumo de APIs externas, operações CRUD, predição básica e integração SOAP.

---

## 🖥️ Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot 3.5.0** (Spring Web, Spring Data JPA, Spring Security, Spring Web Services)
- **Maven** (gerenciamento de dependências e build)
- **Hibernate** (JPA) para persistência de entidades
- **PostgreSQL (versão 16+)** via Docker Compose
- **OpenWeatherMap API** para dados de clima em tempo real
- **JWT (JSON Web Tokens)** para autenticação e autorização
- **SOAP (WebServiceTemplate + Jaxb2Marshaller)** para integração com um serviço público de calculadora
- **Lombok** (redução de boilerplate em entidades e DTOs)
- **Docker Compose** (orquestração do container PostgreSQL)
- **Git** (controle de versão)
- **Eclipse / IntelliJ IDEA** (IDE recomendada, mas não obrigatória)

---

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado em sua máquina:

### 1. **Java 17 (ou superior)**  
   - Verificar com `java -version`.

### 2. **Maven 3.8+**  
   - Verificar com `mvn -v`.

### 3. **Docker & Docker Compose**  
   - Para subir o container PostgreSQL.
   - Verificar com `docker --version` e `docker-compose --version`.

### 4. **Conta gratuita no OpenWeatherMap**  
   - Para obter uma API Key (este projeto já inclui uma chave de exemplo em `application.properties`, mas recomendamos usar sua própria chave em produção).

---

## 📝 Instruções de Configuração

### 1. **Clonar o repositório**  
   ```bash
   git clone https://github.com/Natan-TI/GS_SOA
   cd powercast-service
   ```

### 2. **Banco de Dados PostgreSQL (via Docker Compose)**
No diretório raiz (`powercast-service/`), existe um arquivo `docker-compose.yml` que já define um serviço PostgreSQL:

```yaml
version: '3.8'

services:
postgres:
    image: postgres:16
    container_name: powercast_postgres
    restart: always
    ports:
    - "5432:5432"
    environment:
    POSTGRES_USER: powercast
    POSTGRES_PASSWORD: secret
    POSTGRES_DB: powercast
    volumes:
    - pgdata:/var/lib/postgresql/data

volumes:
pgdata:
```

- Execute: 
    `docker-compose up -d`

- Isso criará um container PostgreSQL acessível via `jdbc:postgresql://localhost:5432/powercast` com usuário `powercast` e senha `secret`.
- Caso já possua um PostgreSQL local, ajuste as configurações em `src/main/resources/application.properties` conforme necessário.

### 3. **Configurar variáveis no application.properties**
O arquivo já vem com valores padrão para desenvolvimento:

```bash
spring.application.name=PowerCastService

# Configurações do PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/powercast
spring.datasource.username=powercast
spring.datasource.password=secret
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# OpenWeatherMap
openweather.api.url=https://api.openweathermap.org/data/2.5
openweather.api.key=7073f22c137b0584cd0e7843e9d0140e

# JWT (exemplo)
token.jwt.secret=umaChaveBemLongaESecreta1234567890
token.jwt.expiration-ms=3600000
```

- Observação: Para produção, altere `openweather.api.key` para sua própria chave, proteja o `token.jwt.secret` e ajuste o `spring.datasource` conforme necessário.

### 4. Build & Dependências
O projeto utiliza Maven. Para baixar dependências e compilar:

```bash
mvn clean package
```

---

## 🚀 Como Executar
Após subir o PostgreSQL com Docker:

### 1. Executar a aplicação via Maven
```bash
mvn spring-boot:run
```

### 2. Verificar logs
A aplicação iniciará na porta padrão 8080 (configuração Spring Boot).
Ao rodar, a aplicação vai estar funcionando no link `http://localhost:8080/`

### 3. Executar testes unitários
`./mvnw test`

## 📌 Endpoints REST (Resumo)

### 1. Autenticação & Usuários
Obs.: A maioria dos endpoints requer um Token JWT válido (exceto o `/auth/login` e o cadastro de usuário).

- **1.1. Autenticação (Login)**
    - URL: `POST /auth/login`
    - Payload JSON:
    ```json
    {
    "username": "usuario_exemplo",
    "password": "senha123"
    }
    ```
    - Retorno (200 OK):
    ```json
    {
    "token": "<JWT_TOKEN>"
    }
    ```
    - Descrição: Autentica as credenciais. Retorna um JWT que deverá ser enviado no header `Authorization: Bearer <token>` para acessar endpoints protegidos.

- **1.2. CRUD de Usuários**
    - Base URL: `/usuarios`
    - Header:
    `Authorization: Bearer <JWT_TOKEN>`
    - POST /usuarios
        - Descrição: Cadastra um novo usuário.
        - Payload JSON (UsuarioRequest):
        ```json
        {
        "username": "novo_usuario",
        "password": "senhaSegura"
        }
        ```
    - GET /usuarios
        - Descrição: Lista todos os usuários cadastrados.
        - Retorno: `200 OK` com lista de `UsuarioDto`.
    - GET /usuarios/{id}
        - Descrição: Retorna um usuário específico via ID.
        - Retorno: `200 OK` com `UsuarioDto` ou `404 Not Found` se não existir.
    - PUT /usuarios/{id}
        - Descrição: Atualiza username e senha de um usuário existente.
        - Payload JSON:
        ```json
        {
        "username": "usuario_atualizado",
        "password": "novaSenha"
        }
        ```
        - Retorno: `200 OK` com `UsuarioDto` ou `404 Not Found` se não existir.
    - DELETE /usuarios/{id}
        Descrição: Remove o usuário de ID especificado.
        Retorno: `204 No Content` ou `404 Not Found`.

- 2. **Clima (OpenWeatherMap)**
    - **2.1. Obter dados meteorológicos por cidade**
    - GET /clima/{cidade}
        - Exemplo: `GET /clima/SaoPaulo`
        - Retorno (200 OK, `ClimaDto`):
        ```json
        {
        "temperatura": 22.5,
        "umidade": 78,
        "descricao": "céu limpo",
        "velocidadeVento": 3.5
        }
        ```
        - Descrição: Consulta a API do OpenWeatherMap e traz temperatura (°C), umidade (%), descrição textual do clima e velocidade do vento (m/s).

- 3. **Falhas (Registro de Incidentes por Bairro)**
    - Base URL: /falhas
    - Cabeçalho:
        - `Authorization: Bearer <JWT_TOKEN>`
    - POST /falhas
        - Descrição: Registra uma nova falha em um bairro.
        - Payload JSON (`FalhaDto`):
        ```json
        {
        "bairro": "Vila Mariana",
        "descricao": "Queda de energia por 2 horas",
        "timestamp": "2025-06-04T14:30:00"    // Opcional; se omitido, usa o horário atual
        }
        ```
        - Retorno: `201 Created` com `FalhaDto` (id, bairro, descricao, timestamp).

    - GET /falhas?bairro={bairro}
        Exemplo: `GET /falhas?bairro=Vila%20Mariana`
        Retorno: `200 OK` com lista de `FalhaDto` cujo campo `bairro` corresponda ao parâmetro (busca exata).
        Obs.: Se `bairro` não for passado, retorna todos os registros (usa “” como filtro).

- 4. **Predição de Risco por Bairro**
    - Base URL: /predicao
    - Cabeçalho:
        - `Authorization: Bearer <JWT_TOKEN>`
    - GET /predicao/{bairro}
        - Exemplo: `GET /predicao/Vila Mariana`
        - Retorno (200 OK, `PredicaoDto`):
        ```json
        {
        "bairro": "Vila Mariana",
        "risco": 37.5,
        "temperatura": 22.5,
        "umidade": 78,
        "falhasRecentes": 5
        }
        ```
        - Descrição: Calcula um “índice de risco” para o bairro, baseado em:
            - Temperatura (obtida via ClimaService)
            - Umidade (ClimaService)
            - Número de falhas recentes (FalhasService: quantidade de registros no bairro)
        - Fórmula simplificada:
            `risco = (temperatura * 0.5) + (umidade * 0.2) + (falhasRecentes * 5)`
            - Se risco > 100, ajusta para 100.

- 5. **Integração SOAP (Calculadora Pública)**
    - Base URL: /soap
    - GET /soap/add/{a}/{b}
        - Exemplo: GET /soap/add/5/7
        - Retorno (200 OK):
        ```json
        {
        "result": 12
        }
        ```

## 🔐 Configuração de Segurança (JWT)
1. Geração de Token
    - O `AuthController` expõe `POST /auth/login`, onde a aplicação autentica via `AuthenticationManager`.
    - Se as credenciais estiverem corretas, gera-se um JWT usando `JwtUtil` (chave e tempo de expiração definidos em `application.properties`).

2. Proteção de Endpoints
- O `SecurityConfig` (classe em `com.powercast.config.SecurityConfig`) configura Spring Security para:
    - Permitir acesso público a:
        - /auth/** (login)
        - POST /usuarios (cadastro de usuário)
        - Endpoint do Soap (/soap/add/**)
        - Recursos estáticos, se houver

    - Exigir autenticação (Bearer <token>) para todas as outras rotas, incluindo:
        - /clima/**
        - /falhas/**
        - /predicao/**
        - /usuarios/** (listagem, atualização, deleção)

- Os tokens JWT devem ser incluídos no header Authorization: Bearer <token>.

## 🤝 Integrantes
<table>
  <tr>
    <td align="center">
        <sub>
          <b>João Pedro Marques Rodrigues - RM98307</b>
          <br>
        </sub>
        <sub>
          <b>Natan Eguchi dos Santos - RM98720</b>
          <br>
        </sub>
        <sub>
          <b>Kayky Paschoal Ribeiro - RM99929</b>
          <br>
        </sub>
    </td>
  </tr>
</table>
