# 🚀 Guia Completo de CI/CD e Deployment

## 📁 Arquivos Criados

```
.github/
├── workflows/
│   ├── ci.yml              # Pipeline de CI (testes e build)
│   └── deploy.yml          # Pipeline de Deploy em produção
.
├── Dockerfile              # Imagem Docker para produção
├── .dockerignore            # Arquivos ignorados no build Docker
├── docker-compose.ci.yml   # Compose para ambiente CI
├── deploy.sh               # Script auxiliar de deployment
├── .env.example            # Variáveis de ambiente exemplo
├── PIPELINE_GUIDE.md       # Guia detalhado da pipeline
└── bootstrap/src/test/resources/
    └── application-test.properties  # Config para testes
```

## 🔄 Workflows Automáticos

### CI Pipeline (`.github/workflows/ci.yml`)
**Quando dispara:** Push em `main`, `develop` ou `feat/**` (ou PR para `main`/`develop`)

**Executa:**
1. ✅ Checkout do código
2. ✅ Setup Java 21 com cache Maven
3. ✅ Inicia PostgreSQL 15.4 como serviço
4. ✅ Aguarda PostgreSQL estar pronto
5. ✅ Executa testes (`mvn test`)
6. ✅ Constrói aplicação (`mvn package`)
7. ✅ Faz upload dos artefatos (30 dias retenção)
8. ✅ Análise de código (SonarCloud - opcional)

**Artefato gerado:**
- Nome: `feira-ciencias-app`
- Localização: `bootstrap/target/quarkus-app/`
- Uso: Download para testes ou deployment manual

### Deploy Pipeline (`.github/workflows/deploy.yml`)
**Quando dispara:** Tag criada (ex: `git tag v1.0.0`)

**Executa:**
1. ✅ Constrói imagem Docker
2. ✅ Faz push para GitHub Container Registry
3. ✅ Cria Release no GitHub

**Imagem gerada:**
```
ghcr.io/[seu-usuario]/feira-ciencias-api:v1.0.0
```

---

## 📚 Como Usar

### 1️⃣ Primeiro Uso - Fazer Commit dos Arquivos

```bash
# Adicionar todos os arquivos
git add .github/ Dockerfile .dockerignore deploy.sh .env.example PIPELINE_GUIDE.md

# Commit
git commit -m "chore: configure CI/CD pipeline with GitHub Actions"

# Push
git push origin feat/projects-refactor
```

### 2️⃣ Disparar Pipeline CI

```bash
# Opção A: Push em branch feat/**
git push origin feat/sua-feature

# Opção B: Criar Pull Request
# (vai disparar automático na criação)

# Opção C: Push direto em main/develop
git push origin main
```

**Verificar status:**
- Vá para **Actions** na aba do repositório GitHub
- Clique no workflow mais recente
- Visualize logs em tempo real

### 3️⃣ Disparar Pipeline Deploy

```bash
# Criar tag
git tag v1.0.0

# Push tag
git push origin v1.0.0

# Workflow vai automaticamente:
# 1. Buildar aplicação
# 2. Criar Docker image
# 3. Fazer push para GHCR
# 4. Criar Release no GitHub
```

### 4️⃣ Usar Localmente com Docker Compose

```bash
# Clonar repositório e entrar no diretório
git clone <repo>
cd feira-ciencias-api

# Copiar variáveis de ambiente
cp .env.example .env

# Usar o script de deployment
chmod +x deploy.sh

# Opções disponíveis:
./deploy.sh help          # Ver ajuda
./deploy.sh check         # Verificar pré-requisitos
./deploy.sh build         # Build local
./deploy.sh test          # Rodar testes
./deploy.sh docker        # Build Docker image
./deploy.sh start         # Iniciar serviços
./deploy.sh full          # Build + Docker + Start (completo)
```

---

## 🔐 Configuração de Secrets

### 1. SonarCloud (Opcional - para análise de código)

```bash
# 1. Ir para https://sonarcloud.io/
# 2. Conectar com GitHub
# 3. Selecionar repositório
# 4. Copiar token
# 5. Adicionar em Settings > Secrets and variables > Actions
```

**Adicionar secret:**
- Name: `SONAR_TOKEN`
- Value: (colar token do SonarCloud)

### 2. Outras Credenciais (Automáticas)

A pipeline usa automaticamente:
- `GITHUB_TOKEN` (disponível por padrão)
- Credenciais do PostgreSQL (hardcoded nos workflows, iguais ao docker-compose.yml)

---

## 🐳 Build e Deploy com Docker

### Build Local

```bash
docker build -t feira-ciencias-api:1.0.0 .
```

### Run Container

```bash
docker run -d \
  --name feira-app \
  -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/feira_ciencias \
  -e DB_USER=feira_user \
  -e DB_PASS=feira_pass \
  -e QUARKUS_PROFILE=prod \
  feira-ciencias-api:1.0.0
```

### Com Docker Compose

```bash
# Criar arquivo .env com variáveis
cp .env.example .env

# Iniciar todos os serviços
docker-compose -f docker-compose.ci.yml up -d

# Verificar status
docker-compose -f docker-compose.ci.yml ps

# Parar serviços
docker-compose -f docker-compose.ci.yml down
```

---

## 📊 Estrutura do Artefato Quarkus

```
quarkus-app/
├── app-runner.jar          # JAR principal (fast-jar)
├── quarkus-run.jar         # Jar alternativo
├── quarkus/                # Runtime Quarkus
├── lib/                    # Dependências
└── app/                    # Aplicação
```

O Dockerfile foi configurado para usar a estrutura fast-jar do Quarkus, que é otimizada para containers.

---

## 🚨 Troubleshooting

| Problema | Solução |
|----------|---------|
| Pipeline não dispara | Verificar se branch está em `main`, `develop` ou `feat/**` |
| PostgreSQL connection refused | Pipeline aguarda com health check automático (até 50s) |
| Build falha no Maven | Verificar `pom.xml` e versão Java 21 |
| Docker image não faz push | Adicionar token GitHub e configurar GHCR |
| Testes falham com JWT | Certificar que `application-test.properties` está correto |

---

## 📞 Próximos Passos

1. ✅ Fazer commit dos arquivos
2. ✅ Fazer push para verificar primeira execução da pipeline
3. ✅ Configurar secrets do SonarCloud (opcional)
4. ✅ Criar tag `v1.0.0` para testar deploy
5. ✅ Monitorar Actions no GitHub

---

## 🔗 Referências

- [Quarkus Guide](https://quarkus.io/guides/)
- [GitHub Actions Docs](https://docs.github.com/en/actions)
- [PostgreSQL Docker](https://hub.docker.com/_/postgres)
- [Buildpacks for Java](https://paketo.io/docs/buildpacks/language-families/java/)

---

**Criado com ❤️ para feira-ciencias-api**
