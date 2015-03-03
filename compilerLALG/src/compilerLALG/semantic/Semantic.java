package compilerLALG.semantic;

import java.util.ArrayList;
import java.util.Scanner;

import compilerLALG.errors.CompilerError;
import compilerLALG.lexical.Token;

/**
 * Realiza a análise semântica sobre um conjunto de tokens obtidos na análise léxica
 */
public class Semantic {
	
	private SemanticTable semanticTable;
	private ArrayList<ProcedureSignature> procedureSignatures;
	private ArrayList<CompilerError> semanticErrors;
	
	private int tokensIndex;
	private Token token;
	
	private boolean globalScope;
	private int beginNum;
	
	public Semantic() {
		semanticTable = new SemanticTable();
	}
	
	public void execute(ArrayList<Token> tokens) {
				
		tokensIndex = 0;
		token = tokens.get(tokensIndex++);
		
		globalScope = true;
		beginNum = 0;
		
		semanticErrors = new ArrayList<>();
		procedureSignatures = new ArrayList<>();
		
		semanticTable.reset();
		setDefaultProcedures();
		
		while(tokensIndex < tokens.size()) {
			
			System.out.println(token);
			System.out.println(semanticTable);
			System.out.println(globalScope);
			System.out.println(beginNum);
			System.out.println(procedureSignatures);
			System.out.println(semanticErrors);
			System.out.println("------------");
			
			switch (token.getToken()) {
			
				case "program":
					addProgramIdentifier(tokens);
					break;
					
				case "var":
					addVars(tokens);
					break;
					
				case "procedure":
					addProcedure(tokens);
					break;
					
				case "begin":
					begin(tokens);
					break;
					
				case "end":
					end(tokens);
					break;
	
				default:
					if(token.getType() == Token.IDENTIFIER) System.out.println(">>>" + token);
					token = tokens.get(tokensIndex++);
					break;
			}
						
			new Scanner(System.in).nextLine();
			
		}		
		
	}
	
	private void setDefaultProcedures() {
		
		SemanticContext semanticContext = new SemanticContext("read", SemanticContext.TOKEN_ID, SemanticContext.CATEGORY_PROC, 
				SemanticContext.TYPE_UNDEFINED, Double.MAX_VALUE, SemanticContext.SCOPE_GLOBAL, true);
		semanticTable.addSemanticContext(semanticContext);
		
		procedureSignatures.add(new ProcedureSignature("read", new int[]{SemanticContext.TYPE_BOOLEAN}));
		procedureSignatures.add(new ProcedureSignature("read", new int[]{SemanticContext.TYPE_INT}));
		procedureSignatures.add(new ProcedureSignature("read", new int[]{SemanticContext.TYPE_REAL}));
		procedureSignatures.add(new ProcedureSignature("read", new int[]{SemanticContext.TYPE_UNDEFINED}));
		
		
		semanticContext = new SemanticContext("write", SemanticContext.TOKEN_ID, SemanticContext.CATEGORY_PROC, 
				SemanticContext.TYPE_UNDEFINED,	Double.MAX_VALUE, SemanticContext.SCOPE_GLOBAL, true);
		
		semanticTable.addSemanticContext(semanticContext);
		
		procedureSignatures.add(new ProcedureSignature("write", new int[]{SemanticContext.TYPE_BOOLEAN}));
		procedureSignatures.add(new ProcedureSignature("write", new int[]{SemanticContext.TYPE_INT}));
		procedureSignatures.add(new ProcedureSignature("write", new int[]{SemanticContext.TYPE_REAL}));
		procedureSignatures.add(new ProcedureSignature("write", new int[]{SemanticContext.TYPE_UNDEFINED}));
		
	}
		
	private void addProgramIdentifier(ArrayList<Token> tokens) {
		
		if(tokensIndex < tokens.size()) {
			
			token = tokens.get(tokensIndex++);
			
			if(token.getType() == Token.IDENTIFIER) {
				
				SemanticContext semanticContext = new SemanticContext(token.getToken(), SemanticContext.TOKEN_ID, SemanticContext.CATEGORY_PROG, 
						SemanticContext.TYPE_UNDEFINED, Double.MAX_VALUE, SemanticContext.SCOPE_GLOBAL, true);
				
				boolean b = semanticTable.addSemanticContext(semanticContext);
				
				if(!b) {
					String errorMessage = "Identificador " + token.getToken() + " duplicado";
					semanticErrors.add(new CompilerError(CompilerError.SEMANTIC, token, errorMessage));
				}
				
				if(tokensIndex < tokens.size()) token = tokens.get(tokensIndex++); 
			}
			
		}
				
	}
		
	private void addVars(ArrayList<Token> tokens) {
				
		ArrayList<SemanticContext> contexts = new ArrayList<>();
		
		token = tokens.get(tokensIndex++);
		
		boolean flag = true;
						
		while(flag) {
									
			while(token.getType() == Token.IDENTIFIER) {
																
				SemanticContext s;
				
				if(globalScope)	{
					s = new SemanticContext(token.getToken(), SemanticContext.TOKEN_ID, SemanticContext.CATEGORY_VAR, 
							SemanticContext.TYPE_UNDEFINED, Double.MAX_VALUE, SemanticContext.SCOPE_GLOBAL, false);	
				}
				else {
					s = new SemanticContext(token.getToken(), SemanticContext.TOKEN_ID, SemanticContext.CATEGORY_VAR, 
							SemanticContext.TYPE_UNDEFINED, Double.MAX_VALUE, SemanticContext.SCOPE_LOCAL, false);
				}
				
				boolean b = semanticTable.addSemanticContext(s);
					
				if(b) contexts.add(s);
				else {
					
					String errorMessage; 
					
					if(globalScope) errorMessage = "Declaração de variável global " + token.getToken() + " duplicada";
					else errorMessage = "Declaração de variável local " + token.getToken() + " duplicada";
					
					semanticErrors.add(new CompilerError(CompilerError.SEMANTIC, token, errorMessage));
				}
				
				if(tokensIndex < tokens.size()) token = tokens.get(tokensIndex++);
				else return;
				
				if(token.getToken().equals(",")) {
					if(tokensIndex < tokens.size()) token = tokens.get(tokensIndex++);
					else return;
				}
				
			}
			
			if(token.getToken().equals(":")) {
								
				if(tokensIndex < tokens.size()) {
					
					token = tokens.get(tokensIndex++);
					
					int varType = SemanticContext.TYPE_UNDEFINED;
					
					switch (token.getToken()) {
						case "int":     varType = SemanticContext.TYPE_INT; break;
						case "real":    varType = SemanticContext.TYPE_REAL; break;
						case "boolean": varType = SemanticContext.TYPE_BOOLEAN; break;
						default: break;
					}
										
					for(SemanticContext context : contexts) context.setType(varType);
					
					if(tokensIndex < tokens.size()) token = tokens.get(tokensIndex++);
					else return;
				}
				else return;
			}
						
			if(!(tokensIndex < tokens.size())) return;

			if(token.getToken().equals(";") && tokens.get(tokensIndex).getType() == Token.IDENTIFIER) {
				token = tokens.get(tokensIndex++);
				contexts.removeAll(contexts);
				flag = true;
			}
			else flag = false;
						
		}
				
	}

	private void addProcedure(ArrayList<Token> tokens) {
		
		globalScope = false;
		
		if(tokensIndex < tokens.size()) token = tokens.get(tokensIndex++);
		else return;
		
		if(token.getType() != Token.IDENTIFIER) return;
		
		String procedureName = token.getToken();
		
		SemanticContext semanticContext = new SemanticContext(token.getToken(), SemanticContext.TOKEN_ID, SemanticContext.CATEGORY_PROC, 
				SemanticContext.TYPE_UNDEFINED, Double.MAX_VALUE, SemanticContext.SCOPE_GLOBAL, false);
		
		boolean b = semanticTable.addSemanticContext(semanticContext);
		
		if(!b) {
			String errorMessage = "Declaração de procedimento " + token.getToken() + " duplicado";
			semanticErrors.add(new CompilerError(CompilerError.SEMANTIC, token, errorMessage));
		}
		
		if(tokensIndex < tokens.size()) token = tokens.get(tokensIndex++);
		else {
			addProcedureSignature(procedureName, null);
			return;
		}
		
		if(token.getToken().equals("(")) {
			
			if(tokensIndex < tokens.size()) token = tokens.get(tokensIndex++);
			else {
				addProcedureSignature(procedureName, null);
				return;
			}
			
			if(token.getToken().equals("var")) {
				addVars(tokens);
			}
			
			while(!token.getToken().equals(")") && tokensIndex < tokens.size()) token = tokens.get(tokensIndex++);
						
			ArrayList<SemanticContext> localVars = semanticTable.getLocalVars();
			addProcedureSignature(procedureName, localVars);			
		}
				
	}
	
	private void addProcedureSignature(String procedureName, ArrayList<SemanticContext> localVars) {
		
		int types[] = null;
		
		if(localVars == null) types = new int[0];
		else {
			
			types = new int[localVars.size()];
			
			for(int i = 0; i < localVars.size(); ++i) {
				types[i] = localVars.get(i).getType();
			}
						
		}
		
		procedureSignatures.add(new ProcedureSignature(procedureName, types));
				
	}

	private void begin(ArrayList<Token> tokens) {
		beginNum++;
		if(tokensIndex < tokens.size()) token = tokens.get(tokensIndex++);
	}
	
	private void end(ArrayList<Token> tokens) {
		if(--beginNum == 0) {
			globalScope = true;
		}
		if(tokensIndex < tokens.size()) token = tokens.get(tokensIndex++);
	}
	
}
