; ----------------------------------------------------------------------------
; Exemplo de programa escrito na linguagem de montagem da MVN
; Referência da linguagem:
; Slides das aulas 10 e 11
; ----------------------------------------------------------------------------

    JP INICIO      ; Jump incondicional para o início

@   /100           ; Define que o endereço agora é: 0x100
C0  K  /100        ; Declara uma constante chamada C0 com valor 0x100
C1  K  /111        ; Declara outra constante, e etc.
C2  K  /122
C3  K  /133
C4  K  /144
C5  K  /155
C6  K  /166
C7  K  /177
C8  K  /188
C9  K  /199
CA  K  /1AA
CB  K  /1BB
CC  K  /1CC
CD  K  /1DD
CE  K  /1EE
CF  K  /1FF

; Exemplos de declaração de constantes em diferentes formatos:

GG  K  'ER         ; Base 256 (valor = 'E'*256^1 + 'R'*256^0)
GH  K  'RO         ; Base 256
GJ  K  =1000       ; Base 10 (1000 em decimal)
GK  K  /3E8        ; Base 16 (equivalente a 0x3E8)
GL  K  @1750       ; Base 8
GÇ  K  #1111101000 ; Base 2 (1111101000 em binário)

@   /200           ; Define que o endereço agora é: 0x200
$   /A             ; Reserva 10 words

INICIO JP C0       ; Começo do programa
       JZ C1       ; Exemplo de uso de todos os mnemônicos da linguagem
       JN C2
       LV C3
       +  C4
       -  C5
       *  C6
       /  C7
       LD C8
       MM C9
       SC CA
       RS CB
       HM CC
       GD CD
       PD CE
       OS CF

# INICIO           ; Marca o final do programa
