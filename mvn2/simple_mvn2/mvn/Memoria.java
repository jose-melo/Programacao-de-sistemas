package mvn;

import java.io.*;
import java.util.*;

import mvn.controle.MVNException;


/**
 * Representa a memória da MVN.
 *
 * @author PSMuniz
 * @version 1.0 - PCS/EPUSP
 */
public class Memoria {

  public final static int MIN_ADDRESS = 0x0000;
  public final static int MAX_ADDRESS = 0x0FFF;
  public final static int MEM_CAPACITY = MAX_ADDRESS - MIN_ADDRESS + 1;

  /* A memória da MVN é um vetor (array) de Bits8. */
  private Bits8[] store;

  /**
   * Construtor: cria um array de memória de tamanho MEM_CAPACITY e inicia as
   * posiçoes com valor 0.<br>
   * <b>Pós-condição </b>: a memória contém MEM_CAPACITY Bits8 com valor
   * zerado.
   */
  public Memoria() {
    store = new Bits8[MEM_CAPACITY];
    for (int i = 0; i < MEM_CAPACITY; i++) {
      store[i] = new Bits8(0);
    }
  }

  /**
   * Zera a memoria. <br>
   * <b>Pós-condição </b>: os Bits8 da memória tem valor 0.
   */
  public void clear() {
    // Esta mensagem deve ser removida após a implementação do método.
    //System.out.println("[erro] O método clear() não está implementado.");

    for (int i = 0; i < MEM_CAPACITY; i++) {
      store[i].reset();
    }

    // TODO
  }

  /**
   * Escreve um byte em um endereço da memória. <br>
   * <b>Pré-condição </b>: o Bits8 a ser escrito não pode ser nulo. <br>
   * <b>Pós-condição </b>: a posição de memória <i>addr</i> contém o valor
   * <i>p </i>.
   *
   * @param p
   *            O Bits8 a ser escrito.
   * @param addr
   *            O endereço o qual o Bits8 será escrito (entre MIN_ADDRESS e
   *            MAX_ADDRESS).
   */
  public void write(Bits8 p, int addr) throws MVNException {
    
    if (addr < MIN_ADDRESS || addr > MAX_ADDRESS) 
      throw new MVNException("[erro] Endereço " + addr + " é inválido");

    store[addr].reset();
    store[addr].add(p);

    // TODO:
    // - Verificar se o endereço é válido. Caso não seja, lançar uma
    //   MVNException com a mensagem de erro "[erro] Endereço XXXX é inválido",
    //   em que XXXX deve ser substituído pelo endereço passado como parâmetro.
    // - Caso o endereço seja válido, escrever o valor na memória.
  }

  /**
   * Retorna um Byte8 armazenado em um endereço da memória.
   *
   * @param addr
   *            O endereço de memória do Bits8 a ser retornado.
   */
  public Bits8 read(int addr) throws MVNException {

   if (addr < MIN_ADDRESS || addr > MAX_ADDRESS) 
      throw new MVNException("[erro] Endereço " + addr + " é inválido");


    return store[addr];
    // TODO:
    // - Verificar se o endereço é válido. Caso não seja, lançar uma
    //   MVNException com a mensagem de erro "[erro] Endereço XXXX é inválido",
    //   em que XXXX deve ser substituído pelo endereço passado como parâmetro.
    // - Caso o endereço seja válido, retornar o valor da memória.

    // return null;
  }

  /**
   * Carrega o arquivo texto, contendo o programa em código de máquina, na
   * memória, sobrescrevendo seus valores correntes. <br>
   * O arquivo é composto de linhas com palavras, i.e. 2 pares
   * &lt;endereço&gt;&lt;valor&gt;(separados por um branco) Se o simbolo ';'
   * estiver na primeira coluna de uma linha, com pelo menos um branco
   * separando outro caractere, ignora o restante da linha (comentario). <br>
   * <b>Pré-condição </b>: o arquivo passado não deve ser nulo, ou inválido.
   * <br>
   * <b>Pós-condição </b>: o programa contido no arquivo é colocado na
   * memória.
   *
   * @param fileName
   *            O nome do arquivo texto a ser lido
   * @return true caso tenha sido possivel carregar arquivo em memoria
   * @throws IOException
   *             Caso haja algum problema na leitura do arquivo, até mesmo
   *             caso o texto não esteja no padrão (por enquanto).
   */
  public String loadArqTexto(String fileName) throws MVNException, IOException {

    int firstAddr = -1;

    int linhaAtual = 0; // Contador para Debug
    String line;
    StringTokenizer dataLine;
    String dataToken, xtraToken;
    Bits8 memByte0, memByte1;
    int address;
    String comentario = ";"; // Se estiver na primeira coluna, ignora a
    // linha

    // Cria um buffer de leitura para o arquivo texto
    BufferedReader inFile = new BufferedReader(new FileReader(fileName));

    // Itera sobre as linhas do arquivo, obtendo os endereços e seus
    // valores, armazenando-os na memória.
    while ((line = inFile.readLine()) != null) {
      dataLine = new StringTokenizer(line, " "); // Um token é delimitado
      // por um branco
      linhaAtual++;

      // Obtém o token do endereço
      try {
        dataToken = dataLine.nextToken();
      } catch (NoSuchElementException e) {
        clear();
        throw new MVNException("Erro no formato do arquivo com codigo de maquina: <endereco>. Linha: " + linhaAtual + ".", e);
      }

      if (dataToken.equals(comentario))
        continue;

      // Converte os valores ASCII do <endereço> em hexadecimal
      try {
        address = Integer.parseInt(dataToken, 16);
        if (firstAddr == -1) {
          firstAddr = address;
        }
      } catch (NumberFormatException e) {
        clear();
        throw new MVNException("Erro na obtencao do endereco. Linha: " + linhaAtual + ".", e);
      }

      // le o token de valor
      try {
        dataToken = dataLine.nextToken();
      } catch (NoSuchElementException e) {
        clear();
        throw new MVNException("Erro no formato do arquivo com codigo de maquina: <valor>.", e);
      }

      // le o possivel 2o token de valor
      xtraToken = null;
      try {
        xtraToken = dataLine.nextToken();
      } catch (NoSuchElementException ex) {
        // pode nao ter o token - nao e' erro
      }

      // Se o próximo token é comentário (ou nulo) a instrução está
      // codificada em um único token
      if (xtraToken == null || xtraToken.equals(comentario)) {

        // Converte os valores ASCII do <valor> em hexadecimal,
        // dividindo-os em 2 bytes. Armazena
        // cada byte no endereço adequado.
        if (dataToken.length() != 4) {
          clear();
          throw new MVNException("Erro na instrucao. Linha: " + linhaAtual + ".");
        }
        String strByt0 = dataToken.substring(0, 2);
        String strByt1 = dataToken.substring(2, 4);
        try {
          memByte0 = new Bits8(Integer.parseInt(strByt0, 16));
          memByte1 = new Bits8(Integer.parseInt(strByt1, 16));
        } catch (NumberFormatException e) {
          clear();
          throw new MVNException("Erro na instrucao. Linha: " + linhaAtual + ".", e);
        }
      } // Senão há dois tokens para cada metade da instrução
      else {
        try {
          // Obtém o token da parte baixa do valor (um byte) - em
          // dataToken
          // Converte os valores ASCII do <valor> em hexadecimal.
          // Armazena cada byte no endereço adequado.
          memByte0 = new Bits8(Integer.parseInt(dataToken, 16));

          // Obtém o token da parte alta do valor (um byte)
          dataToken = xtraToken;
          // Converte os valores ASCII do <valor> em hexadecimal.
          // Armazena cada byte no endereço adequado.
          memByte1 = new Bits8(Integer.parseInt(dataToken, 16));
        } catch (NumberFormatException e) {
          // o token nao e um numero valido
          clear();
          throw new MVNException("Erro na instrucao. Linha: " + linhaAtual + ".", e);
        }
      }
      // Escreve o valor no endereço correspondente da memória.
      write(memByte0, address);
      write(memByte1, address + 1);
    }
    inFile.close();
    return Integer.toHexString(firstAddr);
  }

  /**
   * Retorna o conteúdo de um trecho da memória em formato texto e hexadecimal
   *
   * @param ini_address endereço do início do trecho
   * @param fim_address endereço do fim do trecho
   * @return conteúdo da memória em formato texto e hexadecimal
   */
  public String toString(int ini_address, int end_address) {
    // TODO
    // Verificar se os endereços são válidos. Se não forem, retornar null.

    int addr = ini_address;
    while(addr <= end_address){
      if (addr < MIN_ADDRESS || addr > MAX_ADDRESS) 
        return null;
      addr += 1;
    }

    String resp = new String();

    for(addr = ini_address; addr <= end_address; addr++){
      resp += store[addr].toString() + " " + store[addr].toHexString() + " \n";
    }
    
    return resp;
  }

  /**
   * Converte o conteúdo da memória em uma string formatada e base hexadecimal.
   *
   * @return O conteúdo da memória em uma String formatada.
   */
  public String toString() {
    
    String resp = new String();

    int addr;
    for(addr = MIN_ADDRESS; addr <= MAX_ADDRESS; addr++){
      resp += store[addr].toString() + " " + store[addr].toHexString() + " \n";
    }
    
    return resp;
  }

  /**
   * Verifica se o endereço é válido. Usado internamente por memRead() e
   * memWrite() (por isso privado).
   *
   * @param address
   *            O endereço a ser validado.
   * @return Verdadeiro se o endereço for válido e falso caso contrário.
   */
  private boolean validAddress(int address) {
     if (address < MIN_ADDRESS || address > MAX_ADDRESS) 
        return false;
    return true;
  }

}//Memoria.java
