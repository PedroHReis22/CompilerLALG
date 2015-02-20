package compilerLALG.lexical;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import compilerLALG.reservedWords.ReservedWords;

/**
 * Representa um token utilizado para a análise léxica
 */
public class Token {
	
	public static final int REAL_NUMBER = 0;
	public static final int INTEGER_NUMBER = 1;
	public static final int SPECIAL_SYMBOL = 2;
	public static final int RESERVED_WORD = 3;
	public static final int IDENTIFIER = 4;
	public static final int BLOCK_COMMENT = 5;
	public static final int LINE_COMMENT = 6;
	public static final int MALFORMED_REAL_NUMBER = 7;
	public static final int MALFORMED_INTEGER_NUMBER = 8;
	public static final int MALFORMED_IDENTIFIER = 9;
	public static final int UNKNOWN = 10;
	public static final int EMPTY = 11;
	public static final int BOOLEAN_VALUE = 12;
		
	private String token;
	private int startLine;
	private int endLine;
	private int startColumn;
	private int endColumn;
	private int type;
	
	/**
	 * Cria a representação de um token
	 * 
	 * @param token A representação do token
	 * @param startLine A linha inicial do token
	 * @param initialColumn A coluna inicial do token
	 */
	public Token(String token, int startLine, int initialColumn) {
		this.token = token;
		this.startLine = this.endLine = startLine;
		this.startColumn = initialColumn;
		endColumn = initialColumn + token.length() - 1;
		classifiesToken();
	}
	
	/**
	 * Cria a representação de um token a partir de um token existente
	 * 
	 * @param token A representação do token já existente
	 */
	public Token(Token t) {
		this.token = t.getToken();
		this.startLine = t.getStartLine();
		this.endLine = t.getEndLine();
		this.startColumn = t.getStartColumn();
		this.endColumn = t.getEndColumn();
		this.type = t.getType();		
	}
	
	/**
	 * Cria a reoresentação de um token
	 * 
	 * @param token A representação do token
	 * @param startLine A linha inicial do token
	 * @param endLine A linha final do token
	 * @param startColumn A coluna inicial do token
	 * @param endColumn A coluna final do token
	 */
	public Token(String token, int startLine, int endLine, int startColumn, int endColumn) {
		this.token = token;
		this.startLine = startLine;
		this.endLine = endLine;
		this.startColumn = startColumn;
		this.endColumn = endColumn;
		classifiesToken();
	}
	
	/**
	 * Classifica o token entre os diversos tipos de tokens possíveis
	 */
	private void classifiesToken() {
				
		Matcher real = Pattern.compile("\\d+\\.\\d+").matcher(token);
		Matcher integer = Pattern.compile("\\d+|\\d+\\.").matcher(token);
		Matcher blockComment = Pattern.compile("\\{.*\\}").matcher(token);
		Matcher lineComment = Pattern.compile("//.*").matcher(token);
		Matcher identifier = Pattern.compile("([a-zA-Z]|_)([a-zA-Z]|\\d||_)*").matcher(token);
		
		if(token.equals("true") || token.equals("false")) {
			type = BOOLEAN_VALUE;
			return;
		}
		
		if(real.matches()) {
			
			String s[] = token.split("\\.");
						
			if(!(s[0].length() > 15 || s[1].length() > 10)) type = REAL_NUMBER;
			else type = MALFORMED_REAL_NUMBER;
			
			return;
			
		}
		
		if(integer.matches()) {
			
			if(token.length() < 16) type = INTEGER_NUMBER;
			else type = MALFORMED_INTEGER_NUMBER;
			
			return;
			
		}
		
		if(ReservedWords.getSpecialSymbols().contains(token)) {
			type = SPECIAL_SYMBOL;
			return;
		}
		
		if(ReservedWords.getReservedWords().contains(token)) {
			type = RESERVED_WORD;
			return;
		}
		
		if(identifier.matches()) {
			
			if(token.length() < 20) type = IDENTIFIER;
			else type = MALFORMED_IDENTIFIER;
			
			return;
		}
		
		if(blockComment.matches()) {
			type = BLOCK_COMMENT;
			return;
		}
		
		if(lineComment.matches()) {
			type = LINE_COMMENT;
			return;
		}
		
		if(token.equals("$")) {
			type = EMPTY;
			return;
		}
		
		type = UNKNOWN;
		
	}
	
	/**
	 * Retorna a classificação do token em forma de string
	 * 
	 * @return A classificação do token
	 */
	public String getTokenType() {
				
		switch (type) {
			case REAL_NUMBER: 				return "Número Real";
			case INTEGER_NUMBER: 			return "Número Inteiro";
			case SPECIAL_SYMBOL: 			return "Simbolo Especial " + token;
			case RESERVED_WORD: 			return "Palavra Reservada " + token;
			case IDENTIFIER: 				return "Identificador";
			case BLOCK_COMMENT:				return "Comentário de Bloco";
			case LINE_COMMENT:				return "Comentário de linha";
			case MALFORMED_REAL_NUMBER: 	return "Número Real mal formado";
			case MALFORMED_INTEGER_NUMBER: 	return "Número Inteiro mal formado";
			case MALFORMED_IDENTIFIER:		return "Identificador mal formado";
			case EMPTY:						return "Final de Cadeia";
			case BOOLEAN_VALUE:				return "Valor Boleano";
			default: 						return "Token Inválido";
			
		}
				
	}

	
	/**
	 * Retorna o token armazenado
	 * 
	 * @return O token armazenado
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Define o token a ser armazenado
	 * 
	 * @param token O token a ser armazenado
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Retorna a linha inicial da token
	 *  
	 * @return A linha inicial do token
	 */
	public int getStartLine() {
		return startLine;
	}

	/**
	 * Define a linha inicial do token
	 * 
	 * @param startLine A linha incial do token
	 */
	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}
	
	/**
	 * Retorna a linha final da token
	 *  
	 * @return A linha final do token
	 */
	public int getEndLine() {
		return endLine;
	}

	/**
	 * Define a linha final do token
	 * 
	 * @param endLine A linha final do token
	 */
	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}

	/**
	 * Retorna a coluna inicial do token
	 * 
	 * @return A coluna inicial do token
	 */
	public int getStartColumn() {
		return startColumn;
	}

	/**
	 * Define a coluna final do token
	 * 
	 * @param startColumn A coluna final do token
	 */
	public void setStartColumn(int startColumn) {
		this.startColumn = startColumn;
	}

	/**
	 * Retorna a coluna final do token
	 * 
	 * @return A coluna final do token
	 */
	public int getEndColumn() {
		return endColumn;
	}

	/**
	 * Define a coluna final do token
	 * 
	 * @param endColumn A coluna final do token
	 */
	public void setEndColumn(int endColumn) {
		this.endColumn = endColumn;
	}
	
	/**
	 * Retorna o tipo do token
	 * 
	 * @return O tipo do token
	 */
	public int getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Token [token=>" + token + "<, type=" + getTokenType() + "]";
	}
	
}
