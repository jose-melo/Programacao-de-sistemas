package dumperFormatConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/***
 * 
 * Conversor para código do LOADER/DUMPER (Aula 06) <br>
 * 
 * Classe responsável pela conversão do código mvn para um arquivo a ser lido pelo LOADER implementado na
 * aula sobre DUMPER/LOADER.
 * 
 * @author Tiago 
 * @version 01.02.2010 : inicial
 * @version 01.07.2010 : versão para a disciplina 2010
 * 
 * */
public class Converter {

    /**Número de words por bloco*/
    private int numberOfWords;
    /**Nome do Arquivo de Saída*/
    private String fileName;
    /**List que simula o buffer de informação ( bloco ) a ser gravado no arquivo de saída*/
    private List<String> buffer;
    /**Guarda o endereço inicial do bloco*/
    private int initialAddress;

    /**
     *
     * @param numberOfWords número máximo de Words a serem gravadas em cada um dos blocos
     * @param fileName arquivo de Saída
     *
     * */
    public Converter(int numberOfWords, String fileName) {
        //TODO: Converter
    }//

    /**
     * Grava uma word de informação no arquivo de saída.
     *
     *  @param adress o endereço da word
     *  @param word a word
     *
     * */
    public void write(int address, String word) {
        //TODO: write
		/*
         * Coloca a word no buffer de saída
         * */
    }//

    /**
     * Coloca a word no buffer de saída. Grava o bloco no arquivo de saída quando atinge a quantidade máxima de words especificada.
     *
     *  @param adress o endereço da word
     *  @param word a word a ser gravada no arquivo de saída
     *
     * */
    private void putInBuffer(int address, String word) {
        //TODO: putInBuffer
        /*
         * Adiciona a word no buffer.
         * Grava o bloco no arquivo de saída se acaso chegou no limite da quantidade de words por bloco.
         * */
        //Se o endereço passado não é igual ao esperado e o buffer não está vazio finaliza o buffer
    }//

    /**
     * Calcula o checksum do bloco passado como parametro.
     *
     * @param address o endereço inicial do bloco
     * @param block uma lista contendo as Words a serem gravadas no arquivo de saída.
     *
     * @return uma String contendo o cálculo do checksum representado no formato hexadecimal.
     *
     * */
    private String calculateCheckSum(int address, List<String> block) {

        //TODO: calculateCheckSum
		/*
         * Calcula o checksum: endereço + tamanho do bloco + info das words
         *
         * */

        return "implementar";
    }//

    /**
     *
     * Grava o bloco de informação no arquivo de saída, convertendo o buffer para o formato desejado
     * e calculando o checksum ( endereço inicial + tamanho do bloco + INFO ).
     *
     * @param address o endereço inicial do bloco
     * @param block uma lista contendo as Words a serem gravadas no arquivo de saida.
     *
     * */
    private void writeBufferInFile(List<String> block) {
        //TODO: writeBufferInFile
		/*
         * Grava o bloco no arquivo de saída: endereço inicial + tamanho bloco + words do bloco + checksum
         * */
    }//m

    /**
     *
     * Termina a gravação do arquivo no formato DUMPER/LOADEr.
     * Cria um bloco para as words existentes no buffer se ainda existirem words a serem gravadas no buffer.
     * Cria um bloco de identificação de terminação do arquivo. (Arquivo com endereço=0000, size=0000,checksum=0000)
     *
     * */
    public void finalizeFile() {

        //TODO: finalizeFile

    }//

    /**
     * @param args
     */
    public static void main(String[] args) {


        /*
         * Teste simples do Conversor.
         *
         * */

        //Cria o conversor: 3 words por bloco
        Converter con = new Converter(3, "Bla.txt");
        //grava a word 1234 no endere�o 0
        con.write(0, "1234");
        //grava a word 5678 no endere�o 2
        con.write(2, "5678");
        //grava a word 9ABC no endere�o 4
        con.write(4, "9ABC");
        //grava a word DEF0 no endere�o 6
        con.write(6, "DEF0");
        //finaliza a geração do arquivo para o loader
        con.finalizeFile();

    }//mani
}
