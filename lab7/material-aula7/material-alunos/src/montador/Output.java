package montador;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe responsável por criar os arquivos de saída do montador.
 *
 * @author FLevy
 * @version 27.07.2006
 */
public class Output {

    //Variáveis internas de apoio
    private final String HEX_ZERO = "0";
    private final String HEX_4_ZEROS = "0000";
    private final String SPACES = " ";
    private final String TABS = "\t";
    private final String COMM = ";";
    private BufferedWriter out;
    private BufferedWriter list;

    public Output(String objFile, String listFile) throws IOException {
        out = new BufferedWriter(new FileWriter(objFile));
        list = new BufferedWriter(new FileWriter(listFile));
    }

    /**
     * Joga o comentário no arquivo List do mesmo jeito da entrada.
     *
     * @param data
     *            O texto de comentário.
     * @throws IOException
     *             Caso haja um problema de IO ao escrever.
     */
    public void writeComment(String data) throws IOException {
        list.write(data);
        list.newLine();
    }

    /**
     * Coloca uma instrução nos arquivos de saída.
     *
     * @param locationCounter
     *            O contador de instruções
     * @param nibble
     *            O nibble com as informações da relocação.
     * @param inst
     *            A instrução a ser escrita.
     * @param arg
     *            O argumento da instrução.
     * @param originalLine
     *            A linha original (para oarquivo list)
     * @throws IOException
     *             Caso haja um problema de IO ao escrever.
     */
    public void writeInstruction(int locationCounter, int nibble, String inst,
            String arg, String originalLine) throws IOException {
        String addr = Integer.toHexString(locationCounter);
        addr = HEX_4_ZEROS + addr;
        addr = addr.substring(addr.length() - 3, addr.length());

        // colocando o nibble no começo
        addr = Integer.toHexString(nibble) + addr;

        // arrumando o tamanho do argumento
        arg = HEX_4_ZEROS + arg;
        arg = arg.substring(arg.length() - 3, arg.length());

        out.write(addr + SPACES + inst + arg);
        out.newLine();
        list.write(addr + SPACES + inst + arg);
        list.write(TABS + TABS + COMM + TABS + originalLine);
        list.newLine();
    }

    /**
     * Coloca a pseudo instrução constante no arquivo de saída.
     *
     * @param locationCounter
     *            O contador de instruções.
     * @param nibble
     *            O nibble da pseudo instrução.
     * @param constant
     *            A constante.
     * @param originalLine
     *            A linha original.
     * @throws IOException
     *             Caso haja um problema de IO ao escrever.
     */
    public void writeConstant(int locationCounter, int nibble, String constant,
            String originalLine) throws IOException {
        String addr = Integer.toHexString(locationCounter);
        addr = HEX_4_ZEROS + addr;
        addr = addr.substring(addr.length() - 3, addr.length());

        // adicionando o nibble
        addr = Integer.toHexString(nibble) + addr;

        constant = HEX_4_ZEROS + constant;
        constant = constant.substring(constant.length() - 4, constant.length());

        // escrevendo na saida
        out.write(addr + SPACES + constant);
        out.newLine();
        list.write(addr + SPACES + constant);
        writePseudoOriginalLine(originalLine);
    }

    /**
     * Escreve a pseudo instrução no arquivo de saída. <br>
     * <b>Pré-condição </b>: não podem ser pseudo instruções de constante,
     * external point e entry point.
     *
     * @param originalLine
     *            A linha original a ser escrita na saída.
     * @throws IOException
     *             Caso haja um problema de IO ao escrever.
     */
    public void writePseudo(String originalLine) throws IOException {
        list.write(TABS + TABS);
        writePseudoOriginalLine(originalLine);
    }

    /**
     * Escreve um external point na saída.
     *
     * @param nibble
     *            O nibble da instrução.
     * @param address
     *            O endereço a ser escrito.
     * @param name
     *            O nome do external point.
     * @param originalLine
     *            A linha original.
     * @throws IOException
     *             Caso haja um problema de IO ao escrever.
     */
    public void writeExternalPoint(int nibble, String address, String name,
            String originalLine) throws IOException {
        String addr = HEX_4_ZEROS + address;
        addr = addr.substring(addr.length() - 3, addr.length());
        addr = Integer.toHexString(nibble) + addr;

        // external-point: escrevendo na saida
        out.write(addr + SPACES + HEX_4_ZEROS + SPACES + "; \""
                + name + "<\"");
        out.newLine();
        list.write(addr + SPACES + HEX_4_ZEROS + SPACES + "; \""
                + name + "<\"");
        writePseudoOriginalLine(originalLine);
    }

    /**
     * Escreve um entry point na saída.
     *
     * @param nibble
     *            O nibble.
     * @param address
     *            O endereço a ser escrito.
     * @param name
     *            O nome do entry point.
     * @param originalLine
     *            A linha original.
     * @throws IOException
     *             Caso haja um problema de IO ao escrever.
     */
    public void writeEntryPoint(int nibble, String address, String name,
            String originalLine) throws IOException {
        String addr = HEX_4_ZEROS + address;
        addr = addr.substring(addr.length() - 3, addr.length());

        // adicionando o nibble
        addr = Integer.toHexString(nibble) + addr;

        out.write(addr + SPACES + HEX_4_ZEROS + SPACES + "; \""
                + name + ">\"");
        out.newLine();
        list.write(addr + SPACES + HEX_4_ZEROS + SPACES + "; \""
                + name + ">\"");
        writePseudoOriginalLine(originalLine);
    }

    /**
     * Escreve a linha original da pseudo instrução no arquivo list.
     *
     * @param originalLine
     *            A linha original.
     */
    private void writePseudoOriginalLine(String originalLine)
            throws IOException {
        list.write(TABS + TABS + COMM + TABS + originalLine);
        list.newLine();
    }

    /**
     * Fecha os arquivos depois de terminar o uso.
     *
     * @throws IOException
     *             Caso haja um problema ao fechar os arquivos de saída.
     */
    public void close() throws IOException {
        out.close();
        list.close();
    }
}
