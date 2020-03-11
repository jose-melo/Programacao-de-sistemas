package montador;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import util.SymbolTable;
import assemblerException.AssemblerException;

//Versão do aluno gerada em 01.02.2010
/**
 * Representa um passo do montador.
 * 
 * @author RRocha
 * @author FLevy
 * @version 10.10.2005
 * @version 28.01.2010 (refatoramento - Tiago) : conversão para generics
 */
public abstract class Pass {

    //Variáveis de apoio
    protected final String COMM = ";";
    /**Limite de endereços válidos*/
    protected final int LAST_VAL_ADDR = 4096;
    /**Valor máximo que pode ser contido dentro de uma word*/
    protected final int MAX_VALUE = 0xFFFF;
    /**Início do programa*/
    protected final int CONST_INIT_COUNT = 0;
    /**Tabela de Símbolos ( guarda os símbolos e valores ( endereços ))*/
    protected SymbolTable tab;
    /**Guarda a posição do contador de instruções. Indica o endereço da instrução (ou pseudo-instrução) */
    protected int locationCounter = CONST_INIT_COUNT;
    // Códigos do tipo de dado
    protected final String HEX_CODE = "/";
    protected final String ASCII_CODE = "'";
    protected final String DECIMAL_CODE = "=";
    protected final String OCTAL_CODE = "@";
    protected final String BINARY_CODE = "#";
    // Expressões regulares para os valores de cada tipo
    protected final String HEX_CHARS = "[0-9a-fA-F]+";
    protected final String ASCII_CHARS = ".+";
    protected final String DECIMAL_CHARS = "[0-9]+";
    protected final String OCTAL_CHARS = "[0-7]+";
    protected final String BINARY_CHARS = "[0-1]+";
    //Mensagens
    protected final String MSG_PASS_END_ERROR = "Erro: símbolo de final físico do texto fonte não encontrado no arquivo";
    /**Mensagem a ser gerada se acaso a base passada na definição de constante não existir*/
    protected final String MSG_PASS_BASE_ERROR = "Erro: Código do tipo de dado inexistente";

    /**
     * Analisa uma linha de código.
     *
     * @param symbols
     *            O array com os símbolos processados.
     * @param line
     *            A linha original.
     * @return Verdadeiro caso a análise teve sucesso, falso caso contrário.
     * @exception Caso
     *                tenha ocorrido algum problema de IO.
     */
    protected abstract boolean analyzeLine(ArrayList<String> symbols, String line)
            throws IOException;

    /**
     * Processa uma linha de comentário.
     *
     * @param data
     *            A linha apenas com comentário.
     * @throws IOException
     *             Caso tenha ocorrido algum problema de IO.
     */
    protected abstract void processComment(String data) throws IOException;

    /**
     * Retorna a mensagem de erro de IO para esse passo.
     */
    public abstract String getIOErrorMessage();

    /**
     * Retorna a mensagem de erro do montador para esse passo.
     */
    public abstract String getASMErrorMessage();

    /**
     * Analisa os dados e os processa.<br>
     *
     * @param in
     *            Arquivo com o código em linguagem de montador.
     * @return -1 caso tenha ocorrido um erro de IO, 0 caso não haja erro ou o
     *         número da linha com erro.
     */
    public int tokenizeData(BufferedReader in) {
        ArrayList<String> symbols;
        String data;
        StringTokenizer tokens;
        boolean comentario;
        String temp;
        int numLinha = 0;
        /**Indica o final físico do arquivo*/
        boolean flagFinalFisico = false;


        try {
            data = in.readLine();
            numLinha++;

            while (data != null) {
                tokens = new StringTokenizer(data);
                symbols = new ArrayList<String>();

                comentario = false;
                while (tokens.hasMoreTokens() && !comentario) {
                    temp = tokens.nextToken();
                    if (temp.startsWith(COMM)) {
                        // o resto da linha é comentario
                        comentario = true;
                    } else {
                        symbols.add(temp);
                    }
                }

                if (symbols.size() > 0) {
                    if (!analyzeLine(symbols, data)) {
                        return numLinha;
                    }
                } else {
                    processComment(data);
                }
                data = in.readLine();
                numLinha++;
                if (symbols.contains(PseudoTable.END_STR)) {
                    flagFinalFisico = true;
                }//
            }//while

            //Não tem o pseudo símbolo indicador de final físico do arquivo.
            if (!flagFinalFisico) {
                throw new IOException(MSG_PASS_END_ERROR);
            }//

            return 0; // ok
        } catch (IOException e) {
            return numLinha;
        }
    }

    /**
     * Testa se o argumento é um número(HEX, ASCII, DECIMAL, OCTAL e BINARY)
     *
     * @param code
     *            O valor a ser testado se é número.
     * @return Verdadeiro caso o argumento seja um número, falso caso contrário.
     *
     */
    protected boolean isNumber(String code) {
        String base = code.substring(0, 1);
        String resto = code.substring(1, code.length());
        if (!((base.equals(HEX_CODE) && resto.matches(HEX_CHARS))
                || (base.equals(ASCII_CODE) && resto.matches(ASCII_CHARS))
                || (base.equals(DECIMAL_CODE) && resto.matches(DECIMAL_CHARS))
                || (base.equals(OCTAL_CODE) && resto.matches(OCTAL_CHARS))
                || (base.equals(BINARY_CODE) && resto.matches(BINARY_CHARS)))) {
            return false;
        }
        return true;
    }

    /**
     * Obtêm o número em decimal a partir de um número definido nas codificações
     * válidas do montador.
     *
     * @param arg O número em uma determinada codificação.
     *
     * @return O número decimal correspondendo o número.
     *
     * @throws MVNException se acaso a base passada como parametro não estiver definida
     * @throws NumberFormatException se acaso o número ( não estiver na base correta)
     *
     */
    protected int getDecNumber(String arg) throws AssemblerException, NumberFormatException {

        String type = arg.substring(0, 1);

        if (type.equalsIgnoreCase(DECIMAL_CODE)) {
            return Integer.parseInt(arg.substring(1), 10);
        } else if (type.equalsIgnoreCase(OCTAL_CODE)) {
            return Integer.parseInt(arg.substring(1), 8);
        } else if (type.equalsIgnoreCase(BINARY_CODE)) {
            return Integer.parseInt(arg.substring(1), 2);
        } else if (type.equalsIgnoreCase(HEX_CODE)) {
            return Integer.parseInt(arg.substring(1), 16);
        } else if (type.equalsIgnoreCase(ASCII_CODE)) {
            String valor = arg.substring(1, arg.length());
            int result = 0;

            //Verificando se corresponde � um caractere ascii
            if (!arg.substring(1).matches(ASCII_CHARS)) {
                //throw new MVNException(ASM.MSG_PASS_BASE_ERROR);
            }//

            // ascii
            for (int i = 0; i < valor.length(); i++) {
                result += ((int) Math.pow(256, valor.length() - 1 - i)) * (int) valor.charAt(i);
            }
            return result;
        } else {
            //erro
            throw new AssemblerException(MSG_PASS_BASE_ERROR);
        }//if

        // implementar método getDecNumber()

        /*
         * Converter o número passado usando o seguinte pseudo-algoritmo
         * 		-retira a base da string ( primeiro caractere do arg )
         * 		-verifica à qual das bases (definida na classe ASM) corresponde a entrada
         * 			-se não corresponde à nenhuma das bases definidas, gerar uma MVNException com a mensagem de erro
         * 					definida em ASM e sair do programa.
         *
         * 		-converte o restante do arg para um valor decimal
         * 				(se estiver no formato errado, uma NumberFormatException é gerada automaticamente)
         *
         * */


    }//
}//class

