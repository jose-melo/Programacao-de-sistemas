
; ===================================================================
; ADICIONAR CABEÇALHO _AQUI_
; ===================================================================
; ASMB -- MONTADOR PARA A MVN - ©JJN/2005 ©DLS&LGC/2018
; ===================================================================
; 
; SIMBOLOS IMPORTADOS

HEXBIN      <
DECBIN      <
OCTBIN      <
BINHEX      <
BINBIN      <
CONV        <
IC          <
AUX         <
AUX1        <
LD          <
MM          <
CHAR2       <
LINHA       <
LINHA2      <
CRLF        <
LF          <
PLINHA      <
BLANK       <
OPERAND     <
MNEM        <
OPCODE      <
MTOC        <
ORIGEM      <
FIM         <
RT          <
RTOP        <
ER          <
RO          <
COLLONB     <
ULTIMO      <
PTS         <
MAXSIMB     <
DUPDEF      <
OP          <  
SPC2        <
SPCPV       <
MAXBYTES    <

B1          <
B2          < 
H0          <
H1          <
H2          <
H4          <
H6          <
H100        <
H3FFE       < ;_AQUI_ ADICIONADO MAS NÃO HAVIA
H4000       < ;_AQUI_ ADICIONADO MAS NÃO HAVIA
H9000       < ;_AQUI_ ADICIONADO MAS NÃO HAVIA
HFF00       <
HFFFF       <
HD          <

;_AQUI_ NOVAS VARIÁVEIS EXPORTADAS E MONTA2 IMPORTADO
MONTA1      <
MONTA2      < ; FUNÇÃO QUE MONTA O PASSO2
ASMB        >
ERRO        >
FIMASMB     >
UNPACK      >

; ===================================================================
; AREA DE DADOS DA FUNCAO READLINE
WORD        <
ISB         <
COMMENT     <
P1END       <
PARG        <
ARGCNT      <
PARG1       <
PARG2       <
PARG3       <
EOL         <
EOF         <
STI         <
STE         <
PST         <
ADDRI       <



;** OUTRAS CONSTANTES NÃO DEFINIDAS PELO PROFESSOR***
ETS         K     'ES
EP          K     'EX

TPEND+6     K   /0000 ; < ;_AQUI_ ADICIONADO MAS NÃO HÁ
LDTPEND     K   /0000 ; LD + TPEND
CI          K   /0000 ; ???
MMTLINKS    K   /0000 ; ???
LDLINKS     K   /0000 ; ???
LDTS        K   /0000 ; ???
ND          K   /0000 ; ???
LAST        K   /0000 ; ???

; ===================================================================
; ===================================================================
; MONTADOR -- PROGRAMA PRINCIPAL - ©JJN/2005
;
              @     /0200
     ASMB     LD    H0        ; POSICIONA ENDEREÇO EM ZERO
              MM    IC        ; PARA GERAR CÓDIGO
    PASSO1    LV    /0000
              MM    UL
              SC    READLINE  ; LÊ PRÓXIMA LINHA
              SC    MONTA1    ; MONTA A LINHA LIDA E GERA O ENDEREÇO DO RÓTULO
              LD    P1END     ; CHECA EOF
              JZ    PASSO1    ;

    PASSO2    LV    /0001
              MM    UL
              SC    READLINE  ; LÊ PRÓXIMA LINHA
              SC    MONTA2    ; MONTA O CÓDIGO E O SALVA NO ARQUIVO ESPECIFICADO NA UNIDADE LÓGICA 2
            ; SC    CHECKE    ; CHECA EXCECAO DE EXCESSO DE LEITURA E EOF
              JZ    PASSO2

FIMASMB       HM    FIMASMB   ; TÉRMINO DO PROGRAMA


; ===================================================================
; READLINE -- LÊ UMA LINHA DE CÓDIGO-FONTE NO DISCO - ©JJN/2005
     UL       K    /0000    ; UNIDADE LÓGICA DE ONDE SE LÊ
     READLINE $    /0001    ; PONTO DE ENTRADA DE READLINE
              LV   /0300    ; MONTA A INSTRUÇÃO DE LEITURA
              +    UL
              +    HD   ; 
              MM   READ 
     READ     $    /0001    ; LÊ DA UNIDADE LÓGICA ESPECIFICADA
              MM   WORD     ; GUARDA LEITURA
            

; ===================================================================
; CHK -- SEQUENCIA DE SUB-ROTINAS QUE VERIFICAM BRANCO, COMENTARIO, EOL E EOF EM B1

      CHKB    -  BLANK      ; CHECA BRANCO
              JZ ISBLANK    ;
      CHKC    LD WORD       ; CHECA COMENTARIO
              -  COMMENT    ;
              JZ FIMREAD    ;
      CHKEOL  LD WORD       ; CHECA EOL
              -  EOL        ;
              JZ FIMREAD    ; TERMINA LEITURA EM CASO POSITIVO
      CHKEOF  LD WORD       ; CHECA EOF
              -  EOF        ;
              JZ FIMEOF     ; EXCECAO TRATADA POSTERIORMENTE
 
              SC SAVE       ; GUARDA ARGUMENTO (ROT INS OP)
              JP READ       ; CONTINUA LEITURA

; ===================================================================
; CASO ESPECIFICO ENCONTRADO
      
      ISBLANK LD ISB        ; CHECA SE PALAVRA ANTERIOR ERA BRANCOS
              -  H1         ;
              JZ READ       ; EM CASO POSITIVO, CONTINUA LENDO
              SC ATTP       ; CASO CONTRARIO SIGNIFICA QUE ESTAVA GUARDANDO
              LD H1         ; UMA PALAVRA E AGORA VAI GUARDAR OUTRA PORTANTO
              MM ISB        ; ATUALIZA PONTEIRO DE ARMAZENAMENTO 
              JP READ

      IGNORE  GD /300       ; LE ATE ENCONTRAR EOL
              -  EOL        ;
              JZ FIMREAD    ; SE ACHOU, TERMINA LEITURA DA LINHA
              JP IGNORE     ; CASO CONTRARIO CONTINUA

      FIMEOF  LD H1         ; CARREGA 1 NA
              MM P1END      ; VAR QUE INDICA EOF 
      FIMREAD RS READLINE ; FIM DE LEITURA DA LINHA

; ===================================================================
; ATTP -- ATUALIZA PONTEIRO DE ARMANZENAMENTO DE ARGUMENTOS
      ATTP    K  /00
              LD ARGCNT
              JZ TOARG2
      TOARG3  LD PARG3
              MM PARG
              JZ FIMAT
      TOARG2  LD PARG2
              MM PARG
      FIMAT   LD ARGCNT
              +  H1
              MM ARGCNT
              SC ATTP ;_gaia_ver_

; ==================================================================
; UNPACK -- DESEMPACOTA ACUMULADOR = /XYZT COMO - ©JJN/2005
;           B1 = /00XY    E     B2 = /00ZT

     UNPACK  JP    /00      ; ENTRADA DE UNPACK
             MM    AUX      ; SALVA O ACUMULADOR
;
; ACUMULADOR = /XY * /100 + /ZT
; DESCOMPACTA-OS EM DOIS BYTES (B1 E B2)
;
; B1 = /XY = (ACUMULADOR DIV /100)
; B2 = /XYZT - (/100 * /XY)
;
             /     H100     ; SEPARA PRIMEIRO CARACTERE
             MM    B1       ; GUARDA EM B1
             *     HFF00    ; CALCULA -(/100 * B1)
             +     AUX      ; SEPARA SEGUNDO CARACTERE
             MM    B2       ; GUARDA EM B2
             RS    UNPACK   ; RETORNA


; ===================================================================
; SAVEARG -- GUARDA VALOR DE RÓTULO, OPERAÇÃO OU OPERANDO NA MEMORIA
      
      SAVE    K  /00
              LD PARG     ; CARREGA PONTEIRO PARA ARMAZENAMENTO DE ARGUMENTO
              +  MM       ; MONTA INSTRUCAO MM PARG
              MM SAVE2    ; ARMAZENA
              LD PARG     ;
              +  H2       ;
              MM PARG     ; ATUALIZA PONTEIRO
              LD WORD     ; CARREGA PALAVRA LIDA
      SAVE2   MM PARG     ; SALVA EM PARG
              RS SAVE



; ==================================================================
; ERRO -- IMPRIME MENSAGEM DE ERRO - ©JJN/2005
;         COM O CÓDIGO ASCII DE DUAS LETRAS PASSADO NO ACUMULADOR
;
; CÓDIGOS DE ERRO:
; 2 - ERRO NO PASSO2
; DD - DUPLA DEFINIÇÃO DE RÓTULO

     ERRO    JP    /00      ; ENTRADA DE ERRO
             MM    AUX      ; SALVA CODIGO DO ERRO
             LD    ER       ; IMPRIME
             PD    /1       ;" ""ER"""
             LD    RO       ; IMPRIME
             PD    /1       ;" ""RO"""
             LD    COLLONB  ; IMPRIME
             PD    /1       ;" "": """
             LD    AUX      ; IMPRIME
             PD    /1       ; CÓDIGO DO ERRO
             LD    CRLF     ; IMPRIME
             PD    /1       ; FIM DE LINHA
             RS    ERRO     ; RETORNA



                       #     ASMB