package linker;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Representa um passo do ligador.
 * 
 * @author FLevy
 * @version 23.10.2006
 * @version 01.11.2010 : Alterações na lógica do nibble de acordo com os slides (Tiago)
 */
public abstract class Pass {

    protected String linkErrorMessage = null;
    protected String ioErrorMessage = null;

    // Mensagens do programa
    private static final String MSG_NUM_TOKENS = "Linha com numero de tokens inadequado.";
    private static final String MSG_NIBBLE = "Linha com nibble inadequado.";
    private static final String MSG_ENDERECO = "Linha com endereco inadequado.";
    private static final String MSG_CODIGO = "Linha com codigo inadequado.";
    private static final String MSG_ENDERECO_SIMBOLICO = "Linha com sintaxe incorreta para endereco simbolico.";
    private static final String MSG_SIMBOLO = "Linha com simbolo incorreto.";

    /**
     * Processa uma linha de código.
     *
     * @param nibble O nibble do endereço da linha.
     * @param address O endereço da linha (sem o nibble).
     * @param code O código da linha.
     * @param currentFile O arquivo atual que está sendo processado.
     * @return Verdadeiro caso a análise teve sucesso, falso caso contrário.
     * @exception Caso tenha ocorrido algum problema de IO.
     */
    protected abstract boolean processCode(int nibble, String address, String code, String currentFile)
            throws IOException;

    /**
     * Processa uma linha de endereço simbólico.
     *
     * @param nibble O nibble do endereço da linha.
     * @param address O endereço referente ao símbolo (sem nibble).
     * @param symbol O símbolo em si.
     * @param currentFile O arquivo atual que está sendo processado.
     * @param originalLine uma String contendo a linha original do código
     * @return Verdadeiro caso a análise teve sucesso, falso caso contrário.
     * @throws IOException
     *             Caso tenha ocorrido algum problema de IO.
     */
    protected abstract boolean processSymbolicalAddress(int nibble, String address, String symbol, String currentFile, String originalLine) throws IOException;

    /**
     * Finaliza o arquivo lido (pode haver um próximo arquivo).
     */
    protected abstract void fileEnd();

    /**
     *
     * Verifica se é entry-point a partir do nibble. <br />
     * Tanto no caso de pseudo-instrução quanto no caso de instrução
     * uma EntryPoint é indicada pelo segundo bit do Nibble.
     * <br />
     * <b>Nibble: 00X0 (X é absoluto ou relocável)</b>
     *
     * @param nibble O nibble a ser verificado.
     * @return Verdadeiro caso for entry-point e falso caso contrário.
     */
    protected boolean isEntryPoint(int nibble) {
        if (nibble == 0 || nibble == 2)
        	return true;
        return false;
    }

    /**
     * Verifica se é um entry-point relocável a partir do nibble. <br />
     *
     * <b>Nibble: 0010</b>
     *
     * @param nibble O nibble a ser verificado.
     * @return Verdadeiro caso for entry-point relocável e falso caso contrário.
     */
    protected boolean isRelocableEntryPoint(int nibble) {
        
    	if (nibble == 2)
    		return true;
        return false;
    }

    /**
     * Verifica se o nibble representa um endereço relocável. <br />
     *
     * <b>Nibble: 1XXX</b>
     *
     * @param nibble O nibble a ser verificado.
     * @return Verdadeiro se o endereço for relocável.
     */
    protected boolean isRelocable(int nibble) {

    	if(nibble == 8 || nibble == 13 || nibble == 10)
    		return true;
    	
        return false;
    }

    /**
     * Verifica se o nibble representa um argumento com endereco resolvido. <br />
     *
     * Nibble: 
     *
     * @param nibble  O nibble a ser verificado.
     * @return Verdadeiro se o endereço for resolvido.
     */
    protected boolean isResolved(int nibble) {

    	if(nibble == 0 || nibble == 8 || nibble == 2 || nibble == 10)
    		return true;
        return false;
    }

    /**
     * Verifica se o nibble representa um argumento relocável.
     *
     * @param nibble
     *            O nibble a ser verificado.
     * @return Verdadeiro se o argumento for relocável.
     */
    protected boolean isArgumentRelocable(int nibble) {
        
    	if(nibble == 2 || nibble == 10)
    		return true;
    	return false;
    }

    /**
     * Verifica se é uma Pseudo-Instrução de símbolo externo. <br>
     * Nibble: 0100
     *
     * @param nibble
     *            O nibble a ser verificado.
     * @return Verdadeiro caso for external e falso caso contrário.
     */
    protected boolean isExternalPseudoInstruction(int nibble) {

    	if(nibble == 4)
    		return true;
    	
        return false;
    }//

    /**
     *
     * Verifica se é uma instrução que utiliza um símbolo externo como argumento. <br>
     * Símbolo externo no argumento é indicado no último bit. (XXX1)
     *
     *
     */
    protected boolean instructionWithExternal(int nibble) {
        
    	if(nibble == 5 || nibble == 13)
    		return true;
    	
        return false;
    }//

    /**
     * Retorna a mensagem de erro de IO para esse passo.
     */
    public String getIOErrorMessage() {
        return ioErrorMessage;
    }

    /**
     * Retorna a mensagem de erro do montador para esse passo.
     */
    public String getLinkErrorMessage() {
        return linkErrorMessage;
    }

    /**
     * Analisa os dados e os processa, passando para as demais funções os dados
     * já processados.<br>
     *
     * @param in Arquivo com o código em linguagem de montador.
     * @param currentFile O arquivo corrente que está sendo processado.
     * @return -1 caso tenha ocorrido um erro de IO, 0 caso não haja erro ou o
     *         número da linha com erro.
     */
    public int tokenizeData(BufferedReader in, String currentFile) {
        String data, temp, address, symbol, code;
        boolean symbolType, analiseOk;
        StringTokenizer tokens;
        int numLinha = 0;
        int numTokens, nibble;

        try {
            data = in.readLine();
            numLinha++;

            while (data != null) {
                tokens = new StringTokenizer(data);
                numTokens = tokens.countTokens();

                if (numTokens != 2 && numTokens != 4) {
                    // número de tokens errado
                    linkErrorMessage = MSG_NUM_TOKENS;
                    return numLinha;
                }

                // pegando o nibble e o endereço
                temp = tokens.nextToken();
         
                if (temp.length() != 4) {
                    // endereço inadequado
                    linkErrorMessage = MSG_ENDERECO;
                    return numLinha;
                }
                try {
                    nibble = Integer.parseInt(temp.charAt(0) + "", 16);
                } catch (Exception e) {
                    linkErrorMessage = MSG_NIBBLE;
                    return numLinha;
                }
                address = temp.substring(1);

                if (numTokens == 2) {
                    code = tokens.nextToken();
                    if (code.length() != 4) {
                        // código inválido
                        linkErrorMessage = MSG_CODIGO;
                        return numLinha;
                    }
                    analiseOk = processCode(nibble, address, code, currentFile);
                    if (!analiseOk) {
                        return numLinha;
                    }
                } else {
                    // numTokens == 4
                    if (!tokens.nextToken().equals("0000") || !tokens.nextElement().equals(";")) {
                        // sintaxe errada
                        linkErrorMessage = MSG_ENDERECO_SIMBOLICO;
                        return numLinha;
                    }

                    temp = tokens.nextToken();
                    if (temp.length() <= 3 || temp.charAt(0) != '"' || temp.charAt(temp.length() - 1) != '"' || (temp.charAt(temp.length() - 2) != '>' && temp.charAt(temp.length() - 2) != '<')) {
                        // simbolo incorreto
                        linkErrorMessage = MSG_SIMBOLO;
                        return numLinha;
                    }

                    symbolType = (temp.charAt(temp.length() - 2) == '>');
                    if (symbolType && !isEntryPoint(nibble) || !symbolType && !isExternalPseudoInstruction(nibble)) {
                        linkErrorMessage = MSG_SIMBOLO;
                        return numLinha;
                    }

                    symbol = temp.substring(1, temp.length() - 2);

                    analiseOk = processSymbolicalAddress(nibble, address, symbol, currentFile, data);

                    if (!analiseOk) {
                        return numLinha;
                    }
                }

                data = in.readLine();
                numLinha++;
            }

            // avisando o passo que esse foi o fim do arquivo
            fileEnd();
            return 0; // ok
        } catch (IOException e) {
            return numLinha;
        }
    }
}
