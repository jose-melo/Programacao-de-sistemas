; -------------------------------------------------------------------
; Módulo "principal".
;
; Este é o módulo "principal", que contém o programa principal e
; chamará o módulo "somador" (que está no arquivo somador.asm).
; -------------------------------------------------------------------

; Declaração de símbolos externos (importados por este módulo)
; -------------------------------------------------------------------
SOMADOR <                ; SOMADOR é um símbolo externo, i.e., está
                         ; definido em algum outro módulo.
ENTRADA1 <               ; ENTRADA1 também é um símbolo externo.
ENTRADA2 <               ; ENTRADA2 também é um símbolo externo.

; Declaração de símbolos 'públicos' (exportados por este módulo)
; -------------------------------------------------------------------
SAIDA >                  ; SAIDA é um "entry point", ou um símbolo que
                         ; está sendo exportado (i.e., outros módulo
                         ; poderão chamar este código.)

; Início do programa
; -------------------------------------------------------------------

        @   /0000           ; Origem absoluta: 0x000
        JP  INICIO          ; Pula para o início.
VALOR1  K   /50             ; Constante: 0x50 (1a. parcela da soma)
VALOR2  K   /FA             ; Constante: 0xFA (2a. parcela da soma)
SAIDA   K   /0000           ; Posição de memória para armazenar o valor da soma.

INICIO  LD  VALOR1          ; Carrega a 1a. parcela da soma
        MM  ENTRADA1        ; Armazena como argumento para o somador.
        LD  VALOR2          ; Carrega a 2a. parcela da soma.
        MM  ENTRADA2        ; Armazena como argumento para o somador.
        SC  SOMADOR         ; Chama o somador (chamada de sub-rotina).
        HM  /00             ; Finaliza execução.
        #   INICIO
