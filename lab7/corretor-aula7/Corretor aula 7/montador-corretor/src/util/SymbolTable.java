package util;

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
 * @version 28.01.2010 ( refatoramento - Tiago) : mudança da especificação dos métodos e classe. Introdução das restrições
 * 												  da implementação.
 */
public class SymbolTable {


	/**Map que guarda os s�mbolos.*/
	private Map<String,String> symbolTable;


	/*
	 * TODO: A fazer:
	 *
	 * 1) Criar o construtor da classe
	 * 2) Inicializar a tabela de s�mbolos ( symbolTable) nele, como sendo uma classe
	 * 	HashTable (http://java.sun.com/javase/6/docs/api/java/util/Hashtable.html)
	 * 3) Especificar que classe dever� ter na key e no valor ( ver como exemplo a InstructionTable ou PseudoTable)
	 * 4) Implementar os m�todos necess�rios.
	 *
	 *
	 * */

	public SymbolTable(){
		symbolTable = new Hashtable<String, String>();
	}//


	/**
	 * Verifica se um s�mbolo est� na tabela.
	 *
	 * @param sym O s�mbolo a ser verificado.
	 *
	 * @return <b>true</b> se o s�mbolo estiver na tabela ou <b>false</b> caso contr�rio.
	 *
	 * @throws  NullPointerException se o s�mbolo passado for <b>null</b>.
	 *
	 */
	public boolean symbolInTable(String sym) {
		return this.symbolTable.containsKey(sym);
	}//

	/**
	 * Define um valor para um s�mbolo.<br>
	 * <b><font color="red">O s�mbolo j� deve existir na tabela.</font><b>
	 *
	 * @param sym O s�mbolo.
	 *
	 * @param address O valor.
	 *
	 * @return <b>true</b> caso a defini��o tenha obtido sucesso, <b>false</b> caso contr�rio.
	 *
	 * @throws NullPointerException se acaso a key (sym) ou valor(address) forem nulos (<b>null</b>).
	 */
	public boolean setSymbolValue(String sym, String address) {
		if(this.symbolTable.put(sym, address) == null)
			return false;

		return true;
	}//

	/**
	 * Verifica se um s�mbolo j� foi definido (j� h� o endere�o).
	 *
	 * @param sym O s�mbolo o qual se deseja saber se j� est� definido.
	 *
	 * @return <b>true</b> se o s�mbolo estiver definido, <b>false</b> caso contr�rio.
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
	 * Insere um s�mbolo na tabela.
	 *
	 * @param sym O s�mbolo a ser inserido.
	 *
	 * @return Falso caso o s�mbolo j� esteja definido, verdadeiro caso contr�rio.
	 *
	 * @throws NullPointerException se acaso a key (sym) for <b>null</b>
	 *
	 */
	public boolean insertSymbol(String sym) {

		if(definedSymbol(sym)){
			return false;
		}

		this.symbolTable.put(sym, "");

		return true;
	}

	/**
	 * Obt�m o valor definido para um s�mbolo.
	 *
	 * @param sym O s�mbolo o qual se deseja saber o valor.
	 *
	 * @return O valor do s�mbolo.
	 *
	 * @throws NullPointerException se acaso a key (sym) for <b>null</b>
	 *
	 */
	public String getSymbolValue(String sym) {
		return this.symbolTable.get(sym);
	}//m

}//class
