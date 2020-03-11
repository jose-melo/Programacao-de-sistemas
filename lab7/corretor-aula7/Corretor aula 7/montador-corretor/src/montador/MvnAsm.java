package montador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Classe principal do Montador.
 *
 * @author RRocha
 * @version 04.10.2005
 * @version 10.10.2005 (Refatoramento - FLevy)
 */
public class MvnAsm {

    //Variáveis de apoio
    public static final String NEWLINES = "\n";
    public static final String KEYBOARDERROR = "Erro na entrada de teclado.";
    public static final String INPUTMESSAGE = "Digite o nome do arquivo, com extensao diferente de 'mvn': ";
    //Extensões dos arquivos
    public static final String MVN_EXT = "mvn";
    public static final String LST_EXT = "lst";
    //Mensagens
    public final static String MSG_BAR = "===============================================================================";
    public final static String MSG_INIT = "              PCS2302/PCS2024  Montador da Maquina de Von Neumann";
    public final static String MSG_COPYRIGHT = "                 Versao 1.1 (a)2010  Todos os direitos reservados\n";
    public static final String MSG_HEADER = "**************************** A T E N C A O ********************************";
    public static final String MSG_USO_ASM = "\t Uso do ASM: 'java MvnAsm <arquivo-programa>' \n";
    public static final String MSG_USO_ASM_EXT = "\t (a extensao dos arquivos gerados sera 'mvn' e 'lst')";
    public static final String MSG_USO_ASM_EXT_MUDAR = "\t o seu arquivo sera apagado pelo montador, mude a extensao ...";

    /**
     * Executa o programa do montador. <br>
     * Uso: MvnAsm source_file object_file list_file
     *
     * @param args
     *            O primeiro argumento deve ser o arquivo o qual contém o código
     *            em linguagem de montador.Caso não seja fornecido argumentos, o
     *            programa solicitará o nome do arquivo. <br>
     *            O extensão do arquivo não pode ser igual à extensão do
     *            montador
     */
    public static void main(String[] args) {
        String arg = "";
        String name;

        // colocando o header
        System.out.println(MSG_BAR);
        System.out.println(MSG_INIT);
        System.out.println(MSG_COPYRIGHT);

        if (args.length >= 1) {
            arg = args[0];
        } else {
            // não tem argumentos - solicita o nome do arquivo
            System.out.println(INPUTMESSAGE);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    System.in));
            arg = lerStringTeclado(in);
            if (arg == null || arg.equals("")) {
                // o usuário não digitou nada
                System.exit(1);
            }
            System.out.println(); // pula uma linha
        }
        if (arg.indexOf(".") != -1) {
            name = arg.substring(0, arg.length() - 3);
            String ext = arg.substring(arg.length() - 3, arg.length());
            // verificando se a extensao e igual a uma das extensoes de saida
            if (ext.toLowerCase().equals(MVN_EXT) || ext.toLowerCase().equals(LST_EXT)) {
                System.out.println(MSG_HEADER);
                System.out.println(MSG_USO_ASM + MSG_USO_ASM_EXT
                        + NEWLINES + MSG_USO_ASM_EXT_MUDAR);
                System.out.println();
                System.exit(1);
            }
        } else {
            // arquivo sem extensão
            name = arg;
        }

        // Iniciando o uso do assembler
        AbstractAssembler asm = new TwoPassAssembler(arg, name + MVN_EXT,
                name + LST_EXT);
        if (!asm.assemble()) {
            // não conseguiu montar - sai com erro
            System.exit(1);
        }
    }

    /**
     * Método auxiliar para obter uma String entrada pelo teclado.
     *
     * @return Se a entrada for inválida retorna uma string nula. Se não,
     *         retorna o valor lido.
     */
    private static String lerStringTeclado(BufferedReader in) {
        String s = "";
        try {
            s = in.readLine();
        } catch (IOException ex) {
            System.out.println(KEYBOARDERROR);
        }
        return s;
    }
}
