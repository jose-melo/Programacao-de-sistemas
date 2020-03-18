package linker;

import java.io.IOException;

import mvn.util.LinkerSymbolTable;

/**
 * Passo 1 do ligador.<br>
 * Nesse passo é criada a tabela de símbolos do linker, passando por todos os
 * arquivos.<br>
 * Por questões de simplicidade, existem algumas simplificações no linker:
 * - cada arquivo apresenta uma e apenas uma origem relocável;
 * - os conflitos de código absoluto não são verificados;
 * @author FLevy
 * @version 23.10.2006 
 * Preparação do arquivo para alunos - PSMuniz 1.11.2006
 * @version 01.01.2010 : atualização da classe de acordo com a definição dos slides (Tiago)
 */
public class Pass1 extends Pass {

    /**A tabela de símbolos para o linker*/
    private LinkerSymbolTable symbolTable;
    /**A posição relocável anterior*/
    private int relativeLocationCounter;
    /**A base de relocação a ser considerada no código*/
    private int base;
    /**Numera as diferentes variáveis externas existentes num arquivo*/
    private int externalCounter = 0;

    public Pass1() {
        symbolTable = new LinkerSymbolTable();
        relativeLocationCounter = 0;
        base = 0;
    }

    /**
     * Processa uma linha de código.
     *
     * @param nibble O nibble com as informações do endereço da linha.
     * @param address O endereço da linha (sem o nibble).
     * @param code O código da linha.
     * @param currentFile O arquivo atual que está sendo processado.
     * @return Verdadeiro caso a análise teve sucesso, falso caso contrário.
     * @exception Caso tenha ocorrido algum problema de IO.
     */
    protected boolean processCode(int nibble, String address, String code, String currentFile){
        /**
         * TODO: processCode
         *
         * Analisar o nibble e incrementar o endereçamento relativo (relativeLocationCounter) de acordo.
         *
         */
    	
    	
    	relativeLocationCounter += 2;
    	
        return true;
    }//

    /**
     * Processa uma linha de endereço simbólico.
     *
     * @param nibble O nibble com as informações do endereço da linha.
     * @param address O endereço referente ao símbolo (sem nibble).
     * @param symbol O símbolo em si.
     * @param currentFile O arquivo atual que esta sendo processado.
     * @return Verdadeiro caso a análise teve sucesso, falso caso contrário.
     * @throws IOException
     *             Caso tenha ocorrido algum problema de IO.
     */
    protected boolean processSymbolicalAddress(int nibble, String address, String symbol, String currentFile, String originalLine)
            throws IOException {
        /*
         * TODO: processSymbolicalAddress
         * Situações típicas: entry point relocável, entry point absoluto e external.
         * O objetivo é resolver os entry points e externals relativos.
         * Devem ser avaliadas as combinações apropriadas do nibble.
         * Deve-se calcular o endereço do codigo e atualizar apropriadamente a tabela de simbolos.
         */
    	int addressInt = Integer.parseInt(address, 16);;
    	
    	try {
	    	if(isEntryPoint(nibble)) {
	    		
	    		if(isArgumentRelocable(nibble)) {
	    	  		addressInt += base;
	    	  		address = Integer.toHexString(addressInt);		
	    	  	}	    		
	    		symbolTable.setSymbolValue(symbol, address);
	    		if(isRelocableEntryPoint(nibble))
	        		symbolTable.setSymbolValue(symbol, address, true);
	    		
	    	}else if(isExternalPseudoInstruction(nibble)){
	    		symbolTable.setCodeForSymbol(symbol, currentFile, externalCounter);
	    		externalCounter++;
	    	}else {
	    		throw new IOException();
	    	}
    	}catch (IOException e) {
    		e.printStackTrace();
    	
    		return false;
    	}

        return true;
    }//

    /**
     * Finaliza o arquivo lido (pode haver um próximo arquivo).
     */
    protected void fileEnd() {

        /*
         * TODO: fileEnd()
         * Quando há mudança de arquivo, deve-se mudar a base!
         * Atualizar as variáveis existentes de acordo.
         *
         * */
    	
    	base += relativeLocationCounter;
    	relativeLocationCounter = 0;
    	externalCounter = 0;
    	
    }//

    public LinkerSymbolTable getSymbolTable() {
        return symbolTable;
    }
}
