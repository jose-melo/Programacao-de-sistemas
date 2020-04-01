#!/bin/bash

echo 'dumper.asm' | java -cp mlr.jar montador.MvnAsm
echo 'loader.asm' | java -cp mlr.jar montador.MvnAsm
echo 'main2.asm' | java -cp mlr.jar montador.MvnAsm
java -cp mlr.jar linker.MvnLinker main2.mvn dumper.mvn loader.mvn -s main-relocavel.mvn
java -cp mlr.jar relocator.MvnRelocator main-relocavel.mvn main-absoluto.mvn 0000
> dumpSaida.txt
java -jar mvn.jar
