#!/usr/bin/env bash
# PLANO B do deploy (so use se NAO conseguir criar a Service Connection no
# Azure DevOps). Habilita a autenticacao basica do SCM (Kudu) e mostra as
# credenciais de publicacao para cadastrar como variaveis SECRETAS na Release.
# ATENCAO: nao rode este script gravando a tela (ele mostra a senha).
set -euo pipefail
source "$(dirname "$0")/config.sh"

SUB_ID=$(az account show --query id -o tsv)
az resource update \
  --ids "/subscriptions/${SUB_ID}/resourceGroups/${RG}/providers/Microsoft.Web/sites/${WEBAPP}/basicPublishingCredentialsPolicies/scm" \
  --set properties.allow=true --output none

echo ">> Credenciais de publicacao (cadastre como DEPLOY_USER e DEPLOY_PASS, marcando o cadeado):"
az webapp deployment list-publishing-credentials \
  --name "$WEBAPP" --resource-group "$RG" \
  --query "{DEPLOY_USER:publishingUserName, DEPLOY_PASS:publishingPassword}" -o table
