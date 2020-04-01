; ===================================================================
; DOUGLAS 14/04 17:59
;** ÁREA DE DADOS DE MTOC ***
        AUX         <
        H2          <
        LOAD        <

        MTOC        >
        
        MNEM        >
        OPCODE      >


;** TABELA DE MNEMONICOS ***
;
      TMNEM    K    /4C44    ; 8 LD Load accumulator from memory -- MNEMONICS
               K    /4D4D    ; 9 MM Memorize from accumulator -- MNEMONICS
               K    /5343    ; a SC Subroutine call -- MNEMONICS
               K    /5253    ; b RS Return from subroutine -- MNEMONICS
               K    /484d    ; c HM Halt MVN -- MNEMONICS
               K    /4744    ; d GD Get data from device -- MNEMONICS
               K    /5044    ; e PD Put data onto device -- MNEMONICS
               K    /4f53    ; f OS Operating system call -- MNEMONICS
               K    /4a50    ; 0 JP Jump unconditional -- MNEMONICS
               K    /4a5a    ; 1 JZ Zero jump -- MNEMONICS
               K    /4a4e    ; 2 JN Negative jump -- MNEMONICS
               K    /4c56    ; 3 LV Value move to accumulator -- MNEMONICS 
               K    /2b20    ; 4 +  Add -- MNEMONICS
               K    /2d20    ; 5 -  Subtract -- MNEMONICS
               K    /2a20    ; 6 *  Multiply -- MNEMONICS
               K    /2f20    ; 7 /  Divide -- MNEMONICS
               K    /0040    ;   @  ORIGEM
               K    /0023    ;   #  FIM
               K    /004B    ;   K  CONSTANTE
;
      TCODE    K    /0008    ; 8 LD Load accumulator from memory -- CODE
               K    /0009    ; 9 MM Memorize from accumulator -- CODE
               K    /000A    ; a SC Subroutine call -- CODE
               K    /000B    ; b RS Return from subroutine -- CODE
               K    /000C    ; c HM Halt MVN -- CODE
               K    /000D    ; d GD Get data from device -- CODE
               K    /000E    ; e PD Put data onto device -- CODE
               K    /000F    ; f OS Operating system call -- CODE
               K    /0000    ; 0 JP Jump unconditional  -- CODE
               K    /0001    ; 1 JZ Zero jump -- CODE
               K    /0002    ; 2 JN Negative jump -- CODE
               K    /0003    ; 3 LV Value move to accumulator -- CODE
               K    /0004    ; 4 +  Add -- CODE
               K    /0005    ; 5 -  Subtract -- CODE
               K    /0006    ; 6 *  Multiply -- CODE
               K    /0007    ; 7 /  Divide -- CODE
               K    /0000    ;   @  ORIGEM
               K    /0000    ;   #  FIM
               K    /0000    ;   K  CONSTANTE

;***** ÁREA DE DADOS DA SUBROTINA MTOC *****
        
        TAMTAB  K   =18      ; comprimento da tabela de mnemônicos-1=19
        PTMNEM   K   TMNEM    ; PONTEIRO PARA TABELA DE MNEMONICOS
        PONTC   K   TCODE    ; PONTEIRO PARA TABELA DE CÓDIGOS
      
        MNEM     K     /0       ; *** CÓDIGO DO MNEMÔNICO ***
        OPCODE   K     /0       ; *** CÓDIGO DE OPERAÇÃO ***
        INDICE   K     /0       ; ÍNDICE PARA PERCORRER A TABELA 

; ===================================================================
;***** MTOC - CONVERTE MNEMONICO PARA CÓDIGO DE OPERAÇÃO - ©JJN/2005
; RECEBE PMNEM - INFORMA O COMPRIMENTO DA TABELA DE MNEMONICOS
;        PONTM - ENDEREÇO DO INICIO DA TABELA DE MNEMONICOS
;        PONTC - ENDEREÇO DO INICIO DA TABELA DE CÓDIGOS
; =======================================================================  RECODIFICADA por DOUGLAS

MTOC        K   /0000 ; PONTO DE ENTRADA DE MTOC
LUP         LD  PTMNEM ; Carrega o ponteiro para a tabela de mnemônicos
            +   INDICE
            +   LOAD
            MM  LDMNEM
LDMNEM      K   /0000 ; Carrega o próximo Mnemônico
            -   MNEM  ; Confere se 
            JZ  IGUAL ; é igual ao mnemônico do argumento.
            LD  INDICE
            +   H2
            MM  INDICE
            LD  TAMTAB ; Confere se o índice ultrapassou o equivalente ao tamanho da tabela
            *   H2
            -   INDICE
            JN  ERRO ; Mnemônico não existe na tabela
            JP  LUP
IGUAL       LD  PONTC ; Carrega o ponteiro para a tabela de códigos
            +   INDICE
            +   LOAD
            MM  GETCODE
GETCODE     K   /0000
            MM  OPCODE
            RS  MTOC ; Return
ERRO        HM  ERRO ; Pára a máquina

ENDFILE      #     ENDFILE 