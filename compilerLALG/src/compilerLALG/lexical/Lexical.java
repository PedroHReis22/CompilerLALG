package compilerLALG.lexical;
import java.util.ArrayList;

import compilerLALG.reservedWords.ReservedWords;

/**
 * Classe responsável pela análise léxica sobre o código-fonte
 */
public class Lexical {
	
	private ArrayList<Token> tokens;
	private ArrayList<Token> lexicalErrors;
			
	/**
	 * Executa a análise léxica em um código fonte passado por parâmetro 
	 * 
	 * @param source O código-fonte a ser extraídos os tokens
	 */
	public void execute(String source) {
		
		tokens = new ArrayList<Token>();
		StringBuffer buffer = new StringBuffer();
		
		source = source.toLowerCase();
		source = source.replaceAll("\\t", "    ");
		String lines[] = source.split("\n");
		
		for(int i = 0; i < lines.length; ++i) {
			
			String s = lines[i];
			
			int start = 0;
			Character c = null;
		
			for(int index = 0; index < s.length(); ++index) {
																											
				if(Character.isDigit(s.charAt(index))) { //números inteiro/real
										
					start = index + 1;
					int dotCount = 0;
					
					while(index < s.length() && s.charAt(index) != ' ' && (!ReservedWords.getSpecialSymbols().contains(String.valueOf(s.charAt(index))) || s.charAt(index) == '.') && dotCount <= 1) {
						buffer.append(s.charAt(index));
						if(s.charAt(index) == '.') {
							++dotCount;
							if(dotCount > 1) {
								--index;
								buffer.delete(buffer.length() - 1, buffer.length());
							}
							
						}
						index++;
					}
					
					tokens.add(new Token(buffer.toString(), i + 1, start));
					buffer.delete(0, buffer.length());
					--index;
				}
				else if((c = s.charAt(index)).compareTo('{') == 0) { //comentário de bloco
					
					start = index + 1;
					int startLine = i + 1;
					boolean continues = true;
					
					while(continues) {
												
						while(index < s.length() && (c = s.charAt(index)).compareTo('}') != 0) {
							buffer.append(s.charAt(index++));
						}
						
						if(c.compareTo('}') == 0) continues = false;
						else {
							if(++i < lines.length) {
								s = lines[i];
								index = 0;
							}
							else {
								continues = false;
							}
						}
						
					}					
					
					buffer.append(c);					
					tokens.add(new Token(buffer.toString(), startLine, i + 1, start, index + 1));
					buffer.delete(0, buffer.length());
					
				}
				else if(s.charAt(index) == '/') { //comentário de linha e operador de divisão
					
					start = index + 1;
					++index;
					
					if(index < s.length() && s.charAt(index) == '/') {
						tokens.add(new Token(s.substring(start - 1), index + 1, start));
						index = s.length();
					}
					
					else {
						tokens.add(new Token("/", i + 1, start));
						--index;
					}
					
				}
				else if(ReservedWords.getSpecialSymbols().contains(String.valueOf(s.charAt(index)))) { //simbolos especiais
					
					if(index + 1 < s.length()) {
						
						if(s.charAt(index) == ':' && s.charAt(index + 1) == '=') {
							tokens.add(new Token(s.substring(index, index + 2), i + 1, index + 1));
							++index;
						}
						else if(s.charAt(index) == '<' && s.charAt(index + 1) == '>') {
							tokens.add(new Token(s.substring(index, index + 2), i + 1, index + 1));
							++index;
						}
						else if(s.charAt(index) == '<' && s.charAt(index + 1) == '=') {
							tokens.add(new Token(s.substring(index, index + 2), i + 1, index + 1));
							++index;
						}
						else if(s.charAt(index) == '>' && s.charAt(index + 1) == '=') {
							tokens.add(new Token(s.substring(index, index + 2), i + 1, index + 1));
							++index;
						}
						else {
							tokens.add(new Token(String.valueOf(s.charAt(index)), i + 1, index + 1));
						}
						
					}
					else {
						tokens.add(new Token(String.valueOf(s.charAt(index)), i + 1, index + 1));
					}
				}
				else { //demais tokens
					
					start = index + 1;
					
					while(index < s.length() && s.charAt(index) != ' ' && !ReservedWords.getSpecialSymbols().contains(String.valueOf(s.charAt(index)))) {
						buffer.append(s.charAt(index++));
					}
					
					if(buffer.toString() != null && !buffer.toString().isEmpty()) {					
						tokens.add(new Token(buffer.toString(), i + 1, start));
						buffer.delete(0, buffer.length());
						--index;
					}
					
				}
							
			}
			
		}
		
		setLexicalErrors();
						
	}
	
	/**
	 * Retorna a lista de tokens resultante da análise léxica
	 * 
	 * @return A lista de tokens resultante da análise léxica
	 */
	public ArrayList<Token> getTokens() {
		return tokens;
	}
	
	/**
	 * Retorna os tokens resultantes da análise léxica desconsiderando os tokens de comentário 
	 * 
	 * @return Os tokens resultantes da análise léxica desconsiderando os tokens de comentário
	 */
	public ArrayList<Token> getTokensWithoutComment() {
		
		ArrayList<Token> tokens = new ArrayList<Token>();
		
		return tokens;
		
	}
	
	/**
	 * Define os tokens de erros léxicos encontrados na análise
	 */
	public void setLexicalErrors() {
		
		lexicalErrors = new ArrayList<Token>();
		
		for(Token token : tokens) {
			
			int tokenType = token.getType();
			
			if(tokenType == Token.UNKNOWN || tokenType == Token.MALFORMED_IDENTIFIER || 
			   tokenType == Token.MALFORMED_INTEGER_NUMBER || tokenType == Token.MALFORMED_REAL_NUMBER) {
				lexicalErrors.add(token);
			}
		}
					
	}
	
	/**
	 * Retorna a lista de erros léxicos encontrados na análise
	 * 
	 * @return A lista de erros léxicos encontrados na análise
	 */
	public ArrayList<Token> getLexicalErrors() {
		return lexicalErrors;	
	}

}
