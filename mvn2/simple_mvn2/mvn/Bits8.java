package mvn;

import java.util.*;

/**
 * Representação de um Byte para a MVN.
 *
 * @author PSMuniz
 * @version 1.0 - PCS/EPUSP
 * @viz.diagram Bits8.tpx
 */
public class Bits8 extends BitSet {

	/**
	 * Constrói um Bits8 com o determinado valor inteiro.
	 */
	public Bits8(int x) {

		super(8); // pattern of 8 bits, using constructor for BitSet
		for (int pos = 0; pos < 8; pos++) {
			super.clear(pos);
		} // 'clear' is a method in class BitSet
		int quot = x;
		int rem = 0;
		int position = 7;
		while (quot > 0 && position >= 0) {
			rem = quot % 2;
			if (rem == 1)
				super.set(position); // 'set' is a method in class BitSet
			quot = quot / 2;
			position--;
		}
		// Esse workaround foi necessário para contabilizar conversão negativa
		if (x < 0) {
			super.set(0);
		}

	} // end of constuctor 'Bits8'

	/**
	 * Acerta todos os bits em '0'. <b>Pós-condição </b>: o Bits8 está resetado.
	 */
	public void reset() {
		for (int pos = 0; pos < 8; pos++) {
			super.clear(pos);
		} // 'clear' is a method in class BitSet
	}

	/**
	 * Adiciona p ao objeto Bits8. <b>Pré-condição </b>: p não deve ser nulo.
	 * <br>
	 * <b>Pós-condição </b>: o objeto tem seu valor somado à p.
	 *
	 * @param p
	 *            O objeto a ser somado.
	 */
	public void add(Bits8 p) {
		boolean carry = false;
		boolean bit1, bit2, oneSet, bothSet, neitherSet;
		for (int i = 7; i >= 0; i--) {
			bit1 = this.get(i);
			bit2 = p.get(i);
			oneSet = bit1 ^ bit2;
			bothSet = bit1 && bit2;
			neitherSet = !bit1 && !bit2;
			if ((oneSet && !carry) || (bothSet && carry)
					|| (neitherSet && carry))
				this.set(i);
			else
				this.clear(i);
			carry = ((oneSet && carry) || bothSet);
		}

	} // End of method 'add'

	/**
	 * Realiza uma rotação <i>times </i> vezes uma rotação à direita.
	 * <b>Pós-condição </b>: o valor é rodado a direita <i>times </i> vezes.
	 *
	 * @param times
	 *            O número de vezes que o valor é rodado à direita.
	 */
	public void rotate(int times) {
		for (int i = 0; i < times; i++) {
			this.rotate();
		}

	} // End of method 'rotate'

	/**
	 * Realiza um passo de "rotação à direita". <b>Pós-condição </b>: o valor é
	 * rodado a direita.
	 */
	private void rotate() {
		boolean temp = this.get(7);
		for (int i = 6; i >= 0; i--) {
			if (this.get(i)) // 'get' is a method from BitSet
				this.set(i + 1);
			else
				this.clear(i + 1);
		}
		if (temp)
			this.set(0);
		else
			this.clear(0);
	} //  End of method 'rotate'

	/**
	 * Cria uma cópia do Bits8. <br>
	 * Esse método é necessário já que a atribuição em Java apenas repassa o
	 * ponteiro do objeto em questão. Dessa forma em alguns casos é necessário
	 * realizar uma cópia idêntica ao objeto em questão.
	 *
	 * @return Uma cópia do Bits8.
	 */
	public Bits8 makeCopy() {
		Bits8 copy = new Bits8(0);
		for (int i = 0; i < 8; i++)
			if (this.get(i))
				copy.set(i);
		return copy;

	} // End of method 'makeCopy'

	/**
	 * Converte o Bits8 a um valor inteiro correspondente.
	 */
	public int toInt() {
		int x = 0;
		for (int i = 0; i < 8; i++) {
			if (this.get(i))
				x = 2 * x + 1;
			else
				x = 2 * x;
		}
		return x;

	} // End of method 'toInt'

	/**
	 * Converte para uma string binaria (bits) a representaçao de um valor em
	 * inteiro.
	 */
	public String toBinaryString() {
		boolean bit;
		String bin = "", repBit = "";

		for (int pos = 0; pos < 8; ++pos) {
			bit = super.get(pos);
			if (bit)
				repBit = "1";
			else
				repBit = "0";
			bin += repBit;
		}
		return bin;
	} // Final de toBinaryString

	/**
	 * Converte para um byte (Java) uma string binaria (bits).
	 */
	public byte binaryString2Byte() {
		boolean bit;
		String bin = "", repBit = "";

		for (int pos = 0; pos < 8; ++pos) {
			bit = super.get(pos);
			if (bit)
				repBit = "1";
			else
				repBit = "0";
			bin += repBit;
		}
		return (Byte.valueOf(bin).byteValue());
	} // Final de toBinaryString

	/**
	 * Converte para uma representação em String hexadecimal. <br>
	 * Os zeros à esquerda não são representados.
	 */
	public String toHexString() {
		return Integer.toHexString(this.toInt());

	} // End of method toHexString

	/**
	 * Converte para uma representação de dois dígitos hexadecimal. <br>
	 * Se necessário será colocado zeros à esquerda.
	 */
	public String toHexString2() {
		int val = this.toInt();
		String s;
		if (val < 16) // then first digit is a zero
			s = "0" + Integer.toHexString(val);
		else
			s = Integer.toHexString(val);
		return s;

	} // End of method toHexString2

	/**
	 * Converte para uma String usando o método <i>toHexString2() </i>.
	 */
	public String toString() {
		return toHexString2();
	}

} // End of class 'Bits8'
