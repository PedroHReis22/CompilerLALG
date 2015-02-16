package compilerLALG.syntactic;

import compilerLALG.lexical.Token;

/**
 * Representação dos símbolos terminais da linguagem
 */
public class Terminal {
	
	public static final int PONTO_VIRGULA = 0;
	public static final int VIRGULA = 1;
	public static final int PONTO = 2;
	public static final int PROGRAM = 3;
	public static final int INTEGER = 4;
	public static final int REAL = 5;
	public static final int DOIS_PONTOS = 6;
	public static final int PROCEDURE = 7;
	public static final int ABRE_PARENTESES = 8;
	public static final int FECHA_PARENTESES = 9;
	public static final int VAR = 10;
	public static final int BEGIN = 11;
	public static final int END = 12;
	public static final int ATRIBUICAO = 13;
	public static final int IF = 14;
	public static final int THEN = 15;
	public static final int ELSE = 16;
	public static final int WHILE = 17;
	public static final int DO = 18;
	public static final int IGUAL = 19;
	public static final int DIFERENTE = 20;
	public static final int MENOR = 21;
	public static final int MENOR_IGUAL = 22;
	public static final int MAIOR_IGUAL = 23;
	public static final int MAIOR = 24;
	public static final int MAIS = 25;
	public static final int MENOS = 26;
	public static final int OR = 27;
	public static final int VEZES = 28;
	public static final int DIV = 29;
	public static final int AND = 30;
	public static final int IDENTIFICADOR = 31;
	public static final int NUMERO_INTEIRO = 32;
	public static final int NUMERO_REAL = 33;
	public static final int NOT = 34;
	public static final int CIFRAO = 35;
	
	public static final int TOTAL = 36;
	
	/**
	 * Retorna o index do simbolo terminal passado por parâmetro
	 * 
	 * @param token O token que deseja-se encontrar o index
	 * 
	 * @return O index do token passado por parâmetro
	 */
	public static int getIndex(Token token) {
		
		if(token.getType() == Token.RESERVED_WORD) {
			
			switch (token.getToken().trim()) {
			
				case "program":   return PROGRAM;
				case "and":       return AND;
				case "or":        return OR;
				case "not":       return NOT;
				case "while":     return WHILE;
				case "do":        return DO;
				case "var":       return VAR;
				case "procedure": return PROCEDURE;
				case "begin":     return BEGIN;
				case "end":       return END;
				case "if":        return IF;
				case "then":	  return THEN;
				case "else":	  return ELSE;
				case "integer":   return INTEGER;
				case "real":      return REAL;
				case "div":		  return DIV;
				default:          return -1;
			}
			
		}
		else if(token.getType() == Token.SPECIAL_SYMBOL) {
			
			switch (token.getToken().trim()) {
			
				case "(":  return ABRE_PARENTESES;
				case ")":  return FECHA_PARENTESES;
				case "+":  return MAIS;
				case "-":  return MENOS;
				case "*":  return VEZES;
				case ";":  return PONTO_VIRGULA;
				case ":=": return ATRIBUICAO;
				case ">":  return MAIOR;
				case ">=": return MAIOR_IGUAL;
				case "<":  return MENOR;
				case "<=": return MENOR_IGUAL;
				case "=":  return IGUAL;
				case "<>": return DIFERENTE;
				case ",":  return VIRGULA;
				case ":":  return DOIS_PONTOS;
				case ".":  return PONTO;

				default: return -1;
			}	
			
		}
		
		else if(token.getType()== Token.IDENTIFIER) return IDENTIFICADOR;
		else if(token.getType() == Token.INTEGER_NUMBER) return NUMERO_INTEIRO;
		else if(token.getType() == Token.REAL_NUMBER) return NUMERO_REAL;
		else if(token.getType() == Token.EMPTY) return CIFRAO;
		
		return -1;
		
	}

}
