package mvn.util;

import java.util.Hashtable;
import java.util.Map;

//Versão do aluno gerada em 01/2010

/**
 * Representação de uma tabela de símbolos.<br>
 * O símbolo é considerado igual independente da presença de letras minúsculas
 * ou maiúsculas diferentes em outras chamadas (<i>case insensitive</i>).
 *
 * @author RRocha
 * @version 04.10.2005
 * @version 28.01.2010 (refatoramento - Tiago) : mudança da especificação dos métodos e classe. Introdução das restrições
 * 												  da implementação.
 */
public class SymbolTable {
	
	
	/**Map que guarda os símbolos.*/
	protected Map<String,String> symbolTable;
	
	
	public SymbolTable(){
		symbolTable = new Hashtable<String, String>();
	}//
	
	
	/**
	 * Verifica se um símbolo está na tabela.
	 *
	 * @param sym O símbolo a ser verificado.
	 * 
	 * @return <b>true</b> se o símbolo estiver na tabela ou <b>false</b> caso contrário.
	 * 
	 * @throws  NullPointerException se o símbolo passado for <b>null</b>.
	 *           
	 */
	public boolean symbolInTable(String sym) {
		return this.symbolTable.containsKey(sym);		
	}//

	/**
	 * Define um valor para um símbolo.<br>
	 * <b><font color="red">O símbolo já deve existir na tabela.</font><b>
	 *
	 * @param sym O símbolo.
	 *            
	 * @param address O valor.
	 *            
	 * @return <b>true</b> caso a definição tenha obtido sucesso, <b>false</b> caso contrário.
	 *         
	 * @throws NullPointerException se acaso a key (sym) ou valor(address) forem nulos (<b>null</b>).        
	 */
	public boolean setSymbolValue(String sym, String address) {
		if(this.symbolTable.put(sym, address) == null)
			return false;

		return true;
	}//

	/**
	 * Verifica se um símbolo já foi definido (já há o endereço).
	 *
	 * @param sym O símbolo o qual se deseja saber se já está definido.
	 * 
	 * @return <b>true</b> se o símbolo estiver definido, <b>false</b> caso contrário.
	 *            
	 * @throws NullPointerException se a key (sym) for <b>null</b>.           
	 *            
	 */
	public boolean definedSymbol(String sym) {
		if(this.symbolTable.containsKey(sym)){
			if(this.symbolTable.get(sym).equalsIgnoreCase("")){
				return false;
			}else{
				return true;
			}
		}else{
			return false;
		}		
	}//m

	/**
	 * Insere um símbolo na tabela.
	 *
	 * @param sym O símbolo a ser inserido.
	 *            
	 * @return Falso caso o símbolo já esteja definido, verdadeiro caso contrário. 
	 * 
	 * @throws NullPointerException se acaso a key (sym) for <b>null</b>
	 * 
	 */
	public boolean insertSymbol(String sym) {

		if(!(symbolTable.containsKey(sym))){
			this.symbolTable.put(sym, "");
			return true;
		}//if		
		return false;
	}//method

	/**
	 * Obtêm o valor definido para um símbolo.
	 *
	 * @param sym O símbolo o qual se deseja saber o valor.
	 *            
	 * @return O valor do símbolo.
	 * 
	 * @throws NullPointerException se acaso a key (sym) for <b>null</b>
	 * 
	 */
	public String getSymbolValue(String sym) {
		return this.symbolTable.get(sym);
	}//m
}//class