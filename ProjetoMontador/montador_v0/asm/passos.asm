; ==================================================================
;** DECLARAÇÃO DE SÍMBOLOS IMPORTADOS E EXPORTADOS
MONTA1      >
MONTA2      >

ERRO        <
PARG3       <
PARG2       <
PARG1       <
WORD        <
CONV        <

STE         <
STI         <
PST         <
FIMASMB     <
H2          <
IC          <
LD          <
MM          <
ARR         <
ECOM        <
CIFRA       <
MTOC        <
MNEM        <
OPCODE      <
SPC2        <
NEWLINE     <
ADDRI       <
OPERAND     <

HEXOP       <
BINOP       <
OCTAOP      <
ASCIIOP     <        

BH          <
OH          <
AH          <        

; ====================================================================
;** MONTALINHA -- TRADUZ OPERADOR E OPERANDO EM INSTRUÇÃO NA LINGUAGEM
; DA MVN, ADICIONANDO ESPAÇO EM BRANCO E FIM DE LINHA - DLS 2018

PONT1     $   =1
PONT2     $   =1

OP-PREFIX $   =0             ; GUARDA O PREFIXO DO OPERANDO PARA DESCOBRIR A BASE
        
MONTALINHA  $   =1
            PD  /0302        ; SALVA NO ARQUIVO MVN
            LD  SPC2         ; CARREGA ESPAÇO EM BRANCO
            PD  /0302        ; SALVA ESTE ESPAÇO NO ARQUIVO
            LD  PONT1        ; MONTA INSTRUÇÃO QUE LÊ O ARGUMENTO 2
            +   LD
            MM  MNTOP        ; MONTA INSTRUÇÃO PARA LEITURA DO OPERADOR
MNTOP       $   =0001        ; CARREGA O MNEMÔNICO
            MM  MNEM         ; SALVA NO MNEMÔNICO 
            SC  MTOC         ; E CONVERTE PARA CÓDIGO
            LD  PONT2        ;
            +   LD           ;
            MM  MNTARG       ; MONTA INSTRUÇÃO PARA LEITURA DA OPERANDO
MNTARG      $   =0001        ; CARREGA O OPERANDO
            MM  OPERAND
            SC  GET-PREFIX   ; PEGA O PREFIX DO OPERANDO PARA DESCOBRIR A BASE
            MM  OP-PREFIX  
            -   HEXOP        ; VERIFICA SE OPERANDO ESTÁ EM HEX
            JZ  IN-HEXOP
            LD  OP-PREFIX
            -   BINOP       
            JZ  IN-BINOP     ; VERIFICA SE OPERANDO ESTÁ EM BINARIO
            LD  OP-PREFIX
            -   OCTAOP       
            JZ  IN-OCTAOP    ; VERIFICA SE OPERANDO ESTÁ EM OCTA
            LD  OP-PREFIX       
            -   ASCIIOP      
            JZ  IN-ASCIIOP   ; VERIFICA SE OPERANDO ESTÁ EM ASCII
GET-PREFIX  $  =1
            SC UNPACK
            LD B1
            RS GET-PREFIX
IN-HEXOP    LD OPERAND          ; JA ESTÁ EM HEX, NADA A FAZER
            JP ESCREVE
IN-BINOP    LD OPERAND          
            SC BINHEX               ; CONVERTE DE BINARIO PARA HEX (REPRESENTACAO ASCII), RESULTADO NO ACUMULADOR
            JP ESCREVE
IN-OCTAOP   LD OPERAND
            SC OB               ; CONVERTE DE OCTA PARA BINARIO, RESULTADO NO ACUMULADOR
            SC BINHEX           ; CONVERTE DE BINARIO PARA HEX (REPRESENTACAO ASCII), RESULTADO NO ACUMULADOR
            JP ESCREVE
IN-ASCIIOP  LD OPERAND
            SC CONV               ; CONVERTE DE ASCII PARA HEX, RESULTADO NO ACUMULADOR            
            JP ESCREVE                 
ESCREVE     +   OPCODE       ; SOMA COM O OPERADOR
            PD  /0302        ; ADICIONA INSTRUÇÃO AO ARQUIVO MVN
            LD  NEWLINE      ; ADICIONA UMA NOVA LINHA
            PD  /0302        ; AO ARQUIVO
            RS  MONTALINHA

; ===================================================================
; MONTA1 -- DEFINE TABELA DE SIMBOLOS
; EM CADA LINHA SÃO GUARDADOS ARG1 ARG2 [ARG3]
; CASO HAJA 3 ARGUMENTOS, INSERE O ROTULO NA TABELA DE SIMBOLOS OU ACUSA ERRO
; AO INSERIR O ROTULO VINCULA IC A ELE
; CASO HAJA APENAS 2 ARGUMENTOS, TRATA PSEUDO-INSTRUCOES
MONTA1      K  /00          ; 
            LD PARG3        ; CARREGA ENDEREÇO DO TERCEIRO ARG LIDO
            +  LD           ; CONSTROI INSTRUCAO LOAD
            MM GETARG3      ; SALVA EM GETARG3
GETARG3     K  /00          ; CARREGA TERCEIRO ARG
            JZ SEMROT       ; SE 0, TEM APENAS 2 ARGS = SEM ROTULO
            LD PARG1        ; CASO CONTRARIO, INSERE ROTULO NA TABELA DE SIMBOLOS
            +  LD           ; CONSTROI INSTRUCAO LOAD
            MM GETROT       ; SALVA EM GETROT
GETROT      K  /00          ; CARREGA ROTULO
            MM WORD         ; SALVA EM WORD
            LD STE          ; CARREGA SYMBOL TABLE END
            MM PST          ; SALVA EM POINTER SYMBOL TABLE
LOOPST      -  STI          ; FIM DA TABELA?
            JZ DEFINE       ; INSERE NOVO SIMBOLO SE SIM
            LD PST          ; CASO CONTRARIO, TESTA PROX END (BAIXO PARA CIMA)
            +  LD           ; CONSTROI INSTRUCAO LOAD
            MM COMP         ; SALVA EM COMP(ARA)
COMP        K  /00          ; CARREGA SIMBOLO TABELADO
            -  WORD         ; COMPARA COM LIDO AGORA
            JZ ERRROT       ; ACHOU, ACUSA ERRO
            LD PST          ; NAO ACHOU,
            -  H2           ; ATUALIZA PONTEIRO
            MM PST          ; DA TABELA
            JP LOOPST       ; REPETE
DEFINE      LD STE          ; CARREGA FIM DA TABELA (POSICAO VAZIA)
            +  MM           ; CONSTROI INSTRUCAO MOVE
            MM INS          ; SALVA EM INS(ERE)
            LD WORD         ; CARREGA SIMBOLO LIDO
INS         MM STE          ; GUARDA NA ULTIMA POSICAO
            LD STE          ; ADQUIRE PONTEIRO PARA
            -  STI          ; TABELA DE ENDERECOS
            +  ADDRI        ; + INICIO DA TABELA DE ENDERECOS
            +  MM           ; + INSTRUCAO MOVE
            MM DEFINE2      ; SALVA EM DEFINE2
            LD IC           ; CARREGA IC
DEFINE2     MM ADDRI        ; SALVA NO ENDERECO APONTADO
            +  H2           ; INCREMENTA IC
            MM IC           ; SALVA
FIMM1       RS MONTA1       ;

SEMROT      LD PARG1        ; CARREGA POSICAO DO PRIMEIRO ARG = OP
            +  LD           ; SOMA COM OPERACAO LOAD
            MM GETOP        ; SALVA EM GETOP
GETOP       LD PARG1        ; CARREGA OPERACAO
            MM MNEM           ; SALVA EM MEMORIA
            -  ARR          ; CHECA SE ARROBA
            JZ RELOC        ; ATUALIZA IC EM CASO POSITIVO
            LD MNEM
            - ECOM          ; CHECA SE E COMERCIAL
            JZ RELOC        ; ATUALIZA IC EM CASO POSITIVO
            LD MNEM
            -  CIFRA        ; CHECA SE CIFRAO
            JZ RESERVE      ; ATUALIZA IC EM CASO POSITIVO
            LD IC           ; CASO NAO SEJA NENHUM DOS ANTERIORES
            +  H2           ; APENAS INCREMENTA IC
            MM IC           ; E SALVA
            JP FIMM1        ; FINALIZA MONTA1

RELOC       LD PARG2      ; CARREGA END DO OPERANDO
            +  LD         ; CONSTROI INSTRUCAO LD
            MM GETOP2     ; SALVA EM GETOP2
GETOP2      LD PARG2      ; CARREGA OPERANDO
            SC CONV       ; CONVERTE PARA HEXA
            LD OPERAND    ; CARREGA VALOR CONVERTIDO
            MM IC         ; ATUALIZA IC
            JP FIMM1      ; FINALIZA MONTA1
RESERVE     LD PARG2      ; CARREGA END DO OPERANDO
            +  LD         ; CONSTROI INSTRUCAO LD
            MM GETOP3     ; SALVA EM GETOP3
GETOP3      LD PARG2      ; CARREGA OPERANDO
            SC CONV       ; CONVERTE PARA HEXA
            LD OPERAND    ; CARREGA VALOR CONVERTIDO        
            *  H2         ; MULTIPLICA AREA RESERVADA POR 2
            +  IC         ; SOMA COM IC
            MM IC         ; ATUALIZA IC
            JP FIMM1      ; FINALIZA MONTA1

ERRROT      LV /2         ; EM CASO DE ROTULO JA DEFINIDO
            SC ERRO       ; ACUSA ERRO3
            JP FIMASMB    ; PARA MONTADOR


; ==================================================================
;** MONTA2 -- MONTA UMA LINHA DE CÓDIGO FONTE - DLS 2018
; EM CADA LINHA, HÁ 3 ARGUMENTOS:
; ARG1      ARG2        ARG3      . ELES FICAM:
; RÓTULO    OPERADOR    OPERANDO  . (1) QUANDO HÁ RÓTULO, ELES SÃO:
; OPERADOR  OPERANDO    0         . (2) QUANDO NÃO HÁ RÓTULO
; EM AMBOS OS CASOS, SE CALCULA O IC:
; QUANDO HÁ RÓTULO, IC = ADDR(RÓTULO)
; QUANDO NÃO HÁ,    IC = IC + 2
; SALVA-SE O IC COMO ENDEREÇO DE CÓDIGO NO ARQUIVO MVN
; DAÍ, OBTÉM-SE O MNEMÔNICO E ESTE É CONVERTIDO PARA CÓDIGO DO OPERADOR
; ENTÃO OBTÉM-SE O OPERANDO, SOMA-SE ELE AO OPERADOR
; POR ÚLTIMO, SALVA-SE A INSTRUÇÃO NO ARQUIVO

MONTA2      $   /0001        ; 
            LD  PARG3        ; 
            +   LD           ; 
            MM  GETARG32     ; 
GETARG32    $   =0001        ; 
            JZ  SEMROT2      ; NÃO HÁ RÓTULO
            LD  PARG1        ; SE HÁ RÓTULO, O TRATA
            +   LD           ; 
            MM  GETROT2      ; 
GETROT2     $   =0001        ; 
            MM  WORD         ; 
            LD  STE          ; SYMBOL TABLE END
            -   H2           ; STE E POSICAO VAZIA
            MM  PST          ; POINTER SYMBOL TABLE
LOOPST2     -   STI          ; PERCORREU TODA A TABELA DO FIM AO INÍCIO?
            JN  ERRSYM       ; SÍMBOLO NÃO ESTÁ NA TABELA
            LD  PST          ; 
            +   LD           ; 
            MM  COMP         ; 
COMP2       $   =0001        ; CARREGA SIMBOLO TABELADO
            -   WORD         ; COMPARA COM LIDO AGORA
            JZ  GETADDR      ; ACHOU, PEGA O SEU ENDEREÇO
            LD  PST          ; NAO ACHOU
            -   H2           ; ATUALIZA PONTEIRO
            MM  PST          ; DA TABELA
            JP  LOOPST2      ; REPETE
ERRSYM      LV  /0002        ; CARREGA CÓDIGO DE ERRO 2
            SC  ERRO         ; IMPRIME ERRO
            JP  FIMASMB      ; FINALIZA COM O ERRO ENCONTRADO
            LD  WORD         ; CARREGA SIMBOLO LIDO
GETADDR     LD  PST          ;
            -   STI          ; PEGA VALOR CORRETO DO PONTEIRO
            +   ADDRI        ; SOMA COM INICIO DA TABELA DE ENDERECOS 
            ;/   H2           ;  ENCONTRA ONDE O ENDEREÇO ESTÁ ARMAZENADO !SÓ HÁ 1 WORD PARA CADA RÓTULO!
            +   LD           ;  PREPARA INSTRUÇÃO PARA CARREGAR O ENDEREÇO DO RÓTULO
            MM  LDADRR       ; 
LDADRR      $   =0001        ;  AQUI É CARREGADO O ENDEREÇO DO RÓTULO
            MM  IC           ; ATUALIZA IC
            JP  COMROT

SEMROT2     LD  IC           ; ATUALIZA IC
            +   H2           ;
            MM  IC           ;
            LD  PARG1        ; LÊ PONTEIRO PARA O OPERADOR
            MM  PONT1        ; SALVA NA VARIÁVEL1 DA FUNÇÃO QUE MONTA A LINHA 
            LD  PARG2        ; LÊ PONTEIRO PARA O OPERANDO          
            MM  PONT2        ; SALVA NA VARIÁVEL2 DA FUNÇÃO QUE MONTA A LINHA 
            JP  CONCLUI

COMROT      LD  PARG2        ; LÊ PONTEIRO PARA O OPERADOR
            MM  PONT1        ; SALVA NA VARIÁVEL1 DA FUNÇÃO QUE MONTA A LINHA 
            LD  PARG3        ; LÊ PONTEIRO PARA O OPERANDO          
            MM  PONT2        ; SALVA NA VARIÁVEL2 DA FUNÇÃO QUE MONTA A LINHA
            JP CONCLUI

CONCLUI     SC  MONTALINHA     
            RS  MONTA2


#           MONTA2
