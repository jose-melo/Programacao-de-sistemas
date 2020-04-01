;Teste
; @ /0000
;SC GETP
;+ ASCII
;PD /100
;FIMTESTE HM FIMTESTE
;--------------------------------------
;EntryPoints e Externals
;--------------------------------------
GETP >
SENHA <
PSENHA <
;======================================
;Variáveis e Constantes
;======================================
 &      /0500
ESPACO K /2020
I K /0000
J K /0000
UM K /0001
OITO K /0008
PASSO K /0002
GETOP K /D000
MOVE K /9000
AUX K /0000
DOIS K /0002
LOAD K /8000
MENOS K /5000
ASCII K /3030
PBUFFER K BUFFER
GET2 K /0300
UL2 K /0000
;--------------------------------------
BUFFER K /0000
 K /0000
 K /0000
 K /0000
 K /0000
 K /0000
 K /0000
 K /0000
;--------------------------------------
;Programa principal
;--------------------------------------
GETP K /0000
 LV /0000
 MM I
 MM J
LOOP SC PEGADADO
 SC ESCREVE
 LD I
 + UM
 MM I
 SC PEGADADO
 - ESPACO
 JZ LOOP
 LV /0000
 MM I
 MM J
LOOP2 SC CHECAI
 JZ ATUALIZA
 JP SENHAINCORRETA
ATUALIZA LD I
 + UM
 MM I
 LD J
 + UM
 MM J
 LD I
 - OITO
 JZ SENHACORRETA
 JP LOOP2
SENHAINCORRETA LV /0000
 JP FIMGETP
SENHACORRETA LV /0001
FIMGETP RS GETP
;======================================
;Função que lê o dado de UL
;======================================
PEGADADO K /0000
 LD GETOP
 + GET2
 + UL2
 MM INSTDADO
INSTDADO K /0000
 RS PEGADADO
;======================================
;Função que escreve
;======================================
ESCREVE K /0000
 MM AUX
 LD I
 * DOIS
 + PBUFFER
 + MOVE
 MM INSTESCREVE
 LD AUX
INSTESCREVE K /0000
 RS ESCREVE
;=====================================
;Funcao que checa a posição i e j
;=====================================
CHECAI K /0000
 LD I
 * DOIS
 + PBUFFER
 + LOAD
 MM LE1
 LD J
 * DOIS
 + PSENHA
 + MENOS
 MM MENOS2
LE1 K /0000
MENOS2 K /0000
 JZ FIM
 LV /0001
FIM RS CHECAI
# GETP 
