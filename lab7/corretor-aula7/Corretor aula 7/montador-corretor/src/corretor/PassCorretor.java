package corretor;

import assemblerException.AssemblerException;
import montador.Pass;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by deborasetton on 14/03/16.
 */
public class PassCorretor extends Pass {

    public int passCount = 0;
    public int failCount = 0;

    public void corrigir() {
        try {
            testGetDecNumber256();
            testGetDecNumber16();
            testGetDecNumber10();
            testGetDecNumber8();
            testGetDecNumber2();
            testInvalidExamples();
        } catch (AssemblerException e) {
            e.printStackTrace();
        }

        double nota = ((float) (passCount)) / (passCount + failCount);
        System.out.printf("Nota: %.3f\n", nota);
    }

//    GG  K  'ER         ; Base 256 (valor = 'E'*256^1 + 'R'*256^0)
//    GH  K  'RO         ; Base 256
//    GJ  K  =1000       ; Base 10 (1000 em decimal)
//    GK  K  /3E8        ; Base 16 (equivalente a 0x3E8)
//    GL  K  @1750       ; Base 8
//    GÇ  K  #1111101000 ; Base 2 (1111101000 em binário)

    private void testGetDecNumber256() throws AssemblerException {
        String str;
        int expected;
        int actual;

        str = "'A";
        expected = 65;
        if ((actual = getDecNumber(str)) == expected) {
            System.out.printf("[v] [testGetDecNumber256] Esperado: %s => %d. Obtido: %d. Ok.\n", str, expected, actual);
            passCount++;
        } else {
            System.out.printf("[x] [testGetDecNumber256] Esperado: %s => %d. Obtido: %d. Incorreto.\n", str, expected, actual);
            failCount++;
        }

        str = "'ER";
        expected = 17746;
        if ((actual = getDecNumber(str)) == expected) {
            System.out.printf("[v] [testGetDecNumber256] Esperado: %s => %d. Obtido: %d. Ok.\n", str, expected, actual);
            passCount++;
        } else {
            System.out.printf("[x] [testGetDecNumber256] Esperado: %s => %d. Obtido: %d. Incorreto.\n", str, expected, actual);
            failCount++;
        }

        str = "'ABC";
        expected = 4276803;
        if ((actual = getDecNumber(str)) == expected) {
            System.out.printf("[v] [testGetDecNumber256] Esperado: %s => %d. Obtido: %d. Ok.\n", str, expected, actual);
            passCount++;
        } else {
            System.out.printf("[x] [testGetDecNumber256] Esperado: %s => %d. Obtido: %d. Incorreto.\n", str, expected, actual);
            failCount++;
        }
    }

    private void testGetDecNumber16() throws AssemblerException {
        String str;
        int expected;
        int actual;

        str = "/3E8";
        expected = 1000;
        if ((actual = getDecNumber(str)) == expected) {
            System.out.printf("[v] [testGetDecNumber16] Esperado: %s => %d. Obtido: %d. Ok.\n", str, expected, actual);
            passCount++;
        } else {
            System.out.printf("[x] [testGetDecNumber16] Esperado: %s => %d. Obtido: %d. Incorreto.\n", str, expected, actual);
            failCount++;
        }

        str = "/1";
        expected = 1;
        if ((actual = getDecNumber(str)) == expected) {
            System.out.printf("[v] [testGetDecNumber16] Esperado: %s => %d. Obtido: %d. Ok.\n", str, expected, actual);
            passCount++;
        } else {
            System.out.printf("[x] [testGetDecNumber16] Esperado: %s => %d. Obtido: %d. Incorreto.\n", str, expected, actual);
            failCount++;
        }

        str = "/1020";
        expected = 4128;
        if ((actual = getDecNumber(str)) == expected) {
            System.out.printf("[v] [testGetDecNumber16] Esperado: %s => %d. Obtido: %d. Ok.\n", str, expected, actual);
            passCount++;
        } else {
            System.out.printf("[x] [testGetDecNumber16] Esperado: %s => %d. Obtido: %d. Incorreto.\n", str, expected, actual);
            failCount++;
        }
    }

    /**
     *
     * Testes para Base 10.
     * @throws AssemblerException
     *
     */
    private void testGetDecNumber10() throws AssemblerException {
        String str;
        int expected;
        int actual;

        str = "=10";
        expected = 10;
        if ((actual = getDecNumber(str)) == expected) {
            System.out.printf("[v] [testGetDecNumber10] Esperado: %s => %d. Obtido: %d. Ok.\n", str, expected, actual);
            passCount++;
        } else {
            System.out.printf("[x] [testGetDecNumber10] Esperado: %s => %d. Obtido: %d. Incorreto.\n", str, expected, actual);
            failCount++;
        }

        str = "=58";
        expected = 58;
        if ((actual = getDecNumber(str)) == expected) {
            System.out.printf("[v] [testGetDecNumber10] Esperado: %s => %d. Obtido: %d. Ok.\n", str, expected, actual);
            passCount++;
        } else {
            System.out.printf("[x] [testGetDecNumber10] Esperado: %s => %d. Obtido: %d. Incorreto.\n", str, expected, actual);
            failCount++;
        }

        str = "=678";
        expected = 678;
        if ((actual = getDecNumber(str)) == expected) {
            System.out.printf("[v] [testGetDecNumber10] Esperado: %s => %d. Obtido: %d. Ok.\n", str, expected, actual);
            passCount++;
        } else {
            System.out.printf("[x] [testGetDecNumber10] Esperado: %s => %d. Obtido: %d. Incorreto.\n", str, expected, actual);
            failCount++;
        }
    }

    /**
     *
     * Testes para Base 8.
     * @throws AssemblerException
     *
     */
    private void testGetDecNumber8() throws AssemblerException {
        String str;
        int expected;
        int actual;

        str = "@1750";
        expected = 1000;
        if ((actual = getDecNumber(str)) == expected) {
            System.out.printf("[v] [testGetDecNumber8] Esperado: %s => %d. Obtido: %d. Ok.\n", str, expected, actual);
            passCount++;
        } else {
            System.out.printf("[x] [testGetDecNumber8] Esperado: %s => %d. Obtido: %d. Incorreto.\n", str, expected, actual);
            failCount++;
        }

        str = "@10";
        expected = 8;
        if ((actual = getDecNumber(str)) == expected) {
            System.out.printf("[v] [testGetDecNumber8] Esperado: %s => %d. Obtido: %d. Ok.\n", str, expected, actual);
            passCount++;
        } else {
            System.out.printf("[x] [testGetDecNumber8] Esperado: %s => %d. Obtido: %d. Incorreto.\n", str, expected, actual);
            failCount++;
        }

        str = "@567";
        expected = 375;
        if ((actual = getDecNumber(str)) == expected) {
            System.out.printf("[v] [testGetDecNumber8] Esperado: %s => %d. Obtido: %d. Ok.\n", str, expected, actual);
            passCount++;
        } else {
            System.out.printf("[x] [testGetDecNumber8] Esperado: %s => %d. Obtido: %d. Incorreto.\n", str, expected, actual);
            failCount++;
        }
    }

    /**
     *
     * Testes para Base 2.
     * @throws AssemblerException
     *
     */
    private void testGetDecNumber2() throws AssemblerException {
        String str;
        int expected;
        int actual;

        str = "#1111101000";
        expected = 1000;
        if ((actual = getDecNumber(str)) == expected) {
            System.out.printf("[v] [testGetDecNumber2] Esperado: %s => %d. Obtido: %d. Ok.\n", str, expected, actual);
            passCount++;
        } else {
            System.out.printf("[x] [testGetDecNumber2] Esperado: %s => %d. Obtido: %d. Incorreto.\n", str, expected, actual);
            failCount++;
        }

        str = "#10";
        expected = 2;
        if ((actual = getDecNumber(str)) == expected) {
            System.out.printf("[v] [testGetDecNumber2] Esperado: %s => %d. Obtido: %d. Ok.\n", str, expected, actual);
            passCount++;
        } else {
            System.out.printf("[x] [testGetDecNumber2] Esperado: %s => %d. Obtido: %d. Incorreto.\n", str, expected, actual);
            failCount++;
        }

        str = "#1101";
        expected = 13;
        if ((actual = getDecNumber(str)) == expected) {
            System.out.printf("[v] [testGetDecNumber2] Esperado: %s => %d. Obtido: %d. Ok.\n", str, expected, actual);
            passCount++;
        } else {
            System.out.printf("[x] [testGetDecNumber2] Esperado: %s => %d. Obtido: %d. Incorreto.\n", str, expected, actual);
            failCount++;
        }
    }

    /**
     *
     * Testes para exemplos inválidos.
     * @throws AssemblerException
     *
     */
    private void testInvalidExamples() throws AssemblerException {
        String str;
        int expected;
        int actual;

        boolean threwException =  false;

        String[] examples =  {
          "=1A",
          "/10G",
          "@9",
          "#012"
        };

        for (int i = 0; i < examples.length; i++) {
            threwException = false;
            try {
                getDecNumber(examples[i]);
            }
            catch (NumberFormatException ex) {
                threwException = true;
            }

            if (threwException) {
                System.out.printf("[v] [testInvalidExamples] Exceção lançada para exemplo inválido: %s\n", examples[i]);
                passCount++;
            }
            else {
                System.out.printf("[v] [testInvalidExamples] Exceção NÃO foi lançada para exemplo inválido: %s\n", examples[i]);
                failCount++;
            }
        }
    }

    @Override
    protected boolean analyzeLine(ArrayList<String> symbols, String line) throws IOException {
        return false;
    }

    protected void processComment(String data) throws IOException {
        // Do nothing.
    }

    @Override
    public String getIOErrorMessage() {
        return null;
    }

    @Override
    public String getASMErrorMessage() {
        return null;
    }

}
