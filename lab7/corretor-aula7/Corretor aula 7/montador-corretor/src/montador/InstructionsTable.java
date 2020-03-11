package montador;

import java.util.HashMap;
import java.util.Map;

/**
 * Tabela de instruções.<br>
 * As instruções são as representações dos comandos existentes no hardware o
 * qual se deseja gerar o código.<br>
 * Para essa classe é um sigleton (apenas uma instância dela é acessível).
 *
 * @author RRocha
 * @version 04.04.2005
 * @version 10.10.2005 (refatoramento - FLevy)
 * @version 28.01.2010 (refatoramento - Tiago) : conversão para generics
 */
public final class InstructionsTable {
    // Códigos das instruções

    private static final int JP = 0;
    private final int JZ = 1;
    private final int JN = 2;
    private final int LV = 3;
    private static final int ADD = 4;
    private static final int SUB = 5;
    private static final int MUL = 6;
    private static final int DIV = 7;
    private static final int LOAD = 8;
    private static final int MOVE = 9;
    private static final int CALL = 10;
    private static final int RET = 11;
    private static final int HALT = 12;
    private static final int IN = 13;
    private static final int OUT = 14;
    private static final int OS = 15;
    // Strings das instruções
    private static final String JP_STR = "JP";
    private static final String JZ_STR = "JZ";
    private static final String JN_STR = "JN";
    private static final String LV_STR = "LV";
    private static final String ADD_STR = "+";
    private static final String SUB_STR = "-";
    private static final String MUL_STR = "*";
    private static final String DIV_STR = "/";
    private static final String LOAD_STR = "LD";
    private static final String MOVE_STR = "MM";
    private static final String CALL_STR = "SC";
    private static final String RET_STR = "RS";
    private static final String HALT_STR = "HM";
    private static final String IN_STR = "GD";
    private static final String OUT_STR = "PD";
    private static final String OS_STR = "OS";
    // Singleton
    private static InstructionsTable it = null;
    // Variável local
    private Map<String, String> instr;

    /**
     * Construtor de uma tabela de instruções.<br>
     * Para haver apenas uma instância da tabela de instruções, o construtor é
     * protegido.
     */
    protected InstructionsTable() {

        instr = new HashMap<String, String>();
        instr.put(JP_STR, Integer.toString(JP, 10));
        instr.put(JZ_STR, Integer.toString(JZ, 10));
        instr.put(JN_STR, Integer.toString(JN, 10));
        instr.put(LV_STR, Integer.toString(LV, 10));
        instr.put(ADD_STR, Integer.toString(ADD, 10));
        instr.put(SUB_STR, Integer.toString(SUB, 10));
        instr.put(MUL_STR, Integer.toString(MUL, 10));
        instr.put(DIV_STR, Integer.toString(DIV, 10));
        instr.put(LOAD_STR, Integer.toString(LOAD, 10));
        instr.put(MOVE_STR, Integer.toString(MOVE, 10));
        instr.put(CALL_STR, Integer.toString(CALL, 10));
        instr.put(RET_STR, Integer.toString(RET, 10));
        instr.put(HALT_STR, Integer.toString(HALT, 10));
        instr.put(IN_STR, Integer.toString(IN, 10));
        instr.put(OUT_STR, Integer.toString(OUT, 10));
        instr.put(OS_STR, Integer.toString(OS, 10));
    }

    /**
     * Verifica se a instrução é definida.
     *
     * @param instruction
     *            A instrução a ser verificada (independente se em letras
     *            maiúsculas ou minúsculas).
     * @return Verdadeiro caso a instrução seja disponível, falso caso
     *         contrário.
     */
    public boolean instructionDefined(String instruction) {
        return instr.containsKey(instruction.toUpperCase());
    }

    /**
     * Obtêm o código para uma determinada instrução.
     *
     * @param instruction
     *            A instrução a qual se deseja o código (independente se
     *            em letras maiúsculas ou minúsculas).
     * @return O código.
     */
    public int getInstructionCode(String instruction) {
        return Integer.parseInt(instr.get(instruction.toUpperCase()));
    }

    /**
     * Obtêm a instância disponível da tabela de instruções.
     *
     * @return A tabela de instruções.
     */
    public static InstructionsTable getTable() {
        if (it == null) {
            it = new InstructionsTable();
        }
        return it;
    }
}
