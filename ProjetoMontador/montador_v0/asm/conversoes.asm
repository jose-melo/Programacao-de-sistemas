; ====================================================================
; ***** ROTINAS DE CONVERSÃO DE DADOS ******

; Símbolos importados e exportados
LINHA   <
PLINHA  <
LD      <
MM      <
OP      <
OPERAND <
AUX     <
AUX1    <
BARRA   <
APOST   <
DIG9    <
H0      <
H2      <
H9      <
H30     <
H40     <
H0A     <
H1000   <
H2F     <
HFFFF   <
RTOP    <
CONT    <
LOAD    <
UNPACK  <
B1      <
B2      <

HEXBIN  >
DECBIN  >
OCTBIN  >
BINHEX  >
BINBIN  >
CONV    >


; ===================================================================
; ÁREAS DE DADOS DAS ROTINAS DE CONVERSÃO NUMÉRICA
; ===================================================================
;
;** INDICADORES DOS TIPOS DE CONVERSÃO DA ROTINA CONV
;     (SE NÃO FOR NENHUM DOS CINCO, ENTÃO NÃO É NUMÉRICO)
;
         TIPOS  K   /02F      ;" ""/"" : HEXADECIMAL"
                K   /03D      ;" ""="" : DECIMAL"
                K   /040      ;" ""@"" : OCTAL"
                K   /023      ;" ""#"" : BINÁRIO"
                K   /027      ;" ""'"" : ASCII "

; ====================================================================
; ====================================================================
;** CURSORES APONTAM OS ÚLTIMOS ELEMENTOS DAS TABELAS DE DÍGITOS
;     NESSAS CONSTANTES ESTÁ A INFORMAÇÃO DO COMPRIMENTO DAS TABELAS
;
       PDHEX   K    /001E     ; /01E=(2 * (N. DE DIG. HEXA = /10) = /20)-2
       PDDEC   K    /0012     ; /012=(2 * (N. DE DIG. DECIMAIS= /0A)= /14)- 2 
       PDOCT   K    /000E     ; /00E=(2 * (N. DE DIG. OCTAIS= /08)= /10)- 2 
       PDBIN   K    /0002     ; /002=(2 * (N. DE DIG. BINÁRIOS= /02)= /04)- 2 
       PDASC   K    /0000     ; PDASC K /FFFF ; -1 (NÃO EXISTE A TABELA DASC);
 
; ====================================================================
;** CURSORES PARA OS PRIMEIROS ELEMENTOS DAS TABELAS DE DÍGITOS

       PPDHEX  K    /0150     ; ENDEREÇO DE DHEX
       PPDDEC  K    /015C     ; ENDEREÇO DE DDEC
       PPDOCT  K    /0160     ; ENDEREÇO DE DOCT
       PPDBIN  K    /016C     ; ENDEREÇO DE DBIN
       PPDASC  K    /FFFF     ; -1 (NÃO EXISTE A TABELA DASC)
; 
; ====================================================================
;** CURSORES PARA OS PRIMEIROS ELEMENTOS DAS TABELAS DE VALORES CONVERTIDOS
;
       PPBHEX  K    /0170     ; ENDEREÇO DE BHEX
       PPBDEC  K    /017C     ; ENDEREÇO DE BDEC
       PPBOCT  K    /0180     ; ENDEREÇO DE BOCT
       PPBBIN  K    /018C     ; ENDEREÇO DE BBIN
       PPBASC  K    /FFFF     ; -1 (NÃO EXISTE A TABELA DASC)
; 
; ====================================================================
;
;
; ==================================================================
; 02F0 A 0233 - ÁREA DA ANTIGA VERSÃO DE TSIMB - LIVRE

TEMP        $   =1
MARKER      $   =1
TOCONVERT   $   =1
; ==================================================================
; Deve converter OPERANDO para HEXA
; RECEBE OPERANDO no ACUMULADOR
; CONV -- CONVERTE OPERANDO de ascii PARA BINÁRIO - ©JJN/2005
;         RETORNA COM VALOR CONVERTIDO EM OPERAND

     CONV     JP /00      ; ENTRADA DE CONV -> ALVO ESTA NO ACUMULADOR
              SC UNPACK   ; SEPARA EM B1 E B2
              LD B1       ; TRATA B1 PRIMEIRO
              -  H40      ; CHECA SE <9
              JN LT9      ; POSITIVO
              +  H9       ; >9, SOMA 9
              *  H0A      ; MULTIPLICA POR =10
              MM OPERAND  ; SALVA EM OPERAND
              JP CONVB2
      LT9     LD B1       ; <9
              -  H30      ; SUBTRAIR 0x30
              *  H0A      ; MULTIPLICA POR =10
              MM OPERAND  ; SALVA EM OPERAND
      CONVB2  LD B2       ; TRATA B2
              -  H40      ; CHECA SE <9
              JN LT92     ; POSITIVO
              +  H9       ; >9, SOMA 9
              +  OPERAND  ; SOMA EM OPERAND
              JP FIMCONV
      LT92    LD B1
              -  H30
              +  OPERAND
      FIMCONV RS CONV

; ===================================================================
;***** TRADUZ1 - CONVERTE CONFORME TABELAS DE CONVERSÃO (TX PARA TY)- ©JJN/2005
;
; RECEBE DADO  - DADO A SER CONVERTIDO (1 PALAVRA) 
;        CTX - INFORMA O COMPRIMENTO DA TABELA (TX OU TY)
;        PTX - ENDEREÇO DO INICIO DA TABELA TX
;        PTY - ENDEREÇO DO INICIO DA TABELA DE CÓDIGOS
;
; PRODUZ RESUL - RESULTADO DA CONVERSÃO (1 PALAVRA)EM RESUL E NO ACUMULADOR
;                NÃO ENCONTRANDO, RETORNA -1 
;
; SEQUENCIA DE CHAMADA (EXEMPLO EXTRAIDO DE HB):
;        LD DIGHEXA ; DÍGITO ASCII A CONVERTER
;        MM DADO    ; SALVA EM DADO O DIGITO A CONVERTER
;        LD PPDHEX  ; ENDEREÇO DE DHEX (TABELA ASCII DE DIGITOS HEXA)
;        MM PTX
;        LD PDHEX   ; COMPRIMENTO DA TABELA DHEX 
;        MM CTX
;        LD PPBHEX  ; ENDEREÇO DE BHEX (TABELA BINÁRIA DE DÍGITOS HEXA)
;        MM PTY
;        SC TRADUZ1 ; CHAMA A SUBROTINA TRADUZ1
                   ; O RESULTADO JÁ FICA NO ACUMULADOR)

;                                                                         OK CODIFICADO
TRADUZ1     JP    /0000    ; PONTO DE ENTRADA DE MTOC
            LD    CTX      ; AC := COMPRIMENTO DA TABELA TX - 2
            MM    AUX      ; GUARDA EM UM CURSOR DE TRABALHO
            LD    PTX      ; MONTA 
            +     CONT     ; INSTRUÇÃO
            +     LOAD     ; LD TX[CONT]
            MM    LT2      ; Modifica a proxima instrucao
LT1         LD    /00      ; LOOP:   AC := TX[CONT]
            -     DADO     ; AC := AC - DADO
            JZ    ENCON2   ; IF AC = 0, goto ENCONTROU2
            LD    CONT     ; AC := CONT
            -     H2       ; AC := AC - 2
            MM    CONT     ; CONT := AC
            JN    ERR2     ; IF AC < 0, goto ERRO2
            JP    LT1      ; goto LOOP
ENCON2      LD    PTY      ; ENCONTROU2: AC := PTY
            +     CONT     ; AC := AC + CONT
            +     LOAD     ; AC := AC + CONT + #LOAD
            MM    LT2      ; Modifica a proxima instrucao
LT2         LD    /00      ;" ""LD TY[CONT]"" RESULTADO DA CONVERSÃO"
LT3         MM    RESUL    ; RESUL := AC
            RS    TRADUZ1  ; RETORNA
ERR2        LD    HFFFF    ; DADO INVÁLIDO 
            JP    LT3      ; VAI RETORNAR -1 



; ====================================================================
;
;** HB - CONVERTE UM DIGITO HEXADECIMAL PARA BINÁRIO - ©JJN/2005
;
;          RECEBE O DADO NO ACUMULADOR
;          RETORNA O RESULTADO NO ACUMULADOR E RESUL (SE INVALIDO, RETORNA -1)
;
; SEQUENCIA DE CHAMADA (EXEMPLO):
;
;           LD INFASC ; SUPONDO QUE INFASC TENHA UM DIGITO HEXA
;           SC HB     ; HB CONVERTE-O PARA BINÁRIO, E
;           MM INFBIN ; O RESULTADO PODE SER GUARDADO EM INFBIN
                     ; MAS TAMBÉM ESTÁ DISPONÍVEL EM RESUL

;                                                                          OK CODIFICADO
      HB      JP /00     ; ENTRADA DE HB
              MM DADO    ; SALVA EM DADO A INFORMAÇÃO RECEBIDA NO ACUMULADOR
              LD PPDHEX  ; ENDEREÇO DE DHEX (TABELA ASCII DE DIGITOS HEXA)
              MM PTX
              LD PDHEX   ; COMPRIMENTO DA TABELA DHEX 
              MM CTX
              LD PPBHEX  ; ENDEREÇO DE BHEX (TABELA BINÁRIA DE DÍGITOS HEXA)
              MM PTY
              SC TRADUZ1 ; CHAMA A SUBROTINA TRADUZ1
              RS HB      ; RETORNA COM O RESULTADO NO ACUMULADOR E RESUL



; ====================================================================
;  *** HEXBIN - ROTINA DE CONVERSÃO HEXADECIMAL PARA BINÁRIO ***  ©JJN/2005 verificar
;               ENTRADA EM LINHA[PLINHA] E SEGUINTES
;               RESULTADO EM OPERAND
;                                                                         OK CODIFICADO
     HEXBIN  JP    /00        ; ENTRADA DE HEXBIN
             LD    H0         ; INICIA OPERANDO 
             MM    OPERAND    ; COM ZERO
             LD    PLINHA     ; MONTA INSTRUÇÃO
             +     LD         ;" ""LD LINHA[PLINHA]"""
     L6      MM    PREXB        ;_aqui ; GUARDA PARA EXECUTAR ADIANTE
     PREXB      LD    LINHA     ;_aqui ; MM LINHA[PLINHA] (TRAZ EM ASCII)
             SC    HB         ; CONVERTE PARA BINÁRIO
             JN    FORAH      ;_aqui ; NÃO ERA DÍGITO HEXA: ACABOU
             LD    OPERAND    ; OBTEM RESULTADO DA CONVERSÃO (AC, RESUL)
             *     H10        ; ERA: MULTIPLICA OPERAND POR (/10 = 16)
             +     RESUL      ; SOMA O DÍGITO CONVERTIDO
             MM    OPERAND    ; ATUALIZA OPERAND
             LD    PREXB        ;_aqui ; MODIFICA NOVAMENTE A INSTRUÇÃO
             +     H2         ; APONTANDO O PRÓXIMO CARACTERE
             JP    L6         ; VAI TRATAR OUTRO DIGITO
     FORAH    RS    HEXBIN    ;_aqui ; NÃO É DÍGITO: RETORNA



; ====================================================================
;** DB - CONVERTE UM DIGITO DECIMAL PARA BINÁRIO - ©JJN/2005
;          RECEBE O DADO NO ACUMULADOR
;          RETORNA O RESULTADO NO ACUMULADOR E RESUL (SE INVALIDO, RETORNA -1)

; SEQUENCIA DE CHAMADA:
;                        LD INFASC ; SUPONDO QUE INFASC TENHA UM DIGITO DECIMAL
;                        SC DB     ; DB CONVERTE-O PARA BINÁRIO, E
;                        MM INFBIN ; O RESULTADO PODE SER GUARDADO EM INFBIN
                                  ; MAS TAMBÉM ESTÁ DISPONÍVEL EM RESUL
;                                                                          OK CODIFICADO
      DB    JP /00     ; ENTRADA DE DB
            MM DADO    ; SALVA EM DADO A INFORMAÇÃO RECEBIDA NO ACUMULADOR
            LD PPDDEC  ; ENDEREÇO DE DDEC (TABELA ASCII DE DIG. DECIMAIS)
            MM PTX
            LD PDDEC   ; COMPRIMENTO DA TABELA DDEC 
            MM CTX
            LD PPBDEC  ; ENDEREÇO DE BDEC (TABELA BINÁRIA DE DÍG. DECIMAIS)
            MM PTY
            SC TRADUZ1 ; CHAMA A SUBROTINA TRADUZ1
            RS DB      ; RETORNA COM O RESULTADO NO ACUMULADOR E RESUL


; ====================================================================
;  *** DECBIN - ROTINA DE CONVERSÃO DECIMAL PARA BINÁRIO *** verificar - ©JJN/2005
;               ENTRADA EM LINHA[PLINHA] E SEGUINTES
;               RESULTADO EM OPERAND
;                                                                          OK CODIFICADO
     DECBIN  JP    /00        ; ENTRADA DE DECBIN
             LD    H0         ; INICIA OPERAND
             MM    OPERAND    ; COM ZERO
             LD    PLINHA     ; MONTA INSTRUÇÃO
             +     LD         ;" ""LD LINHA[PLINHA]"""
     D6D      MM    PRD       ;_aqui  ; GUARDA PARA EXECUTAR ADIANTE
     PRD      LD    LINHA      ; MM LINHA[PLINHA] (TRAZ EM ASCII)
             SC    DB         ; CONVERTE PARA BINÁRIO
             JN    FORAD      ;_aqui ; NÃO ERA DÍGITO HEXA: ACABOU
             LD    OPERAND    ; OBTEM RESULTADO DA CONVERSÃO (AC, RESUL)
             *     H0A        ; ERA: MULTIPLICA OPERAND POR (/0A = 10)
             +     RESUL      ; SOMA O DÍGITO CONVERTIDO
             MM    OPERAND    ; ATUALIZA OPERAND
             LD    PRD        ;_aqui ; MODIFICA NOVAMENTE A INSTRUÇÃO
             +     H2         ; APONTANDO O PRÓXIMO CARACTERE
             JP    D6D         ; VAI TRATAR OUTRO DIGITO
     FORAD    RS    DECBIN    ;_aqui ; NÃO É DÍGITO: RETORNA


; ====================================================================
;
;** BH - CONVERTE UM NIBBLE PARA HEXADECIMAL - ©JJN/2005
;
;          RECEBE O DADO NO ACUMULADOR (NUMERO BINÁRIO DE 4 BITS S/SINAL)
;          RETORNA O RESULTADO NO ACUMULADOR E RESUL (SE INVALIDO, RETORNA -1)
;
; SEQUENCIA DE CHAMADA (EXEMPLO):

;           LD INFASC ; SUPONDO QUE INFASC TENHA UM NIBBLE
;           SC BH     ; BH CONVERTE-O PARA DIGITO HEXA EM ASCII, E
;           MM INFBIN ; O RESULTADO PODE SER GUARDADO EM INFBIN
                     ; MAS TAMBÉM ESTÁ DISPONÍVEL EM RESUL

                                                                          
      BH      JP /00     ; ENTRADA DE HB
              MM DADO    ; SALVA EM DADO A INFORMAÇÃO RECEBIDA NO ACUMULADOR
              LD PPBHEX  ; ENDEREÇO DE BHEX (TABELA BINÁRIA DE DÍGITOS HEXA)
              MM PTX
              LD PDHEX   ; COMPRIMENTO DA TABELA DHEX 
              MM CTX
              LD PPDHEX  ; ENDEREÇO DE DHEX (TABELA ASCII DE DIGITOS HEXA)
              MM PTY
              SC TRADUZ1 ; CHAMA A SUBROTINA TRADUZ1
              RS BH      ; RETORNA COM O RESULTADO NO ACUMULADOR E RESUL

; ====================================================================
; ====================================================================
;  *** BINHEX - ROTINA DE CONVERSÃO BINÁRIO PARA HEXADECIMAL ***  ©JJN/2005 
;               ENTRADA NO ACUMULADOR - NUMERO BINÁRIO DE 16 BITS
;               RESULTADO: 4 CARACTERES ASCII NO ARQUIVO DE SAIDA
;                                                                          OK CODIFICADO
    FATOR     K     /0000 ;_

     BINHEX  JP    /00        ; ENTRADA DE BINHEX

             MM    AUX        ; SALVA NUMERO ORIGINAL
             LD    H1000      ; FAZ FATOR=2**12 

     LPBHEX  MM    FATOR      ; POSICIONA NO PRÓXIMO NIBBLE A TRATAR

             LD    AUX        ; RECUPERA O QUE RESTA DO NUMERO ORIGINAL
             /     FATOR      ; OBTEM PROXIMO NIBBLE MAIS SIGNIFICATIVO
             MM    AUX1       ; SALVA-O

             SC    BH         ; CONVERTE PARA HEXADECIMAL (ASCII)
             PD    /3         ; GERA-O NO ARQUIVO DE SAIDA

             LD    AUX1       ; RECUPERA O ÚLTIMO NIBBLE TRATADO
             *     FATOR      ; POSICIONA-O ADEQUADAMENTE
             MM    AUX1       ; GUARDA PARA SUBTRAIR ADIANTE

             LD    AUX        ; RECUPERA O RESTANTE DO NUMERO 
             -     AUX1       ; REMOVE O NIBBLE TRATADO
             MM    AUX        ; GUARDA PARA TRATAR O PRÓXIMO NIBBLE

             LD    FATOR      ; FAZ FATOR
             /     H10        ; := FATOR/(2**4)
             JZ    FIMBHEX    ; SE ESGOTOU OS 4 DIGITOS, VAI RETORNAR
             JP    LPBHEX     ; SE NÃO, VOLTA AO LOOP

     FIMBHEX RS    BINHEX     ; RETORNA

; ====================================================================
; ROTINAS ADICIONAIS, A SEREM CODIFICADAS EM LINGUAGEM DE MÁQUINA
; ====================================================================
;
;** OB - CONVERTE UM DIGITO OCTAL PARA BINÁRIO - ©JJN/2005
;          RECEBE O DADO NO ACUMULADOR
;          RETORNA O RESULTADO NO ACUMULADOR E RESUL (SE INVALIDO, RETORNA -1)
;
; SEQUENCIA DE CHAMADA:
;                        LD INFASC  ; SUPONDO QUE INFASC TENHA UM DIGITO DECIMAL
;                        SC OB      ; OB CONVERTE-O PARA BINÁRIO, E
;                        MM INFBIN  ; O RESULTADO PODE SER GUARDADO EM INFBIN
                                   ; MAS TAMBÉM ESTÁ DISPONÍVEL EM RESUL

      OB    JP /00     ; ENTRADA DE OB
            MM DADO    ; SALVA EM DADO A INFORMAÇÃO RECEBIDA NO ACUMULADOR
            LD PPDOCT  ; ENDEREÇO DE DDEC (TABELA ASCII DE DIG. OCTAIS)
            MM PTX
            LD PDOCT   ; COMPRIMENTO DA TABELA DDEC 
            MM CTX
            LD PPBOCT  ; ENDEREÇO DE BDEC (TABELA BINÁRIA DE DÍG. DECIMAIS)
            MM PTY
            SC TRADUZ1 ; CHAMA A SUBROTINA TRADUZ1
            RS OB      ; RETORNA COM O RESULTADO NO ACUMULADOR E RESUL


; ====================================================================
;  *** OCTBIN - ROTINA DE CONVERSÃO OCTAL PARA BINÁRIO *** verificar - ©JJN/2005
;               ENTRADA EM LINHA[PLINHA] E SEGUINTES
;               RESULTADO EM OPERAND
;
     OCTBIN  JP    /00        ; ENTRADA DE OCTBIN
             LD    H0         ; INICIA OPERAND
             MM    OPERAND    ; COM ZERO
             LD    PLINHA     ; MONTA INSTRUÇÃO
             +     LD         ;" ""LD LINHA[PLINHA]"""
     D6O      MM    PREX        ;_aqui_ ; GUARDA PARA EXECUTAR ADIANTE
     PREX      LD    LINHA     ;_aqui ; MM LINHA[PLINHA] (TRAZ EM ASCII)
             SC    OB         ; CONVERTE PARA BINÁRIO
             JN    FORAO      ;_aqui ; NÃO ERA DÍGITO OCTAL: ACABOU
             LD    OPERAND    ; OBTEM RESULTADO DA CONVERSÃO (AC, RESUL)
             *     H8        ; ERA: MULTIPLICA OPERAND POR /08
             +     RESUL      ; SOMA O DÍGITO CONVERTIDO
             MM    OPERAND    ; ATUALIZA OPERAND
             LD    PREX        ;_aqui ; MODIFICA NOVAMENTE A INSTRUÇÃO
             +     H2         ; APONTANDO O PRÓXIMO CARACTERE
             JP    D6O         ; VAI TRATAR OUTRO DIGITO
     FORAO    RS    OCTBIN    ;_aqui ; NÃO É DÍGITO: RETORNA


; ====================================================================
;
;** BB - CONVERTE UM DÍGITO BINÁRIO PARA BINÁRIO - ©JJN/2005
;          RECEBE O DADO NO ACUMULADOR
;          RETORNA O RESULTADO NO ACUMULADOR E RESUL (SE INVÁLIDO, RETORNA -1)
;
; SEQUENCIA DE CHAMADA:
;                        LD INFASC ; SUPONDO QUE INFASC TENHA UM DIGITO BINÁRIO
;                        SC BB     ; BB CONVERTE-O PARA BINÁRIO, E
;                        MM INFBIN ; O RESULTADO PODE SER GUARDADO EM INFBIN
                                  ; MAS TAMBÉM ESTÁ DISPONÍVEL EM RESUL

      BB    JP /00     ; ENTRADA DE BB
            MM DADO    ; SALVA EM DADO A INFORMAÇÃO RECEBIDA NO ACUMULADOR
            LD PPDBIN  ; ENDEREÇO DE DDEC (TABELA ASCII DE DIG. BINÁRIOS)
            MM PTX
            LD PDBIN   ; COMPRIMENTO DA TABELA DDEC 
            MM CTX
            LD PPBBIN  ; ENDEREÇO DE BDEC (TABELA BINÁRIA DE DÍG. BINÁRIOS)
            MM PTY
            SC TRADUZ1 ; CHAMA A SUBROTINA TRADUZ1
            RS BB      ; RETORNA COM O RESULTADO NO ACUMULADOR E RESUL

; ====================================================================
;  *** BINBIN - ROTINA DE CONVERSÃO BINÁRIO PARA BINÁRIO
;               ENTRADA EM LINHA[PLINHA] E SEGUINTES
;               RESULTADO EM OPERAND

     BINBIN  K    /00        ; ENTRADA DE BINBIN
             LD    H0         ; INICIA OPERAND
             MM    OPERAND    ; COM ZERO
             LD    PLINHA     ; MONTA INSTRUÇÃO
             +     LD         ;" ""LD LINHA[PLINHA]"""
     D6B     MM    PRB        ;_aqui ; GUARDA PARA EXECUTAR ADIANTE
     PRB     LD    LINHA      ;_aqui; MM LINHA[PLINHA] (TRAZ EM ASCII)
             SC    BB         ; CONVERTE PARA BINÁRIO
             JN    FORAB      ;_aqui ; NÃO ERA DÍGITO HEXA: ACABOU
             LD    OPERAND    ; OBTEM RESULTADO DA CONVERSÃO (AC, RESUL)
             *     H0A        ; ERA: MULTIPLICA OPERAND POR (/0A = 10)
             +     RESUL      ; SOMA O DÍGITO CONVERTIDO
             MM    OPERAND    ; ATUALIZA OPERAND
             LD    PRB        ;_aqui ; MODIFICA NOVAMENTE A INSTRUÇÃO
             +     H2         ; APONTANDO O PRÓXIMO CARACTERE
             JP    D6B         ; VAI TRATAR OUTRO DIGITO
    FORAB    RS    BINBIN    ;_aqui; NÃO É DÍGITO: RETORNA
;
;


; ====================================================================
;** TABELAS DE DIGITOS HEXA, DECIMAIS, OCTAIS, BINÁRIOS ***
;
                          @    /150
        DHEX   K    /46       ;" ""F"""
               K    /45       ;" ""E"""
               K    /44       ;" ""D"""
               K    /43       ;" ""C"""
               K    /42       ;" ""B"""
               K    /41       ;" ""A"""
        DDEC   K    /39       ;" ""9"""
               K    /38       ;" ""8"""
        DOCT   K    /37       ;" ""7"""
               K    /36       ;" ""6"""
               K    /35       ;" ""5"""
               K    /34       ;" ""4"""
               K    /33       ;" ""3"""
               K    /32       ;" ""2"""
        DBIN   K    /31       ;" ""1"""
               K    /30       ;" ""0"""

 
; ====================================================================
;** TABELAS DE NÚMEROS BINÁRIOS CORRESPONDENTES ***
;
        BHEX   K    /000F     ;" ""F"""
               K    /000E     ;" ""E"""
               K    /000D     ;" ""D"""
               K    /000C     ;" ""C"""
               K    /000B     ;" ""B"""
               K    /000A     ;" ""A"""
        BDEC   K    /0009     ;" ""9"""
               K    /0008     ;" ""8"""
        BOCT   K    /0007     ;" ""7"""
               K    /0006     ;" ""6"""
               K    /0005     ;" ""5"""
               K    /0004     ;" ""4"""
               K    /0003     ;" ""3"""
               K    /0002     ;" ""2"""
        BBIN   K    /0001     ;" ""1"""
               K    /0000     ;" ""0"""
;
        DADO   K    /0000     ; PARÂMETRO DE ENTRADA DAS ROTINAS DE CONVERSÃO
        PTX    K    /0000     ; PARÂMETRO DE ENTRADA DAS ROTINAS DE CONVERSÃO
        CTX    K    /0000     ; PARÂMETRO DE ENTRADA DAS ROTINAS DE CONVERSÃO
        PTY    K    /0000     ; PARÂMETRO DE ENTRADA DAS ROTINAS DE CONVERSÃO
        H10    K    /0010     ; BASE HEXADECIMAL
        H8     K    /0008     ; BASE OCTAL
        RESUL  K    /0000     ; RESULTADO DA CONVERSÃO
;
;
ENDFILE   #     ENDFILE 
