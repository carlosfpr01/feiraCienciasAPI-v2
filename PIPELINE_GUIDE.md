# Pipeline GitHub Actions - Guia de Configuração

## 📋 Visão Geral

A pipeline CI/CD está configurada com dois workflows:

### 1. **CI Pipeline** (`ci.yml`)
Executa automaticamente em:
- Push para `main`, `develop` ou branches `feat/**`
- Pull Requests para `main` ou `develop`

**O que faz:**
- ✅ Testa a compilação do código
- ✅ Executa testes unitários
- ✅ Constrói o artefato do Quarkus (fast-jar)
- ✅ Faz análise de qualidade de código (opcional)
- ✅ Faz upload do artefato para download

### 2. **Deploy Pipeline** (`deploy.yml`)
Executa automaticamente quando uma tag é criada (ex: `v1.0.0`)

**O que faz:**
- ✅ Constrói a imagem Docker
- ✅ Faz push para GitHub Container Registry
- ✅ Cria uma release no GitHub

---

## 🔧 Configuração Necessária

### 1. Secrets do GitHub

Acesse **Settings → Secrets and variables → Actions** e adicione:

#### Obrigatórios:
```
SONAR_TOKEN         # Token do SonarCloud (opcional, apenas para análise de código)
```

#### Opcionais (para Deploy):
```
REGISTRY_USERNAME   # Será preenchido automaticamente
REGISTRY_PASSWORD   # Token será preenchido automaticamente
```

### 2. Variáveis de Ambiente (CI)

A pipeline já está configurada com as credenciais padrão do PostgreSQL conforme o `docker-compose.yml`:

```properties
QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://localhost:5432/feira_ciencias
QUARKUS_DATASOURCE_USERNAME=feira_user
QUARKUS_DATASOURCE_PASSWORD=feira_pass
```

### 3. JWT Keys (para testes)

Se seus testes exigirem JWT keys, crie placeholders temporários:

```bash
openssl genrsa -out privateKey.pem 2048
openssl rsa -in privateKey.pem -pubout -out publicKey.pem
```

Ou configure em testes que não precisam validar JWT.

---

## 🚀 Como Usar

### Build Local
```bash
# Desenvolvimento (com Testcontainers automático)
mvn clean quarkus:dev

# Com Docker Compose
docker-compose up -d
mvn clean test
mvn clean package
```

### Disparar Pipeline CI
```bash
git push origin feat/sua-feature
# ou criar um PR
```

### Disparar Pipeline Deploy
```bash
git tag v1.0.0
git push origin v1.0.0
```

---

## 📊 Artefatos

### CI Pipeline
- **Nome:** `feira-ciencias-app`
- **Localização:** `bootstrap/target/quarkus-app/`
- **Retenção:** 30 dias
- **Uso:** Download para testes locais ou deployment manual

### Deploy Pipeline
- **Docker Image:** `ghcr.io/[seu-usuario]/feira-ciencias-api:v1.0.0`
- **Release:** GitHub Releases com o tag

---

## 🔍 Monitoramento

### Ver status da pipeline:
1. Vá para **Actions** no repositório
2. Selecione o workflow
3. Clique no run mais recente

### Logs disponíveis:
- ✅ Checkout do código
- ✅ Setup do JDK 21
- ✅ Testes (com PostgreSQL)
- ✅ Build do Quarkus
- ✅ Upload de artefatos

---

## ⚙️ Customizações

### Adicionar análise SonarCloud
1. Crie conta em https://sonarcloud.io/
2. Configure o repositório
3. Adicione `SONAR_TOKEN` aos secrets
4. O workflow já inclui a step (descomente se necessário)

### Adicionar testes de integração com LocalStack
Modifique o `ci.yml` para incluir LocalStack:

```yaml
services:
  postgres:
    # ...
  localstack:
    image: localstack/localstack:3.4.0
    ports:
      - 4566:4566
    environment:
      - SERVICES=s3
```

### Deploying para Kubernetes/Cloud
Adicione steps adicionais no `deploy.yml` para integração com seu provedor cloud.

---

## 🐛 Troubleshooting

| Problema | Solução |
|----------|---------|
| PostgreSQL connection refused | Pipeline aguarda com health check |
| JWT keys não encontradas | Configure variáveis de ambiente corretas |
| Build falha no Maven | Verifique `pom.xml` e versão do Java 21 |
| Artefato não gerado | Verifique logs do Quarkus Maven Plugin |

---

## 📝 Próximos Passos

1. ✅ Commit dos workflows
2. ⏳ Verifique primeira execução em **Actions**
3. ⏳ Configure secrets conforme necessário
4. ⏳ Customize conforme suas necessidades

---

## 📚 Referências

- [Quarkus Maven Plugin](https://quarkus.io/guides/building-native-image)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [PostgreSQL Service Container](https://docs.github.com/en/actions/using-containerized-services/creating-postgresql-service-containers)
