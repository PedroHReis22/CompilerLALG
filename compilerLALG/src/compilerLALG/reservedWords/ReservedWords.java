package compilerLALG.reservedWords;

import java.util.ArrayList;

public class ReservedWords {
	
	/**
	 * Retorna um ArrayList contendo os delimitadores da linguagem
	 * 
	 * @return Um ArrayList contendo os delimitadores da linguagem
	 * 
	 * @see java.util.ArrayList
	 */
	public static ArrayList<String> getSpecialSymbols() {
		
		ArrayList<String> specialSymbols = new ArrayList<String>();
		
		specialSymbols.add(";");
		specialSymbols.add(".");
		specialSymbols.add(",");
		specialSymbols.add(":=");
		specialSymbols.add(":");
		specialSymbols.add("(");
		specialSymbols.add(")");
		specialSymbols.add("{");
		specialSymbols.add("}");
		specialSymbols.add("//");
		
		specialSymbols.add("=");
		specialSymbols.add("<>");
		specialSymbols.add("<");
		specialSymbols.add("<=");
		specialSymbols.add(">");
		specialSymbols.add(">=");
		specialSymbols.add("+");
		specialSymbols.add("-");
		specialSymbols.add("*");
		
		return specialSymbols;
	}
	
	/**
	 * Retorna um ArrayList contendo as palavras reservadas da linguagem
	 * 
	 * @return Um ArrayList contendo as palavras reservadas da linguagem
	 * 
	 * @see java.util.ArrayList
	 */
	public static ArrayList<String> getReservedWords() {
		
		ArrayList<String> reservedWords = new ArrayList<String>();
		
		reservedWords.add("program");
		reservedWords.add("integer");
		reservedWords.add("real");
		reservedWords.add("procedure");
		reservedWords.add("var");
		reservedWords.add("begin");
		reservedWords.add("end");		
		reservedWords.add("then");
		reservedWords.add("else");
		reservedWords.add("while");
		reservedWords.add("do");
		reservedWords.add("or");
		reservedWords.add("and");
		reservedWords.add("div");
		reservedWords.add("not");
		reservedWords.add("if");
		
		return reservedWords;
		
	}

}
