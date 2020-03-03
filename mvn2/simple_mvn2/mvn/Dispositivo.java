package mvn;

import mvn.controle.MVNException;

/**
 * Representa um dispositivo na MVN.
 *
 * @author PSMuniz, FLevy
 * @version 1.0 - PCS/EPUSP
 * @viz.diagram Dispositivo.tpx
 */
public interface Dispositivo {
	/**
	 * Escreve um Bits8 no dispositivo.
	 *
	 * @param in
	 *            O Bits8 a ser escrito.
	 * @throws MVNException
	 *             Caso haja um problema de entrada e saida ao escrever no
	 *             dispositivo.
	 */
	public void escrever(Bits8 in) throws MVNException;

	/**
	 * Le um Bits8 do dispositivo.
	 *
	 * @return O Bits8 lido.
	 * @throws MVNException
	 *             Caso haja um problema de entrada e saida ao escrever no
	 *             dispositivo.
	 */
	public Bits8 ler() throws MVNException;

} // Dispositivo

