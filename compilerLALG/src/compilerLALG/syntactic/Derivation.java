package compilerLALG.syntactic;

/**
 * Representa uma derivação disponível na gramática da linguagem
 */
public class Derivation {
	
	public static final int NORMAL = 0;
	public static final int EMPTY = 1;
	public static final int SYNC = 2;
	
	private String tokens[];
	
	private int type;
	
	/**
	 * Cria o objeto de derivação, contendo todos os simbolos terminais e não-terminais da derivação
	 * 
	 * @param tokens Os símbolos terminais e não-terminais da derivação
	 */
	public Derivation(String tokens[]) {
		this.tokens = tokens;
		this.type = NORMAL;
	}
		
	/**
	 * Cria o objeto de derivação, contendo todos os simbolos terminais e não-terminais da derivação
	 * 
	 * @param tokens Os símbolos terminais e não-terminais da derivação
	 * @param type O tipo da derivação. Os tipos de derivação podem ser:
	 *              <ul>
	 *              	<li> NORMAL - Uma derivação em um conjunto de simbolos terminais e/ou não terminais
	 *              	<li> EMPTY - Uma derivação vazia e que portanto implica em um erro sintático e eliminação do token atual
	 *              	<li> SYNC - Uma derivação de sincronização. O compillador tentará se recuperar do erro e continuar a análise.
	 *              </ul>
	 */
	public Derivation(String token, int type) {
		this.tokens = new String[1];
		this.tokens[0] = token;
		this.type = type;
	}
	
	/**
	 * Retorna o tipo da derivação
	 * 
	 * @return O tipo da derivação
	 */
	public int getType() {
		return type;
	}

	/**
	 * Retorna um vetor com a lista de derivações
	 * 
	 * @return Um vetor com a lista de derivações
	 */
	public String[] getDerivations() {
		return tokens;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(String t : tokens) builder.append(t);
		return builder.toString().trim();
	}
}
