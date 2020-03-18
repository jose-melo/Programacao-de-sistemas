package relocator;

import dumperFormatConverter.Converter;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;

/**
 * Classe responsável pela relocação do código.<br>
 * @author FLevy
 * @version 20.11.2005
 * @version 01.01.2010 : alinhamento do código com a lógica dos slides
 */
class Relocator {

    private File arqEntrada;
    private File arqSaida;
    private int base;
    private static String MSG_ERROR_EXTERNAL_SYMBOL = "Simbolo externo não resolvido: ";
    // Mensagens do programa
    private static final String MSG_SINTAXE_INADEQUADA = "Linha com sintaxe inadequada. Linha: ";
    private static final String MSG_ERRO_LIMITE_MEM = "Contador de instrucoes ultrapassou o limite de memoria. Linha: ";
    private static final String ASM_SPACE = " ";
    // Limite dos endereços válidos
    private static final int LAST_VAL_ADDR = 4096;

    /**
     * Constrói um relocador.
     * @param entrada O arquivo de entrada.
     * @param saida O arquivo de saída.
     * @param base A base para relocação.
     */
    public Relocator(File entrada, File saida, int base) {
        this.arqEntrada = entrada;
        this.arqSaida = saida;
        this.base = base;

        String na = saida.getName().substring(saida.getName().lastIndexOf(".")+1);       
    }

    /**
     * Processo o arquivo de entrada e gera a saída relocável.
     * @return Verdadeiro caso a relocação tenha sido completada com sucesso.
     * @throws IOException Caso haja algum erro para ao ler ou escrever nos
     * arquivos.
     */
    public boolean processar() throws IOException {
        BufferedWriter saida = new BufferedWriter(new FileWriter(arqSaida));
        String operacao, operacaoSaida;

        StringTokenizer tokenizer;
        String endereco, enderecoSaida;
        int nibble;
        BufferedReader leitor;
        int numLinha = 0;
        String linha;
        int end = 0; // endereco de saida

        leitor = new BufferedReader(new FileReader(arqEntrada));
        linha = leitor.readLine();
        numLinha = 0;

        while (linha != null) {
            numLinha++;
            tokenizer = new StringTokenizer(linha);

            if (tokenizer.countTokens() == 2) {
                // só endereço e código
                endereco = tokenizer.nextToken();
                operacao = tokenizer.nextToken();

                if (endereco.length() != 4) {
                    // endereco invalido
                    System.out.println(MSG_SINTAXE_INADEQUADA + numLinha);
                    return false;
                }

                if (operacao.length() != 4) {
                    // operacao invalida
                    System.out.println(MSG_SINTAXE_INADEQUADA + numLinha);
                    return false;
                }

                try {
                    nibble = Integer.parseInt(endereco.charAt(0) + "", 16);
                } catch (NumberFormatException e) {
                    System.out.println(MSG_SINTAXE_INADEQUADA + numLinha);
                    return false;
                }

                if (isRelocable(nibble)) {
                    // obtendo endereco
                    try {
                        end = Integer.parseInt(endereco.substring(1, endereco.length()), 16);
                    } catch (NumberFormatException e) {
                        System.out.println(MSG_SINTAXE_INADEQUADA + numLinha);
                        return false;
                    }

                    if (end + base > LAST_VAL_ADDR) {
                        System.out.println(MSG_ERRO_LIMITE_MEM + numLinha);
                        return false;
                    }

                    enderecoSaida = Integer.toHexString(end + base);
                    enderecoSaida = "0000" + enderecoSaida;
                    enderecoSaida = enderecoSaida.substring(enderecoSaida.length() - 4, enderecoSaida.length());
                } else {
                    // tira o nibble
                    enderecoSaida = "0000" + endereco.substring(1, endereco.length());
                    enderecoSaida = enderecoSaida.substring(enderecoSaida.length() - 4, enderecoSaida.length());
                }

                if (isArgumentRelocable(nibble)) {
                    // precisa corrigir o argumento a partir da base
                    operacaoSaida = "0000" + Integer.toHexString(Integer.parseInt(operacao.substring(1, operacao.length()), 16) + base);
                    operacaoSaida = operacaoSaida.substring(operacaoSaida.length() - 3, operacaoSaida.length());
                    operacaoSaida = operacao.charAt(0) + operacaoSaida;
                } else {
                    operacaoSaida = operacao;
                }
                saida.write(enderecoSaida + ASM_SPACE + operacaoSaida);

                saida.newLine();
            } else if (tokenizer.countTokens() == 4) {
                //Número de tokens igual a 4 : pode ser símbolo exportável ou importável não resolvido
                //Exportável: ignoro
                //Importável: não resolvido, portanto gera mensagem de erro adequada
                //Verificando o quarto token da linha
                tokenizer.nextToken();
                tokenizer.nextToken();
                tokenizer.nextToken();
                String rotulo = tokenizer.nextToken();
                if (rotulo.contains("<")) {
                    //Símbolo externo não resolvido: gera mensagem de erro
                    System.out.println(MSG_ERROR_EXTERNAL_SYMBOL + rotulo);
                    return false;
                }//
                if (!rotulo.contains(">")) {
                    //Linha com quantidade de tokens inadequada
                    System.out.println(MSG_SINTAXE_INADEQUADA + numLinha + " (" + arqEntrada.getName() + ")");
                    return false;
                }//
                //Caso contrário é um símbolo exportável: vai ser ignorado
            } else if (tokenizer.countTokens() != 0) {
                // n�mero de tokens inadequado
                System.out.println(MSG_SINTAXE_INADEQUADA + numLinha + " (" + arqEntrada.getName() + ")");
                return false;
            }
            linha = leitor.readLine(); // proxima linha
        }
        leitor.close();
        leitor = null;
        saida.close();
        saida = null;

        return true;
    }

    /**
     * Verifica se o nibble representa um endereço relocável.
     * @param nibble O nibble a ser verificado.
     * @return Verdadeiro se o endereço for relocável.
     */
    private boolean isRelocable(int nibble) {
        return (nibble >> 3) % 2 > 0;
    }

    /**
     * Verifica se o nibble representa um argumento relocável.
     * @param nibble O nibble a ser verificado.
     * @return Verdadeiro se o argumento for relocável.
     */
    private boolean isArgumentRelocable(int nibble) {
        return (nibble >> 1) % 2 > 0;
    }
}
