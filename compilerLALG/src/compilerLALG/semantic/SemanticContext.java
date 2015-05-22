package compilerLALG.semantic;

import compilerLALG.lexical.Token;

public class SemanticContext {
	
	public static final int TOKEN_TYPE_ID = 0;
	public static final int TOKEN_TYPE_NUM = 1;
	
	public static final int CATEGORY_VAR = 0;
	public static final int CATEGORY_PROC = 1;
	public static final int CATEGORY_PROG = 2;
	
	public static final int TYPE_UNDEFINED = 0;
	public static final int TYPE_INT = 1;
	public static final int TYPE_REAL = 2;
	public static final int TYPE_BOOLEAN = 3;
	
	public static final int SCOPE_GLOBAL = 0;
	public static final int SCOPE_LOCAL = 1;

	private String lexeme;
	private int tokenType;
	private int category;
	private int type;
	private double value;
	private int scope;
	private boolean utilized;
	private Token token;
	
	public SemanticContext(String lexeme, int tokenType, int category, int type, double value, int scope, boolean utilized, Token token) {
		this.lexeme = lexeme;
		this.tokenType = tokenType;
		this.category = category;
		this.type = type;
		this.value = value;
		this.scope = scope;
		this.utilized = utilized;
		this.token = token;
	}
	
	public String getLexeme() {
		return lexeme;
	}
	
	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}
	
	public int getTokenType() {
		return tokenType;
	}
	
	public void setTokenType(int tokenType) {
		this.tokenType = tokenType;
	}
	
	public int getCategory() {
		return category;
	}
	
	public void setCategory(int category) {
		this.category = category;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public int getScope() {
		return scope;
	}
	
	public void setScope(int scope) {
		this.scope = scope;
	}
	
	public boolean isUtilized() {
		return utilized;
	}
	
	public void setUtilized(boolean utilized) {
		this.utilized = utilized;
	}
	
	public Token getToken() {
		return token;
	}
	
	public void setToken(Token token) {
		this.token = token;
	}

	@Override
	public String toString() {
		
		String tok[] = new String[]{"id", "num"};
		String cat[] = new String[]{"var", "proc", "prog"};
		String typ[] = new String[]{"undefined", "int", "real", "boolean"};
		String scp[] = new String[]{"global", "local"};
		
		if(value == Double.MAX_VALUE)
			return "lexeme=" + lexeme + ", token=" + tok[tokenType] + ", category=" + cat[category] + ", type=" + typ[type] 
					 + ", value=undefined"  + ", scope=" + scp[scope] + ", utilized=" + utilized;
		
		return "lexeme=" + lexeme + ", token=" + tok[tokenType] + ", category=" + cat[category] + ", type=" + typ[type] 
				 + ", value=" + value + ", scope=" + scp[scope] + ", utilized=" + utilized;
	}
	
}
