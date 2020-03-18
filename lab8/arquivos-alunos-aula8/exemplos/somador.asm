; -------------------------------------------------------------------
; Módulo "somador".
;
; Este é o módulo "somador", que contém uma sub-rotina que recebe duas
; entradas, nas posições ENTRADA1 e ENTRADA2, e coloca o resultado da
; soma na posição SAIDA.
; -------------------------------------------------------------------

; Declaração de símbolos externos (importados por este módulo)
; -------------------------------------------------------------------
SAIDA <

; Declaração de símbolos 'públicos' (exportados por este módulo)
; -------------------------------------------------------------------
SOMADOR >
ENTRADA1 >
ENTRADA2 >

; Início do código
; -------------------------------------------------------------------

         & /0000      ; Origem relocável

ENTRADA1 K /0000      ; 1o. parâmetro da sub-rotina.
ENTRADA2 K /0000      ; 2o. parâmetro da sub-rotina.

SOMADOR  JP /000      ; Ponto de entrada da sub-rotina.
         LD ENTRADA1  ; Carrega 1o. paramêtro.
         +  ENTRADA2  ; Soma com o 2o. parâmetro.
         MM SAIDA     ; Armazena na variável SAIDA.
         RS SOMADOR   ; Retorna da sub-rotina.
         # INICIO
