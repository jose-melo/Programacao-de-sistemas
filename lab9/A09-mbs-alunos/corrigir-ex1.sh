#!/bin/bash

# ------------------------------------------------------------------
# PARTE 1: Setup

# Aborta o script em caso de erro.
set -e

# Diretório em que este script está.
SELFDIR=$( cd "$(dirname "$0")" ; pwd -P )

# Caso o script tenha sido chamado de outro diretório, vai até
# o diretório do script.
cd ${SELFDIR}

# Importa arquivo com funções auxiliares
source 'utils.sh'

# ------------------------------------------------------------------

ZIPFILE='mbs-parte1.zip'

p_info '-------------------------------------------------------------------'
p_info 'Correção do Exercício 1 (Aula 9 - Monitor Batch)'
p_info '-------------------------------------------------------------------'

if [[ "$NO_ZIP" ]]; then
  p_info "Corrigindo diretamente do diretório .ex1"
else
  # Verifica se o arquivo zip existe.
  if [[ -f "${ZIPFILE}" ]]; then
    p_ok "[ok] Arquivo ${ZIPFILE} existe"
  else
    p_err "[erro] Arquivo ${ZIPFILE} não existe no diretório: $(pwd)"
    exit 1
  fi

  # Extrai o conteúdo do arquivo.
  rm -rf .ex1
  unzip -d .ex1 ${ZIPFILE}
fi

# Verifica se o arquivo Java existe.
if [[ -f ".ex1/UnidadeControle.java" ]]; then
  p_ok "[ok] Arquivo UnidadeControle.java existe"
else
  p_err "[erro] Arquivo UnidadeControle.java não encontrado no arquivo zip (${ZIPFILE})"
  exit 1
fi

# Move o arquivo submetido pelo aluno para o projeto.
cp '.ex1/UnidadeControle.java' 'mvn-source/src/mvn/'

# -------------------------------------------------------------------
# Compilação do projeto

set +e

echo -e "${COLOR_YELLOW}"
( cd mvn-source && ant compile )
return_code=$?
echo -e "${COLOR_END}"

if [[ $return_code -ne 0 ]]; then
  p_err '[erro] Não foi possível compilar o projeto.'
  p_err 'Nota: 0.000'
  exit 1
else
  p_ok "[Ok] Projeto compilado com sucesso."
fi

set -e
# -------------------------------------------------------------------

p_info "\nExecutando corretor..."

# Apaga arquivos de log de possíveis execuções anteriores.
rm -f mvn-source/src/log*.txt
rm -f mvn-source/build/log*.txt

# -------------------------------------------------------------------
# Execução dos testes (Java)

echo -e "${COLOR_YELLOW}"
( cd mvn-source/build && java -cp . mvn.corretor.MvnCorretorA09 | tee ../../.ex1/correcao.stdout )
echo -e "${COLOR_END}"

# -------------------------------------------------------------------

cmd=$(ruby <<'EORB'
  c = File.read(".ex1/correcao.stdout")
  pass = c.scan(/^\[Ok/).size
  fail = c.scan(/^\[Fail/).size
  total = pass.to_f/(pass+fail)
  total > 0.5 ? (puts %Q=p_ok Nota do ex.1: #{total}=) : (puts %Q=p_err Nota do ex.1: #{total}=)
EORB
)

$cmd
