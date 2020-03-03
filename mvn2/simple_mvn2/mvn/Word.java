package mvn;

/**
 * Esta classe implementa a abstraçao Palavra, contendo 2 Bits8 (2 bytes).
 *
 * @author PSMuniz
 * @version 1.0 - PCS/EPUSP.
 * @viz.diagram Word.tpx
 */

public class Word {

	private Bits8 byte0;
	private Bits8 byte1;

	/**
	 * Construtor default.
	 */
	public Word() {
		byte0 = new Bits8(0);
		byte1 = new Bits8(0);
	}

	/**
	 * Construtor: a partir de 2 bytes.
	 *
	 * @param byt0
	 *            O byte alto.
	 * @param byt1
	 *            O byte baixo.
	 */
	public Word(Bits8 byt0, Bits8 byt1) {
		byte0 = byt0.makeCopy();
		byte1 = byt1.makeCopy();
	}

	/**
	 * Acertar em binario uma string em hexa de 2 bytes: XYZT, em que X, Y, Z, T
	 * sao nibbles. <br>
	 * <b>Pré-condição </b>: a String <i>hexa </i> não seja nula e esteja em
	 * formato hexadecimal com 2 bytes. <br>
	 * <b>Pós-condição </b>: a palavra será o valor da String hexadecimal.
	 *
	 * @param hexa
	 *            A String em formato hexadecimal.
	 */
	public void setHex(String hexa) {
		// Testa inicialmente o numero de caracteres de hexa16 e trata-os
		// adequadamente.
		int tamanho = hexa.length();
		StringBuffer buf = new StringBuffer(hexa);

		if (tamanho < 4) {
			buf.reverse();
			for (int i = 0; i < (4 - tamanho); ++i)
				buf.append('0');
			hexa = buf.reverse().toString();
		}
		// Decompõe hexa16 em dois bytes e converte-os para a representacao
		// interna.
		String byt0 = hexa.substring(0, 2);
		String byt1 = hexa.substring(2, 4);
		// String byts = byt0 + " " + byt1;
		int dec0 = Integer.valueOf(byt0, 16).intValue();
		int dec1 = Integer.valueOf(byt1, 16).intValue();
		Bits8 b0 = new Bits8(dec0);
		Bits8 b1 = new Bits8(dec1);
		byte0 = b0.makeCopy();
		byte1 = b1.makeCopy();
	}

	/**
	 * Acertar os bytes para um inteiro. <br>
	 * <b>Pré-condição </b>: o valor inteiro esteja entre 0 e 65535. <br>
	 * <b>Pós-condição </b>: o valor da palavra será o correspondente ao
	 * inteiro.
	 *
	 * @param valor
	 *            O valor inteiro entre 0 e 65535.
	 */
	public void setInt(int valor) {
		// Converte o valor para uma string hexa de 2 bytes (palavra)
		String hexstr = Integer.toHexString(0x10000 | valor).substring(1);
		StringBuffer buf = new StringBuffer(hexstr);
		buf.reverse();
		buf = buf.delete(4, buf.length());
		hexstr = buf.reverse().toString();

		// Converte para a representacao interna.
		this.setHex(hexstr);
	}

	/**
	 * Clonagem da palavra. <br>
	 *
	 * @return Uma cópia da palavra.
	 */
	public Word makeCopy() {
		Word copia = new Word();
		int valPalavra = this.getInt();
		copia.setInt(valPalavra);

		return copia;
	}

	/**
	 * Obtêm o valor da palavra em inteiro.
	 *
	 * @return O valor da palavra em inteiro.
	 */
	public int getInt() {
		String byt00 = byte0.toBinaryString();
		String byt01 = byte1.toBinaryString();
		String pal = byt00 + byt01;

		// Converte a palavra para inteiro com sinal.
		int x = 0;
		for (int i = 0; i < 16; i++) {
			if (pal.charAt(i) == '1')
				x = 2 * x + 1;
			else
				x = 2 * x;
		}
		if (pal.charAt(0) == '1')
			x -= 65536;

		return x;
	}

	/**
	 * Obtêm o valor da palavra em formato String hexadecimal.
	 *
	 * @return A palavra em String hexadecimal.
	 */
	public String getHex() {
		String byt0 = byte0.toHexString2();
		String byt1 = byte1.toHexString2();
		String palavra = byt0 + byt1;

		return palavra;
	}

	/**
	 * Obtêm o valor do byte alto (byte0) em string hexa.
	 *
	 * @return O byte alto (byte0) em String Hexa.
	 */
	public String getByte0Hex() {
		String byt0 = byte0.toHexString2();

		return byt0;
	}

	/**
	 * Obtêm o valor do byte1 em string hexa.
	 *
	 * @return O byte baixo (byte1) em String Hexa.
	 */
	public String getByte1Hex() {
		String byt1 = byte1.toHexString2();

		return byt1;
	}

	/**
	 * Obtêm o valor do byte alto (byte0) em Bits8
	 *
	 * @return O byte alto (byte0) em Bits8.
	 */
	public Bits8 getByte0() {
		return this.byte0;
	}

	/**
	 * Obter o valor do byte baixo (byte1) em Bits8
	 *
	 * @return O byte baixo (byte1) em Bits8.
	 */
	public Bits8 getByte1() {
		return this.byte1;
	}

	/**
	 * Obtêm o valor em inteiro de um determinado nibble [0..3]. <br>
	 * <b>Pré-condição </b>: o valor do nibble deve ser de 0 a 3.
	 *
	 * @return o valor inteiro do nibble.
	 */
	public int getNibbleInt(int nibble) {
		int retNibble = 0;
		String byt0 = byte0.toHexString2();
		String byt1 = byte1.toHexString2();

		// OBS: Proteger o código: nibble [0..3]
		if (nibble == 0)
			retNibble = Integer.valueOf(byt0.substring(0, 1), 16).intValue();
		else if (nibble == 1)
			retNibble = Integer.valueOf(byt0.substring(1, 2), 16).intValue();
		else if (nibble == 2)
			retNibble = Integer.valueOf(byt1.substring(0, 1), 16).intValue();
		else if (nibble == 3)
			retNibble = Integer.valueOf(byt1.substring(1, 2), 16).intValue();

		return retNibble;
	}

	/**
	 * Obtêm o valor de um determinado nibble [0..3] em string. <b>Pré-condição
	 * </b>: o valor do nibble deve ser de 0 a 3.
	 *
	 * @return o valor em String do nibble.
	 */
	public String getNibbleHex(int nibble) {
		String retNibble = "";
		String byt0 = byte0.toHexString2();
		String byt1 = byte1.toHexString2();

		// OBS: Proteger o código: nibble [0..3]
		if (nibble == 0)
			retNibble = byt0.substring(0, 1);
		else if (nibble == 1)
			retNibble = byt0.substring(1, 2);
		else if (nibble == 2)
			retNibble = byt1.substring(0, 1);
		else
			retNibble = byt1.substring(1, 2);

		return retNibble;
	}

}//Word.java
