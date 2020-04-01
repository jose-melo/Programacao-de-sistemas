# Montador da MVN escrito em Linguagem de Montagem
## Douglas Luan de Souza
## Lucas Gaia de Castro


*Pronto*:
- Funções não-utilizadas removidas de montador.asm
- Teste de montagem, ligação e relocação

*A conferir*:
- Blocos de código abaixo de Readline:
  > Caso sejam parte da subrotina Readline, melhor tirar os 
  > ;=======
  > pois eles dão um sinal de um bloco de código totalmente novo, como uma subrotina diferente.
- Verificar final da subrotina ATTP
- Ver se no passo1 e no passo2 há condição de parada


*Tarefas*:
A definir:
- Desenvolver sub-rotina CLEAR ARGS, que limpa o campo onde a READLINE salva.

Lucas:
- terminar MONTA1: trata op

Douglas:
-Desenvolver MONTA2, que deve ler o arquivo .asm e criar o .mvn

*Considerações*:
- O arquivo .asm é "amigável". A escrita é sempre par e o EOL é " \n"
- A sub-rotina READLINE salva até 3 argumentos que le na linha.
  - Se forem lidos 3, o primeiro é rótulo.
  - Se o começo do terceiro for 0000, só há 2.
- Após o passo MONTA1 temos a tabela de símbolos definida.
  - A tabela de símbolos começa em STI (Symbol Table Init)
  - Após o final dela comeca a SADR (Symbol Address).
  - SADR contém o addr do respectivo sym na ST

*Variáveis*:
- PARG é genérico para uso na área reservada para READLINE guardar leituras.
- PARG1, PARG2 E PARG3 são fixas.
  - Apontam para o começo da área reservada para ARGn.
Exemplo:
; Area de dados
ARG1 $ /4 <- PARG1 aponta para cá
.
.
.
ARG2 $ /4 <- PARG2 para cá
.
.
.
ARG3 $ /4 <- PARG3 para cá

### Passo2
 Em Cada Linha, Há 3 Argumentos:
 Arg1      Arg2        Arg3      . Eles Ficam:
 Rótulo    Operador    Operando  . (1) Quando Há Rótulo, Eles São:
 Operador  Operando    0         . (2) Quando Não Há Rótulo
 Em Ambos Os Casos, Se Calcula O Ic:
 Quando Há Rótulo, Ic = Addr(rótulo)
 Quando Não Há,    Ic = Ic + 2
 Salva-se O Ic Como Endereço De Código No Arquivo Mvn
 Daí, Obtém-se O Mnemônico E Este É Convertido Para Código Do Operador
 Então Obtém-se O Operando, Soma-se Ele Ao Operador
 Por Último, Salva-se A Instrução No Arquivo

 > Talvez seja necessário converter os valores para hexa antes de serem escritos no arquivo. Mas isso vai depender de como eles foram salvos na memória.
 > Também não é necessário ler novamente do arquivo, pois as tabelas já tem tudo?
 > Falta adicionar código para identificar o final do arquivo. 
