package montador;

import java.util.HashMap;
import java.util.Map;

/**
 * Tabela de pseudo-instruções.<br>
 * As pseudo-instruções são instruções definidas pelo montador para facilitar a
 * programação.<br>
 * Para essa classe é um sigleton (apenas uma instância dela é acessível).
 *
 * @author RRocha
 * @version 04.04.2005
 * @version 10.10.2005 (refatoramento - FLevy)
 * @version 30.10.2005 (novas instruções para o montador relocável - FLevy)
 * @version 21.09.2006 (apenas montador absoluto - FLevy)
 * @version 28.01.2010 (refatoramento - Tiago) : conversão para generics
 */
public final class PseudoTable {

    // Código das pseudo-instruções
    /**Origem absoluta*/
    public static final int ORG = 0;
    /**Definição de constante*/
    public static final int DC = 1;
    /**Fim físico do arquivo fonte*/
    public static final int END = 2;
    /**Reserva de bloco de memória*/
    public static final int MEM = 3;

    // Strings das pseudo-instruçõe
    public static final String ORG_STR = "@";
    public static final String DC_STR = "K";
    public static final String END_STR = "#";
    public static final String MEM_STR = "$";


    // Singleton
    private static PseudoTable pt;
    // Variável local - Tabela de pseudo
    private Map<String, String> pseudos;

    /**
     * Construtor de uma tabela de pseudo-instruções.<br>
     * Para haver apenas uma instância da tabela de pseudo-instruções, o
     * construtor é protegido.
     */
    protected PseudoTable() {
        pseudos = new HashMap<String, String>();
        pseudos.put(ORG_STR, Integer.toString(ORG, 10));
        pseudos.put(DC_STR, Integer.toString(DC, 10));
        pseudos.put(END_STR, Integer.toString(END, 10));
        pseudos.put(MEM_STR, Integer.toString(MEM, 10));
    }

    /**
     * Obtêm o código da pseudo-instrução.
     *
     * @param pseudo
     *            A pseudo-instrução a qual se deseja saber o código
     *            (independente se em letras maiúsculas ou minúsculas)..
     * @return O código.
     */
    public int getPseudoCode(String pseudo) {
        return Integer.parseInt(pseudos.get(pseudo.toUpperCase()));
    }

    /**
     * Verifica se a pseudo-instrução está definida.
     *
     * @param pseudo
     *            A pseudo-instrução a ser verifida (independente se em letras
     *            maiúsculas ou minúsculas).
     * @return Verdadeiro caso a instrução seja definida, falso caso contrário.
     */
    public boolean pseudoDefined(String pseudo) {
        return pseudos.containsKey(pseudo.toUpperCase());
    }

    /**
     * Obtêm a instância disponível da tabela de pseudo-instruções.
     *
     * @return A tabela de pseudo-instruções.
     */
    public static PseudoTable getTable() {
        if (pt == null) {
            pt = new PseudoTable();
        }
        return pt;
    }
}
