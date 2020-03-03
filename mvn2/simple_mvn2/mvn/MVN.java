/**
 * Escola Politecnica da Universidade de Sao Paulo
 * Copyright 2008 - todos os direitos reservados
 */
package mvn;

/**
 * Esta classe possui costante a serem
 * utilizadas durante a execução da MVN
 *
 * @author Humberto Sandmann
 *
 */
public final class MVN {

	// MVN
	public final static String VERSION       = "MVN versao 3.5b (Julho/2008)";
	public final static String TXT_POLI      = "                Escola Politecnica da Universidade de Sao Paulo";
	public final static String TXT_MVN       = "              PCS2302/PCS2024  Simulador da Maquina de Von Neumann";
	public final static String TXT_COPYRIGHT = "           " + VERSION + " - Todos os direitos reservados";
	public final static String ABOUT = TXT_POLI + '\n' + TXT_MVN + '\n' + TXT_COPYRIGHT;

	// Terminal
	public final static String PROMPT = "> ";
	public final static String BYE = "Terminal encerrado.";

	public final static String INFORME_NOME_ARQUIVO_ENTRADA = "Informe o nome do arquivo de entrada: ";
	public final static String INFORME_NOME_ARQUIVO_SAIDA = "Informe o nome do arquivo de saida: ";
	public final static String INFORME_ENDERECO_INI = "Informe endereco inicial";
	public final static String INFORME_ENDERECO_FIM = "Informe endereco final";
	public final static String INFORME_FORMATO_ARQUIVO = "Informe o formato de arquivo (a)scII ou (b)inario [a]: ";
	public final static String GRAVA_ARQUIVO = "Persistir em arquivo (s/n)[n]: ";

	public final static String ENDERECO_ATUAL_IC = "Endereco atual do IC: ";
	public final static String ENDERECO_NOVO_IC  = "Informe (novo) endereco do IC: ";
	public final static String MOSTRA_REGS_POR_FDE = "Exibir valores dos registradores a cada passo do ciclo FDE (s/n)[s]: ";
	public final static String EXECUTA_PASSO_A_PASSO = "Executar MVN passo a passo (s/n)[n]: ";
	public final static String EXECUCAO_CONTINUA = "Continua (s/n)[s]: ";

	public final static String ERRO_ABRIR_ARQUIVO = "Erro ao abrir arquivo";
	public final static String ERRO_GRAVAR_ARQUIVO = "Erro ao gravar arquivo";
	public final static String ERRO_EXECUCAO_TERMINAL = "Erro durante a execucao do terminal";
	public final static String ERRO_ENTRADA_TECLADO = "Erro na entrada do teclado";

	public final static String ENTRAR_OPERACAO_DISPOSITIVO = "Adicionar(a) ou remover(r) (ENTER para cancelar): ";
	public final static String ENTRAR_COM_TIPO_DISPOSITIVO = "Entrar com o tipo de dispositivo (ou ENTER para cancelar): ";
	public final static String ENTRAR_COM_UNIDADE_LOGICA = "Entrar com a unidade logica (ou ENTER para cancelar): ";
	public final static String ENTRAR_COM_NOME_ARQUIVO_UNIDADE_LOGICA = "Nome do arquivo para essa unidade logica: ";
	public final static String TIPO_DISPOSITIVO_DEVE_SER_NUMERICO = "O tipo de dispositivo deve ser numerico";
	public final static String UNIDADE_LOGICA_DEVE_SER_NUMERICA = "A unidade logica ser numerica";
	public final static String INTERFACE_ACESSO_IMPRESSORA_NAO_IMPLEMENTADA = "Interface de acesso a impressora nao implementada";
	public final static String ENTRAR_COM_MODO_OPERACAO = "Modo de operacao: Leitura(l)  Escrita(e)  Leitura e Escrita(b): ";
	public final static String DISPOSITIVO_ADICIONADO = "Dispositivo adicionado";

	public final static String LOADER_EXECUTADO = "Comando LOADER executado com sucesso";
	public final static String DUMPER_EXECUTADO = "Comando DUMPER executado com sucesso";

	// Controle de Dumper/Loader
	public final static String ARQ_LOAD = "load.abs";
	public final static String ARQ_DUMP = "dump.abs";

	// Constantes para os dispositivos de E/S
	public final static int MAX_TIPOS_DISPOSITIVOS = 16;
	public final static int MAX_UNIDADE_LOGICA = 256;

	// Tipos de dispositivos básicos
	public final static int TECLADO = 0;
	public final static int MONITOR = 1;
	public final static int IMPRESSORA = 2;
	public final static int DISCO = 3;

	// Nome do arquivo padrão de dispositivos
	public final static String ARQ_DISP = "disp.lst";

	public final static String INICIALIZACAO_PADRAO_DISPOSITIVOS = "Inicializacao padrao de dispositivos";
	public final static String INICIALIAZACAO_PADRAO_BASEADA_EM_ARQUIVO = "Inicializacao padrao de dispositivos baseada em arquivo";
	public final static String DISPOSITIVO_NAO_ENCONTRADO = "Dispositivo nao encontrado";
	public final static String TIPO_DISPOSITIVO_INVALIDO = "Tipo de dispositivo invalido [0; " + MVN.MAX_TIPOS_DISPOSITIVOS + "]";
	public final static String POSICAO_UNIDADE_LOGICA_INVALIDA = "Posicao da unidade logica invalida [0; " + MVN.MAX_UNIDADE_LOGICA + "]";
	public final static String DISPOSITIVO_INVALIDO = "Dispositivo invalido";

	public final static char SIM = 's';
	public final static char NAO = 'n';

	public final static String ATIVADO = "Ativado";
	public final static String DESATIVADO = "Desativado";

}
