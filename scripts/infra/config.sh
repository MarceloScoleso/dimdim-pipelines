#!/usr/bin/env bash
# =====================================================================
# Configuracao compartilhada pelos scripts de infraestrutura
# Edite SOMENTE este arquivo (RM e regiao). Nenhuma senha fica aqui.
# =====================================================================

export RM="557410"                 # RM do aluno (usado nos nomes dos recursos)
export LOCATION="eastus2"          # Rode 00-testar-regioes.sh se essa regiao for bloqueada

export RG="rg-dimdim-cp3-${RM}"
export SQL_SERVER="sql-dimdim-${RM}"
export SQL_DB="dimdimdb"
export SQL_ADMIN="dimdimadmin"
export PLAN="plan-dimdim-${RM}"
export WEBAPP="dimdim-api-${RM}"
export RUNTIME="JAVA:17-java17"

# Git Bash no Windows: evita que caminhos que comecam com "/" sejam convertidos
export MSYS_NO_PATHCONV=1
