#!/usr/bin/env bash
# Cria o Azure SQL Server + Database (PaaS) e as regras de firewall
set -euo pipefail
source "$(dirname "$0")/config.sh"

# Senha digitada de forma oculta: nao aparece na tela, nao fica em arquivo
read -s -p "Senha do admin do SQL (min. 8 caracteres, maiuscula, minuscula, numero e simbolo): " SQL_PASSWORD
echo

echo ">> Criando SQL Server ${SQL_SERVER}"
az sql server create \
  --name "$SQL_SERVER" \
  --resource-group "$RG" \
  --location "$LOCATION" \
  --admin-user "$SQL_ADMIN" \
  --admin-password "$SQL_PASSWORD" \
  --output table

echo ">> Criando banco ${SQL_DB} (Basic)"
az sql db create \
  --resource-group "$RG" \
  --server "$SQL_SERVER" \
  --name "$SQL_DB" \
  --service-objective Basic \
  --backup-storage-redundancy Local \
  --output table

echo ">> Liberando acesso dos servicos do Azure (Web App) ao banco"
az sql server firewall-rule create \
  --resource-group "$RG" \
  --server "$SQL_SERVER" \
  --name AllowAzureServices \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 0.0.0.0 \
  --output table

MEU_IP=$(curl -s https://api.ipify.org || true)
if [ -n "$MEU_IP" ]; then
  echo ">> Liberando o seu IP (${MEU_IP}) para usar o Editor de consultas / cliente SQL"
  az sql server firewall-rule create \
    --resource-group "$RG" \
    --server "$SQL_SERVER" \
    --name MeuIP \
    --start-ip-address "$MEU_IP" \
    --end-ip-address "$MEU_IP" \
    --output table
fi

echo ">> Banco pronto: ${SQL_SERVER}.database.windows.net / ${SQL_DB}"
echo ">> Proximo passo: rodar o script.sql no Editor de consultas do Portal"
