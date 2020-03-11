package corretor;

import util.SymbolTable;

import java.lang.reflect.Field;

/**
 * Created by deborasetton on 14/03/16.
 */
public class MvnMontadorCorretor {

    private int passCount = 0;
    private int failCount = 0;

    public static void main(String[] args) {
        System.out.println("Corrigindo as classes do montador.");

        MvnMontadorCorretor corretor = new MvnMontadorCorretor();

        System.out.println("\nCorreção da classe SymbolTable:");

        corretor.testConstrutor();
        corretor.testSetSymbolValue();
        corretor.testGetSymbolValue();
        corretor.testInsertSymbol();
        corretor.testDefinedSymbol();
        corretor.testSymbolInTable();

        double nota = ((float) (corretor.passCount)) / (corretor.passCount + corretor.failCount);
        System.out.printf("Nota: %.3f\n", nota);

        System.out.println("\nCorreção da classe Pass:");
        PassCorretor passCorretor = new PassCorretor();
        passCorretor.corrigir();

        System.out.println("\nCorreção da classe Pass1:");
        Pass1Corretor pass1Corretor = new Pass1Corretor();
        pass1Corretor.corrigir();

    }

    /**
     * Teste do construtor.
     */
    private boolean testConstrutor() {
        SymbolTable tab = new SymbolTable();
        Class symbolTableKlass = tab.getClass();

        try {
            Field field = symbolTableKlass.getDeclaredField("symbolTable");
            field.setAccessible(true);

            Object obj = field.get(tab);

            if (obj == null) {
                System.out.println("[X] [testConstrutor] Tabela de símbolos não foi inicializada");
                failCount++;
                return false;
            }

            System.out.println("[v] [testConstrutor] Tabela de símbolos foi inicializada");
            passCount++;
            return true;

        } catch (NoSuchFieldException e) {
            System.out.println("[X] [testConstrutor] Campo 'symbolTable' não existe na classe util.SymbolTable");
            e.printStackTrace();
            failCount++;
            return false;
        } catch (IllegalAccessException e) {
            System.out.println("[X] [testConstrutor] Não foi possível acessar o campo 'symbolTable'.");
            e.printStackTrace();
            failCount++;
            return false;
        }
    }

    private void testSymbolInTable() {
        // Testar se retorna true quando o símbolo está, mas não está definido.
        // Testar se retorna true quando o símbolo está e está definido.
        // Testar se retorna false quando o símbolo não está na tabela.

        SymbolTable tab = new SymbolTable();
        boolean result;

        if (!(result = tab.symbolInTable("STRING"))) {
            System.out.println("[v] [testSymbolInTable] Retorna falso caso o símbolo não esteja na tabela");
            passCount++;
        } else {
            System.out.println("[x] [testSymbolInTable] Não retornou falso para um símbolo que não estava na tabela");
            failCount++;
        }

        tab.insertSymbol("STRING");fset

        if (result = tab.symbolInTable("STRING")) {
            System.out.println("[v] [testSymbolInTable] Retorna falso caso o símbolo esteja na tabela (mesmo indefinido)");
            passCount++;
        } else {
            System.out.println("[x] [testSymbolInTable] Não retornou falso para um símbolo que estava inserido, mas não definido");
            failCount++;
        }
	tab.insertSymbol("SYMBOL");         // corrigido por Beogival
        tab.setSymbolValue("SYMBOL", "VALUE");

        if (result = tab.symbolInTable("SYMBOL")) {
            System.out.println("[v] [testSymbolInTable] Retorna verdadeiro caso o símbolo esteja definido");
            passCount++;
        } else {
            System.out.println("[x] [testSymbolInTable] Não retornou verdadeiro para um símbolo que estava definido");
            failCount++;
        }
    }

    private void testSetSymbolValue() {
        SymbolTable tab = new SymbolTable();

        boolean result = tab.setSymbolValue("TEST_SYMBOL_1", "1234");

        if (!result) {
            System.out.println("[v] [testSetSymbolValue] Retorna falso se o símbolo ainda não estava na tabela");
            passCount++;
        } else {
            System.out.println("[x] [testSetSymbolValue] Não retornou falso para um símbolo que ainda não estava na tabela");
            failCount++;
        }

        tab.insertSymbol("TEST_SYMBOL_2");
        result = tab.setSymbolValue("TEST_SYMBOL_2", "1234");

        if (result) {
            System.out.println("[v] [testSetSymbolValue] Definiu o valor de TEST_SYMBOL_1");
            passCount++;
        } else {
            System.out.println("[x] [testSetSymbolValue] Não definiu o valor de TEST_SYMBOL_1");
            failCount++;
        }

    }

    private void testGetSymbolValue() {
        SymbolTable tab = new SymbolTable();

        String sym = "TEST_SYMBOL_2";
        String expected = "1234";
					// corrigido por Beogival
	tab.insertSymbol(sym);
        tab.setSymbolValue(sym, "1234");
        String actual = tab.getSymbolValue(sym);

        if (expected.equals(actual)) {
            System.out.println("[v] [testGetSymbolValue] Recuperou corretamente o valor do símbolo");
            passCount++;
        } else {
            System.out.println("[x] [testGetSymbolValue] Não recuperou corretamente o valor do símbolo");
            failCount++;
        }
    }

    private void testDefinedSymbol() {

        SymbolTable tab = new SymbolTable();
        boolean result;

        if (!(result = tab.definedSymbol("STRING"))) {
            System.out.println("[v] [testDefinedSymbol] Retorna falso caso o símbolo não esteja definido");
            passCount++;
        } else {
            System.out.println("[x] [testDefinedSymbol] Não retornou falso para um símbolo que não estava definido");
            failCount++;
        }

        tab.insertSymbol("STRING");

        if (!(result = tab.definedSymbol("STRING"))) {
            System.out.println("[v] [testDefinedSymbol] Retorna falso caso o símbolo não esteja definido");
            passCount++;
        } else {
            System.out.println("[x] [testDefinedSymbol] Não retornou falso para um símbolo que estava inserido, mas não definido");
            failCount++;
        }

        tab.insertSymbol("SYMBOL");
        tab.setSymbolValue("SYMBOL", "VALUE");

        if (result = tab.definedSymbol("SYMBOL")) {
            System.out.println("[v] [testDefinedSymbol] Retorna verdadeiro caso o símbolo esteja definido");
            passCount++;
        } else {
            System.out.println("[x] [testDefinedSymbol] Não retornou verdadeiro para um símbolo que estava definido");
            failCount++;
        }
    }

    /**
     *
     */
    private void testInsertSymbol() {
        SymbolTable tab = new SymbolTable();
        boolean result = tab.insertSymbol("TEST_SYMBOL");

        if (result) {
            System.out.println("[v] [testInsertSymbol] Inseriu TEST_SYMBOL na tabela vazia");
            passCount++;
        } else {
            System.out.println("[x] [testInsertSymbol] Não inseriu TEST_SYMBOL na tabela vazia");
            failCount++;
        }


        tab.insertSymbol("TEST_SYMBOL");
        tab.setSymbolValue("TEST_SYMBOL", "SOME_VALUE");
        result = tab.insertSymbol("TEST_SYMBOL");

        if (!result) {
            System.out.println("[v] [testInsertSymbol] Não inseriu símbolo duplicado TEST_SYMBOL na tabela.");
            passCount++;
        } else {
            System.out.println("[x] [testInsertSymbol] Inseriu símbolo duplicado TEST_SYMBOL na tabela.");
            failCount++;
        }
    }

}
