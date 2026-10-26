#!/usr/bin/env bash
# Testa em quais regioes a assinatura Azure for Students deixa criar recursos.
# Cria e apaga Resource Groups VAZIOS (custo zero).
set -u
export MSYS_NO_PATHCONV=1
for REGION in eastus eastus2 centralus southcentralus westus2 westus3 canadacentral brazilsouth; do
  printf "%-16s " "$REGION"
  if az group create --name "rg-teste-${REGION}" --location "$REGION" --output none 2>/dev/null; then
    echo "permitida"
    az group delete --name "rg-teste-${REGION}" --yes --no-wait 2>/dev/null
  else
    echo "bloqueada"
  fi
done
echo "Coloque uma regiao permitida em LOCATION no config.sh"
