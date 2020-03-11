package montador;

/**
 * Montador abstrato. <br>
 * A partir dessa abstração podem ser implementados diversos montadores, como um
 * montador de um passo ou um montador de dois passos.
 *
 * @author RRocha
 * @version 04.10.2005
 */
public abstract class AbstractAssembler {

    private String inFile = "";
    //Mensagens
    protected final String MSG_PASS2_OK = "Montador finalizou corretamente, arquivos gerados.";
    protected final String MSG_PASS2_IO_ERROR = "Erro de I/O no passo 2, verifique o arquivo";

    /**
     * Cria um montador abstrato.
     *
     * @param fileName
     *            O arquivo a passar pelo montador.
     */
    public AbstractAssembler(String fileName) {
        this.inFile = fileName;
    }

    /**
     * Obtêm o arquivo a passar pelo montador.
     *
     * @return O arquivo a ser montado.
     */
    public String getInFile() {
        return inFile;
    }

    /**
     * Define o arquivo a passar pelo montador.
     *
     * @param inFile
     *            O arquivo.
     */
    public void setInFile(String inFile) {
        this.inFile = inFile;
    }

    /**
     * Monta o arquivo definido.
     *
     * @return Verdadeiro caso tenha sido possivel montar, falso caso contrário.
     */
    public abstract boolean assemble();
}
