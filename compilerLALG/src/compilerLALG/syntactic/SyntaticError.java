package compilerLALG.syntactic;

import compilerLALG.lexical.Token;

/**
 * Representação de um erro sintático encontrado na análise sintática
 */
public class SyntaticError {
	
	private String error;
	private Token token;
	
	/**
	 * Cria o objeto de representação de erro sintático
	 * 
	 * @param error A representação em forma de string do erro sintático
	 * @param token O token em que foi encontrado o erro
	 */
	public SyntaticError(String error, Token token) {
		this.error = error;
		this.token = token;
	}
	
	/**
	 * Retorna a representação em forma de texto do erro sintático.
	 * 
	 * @return A representação em forma de texto do erro sintático.
	 */
	public String getError() {
		return error;
	}
	
	/**
	 * Retorna o token em que foi obtido o erro sintático
	 * 
	 * return O token em que foi encontrado o erro sintático
	 */
	public Token getToken() {
		return token;
	}
	
	@Override
	public String toString() {
		return error;
	}

}
