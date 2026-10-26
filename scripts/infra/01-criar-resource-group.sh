#!/usr/bin/env bash
# Cria o Resource Group que vai abrigar todos os recursos do projeto
set -euo pipefail
source "$(dirname "$0")/config.sh"

echo ">> Criando Resource Group ${RG} em ${LOCATION}"
az group create --name "$RG" --location "$LOCATION" --output table
