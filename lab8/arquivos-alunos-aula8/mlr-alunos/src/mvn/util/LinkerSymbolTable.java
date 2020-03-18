package mvn.util;
import java.util.HashMap;
import java.util.Map;


/**
 * Tabela de símbolos para uso do Linker.
 * @author FLevy
 * @version 14.11.2005
 * @version 01.01.2010 (refatoramento - Tiago) : introdução generics
 */
public class LinkerSymbolTable extends SymbolTable {

    /**Mapeia se o simbolo é relocavel ou não*/
    protected Map<String, Boolean> isRelocableTable; //
    
    /**Mapeia um código de um arquivo a um simbolo*/
    protected Map<String, String> codeTable; // 

    public LinkerSymbolTable() {
        super();
        isRelocableTable = new HashMap<String, Boolean>(); 
        codeTable = new HashMap<String, String>();
    }
    
    /**
     * Define um valor para um símbolo.<br>
     * O símbolo já deve existir na tabela.
     *
     * @param sym
     *            O símbolo.
     * @param address
     *            O valor.
     * @param relocable Se o símbolo é relocável ou não.
     * @return Verdadeiro caso a definição tenha obtido sucesso, falso caso
     *         contrário.
     */
    public boolean setSymbolValue(String sym, String address, boolean relocable) {
        boolean result = false;
        if(this.symbolInTable(sym.toUpperCase())){
          super.setSymbolValue(sym.toUpperCase(), address);
          isRelocableTable.put(sym.toUpperCase(), relocable);
          result = true;
        }
        return result;
    }
    
    @Override
    public boolean setSymbolValue(String symbol, String address) {
    	if (super.setSymbolValue(symbol.toUpperCase(), address)) {
    		isRelocableTable.put(symbol.toUpperCase(), false);
    		return true;
    	} else return false;
    }

    /**
     * Define um código numérico para um determinado símbolo.<br>
     * @param sym O símbolo.
     * @param file O arquivo que define esse código para o simbolo.
     * @param code O código numérico para o símbolo.
     * @return Verdadeiro caso a definição tenha obtido sucesso, falso caso
     * contrário.
     */
    public boolean setCodeForSymbol(String sym, String file, int code) {
      super.insertSymbol(sym.toUpperCase());
      if (codeTable.containsKey(file + code)) {
        return false;
      }
      codeTable.put(file + code, sym.toUpperCase());
      return true;
    }

    /**
     * Obtêm o endereço de um símbolo a partir de um código de um arquivo.
     * @param file O arquivo.
     * @param code O código.
     * @return O endereço do símbolo.
     */
    public String getAddressByCode(String file, int code) {
      String simbolo = getSymbol(file, code);
      if (simbolo == null) return null;
      return super.getSymbolValue(simbolo.toUpperCase());
    }

    /**
     * Obtêm o símbolo referente ao determinado código de um arquivo.
     * @param file O arquivo.
     * @param code O código.
     * @return O símbolo que é referente a esse código.
     */
    public String getSymbol(String file, int code) {
      return (String) codeTable.get(file + code);
    }

    /**
     * Verifica se o símbolo é relocável.
     * @param sym O símbolo o qual se deseja saber se é relocável.
     * @return Verdadeiro caso ele seja relocável.
     */
    public boolean isRelocable(String sym) {
      Boolean result = (Boolean) isRelocableTable.get(sym.toUpperCase());
      if (result == null) {
        return false;
      }
      return result.booleanValue();
    }

    /**
     * Verifica se o símbolo é relocável a partir de um código de um arquivo.
     * @param file O arquivo.
     * @param code O código.
     * @return Verdadeiro caso ele seja relocável.
     */
    public boolean isRelocable(String file, int code) {
      return isRelocable(getSymbol(file, code));
    }
}
