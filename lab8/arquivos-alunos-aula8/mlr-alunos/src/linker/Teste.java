package linker;


import mvn.util.LinkerSymbolTable;

public class Teste {

	public static void main(String args[]) {
		
		int addressInt;
		String address, temp;
		
	    LinkerSymbolTable symbolTable = new LinkerSymbolTable();
		
		
		addressInt = 10;
		address = Integer.toHexString(addressInt).toUpperCase();
		
		String code = "9001";
		
		symbolTable.setCodeForSymbol("SAIDA", "0", 1);
		symbolTable.setSymbolValue("SAIDA", "006", true);
		temp = symbolTable.getAddressByCode("0", Integer.parseInt(code.substring(1)));
		
		System.out.println(temp == "");
		System.out.println("temp = "+temp);
		code = code.charAt(0)+temp;
		System.out.println("code = "+code);
		
		
	}
	
}
