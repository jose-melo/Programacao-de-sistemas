#!/bin/bash

# Aborta o script em caso de erro.
set -e

# Assumindo que o script foi chamado da mesma pasta onde ele está,
# volta um diretório, para ficarmos no diretório que contém os
# arquivos JAR da MVN.
cd ..

# Testa se o arquivo mlr.jar existe.
if [[ -f mlr.jar ]]; then
  # Estamos no diretório correto.
  echo 'Ok.'
else
  # Estamos no diretório incorreto. O script deve ser chamado
  # de dentro do diretório 'exemplos'
  echo "[erro] O script deve ser chamado diretamente do diretório 'exemplos'."
fi

#
# Passo 1: montar cada arquivo individualmente.
# ------------------------------------------------------

echo "Montando o arquivo 'principal.asm'..."
echo 'exemplos/principal.asm' | java -cp mlr.jar montador.MvnAsm

echo "Montando o arquivo 'somador.asm'..."
echo 'exemplos/somador.asm' | java -cp mlr.jar montador.MvnAsm

#
# Passo 2: ligar os arquivos.
# ------------------------------------------------------

java -cp mlr.jar linker.MvnLinker exemplos/somador.mvn exemplos/principal.mvn -s exemplos/saida-relocavel.mvn

#
# Passo 3: usar o relocador para gerar o programa final.
# ------------------------------------------------------

java -cp mlr.jar relocator.MvnRelocator exemplos/saida-relocavel.mvn exemplos/saida-abs.mvn 0100

#
# Passo 4: executar o programa final na MVN.
# ------------------------------------------------------

echo -e "p exemplos/saida-abs.mvn\nr 0000 s\nn\nx" | rlwrap java -jar mvn.jar
