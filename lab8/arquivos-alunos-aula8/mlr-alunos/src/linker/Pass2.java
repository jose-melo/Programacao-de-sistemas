package linker;

import java.io.IOException;
import java.util.StringTokenizer;

import mvn.util.LinkerSymbolTable;

/**
 * Passo 2 do ligador.<br>
 * Nesse passo é gerado o código objeto a partir da tabela
 * de símbolos obtida.
 * @author FLevy
 * @version 23.10.2006
 * Preparação do arquivo para alunos - PSMuniz 1.11.2006
 * @version 01.01.2010 : atualização da classe de acordo com a definição dos slides. (Tiago)
 */
public class Pass2 extends Pass {

    /**Gerencia o arquivo de saída*/
    private Output out;
    /**Tabela de símbolos utilizada pelo Linker*/
    private LinkerSymbolTable symbolTable;
    /**Indica o endereçamento corrente da parte relocável do código. */
    private int relativeLocationCounter;
    /**A base de relocação a ser considerada no código. */
    private int base;

    /**Contador de variáveis externas que não foram resolvidas*/
    private int externalCounter = 0;


    public Pass2(LinkerSymbolTable symbolTable, String objFile) throws IOException {
        out = new Output(objFile);
        this.symbolTable = symbolTable;
        relativeLocationCounter = 0;
        base = 0;
    }

    /**
     * Processa uma linha de código.
     *
     * @param nibble O nibble do endereço da linha.
     * @param address O endereço da linha (sem o nibble).
     * @param code O código da linha.
     * @param currentFile O arquivo atual que está sendo processado.
     * @return Verdadeiro caso a análise teve sucesso, falso caso contrario.
     * @exception Caso tenha ocorrido algum problema de IO.
     */
    protected boolean processCode(int nibble, String address, String code, String currentFile)
            throws IOException {
        /*
         * TODO: processCode
         *
         * Aqui, deve-se gerar o código objeto a partir da tabela de simbolos (ST).
         * Deve-se avaliar as combinações apropriadas do nibble. Se houver pendência (relocável
         * ou absoluta), ela deve ter ser resolvida e seu valor inserido na ST.
         * O código resolvido deve ser enviado para a saída.
         *
         */
    	int addressInt;
    	int codeInt;
    	boolean isResolved = true;
    	String temp;
    	

  		addressInt = Integer.parseInt(address, 16);
    	
    	codeInt = Integer.parseInt(code, 16);    	
    	
  	   	if(nibble == 5 || nibble == 13) {
    		//Pendência externa
    		temp = symbolTable.getAddressByCode(currentFile, Integer.parseInt(code.substring(1)));
    		if(temp == null)
    			isResolved = false;
    		else
    			address = temp;
    	}else if(isArgumentRelocable(nibble)) {
	  		codeInt += base;
	      	code = Integer.toHexString(codeInt);
	  	}  	
    	
	  	 if(isRelocable(nibble)) {
	  		addressInt += base;
	  		address = Integer.toHexString(addressInt);		
	  	}
	  
  	   	out.write(addressInt, code, isRelocable(nibble), isArgumentRelocable(nibble), isResolved);
    	
    	
        return false;

    }//method

    protected boolean processSymbolicalAddress(int nibble, String address, String symbol, String currentFile, String originalLine)
            throws IOException {
        
        /**
         * TODO: processSymbolicalAddress
         * Tratamento do Endereçamento simbólico. 
         * Caso EntryPoint: escreve no arquivo de saída
         * Caso External: se resolvido ignora, caso contrário insere na tabela o arquivo com um novo endereçamento "virtual"
         *                  e escreve o external no arquivo de saída.
         * 
         *          
         */

        //Se for símbolo exportável: gero no arquivo de saída as informações a respeito dele
        //...
        //...
    	int addressInt;
    	
	  	addressInt = Integer.parseInt(address, 16);
    	
    	if(isRelocable(nibble)) {
 	  		addressInt += base;
 	  		address = Integer.toHexString(addressInt);		
 	  	}
    	
    	if(isEntryPoint(nibble)) {
    		out.write(addressInt, "0000", isRelocable(nibble), isArgumentRelocable(nibble), true);
    		
    	}else if(isExternalPseudoInstruction(nibble)) {
    		
    		if(nibble == 4) {
    			out.writeExternal(Integer.toString(nibble), externalCounter, originalLine);
    			externalCounter += 1;
    		}
    	}
    	
        return false;
    }

    /**
     * Finaliza o arquivo lido (pode haver um próximo arquivo).
     */
    protected void fileEnd() {
        /*
         * TODO: fileEnd()
         * Quando há mudança de arquivo, deve-se atualizar a base e o relativeLocationCounter!
         * */
    	base += relativeLocationCounter;
    	relativeLocationCounter = 0;
    	externalCounter = 0;
    	
    }

    public void closeOutput() throws IOException {
        out.close();
    }
}
