package linker;

/**
 * Linker abstrato.<br>
 * A partir dessa base podem ser criados diversos linkers.
 * @author FLevy
 * @version 27.10.2006
 */
public abstract class AbstractLinker {

    protected String[] inputFile;
    protected String outputFile;

    public AbstractLinker(String[] inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public String[] getInputFile() {
        return inputFile;
    }

    /**
     * Faz a ligação dos códigos de entrada e gera o arquivo de saída.
     * @return
     */
    public abstract boolean link();
}
