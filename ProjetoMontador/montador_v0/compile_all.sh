#!/bin/bash
cd asm

java -cp ../mlr.jar montador.MvnAsm montador.asm
java -cp ../mlr.jar montador.MvnAsm conversoes.asm
java -cp ../mlr.jar montador.MvnAsm ctes.asm
java -cp ../mlr.jar montador.MvnAsm mtoc.asm
java -cp ../mlr.jar montador.MvnAsm passos.asm

mv *.lst ../lst
mv *.mvn ../mvn

cd ../mvn
java -cp ../mlr.jar linker.MvnLinker montador.mvn ctes.mvn conversoes.mvn mtoc.mvn passos.mvn -s montador.rlc
mv montador.rlc ..
cd ..
java -cp mlr.jar relocator.MvnRelocator  montador.rlc montador.mvn 0000

java -jar mvn.jar < comandos.in
