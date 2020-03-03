/**
 * Escola Politecnica da Universidade de Sao Paulo
 * Copyright 2008 - todos os direitos reservados
 */
package mvn.controle;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import mvn.MVN;
import mvn.Memoria;
import mvn.MvnControle;

/**
 * Classe responsável pela interface monitor/teclado com o usuário
 *
 * @author Humberto Sandmann
 *
 */
public class PainelControle {

	/**
	 * Classe responsável para exibição e captura de informações via monitor/teclado
	 *
	 * @author Humberto Sandmann
	 *
	 */
	public class Terminal {

		// Buffer para ler adequadamente do teclado e exibir na saída padrão
		private final BufferedReader in;
		private final PrintStream out;
		private final PrintStream err;

		/**
		 * Construtor padrão
		 */
		public Terminal() {
			this.in = new BufferedReader(new InputStreamReader(System.in));
			this.out = System.out;
			this.err = System.err;
		}

		/**
		 * Exibe uma mensagem na saída padrão
		 *
		 * @param mensagem conteúdo a ser exibido
		 */
		public void exibe(String mensagem) {
			out.print(mensagem);
		}

		/**
		 * Exibe uma mensagem com quebra de linha na saída padrão
		 *
		 * @param mensagem conteúdo a ser exibido
		 */
		public void exibeLinha(String mensagem) {
			out.println(mensagem);
		}

		/**
		 * Exibe uma mensagem de erro com quebra de linha na saída padrão de erro
		 *
		 * @param e exceção levantada pelo sistema
		 */
		public void erro(Exception e) {
			if (e != null) {
				if (debug) {
					e.printStackTrace(err);
				} else {
					err.println(e.getMessage());
				}
			}
		}

		/**
		 * Exibe uma mensagem de erro com quebra de linha na saída padrão de erro
		 *
		 * @param mensagem mensagem texto de erro
		 */
		public void erro(String mensagem) {
			err.println(mensagem);
		}

		/**
		 * Exibe uma mensagem de erro com quebra de linha na saída padrão de erro
		 *
		 * @param mensagem mensagem de texto
		 * @param e erro levantado pelo sistema
		 */
		public void erro(String mensagem, Exception e) {
			if (debug) {
				err.println(mensagem);
				if (e != null) {
					e.printStackTrace(err);
				}
			} else {
				if (e != null) {
					mensagem += " - " + e.getMessage();
				}
				err.println(mensagem);
			}
		}

		/**
		 * Auxilia para obter uma String Entrada pelo teclado. Se a entrada
		 * for inválida retorna uma String em branco.
		 *
		 * @return retorna um String digitada no teclado, caso ocorra algum erro
		 * o resultado é uma String vazia.
		 */
		public final synchronized String ler() {
			String s = "";
			try {
				s = in.readLine();
			} catch (IOException e) {
				erro(MVN.ERRO_ENTRADA_TECLADO, e);
			}
			return s;
		}

		/**
		 * Exibe um mensagem e retorna o valor digitado na entrada padrão, caso não seja digitado
		 * nada retorna o valor padrão.
		 *
		 * @param mensagem mensagem a ser exibida
		 * @param padrao valor padrão a retornar, caso não digite nada
		 * @return valor retornado
		 */
		public String obtem(String mensagem, String padrao) {
			exibe(mensagem);
			String s = ler().trim();
			if (s.length() == 0) {
				s = padrao;
			}
			return s;
		}

		/**
		 * Exibe o símbolo de prompto e captura o texto digitado no terminal
		 *
		 * @return texto digitado
		 */
		public String prompt() {
			exibeLinha("");
			while (true) {
				String conteudo = obtem(MVN.PROMPT, "");
				if (conteudo.length() != 0) {
					return conteudo;
				}
			}
		}

	}

	/**
	 * Esta classe é responsável por armazenar as constantes de comandos do terminal
	 *
	 * @author Humberto Sandmann
	 *
	 */
	private static class Operacao {

		public static final char CMD_AJUDA         = 'h';
		public static final char CMD_DEBUG         = 'b';
		public static final char CMD_INICIALIZA    = 'i';
		public static final char CMD_FINALIZA      = 'x';
		public static final char CMD_DISPOSITIVOS  = 's';
		public static final char CMD_REGISTRADORES = 'g';
		public static final char CMD_MEMORIA       = 'm';
		public static final char CMD_ASCII         = 'p';
		public static final char CMD_LOADER        = 'l';
		public static final char CMD_DUMPER        = 'd';
		public static final char CMD_EXECUTA       = 'r';

		public static final String[] AJUDA         = new String[]{String.valueOf(CMD_AJUDA)        , "                 ", "Ajuda"};
		public static final String[] DEBUG         = new String[]{String.valueOf(CMD_DEBUG)        , "                 ", "Ativa/Desativa modo Debug"};
		public static final String[] INICIALIZA    = new String[]{String.valueOf(CMD_INICIALIZA)   , "                 ", "Inicializa MVN"};
		public static final String[] FINALIZA      = new String[]{String.valueOf(CMD_FINALIZA)     , "                 ", "Finaliza MVN e terminal"};
		public static final String[] DISPOSITIVOS  = new String[]{String.valueOf(CMD_DISPOSITIVOS) , "                 ", "Manipula dispositivos de I/O"};
		public static final String[] REGISTRADORES = new String[]{String.valueOf(CMD_REGISTRADORES), "                 ", "Lista conteudo dos registradores"};
		public static final String[] MEMORIA       = new String[]{String.valueOf(CMD_MEMORIA)      , "[ini] [fim] [arq]", "Lista conteudo da memoria"};
		public static final String[] ASCII         = new String[]{String.valueOf(CMD_ASCII)        , "[arq]            ", "Carrega programa ASCII para memoria"};
		public static final String[] LOADER        = new String[]{String.valueOf(CMD_LOADER)       , "                 ", "Loader"};
		public static final String[] DUMPER        = new String[]{String.valueOf(CMD_DUMPER)       , "                 ", "Dumper"};
		public static final String[] EXECUTA       = new String[]{String.valueOf(CMD_EXECUTA)      , "[addr] [regs]    ", "Executa programa em um determinado endereco"};

		public static final String[][] OPERACOES = new String[][] {
				AJUDA,
				DEBUG,
				INICIALIZA,
				DISPOSITIVOS,
				REGISTRADORES,
				ASCII,
				MEMORIA,
				LOADER,
				DUMPER,
				EXECUTA,
				FINALIZA
			};

		public static final String ERRO_COMANDO_INVALIDO = "Comando invalido";

		/**
		 * Retorna uma String com as opções de comando disponíveis para este terminal
		 *
		 * @return String com as opções de comando
		 */
		public static final String ajuda() {
			StringBuffer out = new StringBuffer();
			out.append("COMANDO SINTAXE              OPERACAO\n");
			out.append("---------------------------------------------------------");
			for (int i = 0; i < OPERACOES.length; i++) {
				out.append("\n   ");
				String[] op = OPERACOES[i];
				out.append(op[0]);
				out.append("    ");
				out.append(op[1]);
				out.append("    ");
				out.append(op[2]);
			}
			return out.toString();
		}
	}

	private MvnControle mvn = null;
	private Terminal terminal = null;
	private boolean debug = false;

	/**
	 * Contrutor padrão da classe, recebe uma MVN para gerenciar
	 *
	 * @param mvn MVN alvo do gerenciador, ao qual o Painel será acoplado
	 */
	public PainelControle(MvnControle mvn) {
		this.mvn = mvn;
		this.terminal = new Terminal();
		try {
			initialize();
		} catch (MVNException e) {
			terminal.erro("ERRO - MVN NAO inicializada", e);
		}
	}

	/**
	 * Exibe o controle de terminal para o operador
	 */
	public void mostrarTerminal() {

		ajuda();

		boolean leTerminal = true;
		while (leTerminal) {

			String linha = terminal.prompt();

			try {

				// Definindo elementos da linha de comando
				StringTokenizer token = new StringTokenizer(linha);
				char comando = token.nextToken().charAt(0);
				String[] args = new String[token.countTokens()];
				for (int i = 0; token.hasMoreElements(); i++) {
					args[i] = (String) token.nextElement();
				}

				switch (comando) {

					case Operacao.CMD_AJUDA:
						ajuda();
						break;

					case Operacao.CMD_DEBUG:
						this.debug = !debug;
						terminal.exibeLinha("Modo de depuracao foi " + (debug ? MVN.ATIVADO.toUpperCase() : MVN.DESATIVADO.toUpperCase()));
						break;

					case Operacao.CMD_INICIALIZA:
						initialize();
						break;

					case Operacao.CMD_FINALIZA:
						leTerminal = false;
						break;

					case Operacao.CMD_REGISTRADORES:
						String regs = mvn.dumpRegistradoresTexto();
						terminal.exibeLinha(regs);
						break;

					case Operacao.CMD_ASCII:
						loadMemoriaAscii(args);
						break;

					case Operacao.CMD_MEMORIA:
						memoria(args);
						break;

					case Operacao.CMD_DISPOSITIVOS:
						dispositivos();
						break;

					case Operacao.CMD_LOADER:
						mvn.loader();
						terminal.exibeLinha(MVN.LOADER_EXECUTADO);
						break;

					case Operacao.CMD_DUMPER:
						mvn.dumper();
						terminal.exibeLinha(MVN.DUMPER_EXECUTADO);
						break;

					case Operacao.CMD_EXECUTA:
						executa(args);
						break;

					default:
						throw new MVNException(Operacao.ERRO_COMANDO_INVALIDO);
				}

			} catch (MVNException e) {
				terminal.erro(e);
			} catch (Exception e) {
				terminal.erro(MVN.ERRO_EXECUCAO_TERMINAL, e);
			}
		}
		terminal.exibeLinha(MVN.BYE);
	}

	/**
	 * Executa o comando de ajuda
	 */
	private void ajuda() {
		terminal.exibeLinha("");
		terminal.exibeLinha(MVN.ABOUT);
		terminal.exibeLinha("");
		terminal.exibeLinha(Operacao.ajuda());
	}

	/**
	 * Executa o comando de inicialização
	 *
	 * @throws MVNException caso ocorra algum erro de execução
	 */
	private void initialize() throws MVNException {
		mvn.initialize();
		terminal.exibeLinha("MVN Inicializada");
	}

	/**
	 * Executa o comando de dispositivos
	 *
	 * @throws MVNException caso ocorra algum erro de execução
	 */
	private void dispositivos() throws MVNException {
		// TODO
	}

	/**
	 * Executa o comando de carregamento de programas em formato ASCII para memória
	 *
	 * @param args argumentos da linha de comando
	 * @throws MVNException caso ocorra algum erro de execução
	 */
	private void loadMemoriaAscii(String[] args) throws MVNException {
		String filename;
		if (args.length < 1) {
			filename = terminal.obtem(MVN.INFORME_NOME_ARQUIVO_ENTRADA, "");
		} else {
			filename = args[0];
		}
		if (filename.length() == 0) {
			throw new MVNException(MVN.ERRO_ABRIR_ARQUIVO);
		}
		mvn.loadMemoriaAscii(filename);
		terminal.exibeLinha("Programa " + filename + " carregado");
	}

	/**
	 * Realiza dump em tela ou arquivo da memória, em formato texto.
	 *
	 * @param args [endereço de inicio] - Endereço da posição inicial
	 *             [endereço de fim]    - Endereço da posição final
	 *             [nome do arquivo]    - Arquivo para gravar conteúdo de memória em formato texto
	 * @throws MVNException caso ocorra algum erro de execução
	 */
	private void memoria(String[] args) throws MVNException {

		String minAddrHex = Integer.toHexString(Memoria.MIN_ADDRESS);
		String maxAddrHex = Integer.toHexString(Memoria.MAX_ADDRESS);

		// Trata endereço inicial da memória
		String sAddIni;
		if (args.length < 1) {
			sAddIni = terminal.obtem(MVN.INFORME_ENDERECO_INI + " [" + minAddrHex + "]: ", minAddrHex);
		} else {
			sAddIni = args[0];
		}
		int add_ini = inputHex(sAddIni, Memoria.MIN_ADDRESS);

		// Trata endereço final da memória
		String sAddFim;
		if (args.length < 2) {
			sAddFim = terminal.obtem(MVN.INFORME_ENDERECO_FIM + " [" + maxAddrHex + "]: ", maxAddrHex);
		} else {
			sAddFim = args[1];
		}
		int add_fim = inputHex(sAddFim, Memoria.MAX_ADDRESS);

		// Obtém o dumpping de memória em formato texto
		String dump = mvn.dumpMemoriaTexto(add_ini, add_fim);

		// Tratar persistência em arquivo
		String opcao = "";
		String arq = "";
		if (args.length > 2) {
			arq = args[2];
		} else if (args.length == 0) {
			opcao = terminal.obtem(MVN.GRAVA_ARQUIVO, String.valueOf(MVN.NAO));
			if (opcao.length() > 0 && opcao.toLowerCase().charAt(0) == MVN.SIM) {
				arq = terminal.obtem(MVN.INFORME_NOME_ARQUIVO_SAIDA, "");
			}
		}
		if (arq.length() > 0) {
			try {

				gravaArquivoTexto(arq, dump);

			} catch (IOException e) {
				throw new MVNException(MVN.ERRO_GRAVAR_ARQUIVO + " " + arq, e);
			}
		} else {
			terminal.exibeLinha(dump);
		}
	}

	/**
	 * Grava um conteúdo de String em um arquivo externo
	 *
	 * @param filename nome do arquivo onde ser persistido o conteúdo
	 * @param conteudo String com o valor a ser gravado
	 * @throws IOException caso ocorra algum erro de execução
	 */
	private void gravaArquivoTexto(String filename, String conteudo) throws IOException {
		// Cria um buffer de escrita, associando-o a um arquivo.
		// TODO
		// Converte o conteúdo do buffer em string e escreve no arquivo, no
		// formato especificado.
		// TODO
	}

	/**
	 * Executa o comando de execução de programas na memória
	 *
	 * @param args argumentos da linha de comando
	 * @throws MVNException caso ocorra algum erro de execução
	 */
	private void executa(String[] args) throws MVNException {
		// TODO
	}

	/**
	 * Auxiliar para transformação de um valor de entrada em hexadecimal
	 *
	 * @param input valor a transformar
	 * @param defaultValue valor padrão caso não consigo realizar a operação
	 * @return valor transformado
	 */
	private int inputHex(String input, int defaultValue) {
		try {
			return Integer.parseInt(input, 16);
		} catch (Exception e) {
			return defaultValue;
		}
	}

}
