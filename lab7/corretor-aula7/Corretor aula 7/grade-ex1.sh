#!/bin/bash

# Aborta o script em caso de erro.
set -e

# Cores *-*
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}--------------------------------------------------------------${NC}"
echo -e "${BLUE}Correção do Exercício 1 (Aula 7 - Montador absoluto)${NC}"
echo -e "${BLUE}--------------------------------------------------------------${NC}"

echo
echo -e "Critérios de correção:"
echo -e "1.1) Nota da implementação da classe SymbolTable"
echo -e "1.2) Nota da implementação da classe Pass"
echo -e "1.3) Nota da implementação da classe Pass1"
echo -e "1.4) Geração CORRETA dos arquivos .mvn e .lst"
echo -e "Nota final do ex. 1 = (N11 + N12 + N13 + N14) / 4"
echo

# Testa se o arquivo montador.zip existe.
if [[ -f montador.zip ]]; then
  # Estamos no diretório correto.
  echo '[ok] Arquivo montador.zip existe'
else
  echo -e "${RED}[erro] Arquivo montador.zip não existe no diretório: $(pwd)${NC}"
  exit 1
fi

# Extrai os arquivos
rm -rf code
unzip -d code montador.zip

# Testa se os arquivos foram entregues
if [[ ! -f code/SymbolTable.java ]]; then
  echo -e "${RED}[erro] Arquivo SymbolTable.java não estava presente no zip${NC}"
fi

if [[ ! -f code/Pass.java ]]; then
  echo -e "${RED}[erro] Arquivo Pass.java não estava presente no zip${NC}"
fi

if [[ ! -f code/Pass1.java ]]; then
  echo -e "${RED}[erro] Arquivo Pass1.java não estava presente no zip${NC}"
fi

# Alteração do arquivo Pass1
ruby -e 'contents = File.read("code/Pass1.java"); File.open("code/Pass1.java", "w") { |f| f.write(contents.gsub("class Pass1 extends Pass", "public class Pass1 extends Pass"))} '

cp code/Pass.java montador-corretor/src/montador/Pass.java
cp code/Pass1.java montador-corretor/src/montador/Pass1.java
cp code/SymbolTable.java montador-corretor/src/util/SymbolTable.java

( cd montador-corretor && ant compile )

if [[ $? -ne 0 ]]; then
  echo '[erro] Não foi possível compilar o projeto.'
  exit 1
fi

echo "Executando testes..."

java -cp montador-corretor/build corretor.MvnMontadorCorretor | tee correcao.out

echo
echo
echo 'tmontabs/tmontabs.asm' | java -cp montador-corretor/build montador.MvnAsm
echo
echo

if [[ $? -ne 0 ]]; then
  echo '[erro] Erro ao executar o montador'
  exit 1
fi

if [[ ! -f tmontabs/tmontabs.lst ]]; then
  echo -e "${RED}[erro] Montador não gerou arquivo .lst${NC}"
  exit 1
fi

if [[ ! -f tmontabs/tmontabs.mvn ]]; then
  echo -e "${RED}[erro] Montador não gerou arquivo .mvn${NC}"
  exit 1
fi

diff_out=$(diff montador-corretor/tmontabs.mvn tmontabs/tmontabs.ref)

if [[ $diff_out = "" ]]; then
  echo -e "${GREEN}Arquivo .mvn de saída foi gerado corretamente${NC}"
  echo -e "${GREEN}Nota: 1.000${NC}"
else
  echo -e "${RED}[erro] Arquivo .mvn de saída NÃO foi gerado corretamente${NC}"
  echo "Use o comando 'diff montador-corretor/tmontabs.mvn tmontabs/tmontabs.ref' para ver a diferença entre as saídas"
fi


