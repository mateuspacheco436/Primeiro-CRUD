# Primeiro CRUD — API de Produtos com Autenticação JWT

API REST em **Java e Spring Boot** para gerenciar um cadastro de produtos (CRUD completo),
com **cadastro e login de usuários**, senhas criptografadas com BCrypt, **autenticação
stateless via JWT** e **controle de acesso por papéis** (USER e ADMIN). Dados em **PostgreSQL**.

## O que o projeto faz

1. **Autenticação (`/auth`)**: cadastra usuários e faz login, devolvendo um token JWT.
2. **Produtos (`/V1/products`)**: usuários autenticados consultam produtos; somente **ADMIN** cria, edita e exclui.

### Fluxo de uso

1. O usuário se cadastra em `POST /auth/register` (sempre com o papel `User`).
2. Faz login em `POST /auth/login` e recebe um token JWT (válido por 2 horas).
3. Envia `Authorization: Bearer <token>` nas demais rotas.
4. O primeiro ADMIN é criado automaticamente na inicialização (veja [Configuração](#configuração)).

## Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 26 | Linguagem |
| Spring Boot 4.1.0 | Framework base |
| Spring Web MVC | Controllers REST |
| Spring Data JPA | Persistência e repositórios |
| PostgreSQL | Banco de dados |
| Flyway | Versionamento do schema |
| Spring Validation | Validação dos dados de entrada |
| Spring Security | Autenticação e autorização por papéis |
| java-jwt (Auth0) | Geração e validação dos tokens |
| BCrypt | Criptografia das senhas |
| Lombok | Redução de código repetitivo |
| Maven Wrapper | Build |

## Endpoints

### Autenticação (públicos)

| Método | Rota | Descrição |
|---|---|---|
| POST | `/auth/register` | Cadastra um usuário (papel `User`) |
| POST | `/auth/login` | Autentica e retorna o token JWT |

**Registro** — `POST /auth/register`
```json
{ "nome": "mateus", "senha": "123456", "email": "mateus@email.com" }
```
- Senha com no mínimo 6 caracteres e e-mail válido.
- O papel **não** é informado pelo cliente: todo cadastro público é `User`.
- `200 OK` em sucesso; `400` se o nome já existir ou os dados forem inválidos.

**Login** — `POST /auth/login`
```json
{ "nome": "mateus", "senha": "123456" }
```
Resposta `200`:
```json
{ "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." }
```
Credenciais inválidas retornam `401`. O login é feito pelo **nome**, não pelo e-mail.

### Produtos

Base: `/V1/products`

| Método | Rota | Descrição | Acesso | Resposta |
|---|---|---|---|---|
| GET | `/V1/products` | Lista todos | USER ou ADMIN | `200` |
| GET | `/V1/products/{id}` | Busca por id | USER ou ADMIN | `200` / `404` |
| POST | `/V1/products` | Cria produto | **ADMIN** | `201` + produto |
| PUT | `/V1/products` | Atualiza (id no corpo) | **ADMIN** | `200` + produto / `404` |
| DELETE | `/V1/products/{id}` | Remove | **ADMIN** | `204` / `404` |

**Criar** — `POST /V1/products`
```json
{ "nome": "Teclado", "preco": 150.00, "quantidade": 10 }
```

**Atualizar** — `PUT /V1/products`
```json
{ "id": "uuid-do-produto", "nome": "Teclado Mecânico", "preco": 220.00, "quantidade": 8 }
```

### Validações

| Campo | Regra |
|---|---|
| `nome` | obrigatório, não pode ser vazio |
| `preco` | obrigatório, maior que zero |
| `quantidade` | obrigatória, zero ou mais |

Dados inválidos retornam `400`:
```json
{ "erro": "Dados invalidos", "campos": { "preco": "O preco deve ser maior que zero" } }
```

### Códigos de erro de segurança

| Situação | Resposta |
|---|---|
| Sem token, token inválido ou expirado | `401 Unauthorized` |
| Token válido, mas sem permissão (ex.: USER tentando criar produto) | `403 Forbidden` |

## Como a segurança funciona

1. No login, o `AuthenticationManager` valida nome e senha (BCrypt).
2. O `TokenService` gera um JWT **HMAC256** (emissor `API CRUD`, assunto = nome do usuário, validade 2 horas).
3. A cada requisição, o `SecurityFilter` valida o token e carrega o usuário. Se o token for inválido, a requisição segue sem autenticação e o Spring responde `401`.
4. A aplicação é **stateless** (sem sessão) e o CSRF está desabilitado.

| Role | Authorities |
|---|---|
| `Admin` | `ROLE_ADMIN` e `ROLE_USER` |
| `User` | `ROLE_USER` |

## Modelo de dados

**Usuarios**: `id` (UUID), `nome`, `email`, `senha` (hash BCrypt), `role` (texto: `Admin` ou `User`)

**produtos**: `id` (UUID), `nome`, `preco` (Double), `quantidade` (Integer)

## Configuração

A aplicação lê segredos de **variáveis de ambiente** (nada sensível fica no código):

| Variável | Obrigatória | Descrição |
|---|---|---|
| `DB_PASSWORD` | Sim | Senha do PostgreSQL |
| `JWT_SECRET` | Sim | Chave de assinatura do JWT (use 32+ caracteres aleatórios) |
| `ADMIN_NOME` | Não | Nome do ADMIN inicial |
| `ADMIN_EMAIL` | Não | E-mail do ADMIN inicial |
| `ADMIN_SENHA` | Não | Senha do ADMIN inicial |

Em vez de variáveis de ambiente, você pode copiar `local.properties.example` para
`local.properties` (na raiz do projeto, ao lado do `pom.xml`) e preencher os valores.
Esse arquivo **não deve ser versionado**: adicione `local.properties` ao `.gitignore`.

Se `ADMIN_NOME` e `ADMIN_SENHA` estiverem definidos, o ADMIN é criado na primeira inicialização
(caso ainda não exista). Veja `.env.example`.

## Como executar

### Pré-requisitos
- JDK 26
- PostgreSQL

### Passos

1. Clone o repositório:
   ```bash
   git clone https://github.com/mateuspacheco436/Primeiro-CRUD.git
   cd Primeiro-CRUD
   ```
2. Crie o banco:
   ```sql
   CREATE DATABASE crud;
   ```
3. Defina as variáveis de ambiente:
   ```bash
   export DB_PASSWORD=sua_senha
   export JWT_SECRET=uma-chave-longa-e-aleatoria-com-32-ou-mais-caracteres
   export ADMIN_NOME=admin
   export ADMIN_EMAIL=admin@email.com
   export ADMIN_SENHA=senha-do-admin
   ```
   No IntelliJ: *Run > Edit Configurations > Environment variables*.
4. Execute:
   ```bash
   ./mvnw spring-boot:run        # Linux/Mac
   mvnw.cmd spring-boot:run      # Windows
   ```
5. A API sobe em `http://localhost:8082`.

## Testando com cURL

```bash
# Login como ADMIN
curl -X POST http://localhost:8082/auth/login \
  -H "Content-Type: application/json" \
  -d '{"nome":"admin","senha":"senha-do-admin"}'

# Criar produto (ADMIN)
curl -X POST http://localhost:8082/V1/products \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nome":"Teclado","preco":150.0,"quantidade":10}'

# Listar produtos (USER ou ADMIN)
curl http://localhost:8082/V1/products -H "Authorization: Bearer SEU_TOKEN"
```

## Estrutura do projeto

```
src/main
├── java/com/example/crud
│   ├── Controller
│   │   ├── AuthenticaionController.java
│   │   └── ProductsController.java
│   ├── Domain
│   │   ├── User        # Usuario, UserRoles, AuthenticaionDTO, ResgisterDTO, LoginResponseDTO
│   │   └── Products    # Products, RequestProduct, RequestUpdateProduct
│   ├── Infra
│   │   ├── Exception   # RestExceptionHandler (erros 400 e 401 padronizados)
│   │   └── Security    # SecurityConfigurations, SecurityFilter, TokenService, AdminInitializer
│   ├── Repository      # UserRepository, ProductsRepository
│   └── Services        # AutorizationServices
└── resources
    └── application.properties
```

## Possíveis melhorias

- Paginação na listagem de produtos
- Migrations do Flyway completas, trocando `ddl-auto=update` por `validate`
- Testes automatizados
- Documentação com Swagger/OpenAPI
- Usar `BigDecimal` para o preço

## Autor

Mateus Pacheco — [@mateuspacheco436](https://github.com/mateuspacheco436)
