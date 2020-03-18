package linker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Classe responsável por criar os arquivos de saída do linker.
 * 
 * @author FLevy
 * @version 27.07.2006
 * @version 01.01.2010 : Tiago 
 */
public class Output {

    private BufferedWriter out;
    private static final String ASM_SPACE = " ";

    public Output(String outFile) throws IOException {
        out = new BufferedWriter(new FileWriter(outFile));
    }

    /**
     * Imprime no arquivo de saída a instrução, calculando o nibble apropriado.
     *
     * @param address
     *            O endereço (sem o nibble a ser escrito)
     * @param operation
     *            A operação a ser escrita (já resolvida).
     * @param isRelocable
     *            Verdadeiro se o endereco for relocável.
     * @param isArgumentRelocable
     *            Verdadeiro se o argumento for relocável.
     * @param isResolved informa se o argumento foi resolvido ou não.
     *
     */
    public void write(int address, String operation, boolean isRelocable,
            boolean isArgumentRelocable, boolean isResolved)
            throws IOException {
        int nibble = 0;
        String endereco;

        if (isRelocable) {
            nibble += 8;
        }
        if (isArgumentRelocable) {
            nibble += 2;
        }
        if (!isResolved) {
            nibble += 5;
        }

        endereco = "0000" + Integer.toHexString(address);
        endereco = endereco.substring(endereco.length() - 3, endereco.length());
        endereco = Integer.toHexString(nibble) + endereco;

        String operacao = "0000" + operation;
        operacao = operacao.substring(operacao.length() - 4, operacao.length());
        
        out.write(endereco + ASM_SPACE + operacao);
        out.newLine();
    }

    /**
     *
     * Grava na saída uma variável externa (importada) que ainda não foi resolvida em nenhum dos módulos
     * tratadas pelo ligador.
     *
     * Exemplo:
     *
     * Existência da variável externa não resolvida SAIDA nos módulos que estão sendo ligados.
     * Sendo ela a primeira variável externa que não foi resolvida, seu endereço virtual será 0 (a próxima variável terá endereço 1, e assim sucessivamente)
     *
     *
     * @param nibble o valor (em hexadecimal) do primeiro nibble
     * @param virtualAddress o endereço (definido pela quantidade de variáveis externas já utilizadas) para a variável externa
     * @param originalLine a linha original da definição da pseudo-instrução de importação de variável
     *
     */
    public void writeExternal(String nibble, int virtualAddress, String originalLine) {
        String newAddress = "0000" + Integer.toHexString(virtualAddress);
        newAddress = newAddress.substring(newAddress.length() - 3, newAddress.length());

        //Retirando apenas a parte útil
        StringTokenizer tokens = new StringTokenizer(originalLine);
        tokens.nextToken();//retira endereço antigo
        tokens.nextToken(); // retira valor antigo
        StringBuilder newComment = new StringBuilder();

        while (tokens.hasMoreTokens()) {
            newComment.append(tokens.nextToken());
            newComment.append(ASM_SPACE);
        }//while

        newAddress = nibble + newAddress;

        try {
            out.write(newAddress + ASM_SPACE + "0000" + ASM_SPACE + newComment.toString());
            out.newLine();
        } catch (Exception e) {
            System.err.println("Erro na escrita de variável externa: " + originalLine);
            System.exit(-1);
        }
    }//

    public void close() throws IOException {
        out.close();
    }
}
