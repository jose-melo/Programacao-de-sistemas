/**
 * Escola Politecnica da Universidade de Sao Paulo
 * Copyright 2008 - todos os direitos reservados
 */
package mvn.controle;

/**
 * @author Humberto Sandmann
 *
 */
public class MVNException extends Exception {

	/**
	 *
	 */
	public MVNException() {
	}

	/**
	 * @param arg0
	 */
	public MVNException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public MVNException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public MVNException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
