package compilerLALG.errors;

import compilerLALG.lexical.Token;

/**
 * Define a representação de um erro de compilação 
 */
public class CompilerError {
	
	public static final int LEXICAL = 0;
	public static final int SYNTATIC = 1;
	
	private int errorType;
	private Token token;
	private String errorMessage;
	
	/**
	 * Constutor de um objeto de erro de compilação
	 * 
	 * @param errorType O tipo do erro de compilação. O tipo do erro pode ser:
	 * 				   <ul>
	 * 					 <li>LEXICAL</li>
	 * 					 <li>SYNTATIC</li>
	 * 				   </ul>
	 * @param token O token que originou o erro
	 * @param errorMessage A mensagem de Erro
	 */
	public CompilerError(int errorType, Token token, String errorMessage) {
		this.errorType = errorType;
		this.token = token;
		this.errorMessage = errorMessage;
	}

	/**
	 * Retorna o tipo do erro
	 * 
	 * @return O tipo do erro
	 */
	public int getErrorType() {
		return errorType;
	}

	/**
	 * Retorna o token do erro
	 * 
	 * @return O token do erro
	 */
	public Token getToken() {
		return token;
	}
	
	/**
	 * Retorna a representação em forma de String do tipo do erro
	 * 
	 * @return A representação em forma de String do tipo do erro
	 */
	public String getErrorTypeStr() {
		switch (errorType) {
			case LEXICAL:  return "Erro Léxico";
			case SYNTATIC: return "Erro Sintático";
			default:       return "Erro Desconhecido";
		}
	}
	
	/**
	 * Retorna a mensagem de erro
	 * 
	 * @return A mensagem de erro
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	
	@Override
	public String toString() {
		return errorMessage;		
	}
	
	@Override
	public boolean equals(Object o) {
		Token token = (Token) o;
		System.out.println("adsl");
		return token.equals(this.token);
		//return false;
	}

}
