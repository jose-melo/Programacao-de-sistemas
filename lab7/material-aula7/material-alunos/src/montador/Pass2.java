package montador;

import java.io.IOException;
import java.util.ArrayList;
import util.SymbolTable;
import assemblerException.AssemblerException;

//versão do aluno gerada em 01.02.2010
/**
 * Representa o passo 2 do montador, quando é gerado o código.
 *
 * @author RRocha
 * @author FLevy
 * @version 10.10.2005
 * @version 27.07.2006 (Separação na classe Output - FLevy)
 * @version 21.09.2006 (Montador absoluto - FLevy)
 * @version 28.01.2010 (Refatoramento - Tiago) : Conversão para generics e alterações na classe/enunciado do exercicio da aula.
 */
public class Pass2 extends Pass {

    private Output out;
    //Variáveis internas de apoio
    private final String SPACES = " ";
    //Mensagens
    private final String MSG_PASS2_IO_ERROR = "Erro de I/O no passo 2, verifique o arquivo";
    private final String MSG_PASS2_INST_ERROR = "Erro: Instrucao fornecida nao reconhecida: ";
    private final String MSG_PASS2_ARG_ERROR = "Erro: Nao foi possivel definir o valor do argumento: ";
    private final String MSG_PASS2_ARG_ABORT_ERROR = " abortando ...";
    private final String MSG_PASS2_WR_ERROR = "Erro ao tentar gravar arquivo, abortando ...";
    private final String MSG_PASS2_OK = "Montador finalizou corretamente, arquivos gerados.";
    private final String MSG_PASS2_ASM_ERROR = "Erro na montagem no passo 2. Linha: ";
    private final String MSG_PASS2_ASM_SYMBOL = "Erro: simbolo nao encontrado.";
    private final String MSG_PASS2_ASM_SPACE = "Erro: argumento ocupa mais espaco do que disponivel";

    /**
     * Constrói um segundo passo para o montador.
     *
     * @param tab
     *            A tabela de símbolos preenchida.
     * @param objFile
     *            O arquivo de saída para o código de máquina.
     * @param listFile
     *            O arquivo de saída que conterá o código de máquina e o código
     *            original comentado (arquivo list).
     * @throws IOException
     *             Caso tenha ocorrido algum problema ao abrir os arquivos de
     *             saída.
     */
    public Pass2(SymbolTable tab, String objFile, String listFile)
            throws IOException {

        this.tab = tab;
        this.out = new Output(objFile, listFile);
    }

    @Override
    public String getIOErrorMessage() {
        return MSG_PASS2_IO_ERROR;
    }

    @Override
    public String getASMErrorMessage() {
        return MSG_PASS2_ASM_ERROR;
    }

    /**
     * Fecha os arquivos de saída.
     * @throws IOException Caso haja um problema de IO ao fechar.
     */
    public void closeOutput() throws IOException {
        out.close();
    }

    @Override
    public boolean analyzeLine(ArrayList<String> symbols, String data)
            throws IOException {

        boolean result = false;

        String instrucao = null;
        String argumento = null;

        if (symbols.size() == 3) {
            // já foi testado se o simbolo foi resolvido
            instrucao = symbols.get(1);
            argumento = symbols.get(2);
        } else {
            instrucao = symbols.get(0);
            argumento = symbols.get(1);
        }

        if (InstructionsTable.getTable().instructionDefined(instrucao)) {
            // É uma instrução
            String enderecoArg = getArgumentValue(argumento);
            if (enderecoArg == null) {
                // endereço não definido
                System.out.println(MSG_PASS2_ASM_SYMBOL + SPACES
                        + argumento);
            } else if (Integer.parseInt(enderecoArg, 16) > LAST_VAL_ADDR) {
                // argumento da instrução precisa de um espaço maior do que
                // o disponível
                System.out.println(MSG_PASS2_ASM_SPACE);
            } else {
                // pegando o codigo da instrução
                int inst = InstructionsTable.getTable().getInstructionCode(instrucao);

                // escrevendo a instrução na saída
                putInstr(Integer.toHexString(inst), enderecoArg, data);
                result = true;
            }
        } else if (PseudoTable.getTable().pseudoDefined(instrucao)) {
            // É uma pseudoInstrução
            result = dataFromPseudo(PseudoTable.getTable().getPseudoCode(instrucao), argumento, data);
        }

        return result;
    }

    /**
     * Joga o comentário no arquivo List do mesmo jeito da entrada.
     *
     * @param data
     *            O texto de comentário.
     * @throws IOException
     *             Caso haja um problema de IO ao escrever.
     */
    @Override
    protected void processComment(String data) throws IOException {
        // joga na saída do arquivo List
        out.writeComment(data);
    }

    /**
     * Coloca uma instrução nos arquivos de saída.
     *
     * @param inst
     *            A instrução a ser escrita.
     * @param arg
     *            O argumento da instrução.
     * @param originalLine A linha original
     * @throws IOException
     *             Caso haja um problema de IO ao escrever.
     */
    private void putInstr(String inst, String arg, String originalLine) throws IOException {
        int nibble = 0;

        // escrevendo na saida.
        out.writeInstruction(locationCounter, nibble, inst, arg, originalLine);
        locationCounter += 2;
    }

    /**
     * Processa os dados de uma Pseudo-instrução e coloca-os na saída.
     *
     * @param pseudo
     *            A pseudo-instrução.
     * @param arg
     *            O argumento da pseudo-instrução.
     * @param originalLine A linha original.
     * @return Falso caso haja um erro na instrução.
     * @throws IOException
     *             Caso haja um problema de IO ao escrever.
     */
    private boolean dataFromPseudo(int pseudo, String arg, String originalLine) throws IOException {

        int nibble;

        switch (pseudo) {
            case PseudoTable.ORG:
                // origem
                try {
                    locationCounter = getDecNumber(arg);
                } catch (AssemblerException e) {
                    System.err.println(e.getMessage());
                    System.exit(-1);
                } catch (NumberFormatException e) {
                    System.err.println(e.getMessage());
                    System.exit(-1);
                }//
                out.writePseudo(originalLine);
                break;
            case PseudoTable.DC:
                // constante
                String addrArg = getArgumentValue(arg);
                if (addrArg == null) {
                    // argumento não resolvido
                    System.out.println(MSG_PASS2_ASM_SYMBOL + SPACES + arg);
                    return false;

                } else if (Integer.parseInt(addrArg, 16) > MAX_VALUE) {
                    // argumento da instrução precisa de um espaço maior do que
                    // o disponível ( o valor disponível é uma Word/2 bytes -> max = FFFF)
                    System.out.println(MSG_PASS2_ASM_SPACE);
                    return false;
                }

                nibble = 0;
                out.writeConstant(locationCounter, nibble, addrArg, originalLine);
                locationCounter += 2;
                break;
            case PseudoTable.MEM:

                /**
                 * TODO AULA 08 : dataFromPseudo
                 * Fazer a reserva explícita de memória  e não apenas incrementar o location counter com a quantidade
                 * de bytes informada no parâmetro arg ( quantidade de bytes a ser reservado  = 2*getDecNumber(arg) ) .
                 *
                 * Para cada Word reservada na memória preencher esta com um valor default ( 0x0000 ).
                 *
                 * OBS: A reserva explicita de memória é necessária para o ligamento correto
                 * de vários arquivos no montador Relocável.
                 *
                 * */
                //Apenas escrevendo a pseudo como comentário no arquivo de saída
                out.writePseudo(originalLine);
        }
        return true;
    }

    /**
     * Retorna o valor de um argumento.<br>
     *
     * @return Caso o argumento seja um número, retorna o número em hexadecimal.
     *         Caso o argumento seja um símbolo, retorna a posição. Retorna nulo
     *         caso o símbolo não tenha sido definido.
     */
    private String getArgumentValue(String arg) {

        if (tab.symbolInTable(arg)) {
            if (tab.definedSymbol(arg)) {
                return tab.getSymbolValue(arg);
            } else {
                return null;
            }
        } else {
            try {
                int decNumber = getDecNumber(arg);
                return Integer.toHexString(decNumber);
            } catch (AssemblerException e) {
                System.err.println(e.getMessage());
                System.exit(-1);
            } catch (NumberFormatException e) {
                System.err.println(e.getMessage());
                System.exit(-1);
            }//
            return null;
        }
    }
}
