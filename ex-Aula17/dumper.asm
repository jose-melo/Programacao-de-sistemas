; -------------------------------------------------------------------
; Dumper
; -------------------------------------------------------------------

; Símbolos exportados
; -------------------------------------------------------------------
DUMPER          >
DUMP_INI        >
DUMP_TAM        >
DUMP_UL         >
DUMP_BL         >
DUMP_EXE        >

; Origem relocável
                &       /0000

; Parâmetros do dumper
DUMP_INI        $       /0001  ; Endereço inicial de dump
DUMP_TAM        $       /0001  ; Tamanho total da imagem
DUMP_UL         $       /0001  ; Número da unidade lógica
DUMP_BL         $       /0001  ; Comprimento do bloco (em words)
DUMP_EXE        $       /0001  ; Endereço da primeira instrução executável

; Variáveis
COUNT           $       /0001
COUNT2          $       /0001
COUNT3          $       /0001
CHECKSUM        $       /0001
LOAD            K       /8000  ; Opcode de LOAD
INICIO          K       /0000
TAM             K       /0800
ONE             K       /0001  ; Constante
TWO             K       /0002  ; Constante
PD_OP           K       /E300  ; Opcode de PD (Put Data) em disco

; Corpo da subrotina
DUMPER          $       /0001

; Definindo PD_OP a partir de DUMP_UL
                LD      DUMP_UL
                +       PD_OP
                MM      WRITE
                MM      WRITE2
                MM      WRITE3
                MM      WRITE4
                MM      WRITE5
                MM      WRITE6
                MM      WRITE7

; Escrevendo endereço inicial e tamanho
                LD      DUMP_INI
WRITE2          $       /0001
                LD      DUMP_TAM
WRITE3          $       /0001

; Dump da memória
LOOP            LD      COUNT        ; Carrega o num. de words que já foram escritas
                -       DUMP_TAM     ; Subtrai o tamanho total do arquivo
                JZ      FIM          ; if (COUNT == DUMP_TAM) goto FIM;
                LV      /0000        ; Zera o acumulador
                MM      CHECKSUM     ; CHECKSUM = 0
                LD      COUNT        ; Carrega o num. de worda que já foram escritas
                *       TWO          ;
                +       DUMP_INI     ; ACC = Endereço da próxima word que será escrita
WRITE4          $       /0001        ; Escreve no arquivo de saída
                +       CHECKSUM     ; Atualiza CHECKSUM
                MM      CHECKSUM     ; Armazena CHECKSUM atualizado
                LD      DUMP_TAM     ;
                -       COUNT        ;
                -       DUMP_BL      ;
                JN      SUM_DUMP_BL  ; if ((DUMP_TAM - COUNT - DUMP_BL) < 0) goto SUM_DUMP_BL
                LD      DUMP_BL      ;
                JP      WRITE5
SUM_DUMP_BL     +       DUMP_BL
WRITE5          $       /0001        ; Escreve no arquivo de saída
                MM      COUNT2
                MM      COUNT3
                +       CHECKSUM
                MM      CHECKSUM
LOOP2           LD      COUNT2
                JZ      FIM2
                LD      COUNT
                +       COUNT3
                -       COUNT2
                *       TWO
                +       LOAD
                +       DUMP_INI
                MM      LOAD_NXT
LOAD_NXT        $       /0001
WRITE           $       /0001
                +       CHECKSUM
                MM      CHECKSUM
                LD      COUNT2
                -       ONE
                MM      COUNT2
                JP      LOOP2
FIM2            LD      CHECKSUM
WRITE6          $       /0001
                LD      COUNT
                +       COUNT3
                MM      COUNT
                JP      LOOP
FIM             LD      DUMP_EXE
WRITE7          $       /0001
                RS      DUMPER
                #       DUMPER
