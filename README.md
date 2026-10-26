# DimDim API · Pipelines CI/CD no Azure DevOps

3º Checkpoint 2º Semestre · **DevOps Tools & Cloud Computing** · FIAP · Prof. João Menk

**Grupo:** DimDevs

| Nome | RM |
|---|---|
| Marcelo Antonio Scoleso Junior | RM557481 |
| João Paulo Francisco de Oliveira | RM557410 |

**Vídeo:** https://youtu.be/SEU_LINK_AQUI

---

## Solução

A DimDim migrou sua API de cadastro bancário para o Azure com Build e Deploy automatizados no **Azure DevOps**:

- **API REST Java 17 / Spring Boot 3** com CRUD de **Clientes** e **Contas** (relacionamento **1:N**, FK `conta.cliente_id`).
- **Azure SQL Database (PaaS)** como banco em nuvem.
- **Azure App Service (Linux, Java 17)** hospedando a API.
- **CI (azure-pipeline.yml):** compila, roda **11 testes JUnit** (unitários + integração com MockMvc e H2), publica o resultado dos testes e o artefato `dimdim.jar`.
- **CD (Release):** consome o artefato `drop` e faz o deploy no Web App automaticamente após cada build da `main`.
- Infraestrutura criada **por script Azure CLI** (`scripts/infra`).
- Dados sensíveis (senha do banco) só em **variáveis de ambiente** do Web App e **variável secreta** da Release. Nada de senha no código.

## Fluxo

```
Task (Boards) -> Branch -> Commit -> Pull Request (políticas) -> Merge na main
   -> CI (build + testes JUnit + artefato) -> CD (Release) -> Web App + Azure SQL
```

## Estrutura

| Caminho | Conteúdo |
|---|---|
| `src/main/java` | Código da API (model, repository, controller, dto, exception) |
| `src/test/java` | Testes JUnit 5 |
| `script.sql` | DDL das tabelas `cliente` e `conta` |
| `azure-pipeline.yml` | Pipeline de Build (CI) |
| `scripts/infra/*.sh` | Criação da infra via Azure CLI |
| `docs/api` | JSONs de todas as operações de CRUD |
| `docs/consultas-evidencias.sql` | SELECTs de evidência no banco |

## Scripts de infraestrutura (Azure CLI)

| Script | Recurso |
|---|---|
| `00-testar-regioes.sh` | Descobre regiões liberadas na assinatura Students |
| `01-criar-resource-group.sh` | Resource Group `rg-dimdim-cp3-<RM>` |
| `02-criar-banco.sh` | SQL Server `sql-dimdim-<RM>` + banco `dimdimdb` + firewall |
| `03-criar-webapp.sh` | App Service Plan B1 Linux + Web App Java 17 + App Settings |
| `04-habilitar-deploy-basico.sh` | (Plano B) credenciais de publicação para a Release |
| `99-remover-tudo.sh` | Apaga o Resource Group |

## Endpoints

`GET /` · `/api/clientes` (GET, POST, PUT, DELETE, `/{id}/contas`) · `/api/contas` (GET, POST, PUT, DELETE, `/{id}/deposito`, `/{id}/saque`). Detalhes em [`docs/api/README.md`](docs/api/README.md).

## Rodar local

```bash
mvn clean test        # roda os testes (H2 em memória, não precisa de banco)
mvn clean package     # gera target/dimdim.jar
```
