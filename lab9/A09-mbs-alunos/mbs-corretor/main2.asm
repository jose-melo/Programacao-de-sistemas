; -------------------------------------------------------------------
; Programa principal do Monitor Batch Simples (MBS)
; -------------------------------------------------------------------

; Coloque aqui os símbolos importados
; -------------------------------------------------------------------

DUMPER          >
DUMP_INI        >
DUMP_TAM        >
DUMP_UL         >
DUMP_BL         >
DUMP_EXE        >
LOADER      >
LOADER_UL   >

; Origem relocável
                &       /0000

MAIN            JP      INI      ; salta para o início do programa
UL              K       /0000    ; parâmetro: UL onde está o arquivo de batch

; -------------------------------------------------------------------
; Subrotina: UNPACK
; Extrai os bytes de uma word contida no acumulador, colocando-os
; em dois endereços da memória.
;
; Exemplo: dada a word XYZT no acumulador, ao final da execução,
; UNP_B1="00XY" e UNP_B2="00ZT".
; -------------------------------------------------------------------

; Parâmetros
WORD            $       /0001       ; Word de entrada
UNP_B1          $       /0001       ; Byte mais significativo
UNP_B2          $       /0001       ; Byte menos significativo

; Constantes
SHIFT           K       /0100
CH_0            K       /0030
CH_F            K       /0046
X_INI           K       /003A
X_END           K       /0041
X_DIFF          K       /0007
ONE             K       /0001
MINUS_1         K       /FFFF
ZERO            K       /0000
EIGHT           K       /1000
FOUR            K       /0100
TWO             K       /0010

; Corpo da subrotina
UNPACK          $       /0001
                MM      WORD        ; Carrega word. Primeiramente faremos unpack de B2
                *       SHIFT       ; Desloca os bytes para remover 2 primeiros hex
                SC      RSHIFT2     ; Desloca os bytes menos significativos pro seu lugar
                MM      UNP_B2      ; Salva resultado
                LD      WORD        ;
                SC      RSHIFT2     ;
                MM      UNP_B1      ;
                RS      UNPACK      ; Retorna

; -------------------------------------------------------------------
; Subrotina: RSHIFT2
; Faz um right shift (<) duas vezes do valor do acumulador
; -------------------------------------------------------------------

; Constantes
FIX             K       /8000
REFIX           K       /0080
; Corpo da subrotina
RSHIFT2         $       /0001
                JN      NEG         ; O número é negativo
                /       SHIFT       ; Retorna os 2 bytes à posição inicial
                JP      FIM-RS      ; Vai para final de RSHIFT2
NEG             -       FIX         ; Fix do shift em número negativo
                /       SHIFT       ; Shift
                +       REFIX       ; Fix para voltar número tirado
FIM-RS          RS      RSHIFT2     ; Retorno

; -------------------------------------------------------------------
; Subrotina: IS_HEX
; -------------------------------------------------------------------

  ;; Parâmetros
S_HEX           $       /0001
  ;; Corpo da subrotina
IS_HEX          $       /0001
                MM      S_HEX
  ;; Verifica se < '0'
                -       CH_0
                JN      NOT_HEX
  ;; Verifica se > 'f'
                LD      S_HEX
                -       CH_F
                -       ONE ; we wanna include 'f'
                JN      MIGHTB
  ;; Não é hex. Retorna -1.
NOT_HEX         LD      MINUS_1
                RS      IS_HEX
  ;; Incrementa CH_F decrementado e verifica se é caractere especial.
MIGHTB          LD      S_HEX
                -       X_INI
                JN      YES_HEX
                -       X_DIFF
                JN      NOT_HEX
                LD      S_HEX
                -       X_DIFF
                RS      IS_HEX
YES_HEX         LD      S_HEX
                RS      IS_HEX

; -------------------------------------------------------------------
; Subrotina: CHTOI
; Converte uma word em hexa para um número inteiro.
;
; Exemplo: CHTOI("0010") = 0010 (i.e., 16 em decimal)
; -------------------------------------------------------------------

  ;; Parâmetros
CH_ANS          $       /0001        ; Variável para guardar resultado
CH_IN_A         $       /0001        ; 2 bytes mais significativos (em ASCII)
CH_IN_B         $       /0001        ; 2 bytes menos signicativos (em ASCII)

  ;; Corpo da subrotina
CHTOI           $       /0001
  ;; Zera CH_ANS
                LD      ZERO
                MM      CH_ANS
  ;; Unpack primeira palavra
                LD      CH_IN_A
                MM      WORD
                SC      UNPACK
  ;; Processa primeira palavra
  ;; Processa primeiro byte
                LD      UNP_B1
                SC      IS_HEX
                JN      CH_RET
                -       CH_0
                *       EIGHT
                MM      CH_ANS
  ;; Processa segundo byte
                LD      UNP_B2
                SC      IS_HEX
                JN      CH_RET
                -       CH_0
                *       FOUR
                +       CH_ANS
                MM      CH_ANS
  ;; Unpack segunda palavra
                LD      CH_IN_B
                MM      WORD
                SC      UNPACK
  ;; Processa segunda palavra
  ;; Processa primeiro byte
                LD      UNP_B1
                SC      IS_HEX
                JN      CH_RET
                -       CH_0
                *       TWO
                +       CH_ANS
                MM      CH_ANS
  ;; Processa segundo byte
                LD      UNP_B2
                SC      IS_HEX
                JN      CH_RET
                -       CH_0
                +       CH_ANS
  ;; Valor da resposta está no acumulador!
CH_RET          RS      CHTOI
;==========================================
;Variáveis e constantes
;==========================================
LO              K /4C4F ; LO em ASCII
DU              K /4450 ; DU em ASCII
TKFIM           K /2F2A ; /* em ASCII
EOF             K /0AFF ; END OF FILE
BARRAS2         K /2F2F ; // em ASCII
TKJOB           K /4A42 ; JB em ASCII
;
INI             K       /0000
                GD UL
                SC CHECABARRA
                JN ERROJOB
                SC CHECAJOB
                JN ERROJOB
LOOP            GD UL         ;Le os pula-linhas
                GD UL
                -  TKFIM
                JZ FIM
                +  TKFIM
                -  EOF
                JZ ERROEND
                +  EOF
                SC CHECABARRA
                JN ERROCMD
                GD UL
                -  LO
                JZ OPLOAD
                + LO
                - DU
                JZ OPDUMP
                JP ERROCMD
OPLOAD          SC TRATALOAD
                JN ERROARG
                JP LOOP
OPDUMP          SC TRATADUMP
                JN ERROARG
                JP LOOP
ERROJOB         LV /0000
                OS /0EE
                LV /004A
                PD /100
                JP FIM
ERROJOB         LV /0001
                OS /0EE
                LV /004A
                PD /100
                JP FIM
ERROCMD         LV /0002
                OS /0EE
                LV /0043
                PD /100
                JP FIM
ERROARG         LV /0003
                OS /0EE
                LV /0041
                PD /100
                JP FIM
ERROEND         LV /0004
                OS /0EE
                LV /0044
                PD /100
                JP FIM
FIM             HM      FIM   ; Fim do programa
;
;ChecaBarra
;
CHECABARRA      K  /0000
                -  BARRAS2
                JZ OKBARRA
                LV /FFF
                JP FIMBARRA
OKBARRA         LV /0000
FIMBARRA             RS CHECABARRA
;
;CHECAJOB
;
CHECAJOB        K /0000
                GD UL
                -  TKJOB
                JZ CERTOJOB
                LV /FFF
                JP FIMJOB
CERTOJOB        LV /0
FIMJOB          RS CHECAJOB
;
;TRATADUMP
;
TRATADUMP       K /0000
                GD UL
                MM CH_IN_A
                GD UL
                MM CH_IN_B
                SC CHTOI
                LD CH_ANS
                MM DUMP_BL ;Tamanho do bloco
                GD UL
                GD UL
                MM CH_IN_A
                GD UL
                MM CH_IN_B
                SC CHTOI
                LD CH_ANS
                MM DUMP_INI ; End. inicial
                GD UL
                GD UL
                MM CH_IN_A
                GD UL
                MM CH_IN_B
                SC CHTOI
                LD CH_ANS
                MM DUMP_TAM ; Tamanho total
                GD UL
                GD UL
                MM CH_IN_A
                GD UL
                MM CH_IN_B
                SC CHTOI
                LD CH_ANS
                MM DUMP_EXE ; End. da 1 inst
				GD UL
                GD UL
                MM CH_IN_A
                GD UL
                MM CH_IN_B
                SC CHTOI
                LD CH_ANS
				MM DUMP_UL ; Unidade lógica
                SC DUMPER
                RS TRATADUMP
;
;TRATALOAD
;
TRATALOAD       K /0000
                GD UL
                MM CH_IN_A
                GD UL
                MM CH_IN_B
                SC CHTOI
                LD CH_ANS
                MM LOADER_UL
                SC LOADER
                RS TRATALOAD
# MAIN