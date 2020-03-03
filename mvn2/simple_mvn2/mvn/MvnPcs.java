package mvn;

import java.io.IOException;

import mvn.controle.PainelControle;


/**
 * Classe que inicia a MVN.
 *
 * @author PSMuniz
 * @version 1.0 - PCS/EPUSP
 * @viz.diagram MvnPcs.tpx
 */
public class MvnPcs {

	public static void main(String args[]) throws IOException {

		MvnControle mvnPcs = new MvnControle();

		//  o painel simplesmente coloca os dispositivos "plug-and-play"
		PainelControle painel = new PainelControle(mvnPcs);

		// "Liga" a MVN, apresentando o painel principal de mvn.controle e passando
		// o mvn.controle para ele.
		painel.mostrarTerminal();

	}

}//MvnPCS.java
