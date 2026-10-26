#!/usr/bin/env bash
# Remove TODOS os recursos do projeto (apaga somente o Resource Group do CP3)
set -euo pipefail
source "$(dirname "$0")/config.sh"

read -p "Apagar o Resource Group ${RG} e tudo dentro dele? (s/N) " RESP
if [ "$RESP" = "s" ]; then
  az group delete --name "$RG" --yes --no-wait
  echo ">> Remocao iniciada (leva alguns minutos)"
else
  echo ">> Cancelado"
fi
