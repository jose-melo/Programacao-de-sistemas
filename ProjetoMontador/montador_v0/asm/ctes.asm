            @     /0000     
; ==================================================================
; *** CONSTANTES E ÁREAS DE DADOS - ©JJN/2005
; ==================================================================
;
; SÍMBOLOS EXPORTADOS

LINHA        >
LINHA2       >
PLINHA       >
AUX          >
AUX1         >
H0           >
H1           >
H2           >
H2F          >
H4           >
H6           >
H9           >
H0A          >
H30          >
H40          >
HFF00        >
H3FFE        >
H1000        >
H4000        >
H9000        >
H100         >
HFFFF        >
HD           >
MAXBYTES     >
MAXSIMB      >

AUX          >
B1           >
B2           >
CHAR2        >
OPERAND      >
PTS          >
RTOP         >
ULTIMO       >

LD          >
MM          >
OP          >
RT          >
SPC2        >
SPCPV       >
APOST       >
BLANK       >
BARRA       >
CRLF        >
DIG9        >
IC          >
LOAD        >
STORE       >
NEWLINE     >

LF           >
CR           >
FIM          >
ORIGEM       >
DUPDEF       >
ER           >
RO           >
COLLONB      >


WORD        >
ISB         >
COMMENT     >
P1END       >
PARG        >
ARGCNT      >
PARG1       >
PARG2       >
PARG3       >
EOL         >
EOF         >
STI         >
STE         >
PST         >
ADDRI       >
ARR         >
ECOM        >
CIFRA       >
CONT        >

HEXOP       >
BINOP       >
OCTAOP      >
ASCIIOP     >                

; ***  CONSTANTES NUMÉRICAS E MÁSCARAS  ***
    H0       K     /0       ; ZERO
    H1       K     /1       ; UM
    H2       K     /2       ; DOIS
    H4       K     /4       ; QUATRO
    H6       K     /6       ;_aqui_ SEIS
    H9       K     /9
    H0A      K     /A
    H30      K     /30
    H40      K     /40
    H3FFE    K     /3FFE    ;_aqui_ 3FFE
    H1000    K     /1000    ;_aqui_ 1000
    H4000    K     /4000    ;_aqui_ 4000
    H9000    K     /9000    ;_aqui_ 9000
    HFF00    K     /FF00    ; -(/80 * /80)
    H100     K     /0100    ; /100 = 2**8
    HFFFF    K     /FFFF    ;-1
    MAXBYTES K     /50      ; COMPRIMENTO DA LINHA=80
    MAXSIMB  K     /100     ; 128 SÍMBOLOS NO MÁXIMO
;ERRO:Dupla definição(220)    MNEM     K     /0       ; MNEMÔNICO DA INSTRUÇÃO(2 CARACT.)
    OP       K     /FFFF    ; OPERANDO = -1
;ERRO:Dupla definição(196)    OPCODE   K     /0       ; CÓDIGO DE OPERAÇÃO DA INSTRUÇÃO
;ERRO:Dupla definição (195)    OPERAND  K     /0       ; OPERANDO DA INSTRUÇÃO
    RT       K     /0       ; RÓTULO = 0
    SPC2     K     /2020    ; DOIS ESPAÇOS
    SPCPV    K     /202C    ; ESPAÇO E PONTO-E-VÍRGULA
;
; *** CONSTANTES ASCII E SIMILARES ***
    APOST    K     /0027    ; APÓSTROFE-BARRA
    BLANK    K     /2020    ; ESPAÇO
    BARRA    K     /002F    ; BARRA
    CRLF     K     /0D0A    ; FIM DE LINHA
    DIG9     K     /12      ; (NOVE ASCII=/39)-(APÓSTROFE=/27)
    NEWLINE  K     /200A    ; " \n"
;
; *** MÁSCARAS PARA A CONSTRUÇÃO DE INSTRUÇÕES
    MM       MM    LINHA    ;" MÁSCARA P/ MONTAR ""STA LINHA[X]"""
    LD       LD    LINHA    ;" MÁSCARA P/ MONTAR ""LDA LINHA[X]"""
    
    LOAD     LD    /0000    ; CODIGO DE OPERAÇÃO LOAD
    STORE    MM    /0000    ; CÓDIGO DE OPERAÇÃO MOVE MEMORY
    HD       GD    /0000
; ***  VARIÁVEIS  ***
    AUX      K     /0       ; VARIÁVEL AUXILIAR
    AUX1     K     /0       ;_aqui_ adicionado por Douglas
    B1       K     /0       ; BYTE ESQUERDO
    B2       K     /0       ; BYTE DIREITO
    CHAR2    K     /0000    ; DOIS CARACTERES ASCII LIDOS
    IC       K     /0       ; CONTADOR DE INSTRUÇÕES
    OPERAND  K     /0       ; *** OPERANDO ***
    PLINHA   K     /0       ; PONTEIRO PARA LINHA (CURSOR)
    PTS      K     /0       ; PONTEIRO PARA TAB. DE SIMB.(CURSOR)
    RTOP     K     /0       ; RÓTULO (0) OU OPERANDO (-1)
    ULTIMO   K     /0       ; ULTIMO SIMBOLO NA T.S.

    ;READLINE
    WORD      K     /0
    COMMENT   K     /203B
    EOF       K     /2023
    ISB       K     /0
    P1END     K     /0
    PARG      K     /0000
    ARGCNT    K     /0
    PARG1     K     /0
    PARG2     K     /0
    PARG3     K     /0
    EOL       K     /200A 
    SYMBTAB   $     =10 
    ADDRTAB   $     =10
    STI       K     SYMBTAB
    STE       K     STI
    PST       K     /0
    ADDRI     K     ADDRTAB



;
;** ÁREAS DE DADOS ***
    LINHA    K    /0       ; 80 PALAVRAS PARA UMA LINHA-FONTE
    LINHA2   K    /0       ; AO TODO
;             @    /94     ; RESERVA /50 PALAVRAS;
;
;** MISCELANEA *** MUDAR DE LUGAR POSTERIORMENTE ***
    LF       K    /0A      ; LINE FEED
    CR       K    /0D      ; CARRIAGE RETURN
    FIM      K    /0023    ; SUSTENIDO
    ORIGEM   K    /001D    ; (ARROBA=/40) - (SUSTENIDO=/23)
    DUPDEF   K    /4444    ;" DUPLA DEFINIÇÃO DE SÍMBOLO = ""DD"""
    H2F      K    /002F    ;" (""0"" = /30) - 1"
    ER       K    /4552    ;" ""ER"""
    RO       K    /524F    ;" ""RO"""
    COLLONB  K    /3A20    ; DOIS PONTOS, BRANCO
    ARR      K    '@
    ECOM     K    '&
    CIFRA    K    '$

    CONT     K    /0000

;** PREFIXOS DE OPERANDOS PARA VERIFICAR BASE**;
    HEXOP    K   /002F  ; /
    BINOP    K   /0023  ; #
    OCTAOP   K   /0040  ; @
    ASCIIOP  K   /0027  ; '
        
ENDFILE      #     ENDFILE 
