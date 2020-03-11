package util;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Representação de uma tabela de símbolos relocáveis.<br>
 * O símbolo é considerado igual independente da presença de letras minúsculas
 * ou maiúsculas diferentes em outras chamadas (<i>case insensitive</i>).
 *
 * @author TMatos
 * @version 07.07.2010 : versão inicial para a disciplina
 *
 */
public class RelocatedSymbolTable extends SymbolTable {

    /**Tabela que indica quais símbolos são relocaveis ou não*/
    private Map<String, Boolean> relocable;
    /**Tabela que indica quais símbolos são externos (<b>true</b>) ou internos (<b>false</b>)*/
    private Map<String, Boolean> external;

    public RelocatedSymbolTable() {
        //TODO Aula 09 : RelocatedSymbolTable
    }//c

    @Override
    public boolean definedSymbol(String sym) {
        //TODO Aula 09
        throw new NotImplementedException();
    }

    @Override
    public String getSymbolValue(String sym) {
        //TODO Aula 09
        throw new NotImplementedException();
    }

    @Override
    public boolean insertSymbol(String sym) {
        //TODO Aula 09
        throw new NotImplementedException();
    }//m

    /**
     * Define um valor para um símbolo.<br>
     * <b><font color="red">O símbolo já deve existir na tabela.</font><b>
     *
     * @param sym O símbolo.
     *
     * @param address O valor.
     *
     * @param isRelocated indica se o símbolo é relocavel.
     *
     * @param isExternal indica se o símbolo é externo.
     *
     * @return <b>true</b> caso a definição tenha obtido sucesso, <b>false</b> caso contrário.
     *
     * @throws NullPointerException se acaso a key (sym) ou valor(address) forem nulos (<b>null</b>).
     */
    public boolean setSymbolValue(String sym, String address, boolean isRelocated, boolean isExternal) {
        //TODO Aula 09
        throw new NotImplementedException();
    }

    /**
     * Indica se o símbolo é relocável.
     *
     * @return <b>true</b> se o símbolo é relocável ou <b>false</b> caso contrário.
     *
     *
     */
    public boolean isRelocable(String sym) {
        //TODO Aula 09
        throw new NotImplementedException();
    }//m

    /**
     * Indica se o símbolo é externo.
     *
     * @return <b>true</b> se o símbolo é externo ou <b>false</b> caso contrário.
     *
     */
    public boolean isExternal(String sym) {
        //TODO Aula 09
        throw new NotImplementedException();
    }//m

    @Override
    public boolean symbolInTable(String sym) {
        //TODO Aula 09
        throw new NotImplementedException();
    }

    @Override
    public String toString() {
        //TODO Aula 09
        throw new NotImplementedException();
    }//m
}//classe

