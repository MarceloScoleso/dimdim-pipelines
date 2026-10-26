# JSONs para o CRUD da API

Base: `https://dimdim-api-<RM>.azurewebsites.net`

| Operação | Método e rota | Corpo |
|---|---|---|
| Info da API | `GET /` | |
| Listar clientes | `GET /api/clientes` | |
| Buscar cliente | `GET /api/clientes/{id}` | |
| Contas do cliente | `GET /api/clientes/{id}/contas` | |
| Criar cliente | `POST /api/clientes` | `clientes/01-post-cliente-ana.json`, `clientes/02-post-cliente-bruno.json` |
| Atualizar cliente | `PUT /api/clientes/{id}` | `clientes/03-put-cliente.json` |
| Excluir cliente | `DELETE /api/clientes/{id}` | (retorna 409 se tiver contas) |
| Listar contas | `GET /api/contas` | |
| Buscar conta | `GET /api/contas/{id}` | |
| Criar conta | `POST /api/contas` | `contas/01-post-conta-corrente.json`, `contas/02-post-conta-poupanca.json` |
| Atualizar conta | `PUT /api/contas/{id}` | `contas/03-put-conta.json` |
| Depósito | `POST /api/contas/{id}/deposito` | `contas/04-post-deposito.json` |
| Saque | `POST /api/contas/{id}/saque` | `contas/05-post-saque.json` |
| Excluir conta | `DELETE /api/contas/{id}` | |

## Mesmo CRUD com curl (Git Bash, a partir da raiz do repo)

```bash
URL=https://dimdim-api-557410.azurewebsites.net
H="Content-Type: application/json"

curl -s $URL/
curl -s -X POST $URL/api/clientes -H "$H" -d @docs/api/clientes/01-post-cliente-ana.json
curl -s -X POST $URL/api/clientes -H "$H" -d @docs/api/clientes/02-post-cliente-bruno.json
curl -s -X PUT  $URL/api/clientes/1 -H "$H" -d @docs/api/clientes/03-put-cliente.json
curl -s -X POST $URL/api/contas -H "$H" -d @docs/api/contas/01-post-conta-corrente.json
curl -s -X POST $URL/api/contas -H "$H" -d @docs/api/contas/02-post-conta-poupanca.json
curl -s -X PUT  $URL/api/contas/1 -H "$H" -d @docs/api/contas/03-put-conta.json
curl -s -X POST $URL/api/contas/1/deposito -H "$H" -d @docs/api/contas/04-post-deposito.json
curl -s -X POST $URL/api/contas/1/saque -H "$H" -d @docs/api/contas/05-post-saque.json
curl -s -X DELETE $URL/api/contas/2 -w "%{http_code}\n"
curl -s -X DELETE $URL/api/clientes/2 -w "%{http_code}\n"
```
Depois de cada comando, rode `docs/consultas-evidencias.sql` no Editor de consultas do banco.
