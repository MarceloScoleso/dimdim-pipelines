#!/usr/bin/env bash
# Cria o App Service Plan (Linux) + Web App Java 17 e configura as
# variaveis de ambiente (App Settings) com a conexao do banco.
set -euo pipefail
source "$(dirname "$0")/config.sh"

read -s -p "Senha do admin do SQL (a mesma do script 02): " SQL_PASSWORD
echo

echo ">> Criando App Service Plan ${PLAN} (Linux B1)"
az appservice plan create \
  --name "$PLAN" \
  --resource-group "$RG" \
  --location "$LOCATION" \
  --is-linux \
  --sku B1 \
  --output table

echo ">> Criando Web App ${WEBAPP} (${RUNTIME})"
az webapp create \
  --name "$WEBAPP" \
  --resource-group "$RG" \
  --plan "$PLAN" \
  --runtime "$RUNTIME" \
  --output table

JDBC_URL="jdbc:sqlserver://${SQL_SERVER}.database.windows.net:1433;database=${SQL_DB};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"

echo ">> Configurando variaveis de ambiente (App Settings) do Web App"
az webapp config appsettings set \
  --name "$WEBAPP" \
  --resource-group "$RG" \
  --settings \
    "SPRING_DATASOURCE_URL=${JDBC_URL}" \
    "SPRING_DATASOURCE_USERNAME=${SQL_ADMIN}" \
    "SPRING_DATASOURCE_PASSWORD=${SQL_PASSWORD}" \
    "SERVER_PORT=80" \
  --output none

echo ">> Web App: https://${WEBAPP}.azurewebsites.net"
echo ">> (as App Settings ficam no Portal: Web App > Configuracoes > Variaveis de ambiente)"
