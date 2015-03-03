package compilerLALG.syntactic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import compilerLALG.errors.CompilerError;
import compilerLALG.lexical.Token;
import compilerLALG.reservedWords.ReservedWords;

/**
 * Classe responsável pela análise sintática sobre o código-fonte
 */
public class Syntactic {
			
	private Derivation[][] rules;
	private Stack<String> stack;
	
	private String simbol;
	private Token token;
		
	private ArrayList<CompilerError> syntaticErrors;
	
	private int tokensIndex;
	
	/**
	 * Criar o objeto para realizar a análise sintática do código-fonte
	 */
	public Syntactic() {
		rules = new Derivation[NotTerminal.TOTAL][Terminal.TOTAL];
		loadGrammar();
	}
	
	/**
	 * Carrega a gramática presente em /compilerLALG/grammar/grammar.xls em uma matriz
	 */
	private void loadGrammar() {
		
		try {
						
			@SuppressWarnings("resource")
			HSSFWorkbook workbook = new HSSFWorkbook(getClass().getResourceAsStream("/compilerLALG/grammar/grammar.xls"));
			HSSFSheet sheet = workbook.getSheetAt(0);
						
			for(int line = 0; line <= sheet.getLastRowNum(); ++line) {
				
				Row row = sheet.getRow(line);
								
				for(int column = 0; column < row.getLastCellNum(); ++column) {
										
					Cell cell = row.getCell(column, Row.CREATE_NULL_AS_BLANK);
					String s = cell.getStringCellValue().trim();
															
					if(!s.isEmpty() && !s.equals("sync")) {						
						String aux[] = s.split("->");
						rules[line][column] = new Derivation(aux[1].trim().split(" "));
					}
					else {
						if(s.isEmpty()) {
							rules[line][column] = new Derivation(s, Derivation.EMPTY);
						}
						else if(s.equals("sync")) {
							rules[line][column] = new Derivation(s, Derivation.SYNC);
						}
					}
										
				}
												
			}
			
			
		} catch (IOException e) { e.printStackTrace(); }
				
	}

	/**
	 * Executa a análise sintática sobre o conjunto de tokens resultantes da análise sintática  
	 * 
	 * @param tokens Os tokens resultantes da análise sintática
	 */
	public void execute(ArrayList<Token> tokens) {
		
		syntaticErrors = new ArrayList<CompilerError>();
					
		tokensIndex = 0;
				
		stack = new Stack<String>();
		stack.push("$");
		stack.push("<prog>");
		
		token = tokens.get(tokensIndex++);
		simbol = stack.pop();
								
		while(!stack.isEmpty()) {
									
			if(simbol.startsWith("<") && simbol.endsWith(">") && simbol.length() > 2){ //é um simbolo não terminal
				
				int nt = NotTerminal.getIndex(simbol);
				int t = Terminal.getIndex(token);
				
				derivate(rules[nt][t], tokens);
				
			}
			
			else {
								
				if(simbol.equals("@IDENTIFICADOR@") && token.getType() == Token.IDENTIFIER) { //derivação em um identificador
										
					if(tokensIndex < tokens.size()){
						token = tokens.get(tokensIndex++);
					}
					
					simbol = stack.pop();
										
				}
				
				else if(simbol.equals("@INT@") && token.getType() == Token.INTEGER_NUMBER) { //derivação de um número inteiro
										
					if(tokensIndex < tokens.size()){
						token = tokens.get(tokensIndex++);
					}
					
					simbol = stack.pop();
				}
				
				else if(simbol.equals("@REAL@") && token.getType() == Token.REAL_NUMBER) { //derivação de número real
					
					if(tokensIndex < tokens.size()){
						token = tokens.get(tokensIndex++);
					}
					
					simbol = stack.pop();
					
				}
				
				else if(simbol.equals("&")) { //derivação em vazio
					simbol = stack.pop();
				}
				
				else if(simbol.equals(token.getToken())) {
					
					if(tokensIndex < tokens.size()){
						token = tokens.get(tokensIndex++);
					}
					
					simbol = stack.pop();					
					
				}
				
				else {
					
					if(ReservedWords.getReservedWords().contains(simbol)) {
						syntaticErrors.add(new CompilerError(CompilerError.SYNTATIC, token, "Esperado a palavra reservada " + simbol));
					}
					else {
						syntaticErrors.add(new CompilerError(CompilerError.SYNTATIC, token, "Esperado o símbolo " + simbol));
					}						
					
					simbol = stack.pop();
					
				}
			}			
			
		}
		
		for( ; tokensIndex < tokens.size() - 1; ++tokensIndex) {
			token = tokens.get(tokensIndex);
			syntaticErrors.add(new CompilerError(CompilerError.SYNTATIC, token, "Token Inesperado"));
		}
		
	}
	
	/**
	 * Realiza a derivação de uma regra sintática
	 * 
	 * @param derivation A derivação sintática
	 * @param tokens A lista de Tokens que será realizada a análise
	 */
	private void derivate(Derivation derivation, ArrayList<Token> tokens) {
		
		if(derivation.getType() == Derivation.EMPTY) { //derivação em vazio. Erro léxico e o simbolo da entrada é ignorado
			
			addError();
			
			if(!token.getToken().equals("$")) {
				if(tokensIndex < tokens.size()){
					token = tokens.get(tokensIndex++);
				}
			}
			
			return;
		}
		
		else if(derivation.getType() == Derivation.SYNC) { //derivação de sincronização. Simbolo não terminal é descartado
						
			String message = SyntaticError.getErrorMessage(simbol);
			syntaticErrors.add(new CompilerError(CompilerError.SYNTATIC, token, message));
			simbol = stack.pop();
		}
			
		else {
			
			for(int i = derivation.getDerivations().length-1; i >= 0; --i){
				stack.push(derivation.getDerivations()[i]);
			}
			
			simbol = stack.pop();
			
		}		
		
	}
	
	/**
	 * Adiciona um erro sintático na lista de erros
	 */
	private void addError() {
		String error = SyntaticError.getErrorMessage(simbol);
		syntaticErrors.add(new CompilerError(CompilerError.SYNTATIC, token, error));
	}

	/**
	 * Retorna a lista de erros sintáticos encontrados no processo de derivação
	 * 
	 * @return A lista de erros sintáticos encontrados no processo de derivação
	 */
	public ArrayList<CompilerError> getSyntaticErrors() {
		return syntaticErrors;
	}
	
}
 