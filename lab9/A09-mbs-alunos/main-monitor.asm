; -------------------------------------------------------------------
; Programa principal do Monitor Batch Simples (MBS)
; -------------------------------------------------------------------

; Coloque aqui os símbolos importados
; -------------------------------------------------------------------

DUMPER     <
DUMP_INI   <
DUMP_TAM   <
DUMP_UL    <
DUMP_BL    <
DUMP_EXE   <
LOADER     <
LOADER_UL  <

; Origem relocável
                &       /0000

MAIN            JP      INI      ; salta para o início do programa
UL              K       /0000    ; parâmetro: UL onde está o arquivo de batch
GET             K       /D000
DISCO           K       /0300
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
                MM      CH_ANS
CH_RET          RS      CHTOI
;==========================================
;Variáveis e constantes
;==========================================
LO              K /4C4F ; LO em ASCII
DU              K /4455 ; DU em ASCII
TKFIM           K /2F2A ; /* em ASCII
EOF             K /0AFF ; END OF FILE
BARRAS2         K /2F2F ; // em ASCII
TKJOB           K /4A42 ; JB em ASCII
PULA            K /0A0A ; \N\N
ESPACO          K /2020 ; BB
ERFIM           K /2F0A
FIMARQ          K /FFFF
ZEROZERO        K /3030
AUX             K /0000
;
INI             SC PEGADADO          ;Espera-se que seja um //
                SC CHECABARRA
                JZ ERROJOB           ;Se n for // deu erro, se for continua
;
                SC CHECAJOB          ;Verifica se é JB
                JZ ERROJOB
;
                SC PEGADADO          ; Espera-se que seja \n\n dois pula linhas
				SC CHECAPULA         ; Retorna 0 se não pulou a linha
				JZ ERROJOB           ; Se não pulou a linha deu erro de job
;
LOOP    		SC PEGADADO          ; Pega um novo dado, opções: //CMD ou /*. Outra coisa é erro      
                -  TKFIM             ;
                JZ FIM               ; Se deu zero, encontrei o EOF e a execução do MBS acabou
                +  TKFIM             ;
;
                MM AUX               ; Armazena o valor lido. Espera-se que seja um // 
                SC CHECABARRA        ;
                JZ CMDOUEND          ; Se n for //. Pode ser um cmd errado ou um end errado
                SC PEGADADO          ; Temos //. Acabamos de ler um comando
;
                -  LO                ;
                JZ OPLOAD            ; Teste se o comando é LO
                + LO                 ;
;
                - DU                 ;
                JZ OPDUMP            ; Testa se o comando é um DU
                JP ERROCMD           ; Se não for nem LO nem DU, então é um erro de comando
;
OPLOAD          SC TRATALOAD         ;
				SC PEGADADO
                SC CHECAPULA         ; Espera-se ler um \n\n
                JZ ERROARG           ; Se a linha não acabou, deu um erro de argumento
				JP LOOP              ; Se a linha acabou esta certo
;
OPDUMP          SC TRATADUMP
				SC PEGADADO                
                SC CHECAPULA         ; Espera-se ler um \n\n
                JZ ERROARG           ; Se a linha não acabou, deu um erro de argumento
                JP LOOP
;
CMDOUEND        SC PEGADADO          ; Leio mais um dado
                -  FIMARQ            ; Comparo com o fim de arquivo
                JZ ERROEND           ; Se for fim de arquivo então era um end errado mesmo
                JP ERROCMD           ; Tinha mais coisas. CMD errado.
;
ERROJOB         LV /0001
                OS /0EE
                LV /004A ; J
                PD /100
                JP FIM
ERROCMD         LV /0002
                OS /0EE
                LV /0043 ; C
                PD /100
                JP FIM               ;
ERROARG         LV /0003
                OS /0EE
                LV /0041 ; A
                PD /100
                JP FIM
ERROEND         LV /0004
                OS /0EE
                LV /0044 ; D
                PD /100
                JP FIM
ERRO            OS /0EE
                JP FIM
ERROTRATA       LD AUX
                -  ERFIM
                JZ ERROEND
                + ERFIM
                - ZEROZERO  
                JZ ERROARG
                JP ERROCMD
FIM             HM      FIM          ; Fim do programa
;====================================
;Função ChecaBarra: verifica \\
;====================================
CHECABARRA      K  /0000
                -  BARRAS2
                JZ OKBARRA
                LV /000
                JP FIMBARRA
OKBARRA         LV /0001
FIMBARRA             RS CHECABARRA
;====================================
;Função CHECAJOB: verifica JB
;====================================
CHECAJOB        K /0000
                SC PEGADADO          ; Espera-se que seja um JB
                -  TKJOB             ; Subtrai de JB
                JZ CERTOJOB          ; Se deu zero está certo
                LV /0000             ; Se deu erro carrega 0
                JP FIMJOB
CERTOJOB        LV /0001             ; Se deu certo carrega 1
FIMJOB          RS CHECAJOB
;====================================
;Função CHECAPULA: verifica \n\n
;====================================
CHECAPULA       K /0000
                - PULA
                JZ CERTOPULA
                LV /0000
                JP FIMPULA
CERTOPULA       LV /0001
FIMPULA         RS CHECAPULA
;====================================
;Função CHECAESPAÇO: verifica bb
;====================================
CHECAESPACO     K /0000
                - ESPACO
                JZ CERTOESPACO
                LV /0000
                JP FIMESPACO
CERTOESPACO     LV /0001
FIMESPACO       RS CHECAESPACO
;====================================
;Função TRATADUMP
;====================================
TRATADUMP       K /0000
                SC PEGADADO         ; Le o pula linha
                SC CHECAPULA        ; Verifica se é um pula
                JZ ERROCMD          ; Se não acabar a linha deu um erro de comando
;TAMANHO DO BLOCO
                SC PEGADADO         ; Lê o 1 byte do tamanho do bloco
                MM CH_IN_A          ; Passa como argumento
                SC PEGADADO         ; Lê o 2o byte do tamanho do bloco
                MM CH_IN_B          ; Passa como argumento
                SC CHTOI            ; Converte para binario
                LD CH_ANS           ; Carrega a resposta
                MM DUMP_BL          ; Escreve como parâmetro do Tamanho do bloco
;
                SC PEGADADO         ; Espera-se um BB. Le o espaço em branco
                SC CHECAESPACO      ; Verifica
                JZ ERROARG          ; Se não for espaço em branco (0) então é um erro
; ENDEREÇO INICIAL
                SC PEGADADO         ; Lê o 1o byte do end. inicial 
                MM CH_IN_A            
                SC PEGADADO         ; Lê o 2o byte do end. inicial
                MM CH_IN_B       
                SC CHTOI            ; Converte para binario
                LD CH_ANS           ; Pega a resposta
                MM DUMP_INI         ; Escreve como variável no End. inicial
;
                SC PEGADADO         ; Espera-se um BB. Lê o espaço em branco
                SC CHECAESPACO      ; Verifica
                JZ ERROARG          ; Se não for espaço em branco (return 0) então é um erro
; TAMANHO TOTAL
                SC PEGADADO         ; Lê o 1o byte do end. inicial
                MM CH_IN_A
                SC PEGADADO         ; Lê o 2o byte do end. inicial
                MM CH_IN_B
                SC CHTOI            ; Converte para binario
                LD CH_ANS           ; Pega a resposta
                MM DUMP_TAM         ; Escreve como variável de Tamanho total
;
                SC PEGADADO         ; Espera-se um BB. Lê o espaço em branco
                SC CHECAESPACO      ; Verifica
                JZ ERROARG          ; Se não for espaço em branco (return 0) então é um erro
;
; ENDEREÇO DA 1a INSTRUCAO
;
                SC PEGADADO         ; Lê o 1o byte do end. inicial
                MM CH_IN_A
                SC PEGADADO         ; Lê o 2o byte do end. inicial
                MM CH_IN_B
                SC CHTOI            ; Converte para binario
                LD CH_ANS           ; Pega a resposta
                MM DUMP_EXE         ; End. da 1 inst
;
                SC PEGADADO         ; Espera-se um BB. Lê o espaço em branco
                SC CHECAESPACO      ; Verifica
                JZ ERROARG          ; Se não for espaço em branco (return 0) então é um erro
;UNIDADE LÓGICA
                SC PEGADADO         ; Lê o 1o byte do end. inicial
                MM CH_IN_A
                SC PEGADADO         ; Lê o 2o byte do end. inicial
                MM CH_IN_B
                SC CHTOI            ; Converte para binario
                LD CH_ANS           ; Pega a resposta
				MM DUMP_UL          ; Unidade lógica
;
                SC DUMPER           ; Chama o dumper
FIMOPDUMP       RS TRATADUMP
;
;TRATALOAD
;
TRATALOAD       K /0000
                SC PEGADADO        ; Espera-se que seja um \n\n
                SC CHECAPULA       ; Se retornar zero então deu erro
				JZ ERROCMD         ; O comando está errado
				SC PEGADADO        ; Lê 1 byte da unidade lógica como argumento
                MM CH_IN_A         ; Escreve no parâmetro
                SC PEGADADO        ; Lê 2o byte da Unidade lógica
                MM CH_IN_B         ; Escreve no segundo parâmetro
                SC CHTOI           ; Converte para binario
                LD CH_ANS          ; Pega a resposta
                MM LOADER_UL       ; Passa como parâmetro para o loader
                SC LOADER          ; Chama a função
                RS TRATALOAD
;
;
;
PEGADADO        K /0000
                LD GET
                +  UL
                + DISCO
                MM INST1
INST1           K /0000
                RS PEGADADO
# MAIN
