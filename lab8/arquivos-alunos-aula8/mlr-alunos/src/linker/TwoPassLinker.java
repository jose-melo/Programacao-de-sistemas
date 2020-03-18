package linker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Representa um linker de dois passos. 
 */
public class TwoPassLinker extends AbstractLinker {

    // Mensagens do programa
    private static final String MSG_ARQ_ERRO = "Erro no arquivo: ";
    private static final String MSG_LINHA_ERRO = "Linha: ";
    private static final String MSG_PASS2_OK = "Arquivo gerado com sucesso.";
    private static final String MSG_PASS2_IO_ERROR = "Erro ao escrever no arquivo de saida.";

    public TwoPassLinker(String[] inputFile, String outputFile) {
        super(inputFile, outputFile);
    }

    /**
     * Realiza o link dos diversos arquivos, processando os passos.
     * @return Verdadeiro se a liga??o tenha obtido sucesso.
     */
    public boolean link() {
        Pass passo1 = new Pass1();
        Pass passo2 = null;
        if (executePass(passo1)) {
            try {
                // passo 1 com sucesso, executa o passo 2
                passo2 = new Pass2(((Pass1) passo1).getSymbolTable(), outputFile);
                if (executePass(passo2)) {
                    System.out.println(MSG_PASS2_OK);
                    return true;
                }
            } catch (IOException e) {
                System.out.println(MSG_PASS2_IO_ERROR);                
                return false;
            } finally {
                // para fechar o IO que estava aberto
                try {
                    if (passo2 != null) {
                        ((Pass2) passo2).closeOutput();
                    }
                } catch (IOException unhandled) {
                }
            }
        }
        return false;
    }

    /**
     * Execu??o de um passo do Linker.
     * @param pass O passo a ser executado.
     * @return Verdadeiro caso a execu??o tenha obtido sucesso, falso caso
     *         contr?rio.
     */
    private boolean executePass(Pass pass) {
        // Cria um buffer de leitura para o arquivo texto
        BufferedReader filIn = null;
        try {
            boolean resultado = false;
            for (int i = 0; i < inputFile.length; i++) {
                filIn = new BufferedReader(new FileReader(inputFile[i]));
                int linhaComErro = pass.tokenizeData(filIn, inputFile[i]);

                if (linhaComErro == 0) {
                    // sem erros no passo
                    resultado = true;
                } else if (linhaComErro == -1) {
                    // erro de IO
                    System.out.println(MSG_ARQ_ERRO + inputFile[i]);
                    System.out.println(pass.getIOErrorMessage());

                    filIn.close();
                    return false;
                } else {
                    System.out.println(MSG_ARQ_ERRO + inputFile[i]);
                    System.out.print(MSG_LINHA_ERRO + linhaComErro + ". ");
                    System.out.println(pass.getLinkErrorMessage());

                    filIn.close();
                    return false;
                }

                filIn.close();
            }
            return resultado;
        } catch (Exception e) {
            System.out.println(pass.getIOErrorMessage());
            e.printStackTrace();
            try {
                if (filIn != null) {
                    filIn.close();
                }
            } catch (Exception unhadled) {
            }
            return false;
        }
    }
}
