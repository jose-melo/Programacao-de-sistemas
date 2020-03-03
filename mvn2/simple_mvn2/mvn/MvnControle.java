package mvn;

import mvn.controle.MVNException;

/**
 * Contém a classe MVN que define uma interface,
 * constantes e outros literais utilizados na
 * maquina.
 *
 * @author PSMuniz
 * @version 1.0 - PCS/EPUSP
 * @version 2.0 - Humberto Sandmann - Refactory - PSI/EPUSP
 */

public class MvnControle {

	private Memoria memoria = null;

	/**
	 * Construtor da Classe MVN
	 */
	public MvnControle() {
		this.memoria = new Memoria();
		// TODO
	}

	/**
	 * Responsável por inicializar a MVN
	 *
	 * @throws MVNException caso ocorra algum erro de processamento
	 */
	public void initialize() throws MVNException {
		// Zera a memoria e registradores
		memoria.clear();
		// TODO
		// Inicia entrada/saida.
		// TODO
	}

	/**
	 * Exportar em formato texto o conteúdo dos registradores
	 * da MVN, com cabeçalho
	 *
	 * @return conteúdo dos registradores com cabeçalhos
	 */
	public String dumpRegistradoresTexto() {
		// TODO
		return null;
	}

	/**
	 * Retorna o valor de um registrador na base hexadecimal
	 *
	 * @param key código de identificação do registrador
	 * @return valor do registrador na base hexadecimal
	 */
	public String getRegistradorHex(int key) {
		// TODO
		return null;
	}

	/**
	 * Retorna um trecho da memória em formato texto e hexadecimal
	 *
	 * @param ini_address endereço de início do trecho a ser retornado
	 * @param end_address endereço de término do trecho a ser retornado
	 * @return conteúdo da memória em formato texto e hexadecimal
	 */
	public String dumpMemoriaTexto(int ini_address, int end_address) {
		return memoria.toString(ini_address, end_address);
	}

	/**
	 * Carrega em memória um arquivo mvn em formato ASCII de nome <b>dump.abs</b>
	 * e então executa este arquivo. É esperado que o programa contído neste arquivo
	 * realize um dump na memória.
	 *
	 * @throws MVNException caso ocorra algum problema durante o carregamento ou execução do programa
	 */
	public void dumper() throws MVNException {
		// TODO
	}

	/**
	 * Carrega um arquivo em formato ASCII para memória
	 *
	 * @param filename nome do arquivo
	 * @throws MVNException caso ocorra algum erro de execução
	 */
	public void loadMemoriaAscii(String filename) throws MVNException {
		try {
			// String firstAddr = memoria.loadArqTexto(filename);
			memoria.loadArqTexto(filename);
			// cpu.obterRegs().setRegHex(Registradores.IC, firstAddr);
		} catch (MVNException e) {
			throw e;
		} catch (Exception e) {
			throw new MVNException(MVN.ERRO_ABRIR_ARQUIVO + ": " + filename, e);
		}
	}

	/**
	 * Carrega em memória um arquivo mvn em formato ASCII de nome <b>load.abs</b>
	 * e então executa este arquivo. É esperado que o programa contído neste arquivo
	 * realize um load na memória.
	 *
	 * @throws MVNException caso ocorra algum problema durante o carregamento ou execução do programa
	 */
	public void loader() throws MVNException {
		// TODO
	}

	/**
	 * Adquire a lista de dispositivos disponível para MVN em formato texto
	 *
	 * @return lista de dispositivos disponíveis para a MVN
	 */
	public String listDispositivos() {
		// TODO
		return null;
	}

	/**
	 * Executa um programa alocado em um trecho de memória
	 *
	 * @param addr endereço de memória do início do programa
	 * @param passByPass se a execução deve ser realizada passo à passo
	 * @param showRegs exibir valor dos registradores a cada passo
	 * @throws MVNException caso ocorra algum problema de execução do programa
	 */
	public void execute(String addr, boolean passByPass, boolean showRegs) throws MVNException {
		// TODO
	}

	/**
	 * Retona a execução da MVN no caso de execução passo à passo
	 *
	 * @param passByPass manter execução passo à passo
	 * @return se a máquina termino de executar o programa corrente
	 * @throws MVNException caso ocorra algum problema de execução do programa
	 */
	public boolean resume(boolean passByPass) throws MVNException {
		// TODO
		return false;
	}

	/**
	 * Adiciona um dispositivo de I/O na MVN
	 *
	 * @param tipo especifica o tipo de dispositivo a ser adicinado
	 * @param unidadeLogica unidade lógica do dispositivo
	 * @param obj instância de um objeto do tipo <code>mvn.Dispositivo</code>
	 * @throws MVNException caso ocorra algum problema ao adicionar o dispositivo
	 */
	public void addDispositivo(int tipo, int unidadeLogica, Dispositivo obj) throws MVNException {
		// TODO
	}

	/**
	 * Remove um dispositivo de I/O na MVN
	 *
	 * @param tipo especifica o tipo de dispositivo a ser removido
	 * @param unidadeLogica unidade lógica do dispositivo
	 * @throws MVNException caso ocorra algum problema ao remover o dispositivo
	 */
	public void removeDispositivo(int tipo, int unidadeLogica) throws MVNException {
		// TODO
	}

}
