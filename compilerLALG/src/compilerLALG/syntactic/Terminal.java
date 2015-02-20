package compilerLALG.syntactic;

import compilerLALG.lexical.Token;

/**
 * Representação dos símbolos terminais da linguagem
 */
public class Terminal {
	
	public static final int SEMICOLON = 0;
	public static final int COMMA = 1;
	public static final int DOT = 2;
	public static final int PROGRAM = 3;
	public static final int INTEGER = 4;
	public static final int REAL = 5;
	public static final int COLON = 6;
	public static final int PROCEDURE = 7;
	public static final int OPEN_PARENTHESIS = 8;
	public static final int CLOSE_PARENTHESIS = 9;
	public static final int VAR = 10;
	public static final int BEGIN = 11;
	public static final int END = 12;
	public static final int ASSIGNMENT = 13;
	public static final int IF = 14;
	public static final int THEN = 15;
	public static final int ELSE = 16;
	public static final int WHILE = 17;
	public static final int DO = 18;
	public static final int EQUALS = 19;
	public static final int DIFFERENT = 20;
	public static final int LOWER = 21;
	public static final int LOWER_OR_EQUAL = 22;
	public static final int HIGHER_OR_EQUAL = 23;
	public static final int HIGHER = 24;
	public static final int PLUS = 25;
	public static final int LESS = 26;
	public static final int OR = 27;
	public static final int TIMES = 28;
	public static final int DIV = 29;
	public static final int AND = 30;
	public static final int IDENTIFIER = 31;
	public static final int INTEGER_NUMBER = 32;
	public static final int REAL_NUMBER = 33;
	public static final int NOT = 34;
	public static final int BOOLEAN = 35;
	public static final int TRUE = 36;
	public static final int FALSE = 37;
	public static final int DOLLAR = 38;
	
	public static final int TOTAL = 39;
	
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
				case "int":       return INTEGER;
				case "real":      return REAL;
				case "div":		  return DIV;
				case "boolean":	  return BOOLEAN;
				default:          return -1;
			}
			
		}
		else if(token.getType() == Token.SPECIAL_SYMBOL) {
			
			switch (token.getToken().trim()) {
			
				case "(":  return OPEN_PARENTHESIS;
				case ")":  return CLOSE_PARENTHESIS;
				case "+":  return PLUS;
				case "-":  return LESS;
				case "*":  return TIMES;
				case ";":  return SEMICOLON;
				case ":=": return ASSIGNMENT;
				case ">":  return HIGHER;
				case ">=": return HIGHER_OR_EQUAL;
				case "<":  return LOWER;
				case "<=": return LOWER_OR_EQUAL;
				case "=":  return EQUALS;
				case "<>": return DIFFERENT;
				case ",":  return COMMA;
				case ":":  return COLON;
				case ".":  return DOT;

				default: return -1;
			}	
			
		}
		
		else if(token.getToken().equals("true")) return TRUE;
		else if(token.getToken().equals("false")) return FALSE;
		else if(token.getType()== Token.IDENTIFIER) return IDENTIFIER;
		else if(token.getType() == Token.INTEGER_NUMBER) return INTEGER_NUMBER;
		else if(token.getType() == Token.REAL_NUMBER) return REAL_NUMBER;
		else if(token.getType() == Token.EMPTY) return DOLLAR;
		
		return -1;
		
	}

}
