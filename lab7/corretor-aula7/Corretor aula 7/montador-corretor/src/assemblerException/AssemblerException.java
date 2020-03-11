package assemblerException;

/**
 * Classe para representar exceptions para o Montador.
 *
 * @author Tiago
 * @version 01.02.2010
 *
 * */
public class AssemblerException extends Exception {

    private static final long serialVersionUID = 1L;

    public AssemblerException(String exceptionDescription) {
        super(exceptionDescription);
    }

}
