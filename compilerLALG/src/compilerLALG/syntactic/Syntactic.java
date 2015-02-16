package compilerLALG.syntactic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.DataValidation.ErrorStyle;

import compilerLALG.lexical.Token;
import compilerLALG.reservedWords.ReservedWords;

public class Syntactic {
		
	private Derivation[][] rules;
	private Stack<String> stack;
	
	private String simbol;
	private Token token;
	
	private ArrayList<SyntaticError> syntaticErrors;
	
	public Syntactic() {
		
		syntaticErrors = new ArrayList<SyntaticError>();
		rules = new Derivation[NotTerminal.TOTAL][Terminal.TOTAL];
		loadGrammar();
			
	}
	
	private void loadGrammar() {
		
		try {
			
			@SuppressWarnings("resource")
			HSSFWorkbook workbook = new HSSFWorkbook(getClass().getResourceAsStream("/compilerLALG/grammar/grammar.xls"));
			HSSFSheet sheet = workbook.getSheetAt(0);
			
			for(int line = 0; line < sheet.getLastRowNum(); ++line) {
				
				Row row = sheet.getRow(line);
				
				for(int column = 0; column < row.getLastCellNum(); ++column) {
					
					Cell cell = row.getCell(column);
					
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

	public ArrayList<SyntaticError> execute(ArrayList<Token> tokens) {
		
		int tokensIndex = 0;
		
		stack = new Stack<String>();
		stack.push("$");
		stack.push("<prog>");
		
		token = tokens.get(tokensIndex++);
		simbol = stack.pop();
				
		while(!stack.isEmpty()) { //confirmar
			
			System.out.println(stack);
			System.out.println(simbol);
			System.out.println(token.getToken());
			System.out.println(syntaticErrors);
			System.out.println("----------");

			new Scanner(System.in).nextLine();			
			
			
			
			if(simbol.startsWith("<")&& simbol.endsWith(">")&& simbol.length() > 2){ //é um simbolo não terminal
				
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
				
				else if(simbol.equals(token.getToken())){
					
					if(tokensIndex < tokens.size()){
						token = tokens.get(tokensIndex++);
					}
					
					simbol = stack.pop();	
					
				}
				
				else {
					
					if(ReservedWords.getReservedWords().contains(simbol)) {
						syntaticErrors.add(new SyntaticError("Esperado a palavra reservada " + simbol, token));	
					}
					else syntaticErrors.add(new SyntaticError("Esperado o símbolo " + simbol, token));
					
					simbol = stack.pop();
					
				}
			}			
			
		}
		
		for( ; tokensIndex < tokens.size() - 1; ++tokensIndex) {
			syntaticErrors.add(new SyntaticError("Token Inesperado", tokens.get(tokensIndex)));
		}
		
		System.out.println(syntaticErrors);
		System.exit(0);
		
		return syntaticErrors;
		
	}
	
	private void derivate(Derivation derivation, ArrayList<Token> tokens) {
		
		if(derivation.getType() == Derivation.EMPTY) { //derivação em vazio. Erro léxico e o simbolo da entrada é ignorado
			
			addError();
			
			if(!token.getToken().equals("$")) {
				int index = tokens.indexOf(token) + 1;
				token = tokens.get(index);
			}
			
			return;
		}
		
		else if(derivation.getType() == Derivation.SYNC) { //derivação de sincronização. Simbolo não terminal é descartado
			syntaticErrors.add(new SyntaticError("Sincronização", token));
			simbol = stack.pop();
		}
			
		else {
			
			for(int i = derivation.getDerivations().length-1; i >= 0; --i){
				stack.push(derivation.getDerivations()[i]);
			}
			
			simbol = stack.pop();
			
		}		
		
	}
	
	private void addError() {
		
		String error = null;
		
		switch (simbol) {
		
			case "<prog>":          error = "Esperado a palavra reservada 'program'"; break;
			case "<ident>":         error = "Esperado um identificador"; break;
			case "<bloco>":          error = "Esperado um bloco de instruções"; break;
			case "<bloco_>":        error = "Esperado um bloco de instruções"; break;
			case "<part_dec_sub>":  error = "Esperado declaração de procedimento"; break;
			case "<com_comp>":      error = "Esperado a palavra reservada 'begin'"; break; 
			case "<dec_proc>":      error = "Esperado a palavra reservada 'procedure'"; break;
			case "<dec_proc_>":     error = "Esperado o final da declaração de procedimento {;} ou os parâmtros formais"; break;
			case "<par_form>":      error = "Esperado o simbolo ("; break;
			case "<par_form_>":     error = "Esperado o final do parâmetro formal {;} ou o final dos parâmetros formais {)}"; break;
			case "<sec_par_form>":  error = "Esperado a seção de parâmetros formais"; break;
			case "<list_ident>":    error = "Esperado um identificador"; break;
			case"<list_ident_>":     error = "Esperado {,} para um novo identificador ou : para o tipo de variavel"; break;
			case "<relac>":         error = "Esperado um símbolo de relação: =, <>, <, <=, > ou >="; break;
			case "<exp_simp>":      error = "Esperado um fator"; break;
			case "<tipo>":          error = "Esperado o tipo: integer ou real"; break;
			case "<int>":		    error = "Esperado um número inteiro"; break;
			case "<real>":		    error = "Esperado um número real"; break;
			case "<cond>":		    error = "Esperado a palavra reservada 'if'"; break;
			case "<rep>":		    error = "Esperado a palavra reservada 'while'"; break;
			case "<fator>":		    error = "Esperado: (, identificador, número inteiro, número real ou a palvra reservada 'not'"; break;
			case "<cond_>":	   	    error = "Esperado o final da expressão {;} ou a palavra reservada 'end' ou um bloco else"; break;
			case "<part_dec_var>":  error = "Esperado a palavra reservada 'var'"; break;
			case "<exp>":		    error = "Esperado uma expressão"; break;
			case "<com_comp_>":	    error = "Esperado o fim da expressão: ; ou 'end'"; break;
			case "<part_dec_var_>": error = "Esperado o fim de declaração de varíaveis"; break;
			case "<dec_var>": 		error = "Esperado declaração de variáveis"; break;
			case "<exp_>":		    error = "Esperado uma expressão"; break;
			case "<exp_simp_>":		error = "Esperado +, - ou 'or'"; break;
			case "<termo_>":		error = "Esperado *, 'div' ou 'and'"; break;
			case "<list_exp>":      error = "Esperado uma lista de expressões"; break;
			case "<list_exp_>":		error = "Esperado uma lista de expressões"; break;
			case "<com>":			error = "Esperado um comando"; break;
			case "<com_>":			error = "Esperado um comando"; break; 
			case "<com__>":			error = "Esperado um comando"; break;
			case "<cham_proc>":		error = "Esperado uma chamada de procedimento"; break;
	
			default:				break;
		}
		
		syntaticErrors.add(new SyntaticError(error, token));
		
	}

}
 