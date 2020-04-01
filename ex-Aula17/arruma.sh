echo 'dumper.asm' | java -cp mlr.jar montador.MvnAsm
echo 'loader.asm' | java -cp mlr.jar montador.MvnAsm
echo 'main.asm'   | java -cp mlr.jar montador.MvnAsm
echo 'getp2.asm'   | java -cp mlr.jar montador.MvnAsm

java -cp mlr.jar linker.MvnLinker main.mvn dumper.mvn loader.mvn getp2.mvn -s main-relocavel.mvn

java -cp mlr.jar relocator.MvnRelocator main-relocavel.mvn main-absoluto.mvn 0000


