package montador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Montador de dois passos. No primeiro passo é criada a tabela de símbolo e no
 * segundo passo é gerado o código de máquina.
 *
 * @author RRocha
 * @version 04.10.2005
 * @version 10.10.2005 (Refatoramento - FLevy)
 */
public class TwoPassAssembler extends AbstractAssembler {

    private String objFile;
    private String listFile;

    /**
     * Construtor do montador.
     *
     * @param fileName
     *            Nome do arquivo a passar pelo montador.
     * @param objFileName
     *            Nome do arquivo de saída que conterá o código.
     * @param listFileName
     *            Nome do arquivo de saída que conterá o código e a listagem.
     */
    public TwoPassAssembler(String fileName, String objFileName,
            String listFileName) {
        super(fileName);
        objFile = objFileName;
        listFile = listFileName;
    }

    /**
     * Monta o arquivo definido, passando pelos dois passos do montador.
     *
     * @return Verdadeiro caso a montagem tenha obtido sucesso, falso caso
     *         contrário.
     */
    @Override
    public boolean assemble() {
        Pass passo1 = new Pass1();
        Pass passo2 = null;
        if (executePass(passo1)) {
            try {
                // passo 1 com sucesso
                // executa o passo 2
                passo2 = new Pass2(((Pass1) passo1).getSymbolTable(), this.objFile, this.listFile);
                if (executePass(passo2)) {
                    System.out.println(MSG_PASS2_OK);
                    return true;
                }
            } catch (IOException e) {
                System.out.println(MSG_PASS2_IO_ERROR);
                e.printStackTrace();
                return false;
            } finally {
                // para fechar o IO que estava aberto
                try {
                    if (passo2 != null) {
                        ((Pass2) passo2).closeOutput();
                    }
                } catch (IOException unhandled) {
                    System.out.println(MSG_PASS2_IO_ERROR);
                    unhandled.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Execução de um passo no código.
     *
     * @param pass
     *            O passo a ser executado.
     * @return Verdadeiro caso a execução tenha obtido sucesso, falso caso
     *         contrário.
     */
    private boolean executePass(Pass pass) {
        // Cria um buffer de leitura para o arquivo texto
        BufferedReader filIn;
        try {
            filIn = new BufferedReader(new FileReader(super.getInFile()));

            int linhaComErro = pass.tokenizeData(filIn);
            boolean resultado = false;

            if (linhaComErro == 0) {
                // sem erros no passo
                resultado = true;
            } else if (linhaComErro == -1) {
                // erro de IO
                System.out.println(pass.getIOErrorMessage());
            } else {
                System.out.println(pass.getASMErrorMessage() + linhaComErro);
            }

            filIn.close();
            return resultado;
        } catch (Exception e) {
            System.out.println(pass.getIOErrorMessage());
            e.printStackTrace();
            return false;
        }
    }
}
