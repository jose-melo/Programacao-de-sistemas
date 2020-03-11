#!/bin/bash

# Aborta o script em caso de erro.
set -e

# Cores *-*
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}--------------------------------------------------------------${NC}"
echo -e "${BLUE}Correção do Exercício 2 (Aula 7 - Montador absoluto)${NC}"
echo -e "${BLUE}--------------------------------------------------------------${NC}"

# Testa se o arquivo op-mnem.asm existe.

if [[ -f op-mnem.asm ]]; then
  # Estamos no diretório correto.
  echo -e "${GREEN}[ok] Arquivo op-mnem.asm existe${NC}"
else
  echo -e "${RED}[erro] Arquivo op-mnem.asm não existe no diretório: $(pwd)${NC}"
  exit 1
fi

if [[ ! -f montador-corretor/build/montador/MvnAsm.class ]]; then
  echo -e "${RED}[erro] O projeto do montador ainda não foi compilado. Corrija o ex. 1 primeiro.${NC}"
  exit 1
fi

echo -e "\n"
echo 'op-mnem.asm' | java -cp montador-corretor/build montador.MvnAsm
echo -e "\n"

if [[ ! -f op-mnem.lst ]]; then
  echo -e "${RED}[erro] Montador não gerou arquivo .lst${NC}"
  exit 1
fi

if [[ ! -f op-mnem.mvn ]]; then
  echo -e "${RED}[erro] Montador não gerou arquivo .mvn${NC}"
  exit 1
fi

echo "Montador finalizou; executando programa na MVN..."

echo -e "p op-mnem.mvn\nr 0000 s\nn\nx" | java -jar mvn.jar

echo "${GREEN}Execução finalizada${NC}"
