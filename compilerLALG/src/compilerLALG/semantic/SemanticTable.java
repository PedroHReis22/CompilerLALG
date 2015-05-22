package compilerLALG.semantic;

import java.util.ArrayList;

public class SemanticTable {
	
	private ArrayList<SemanticContext> contexts;
	
	public SemanticTable() {
		contexts = new ArrayList<>();
	}
	
	public boolean addSemanticContext(SemanticContext context) {
		
		for(SemanticContext c : contexts) {			
			if(c.getLexeme().equals(context.getLexeme()) && c.getCategory() == context.getCategory() && c.getScope() == context.getScope()) return false;
		}
		
		contexts.add(context);
		return true;
		
	}
		
	public ArrayList<SemanticContext> getContexts() {
		return contexts;
	}
	
	public void reset() {
		contexts.removeAll(contexts);
	}
	
	public ArrayList<SemanticContext> getLocalVars() {
		
		ArrayList<SemanticContext> localVars = new ArrayList<>();
		
		for(SemanticContext c : contexts) {
			if(c.getCategory() == SemanticContext.CATEGORY_VAR && c.getScope() == SemanticContext.SCOPE_LOCAL) {
				localVars.add(c);
			}
		}
		
		return localVars;
		
	}
	
	public boolean exists(String lexeme, int token, int category) {
		
		for(SemanticContext context : contexts) {
			if(context.getLexeme().equals(lexeme) && context.getTokenType() == token && context.getCategory() == category) {
				return true;
			}
		}
		
		return false;
		
	}
	
	public SemanticContext get(String lexeme, int token, int category) {
		
		for(SemanticContext context : contexts) {
			if(context.getLexeme().equals(lexeme) && context.getTokenType() == token && context.getCategory() == category) {
				return context;
			}
		}
		
		return null;
	}
	
	public SemanticContext get(String lexeme, int token, int category, int scope) {
		
		for(SemanticContext context : contexts) {
			if(context.getLexeme().equals(lexeme) && context.getTokenType() == token && context.getCategory() == category && context.getScope() == scope) {
				return context;
			}
		}
		
		return null;
	}
	
	public ArrayList<SemanticContext> removeLocalVars() {
		ArrayList<SemanticContext> localVars = getLocalVars();
		contexts.removeAll(localVars);
		return localVars;
	}
	
	@Override
	public String toString() {
		
		StringBuffer buffer = new StringBuffer();
		contexts.forEach(c -> buffer.append(c.toString() + "\n"));
			
		return buffer.toString();
		
	}

}
