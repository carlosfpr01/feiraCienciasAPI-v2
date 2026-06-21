# Feira de Ciências API

Repositório: [https://github.com/vicaw/feira-ciencias-api](https://github.com/vicaw/feira-ciencias-api)

Este projeto é o backend do sistema de gestão para Feiras de Ciências Escolares. Ele foi construído utilizando **Quarkus** e **Java**, e desenhado com base em um padrão de **Monólito Modular** guiado por princípios de **Domain-Driven Design (DDD)** e **Clean Architecture**.

O objetivo desta arquitetura é garantir que o sistema seja fácil de manter, altamente testável e que, caso haja necessidade futura, seus módulos possam ser facilmente extraídos para Microsserviços independentes.

---

## 🏗️ Estrutura de Módulos (Maven Modules)

O projeto está dividido em múltiplos módulos independentes gerenciados pelo Maven:

```text
feira-ciencias/
├── bootstrap/          # Módulo principal (Entrypoint da aplicação)
├── shared-kernel/      # Código compartilhado entre todos os módulos funcionais
├── users-module/       # Módulo responsável pelos Usuários (Alunos, Professores)
├── events-module/      # Módulo responsável pelos Eventos (Feiras)
├── projects-module/    # Módulo responsável pelos Projetos dos Alunos
└── storage-module/     # Módulo responsável pelo armazenamento de arquivos
```

### 1. `bootstrap` (Módulo de Inicialização)
É o único módulo que sabe da existência de todos os outros. Ele não contém regras de negócio.
* **Responsabilidade:** Reunir todos os módulos, ler as configurações (`application.properties`), iniciar o servidor Quarkus, lidar com o ciclo de vida da aplicação e expor a API gerada.
* **Conteúdo:** Configurações (ex: `application.properties`), `Dockerfile`, etc.

### 2. `shared-kernel` (Núcleo Compartilhado)
Contém componentes transversais que pertencem a toda a aplicação, garantindo padronização sem gerar acoplamento de negócios.
* **Responsabilidade:** Prover blocos de construção comuns.
* **Conteúdo:** 
  * Exceções Globais (`BusinessRuleException`, `EntityNotFoundException`).
  * Tratadores de Exceção JAX-RS (`ExceptionMapper`).
  * Enums globais (quando estritamente necessários).

### 3. Módulos Funcionais (`users-module`, `events-module`, `projects-module`, `storage-module`)
São as "fatias" da aplicação. Cada módulo representa um Bounded Context (Contexto Delimitado) do DDD. **Eles não dependem uns dos outros (exceto via chamadas controladas, se necessário)**, eles dependem apenas do `shared-kernel`.

*   **`users-module`**: Gestão de Alunos e Professores, autenticação e perfis.
*   **`events-module`**: Gestão das edições da Feira de Ciências (Datas, Status).
*   **`projects-module`**: O coração do sistema; inscrições, diários de bordo e materiais.
*   **`storage-module`**: Abstração técnica para armazenamento de arquivos e anexos.

---

## 🏛️ Clean Architecture (Dentro de cada Módulo Funcional)

Ao olhar para dentro de um módulo funcional (por exemplo, `users-module`), você encontrará a seguinte divisão de pacotes, refletindo a Clean Architecture:

```text
users-module/src/main/java/br/com/escola/feiraciencias/users/
├── api/                  # Camada de Interface / Adapters de Entrada
│   ├── dto/              # Objetos de Transferência de Dados
│   │   ├── requests/     # Payloads de entrada (ex: LoginRequest)
│   │   └── responses/    # Retornos de saída (ex: TokenResponse)
│   ├── mappers/          # Conversores Automáticos (MapStruct: DTO <-> Domínio)
│   └── resources/        # Endpoints HTTP (Controllers JAX-RS)
│
├── application/          # Camada de Aplicação
│   ├── usecases/         # Casos de Uso (Fluxos de negócio únicos, ex: CadastrarAluno)
│   └── services/         # Serviços de Aplicação (Orquestração repetitiva/ajudantes)
│
├── domain/               # Camada de Domínio (O coração do sistema)
│   ├── model/            # Entidades de Domínio Ricas (Regras de negócio e estado)
│   ├── services/         # Interfaces de Serviços de Domínio (ex: PasswordService)
│   └── repositories/     # Interfaces de Repositório (Contratos de persistência)
│
└── infrastructure/       # Camada de Infraestrutura / Adapters de Saída
    ├── security/         # Implementações de Segurança (ex: BCrypt)
    ├── client/           # Integrações com APIs externas (Clients/Gateways)
    └── persistence/      # Implementações de Banco de Dados
        ├── entities/     # Entidades JPA (@Entity)
        ├── mappers/      # Conversores Automáticos (MapStruct: JPA <-> Domínio)
        └── repositories/ # Implementação concreta do repositório (ex: Panache)
```

### Detalhamento das Camadas (Regra de Dependência)
*A regra fundamental é que as dependências sempre apontam para o centro (Domínio).*

1. **`domain` (Domínio)**: É o coração. Contém a lógica de negócio mais pura. Define **o que** o sistema faz e os contratos (interfaces) que ele precisa que o mundo externo cumpra.
2. **`application` (Aplicação)**: Orquestra os fluxos de dados. Usa os modelos e repositórios do domínio para realizar tarefas específicas. Os **Use Cases** são as ações do sistema, e os **Services** aqui ajudam a evitar repetição de código de orquestração.
3. **`api` (Interface)**: É como o mundo externo (Web, Mobile) fala com o sistema. Traduz requisições para chamadas da Aplicação e Domínio.
4. **`infrastructure` (Infraestrutura)**: Contém as implementações técnicas das interfaces do Domínio. Aqui vive o código que fala com o Banco de Dados, APIs de Terceiros ou sistemas de Hashing.

---

## 🛠️ Tecnologias Utilizadas

* **Quarkus**: Framework Java Cloud-Native super rápido.
* **Hibernate ORM com Panache**: Para persistência simplificada no padrão Active Record / Repository.
* **PostgreSQL**: Banco de dados relacional.
* **Flyway**: Ferramenta de versionamento de banco de dados (Migrações modulares separadas por funcionalidade).
* **MapStruct**: Geração automática de código para Mapeamento de objetos (DTO -> Model -> JPA).
* **Lombok**: Redução de código boilerplate (Getters, Setters, Construtores).
* **SmallRye JWT**: Autenticação e Autorização via tokens JWT.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
* **Java 21** ou superior.
* **Maven 3.9+**.
* **Docker e Docker Compose**.

### 📋 Status Atual do Sistema

O sistema está em desenvolvimento com os seguintes módulos funcionais implementados:

* **`users-module`**: ✅ Gestão de Usuários (Alunos e Professores), autenticação JWT e perfis.
* **`events-module`**: ✅ Gestão de Eventos/Feiras de Ciências (Datas, Status).
* **`projects-module`**: ✅ Gestão de Projetos dos Alunos (Inscrições, Diários, Materiais).
* **`storage-module`**: ✅ Abstração para armazenamento e upload de arquivos.
* **`shared-kernel`**: ✅ Componentes compartilhados (Exceções, Mappers, Utilidades).

---

## Feira de Ciências API — Rotas
 
### Autenticação
 
Todas as rotas protegidas exigem o header:
```
Authorization: Bearer <token>
```
 
---
 
### Usuários `/usuarios`
 
#### Registrar usuário (aceitar convite)
```
POST /usuarios
```
Público. O usuário preenche email e senha usando o token recebido por convite.
 
**Body:**
```json
{
  "token": "uuid-do-convite",
  "email": "usuario@email.com",
  "senha": "minimo6caracteres"
}
```
 
---
 
#### Listar usuários
```
GET /usuarios?tipo=ALUNO&page=0&size=20
```
Requer role: `ADMIN` ou `PROFESSOR`.
 
| Query param | Tipo | Descrição |
|---|---|---|
| `tipo` | `ADMIN \| PROFESSOR \| ALUNO` | Filtra por tipo |
| `page` | integer | Página (default: 0) |
| `size` | integer | Tamanho (default: 20) |
| `totalSize` | integer | Número de Páginas |
| `hasMore` | bool | Indica se existem mais páginas disponíveis |
 
---
 
#### Atualizar dados cadastrais
```
PATCH /usuarios/{id}
```
Requer role: `ADMIN`, `PROFESSOR` ou `ALUNO`.
 
**Body** (todos opcionais):
```json
{
  "nome": "Novo Nome",
  "email": "novo@email.com",
  "matricula": "2024001",
  "anoEscolar": "3º Ano",
  "materia": "Matemática"
}
```
 
---
 
#### Excluir usuário
```
DELETE /usuarios/{id}
```
Requer role: `ADMIN` ou `PROFESSOR`.
 
Regras:
- `ADMIN` pode excluir qualquer usuário exceto outros admins
- `PROFESSOR` só pode excluir alunos vinculados a ele
---
 
#### Alterar senha (próprio usuário)
```
PATCH /usuarios/{id}/senha
```
Requer role: `ADMIN`, `PROFESSOR` ou `ALUNO`.
 
**Body:**
```json
{
  "senhaAtual": "senha123",
  "novaSenha": "novasenha123"
}
```
 
---
 
#### Resetar senha (admin, professores com seus alunos)
```
POST /usuarios/{id}/senha/reset
```
Requer role: `ADMIN, PROFESSOR`.
 
Gera uma senha temporária e a retorna no response. O usuário deverá alterá-la no próximo acesso.
 
---
 
### Convites `/convites`
 
#### Listar convites
```
GET /convites?status=PENDENTE&page=0&size=20
```
Requer role: `ADMIN` ou `PROFESSOR`.
 
| Query param | Tipo | Descrição |
|---|---|---|
| `status` | `PENDENTE \| USADO \| EXPIRADO \| CANCELADO` | Filtra por status |
| `page` | integer | Página (default: 0) |
| `size` | integer | Tamanho (default: 20) |
| `totalSize` | integer | Número de Páginas |
| `hasMore` | bool | Indica se existem mais páginas disponíveis |
 
---
 
#### Gerar convite para aluno
```
POST /convites/alunos
```
Requer role: `PROFESSOR`.
 
**Body:**
```json
{
  "nome": "Nome do Aluno",
  "matricula": "2024001",
  "anoEscolar": "3º Ano"
}
```
 
**Response:**
```json
{
  "token": "uuid-do-convite"
}
```
 
> No momento o token é retornado no response. TODO: Mudar para envio por email (?).
 
---
 
### Gerar convite para professor
```
POST /convites/professores
```
Requer role: `ADMIN`.
 
**Body:**
```json
{
  "nome": "Nome do Professor",
  "disciplina": "Matemática"
}
```
 
**Response:**
```json
{
  "token": "uuid-do-convite"
}
```
 
---
 
### Cancelar convite
```
DELETE /convites/{id}
```
Requer role: `ADMIN` ou `PROFESSOR`.
 
- `PROFESSOR` só pode cancelar convites que ele mesmo gerou
- `ADMIN` pode cancelar qualquer convite
---
 
### Status dos convites
 
| Status | Descrição |
|---|---|
| `PENDENTE` | Aguardando aceite |
| `USADO` | Aceito pelo usuário |
| `EXPIRADO` | Prazo de 7 dias encerrado |
| `CANCELADO` | Cancelado manualmente |
 
---
 
### Fluxo de cadastro
 
```
1. PROFESSOR/ADMIN gera convite  →  POST /convites/alunos ou /convites/professores
2. Sistema envia token  →  usuário recebe link de ativação
3. Usuário aceita o convite       →  POST /usuarios com token + email + senha
```

---

### Modo de Desenvolvimento (Hot Reload)

#### **Passo 1: Iniciar o Banco de Dados**

Na raiz do projeto, execute o comando para subir o PostgreSQL via Docker Compose:

```bash
docker-compose up -d
```

**Verificar se o banco está rodando:**
```bash
docker-compose ps
```

Você deve ver algo como:
```
CONTAINER ID   IMAGE       STATUS
xxxxxxxx       postgres    Up X seconds
```

O banco estará disponível em:
- **Host**: `localhost`
- **Porta**: `5432`
- **Banco de Dados**: `feira_ciencias` (ou conforme configurado no `application.properties`)

#### **Passo 2: Rodar a Aplicação em Modo Dev**

Após confirmar que o banco está rodando, execute o projeto com recarga automática de código:

```bash
mvn quarkus:dev -pl bootstrap -am
```

Este comando:
- Compila todos os módulos (`-am` = also-make)
- Ativa o hot-reload (recarga automática ao salvar arquivos)
- Inicia o servidor Quarkus

**Aguarde a mensagem:**
```
✅ Quarkus x.x.x started in 0.000s
🎉 Listening on: http://localhost:8080
```

---

### 🌐 Acessar a Aplicação

Após rodar com sucesso, acesse:

* **API REST**: `http://localhost:8080`
* **Swagger UI**: `http://localhost:8080/q/swagger-ui`
* **Métricas**: `http://localhost:8080/q/metrics`

---

### 🔑 Usuário de Teste (Modo Dev)

Ao iniciar em modo dev, um professor padrão é criado automaticamente via Flyway:

* **Email**: `professor@escola.com`
* **Senha**: `123456`

Use essas credenciais para fazer login e obter um token JWT.

---

### ⛔ Parar a Aplicação

Para parar a aplicação em desenvolvimento, pressione: **`Ctrl + C`** no terminal.

---

### 🧹 Parar o Banco de Dados

Para parar os containers Docker:

```bash
docker-compose down
```

Para parar **sem remover** os dados:
```bash
docker-compose stop
```

---

### 📦 Gerando o Executável (Produção)

Para gerar o JAR otimizado para produção:

```bash
mvn clean package
```

O JAR será gerado em: `bootstrap/target/`

---

### 🔧 Troubleshooting

**Erro: Port 5432 already in use**
```bash
docker-compose down  # Parar os containers
docker-compose up -d # Reiniciar
```

**Erro: Connection refused ao conectar no banco**
- Aguarde alguns segundos após `docker-compose up -d` para o PostgreSQL iniciar
- Verifique com `docker-compose ps`

**Erro: Maven build failures**
- Certifique-se de que Java 21+ está instalado: `java -version`
- Limpe o cache: `mvn clean`
