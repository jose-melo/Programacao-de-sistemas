package linker;

import java.util.ArrayList;

/**
 * Ponto de entrada do Linker.
 * 
 * @author FLevy
 * @version 26.10.2006
 * @version 01.01.2010 : Tiago 
 */
public class MvnLinker {
    // Mensagens do programa

    private static final String MSG_USO_LINKER = "\t Uso do Linker:\njava linker.MvnLinker <arquivo-objeto1> <arquivo-objeto2> ... <arquivo-objetoN> -s <arquivo-saida>\n";
    private static final String MSG_FALTA_SAIDA = "E preciso determinar o arquivo de saida.";

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println(MSG_USO_LINKER);
            System.exit(1);
        }

        ArrayList<String> entradas = new ArrayList<String>();
        String saida = null;

        // processamento dos argumentos
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-s")) {
                // a próxima String é o arquivo de saída
                i++;
                if (i < args.length) {
                    saida = args[i];
                } else {
                    // falta o arquivo de saida
                    System.out.println(MSG_FALTA_SAIDA);
                    System.out.println(MSG_USO_LINKER);
                    System.exit(1);
                }
            } else {
                entradas.add(args[i]);
            }
        }

        if (saida != null && entradas.size() > 0) {
            // chamando o linker para processar tudo
            String[] vetorEntradas = new String[entradas.size()];
            for (int i = 0; i < entradas.size(); i++) {
                vetorEntradas[i] = (String) entradas.get(i);
            }

            AbstractLinker linker = new TwoPassLinker(vetorEntradas, saida);
            if (!linker.link()) {
                System.exit(1);
            }
        } else {
            // chamada incorreta do Linker
            System.out.println(MSG_USO_LINKER);
            System.exit(1);
        }
    }
}
