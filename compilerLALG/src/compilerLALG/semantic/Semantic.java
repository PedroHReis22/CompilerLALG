package compilerLALG.semantic;

import java.util.ArrayList;
import java.util.Arrays;

import compilerLALG.errors.CompilerError;
import compilerLALG.lexical.Token;

/**
 * Realiza a análise semântica sobre um conjunto de tokens obtidos na análise léxica
 */
public class Semantic {
		
	private SemanticTable semanticTable;
	private ArrayList<ProcedureSignature> procedureSignatures;
	private ArrayList<CompilerError> semanticErrors;
	
	private ArrayList<String> operators;
	
	private int tokensIndex;
	private Token token;
	
	private boolean globalScope;
	private int beginNum;
	
	public Semantic() {
		semanticTable = new SemanticTable();
		operators = new ArrayList<>(Arrays.asList(new String[]{"+", "-", "div", "*", "and", "or", "not", ">", ">=", "<", "<=", "=", "<>"}));
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
			
//			System.out.println(token);
//			System.out.println(semanticTable);
//			System.out.println(globalScope);
//			System.out.println(beginNum);
//			System.out.println(procedureSignatures);
//			System.out.println(semanticErrors);
//			System.out.println("------------");
			
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
					
				case "if":
					ifStatement(tokens);
					break;
					
				case "while":
					whileStatement(tokens);
					break;
	
				default:
					
					if(token.getType() == Token.IDENTIFIER) {
						identifier(tokens);
					}
											
					token = tokens.get(tokensIndex++);
					break;
			}
					
		}	
		
		for(SemanticContext context : semanticTable.getContexts()) {
			
			if(!context.isUtilized()) {
				
				if(context.getCategory() == SemanticContext.CATEGORY_VAR) {
					addSemanticWarning("Variável não utilizada: " + context.getLexeme(), context.getToken());
				}
				else if(context.getCategory() == SemanticContext.CATEGORY_PROC) {
					addSemanticWarning("Procedimento não utilizado: " + context.getLexeme(), context.getToken());	
				}
			}
		}
		
	}
	
	private void setDefaultProcedures() {
		
		SemanticContext semanticContext = new SemanticContext("read", SemanticContext.TOKEN_TYPE_ID, SemanticContext.CATEGORY_PROC, 
				SemanticContext.TYPE_UNDEFINED, Double.MAX_VALUE, SemanticContext.SCOPE_GLOBAL, true, null);
		semanticTable.addSemanticContext(semanticContext);
		
		semanticContext = new SemanticContext("write", SemanticContext.TOKEN_TYPE_ID, SemanticContext.CATEGORY_PROC, 
				SemanticContext.TYPE_UNDEFINED,	Double.MAX_VALUE, SemanticContext.SCOPE_GLOBAL, true, null);
		
		semanticTable.addSemanticContext(semanticContext);
		
	}
		
	private void addProgramIdentifier(ArrayList<Token> tokens) {
		
		if(tokensIndex < tokens.size()) {
			
			token = tokens.get(tokensIndex++);
			
			if(token.getType() == Token.IDENTIFIER) {
				
				SemanticContext semanticContext = new SemanticContext(token.getToken(), SemanticContext.TOKEN_TYPE_ID, SemanticContext.CATEGORY_PROG, 
						SemanticContext.TYPE_UNDEFINED, Double.MAX_VALUE, SemanticContext.SCOPE_GLOBAL, true, token);
				
				boolean b = semanticTable.addSemanticContext(semanticContext);
				
				if(!b) {
					String errorMessage = "Identificador " + token.getToken() + " duplicado";
					semanticErrors.add(new CompilerError(CompilerError.SEMANTIC, token, errorMessage));
				}
				
				if(tokensIndex < tokens.size()) token = tokens.get(tokensIndex++); 
			}
			
		}
				
	}
		
	private ArrayList<SemanticContext> addVars(ArrayList<Token> tokens) {
						
		ArrayList<SemanticContext> contexts = new ArrayList<>();
		ArrayList<SemanticContext> vars = new ArrayList<>();
		
		token = tokens.get(tokensIndex++);
		
		boolean flag = true;
						
		while(flag) {
									
			while(token.getType() == Token.IDENTIFIER) {
																
				SemanticContext s;
				
				if(globalScope)	{
					s = new SemanticContext(token.getToken(), SemanticContext.TOKEN_TYPE_ID, SemanticContext.CATEGORY_VAR, 
							SemanticContext.TYPE_UNDEFINED, Double.MAX_VALUE, SemanticContext.SCOPE_GLOBAL, false, token);	
				}
				else {
					s = new SemanticContext(token.getToken(), SemanticContext.TOKEN_TYPE_ID, SemanticContext.CATEGORY_VAR, 
							SemanticContext.TYPE_UNDEFINED, Double.MAX_VALUE, SemanticContext.SCOPE_LOCAL, false, token);
				}
				
				boolean b = semanticTable.addSemanticContext(s);
					
				if(b) {
					contexts.add(s);
					vars.add(s);
				}
				else {
					
					String errorMessage; 
					
					if(globalScope) errorMessage = "Declaração de variável global " + token.getToken() + " duplicada";
					else errorMessage = "Declaração de variável local " + token.getToken() + " duplicada";
					
					semanticErrors.add(new CompilerError(CompilerError.SEMANTIC, token, errorMessage));
				}
				
				if(tokensIndex < tokens.size()) token = tokens.get(tokensIndex++);
				else return null;
				
				if(token.getToken().equals(",")) {
					if(tokensIndex < tokens.size()) token = tokens.get(tokensIndex++);
					else return null;
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
					else return null;
				}
				else return null;
			}
						
			if(!(tokensIndex < tokens.size())) return null;

			if(token.getToken().equals(";") && tokens.get(tokensIndex).getType() == Token.IDENTIFIER) {
				
				token = tokens.get(tokensIndex++);
				flag = true;
				
				contexts.removeAll(contexts);
								
			}
			else flag = false;
						
		}
		
		return vars;
				
	}

	private void addProcedure(ArrayList<Token> tokens) {
		
		ArrayList<SemanticContext> vars = null;
		
		globalScope = false;
		
		if(tokensIndex < tokens.size()) token = tokens.get(tokensIndex++);
		else return;
		
		if(token.getType() != Token.IDENTIFIER) return;
		
		String procedureName = token.getToken();
		
		SemanticContext semanticContext = new SemanticContext(token.getToken(), SemanticContext.TOKEN_TYPE_ID, SemanticContext.CATEGORY_PROC, 
				SemanticContext.TYPE_UNDEFINED, Double.MAX_VALUE, SemanticContext.SCOPE_GLOBAL, false, token);
		
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
				vars = addVars(tokens);
			}
			
			while(!token.getToken().equals(")") && tokensIndex < tokens.size()) token = tokens.get(tokensIndex++);
						
			addProcedureSignature(procedureName, vars);
			
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
			ArrayList<SemanticContext> localVars = semanticTable.removeLocalVars();
			
			for(SemanticContext var : localVars) {
				
				if(!var.isUtilized()) {
					addSemanticWarning("Variável não utilizada: " + var.getLexeme(), var.getToken());
				}
			}
						
		}
		
		if(tokensIndex < tokens.size()) token = tokens.get(tokensIndex++);
	}
	
	private void identifier(ArrayList<Token> tokens) {
				
		if(tokensIndex < tokens.size()) {
						
			if(tokens.get(tokensIndex).getToken().equals(";")) {
				callProcedureWithoutParameters(tokens);
				if(tokensIndex < tokens.size()) token = tokens.get(tokensIndex++);
			}
			else if(tokens.get(tokensIndex).getToken().equals("(")) {
				callProcedure(tokens);
			}
			else if(tokens.get(tokensIndex).getToken().equals(":=")) {
				assignment(tokens);
			}
			
		}
		
				
	}
	
	private void callProcedureWithoutParameters(ArrayList<Token> tokens) {
		
		String procedure = token.getToken();
		
		if(!procedureExists(procedure)) return;
				
		if(procedure.equals("write") || procedure.equals("read")) {
			CompilerError compilerError = new CompilerError(CompilerError.SEMANTIC, token, "O procedimento " + procedure + " exige parâmetros");
			semanticErrors.add(compilerError);
			return;
		}
								
		ProcedureSignature procedureSignature = getProcedureSignature(procedure);
		semanticTable.get(procedure, SemanticContext.TOKEN_TYPE_ID, SemanticContext.CATEGORY_PROC).setUtilized(true);
		
		boolean b = procedureSignature.verifyParameters(new int[0]);
		if(!b) {
			String messageError = procedureSignature.getErrorMessage() + ", nenhum parâmetro enviado";
			semanticErrors.add(new CompilerError(CompilerError.SEMANTIC, token, messageError));		
		}
						
	}

	private void callProcedure(ArrayList<Token> tokens) {
						
		Token procedureToken = token;
		String procedure = token.getToken();
		
		token = tokens.get(tokensIndex++);
						
		if(!procedureExists(procedure)) return;
		semanticTable.get(procedure, SemanticContext.TOKEN_TYPE_ID, SemanticContext.CATEGORY_PROC).setUtilized(true);
		
		boolean flag = true;
		ArrayList<ArrayList<Token>> parameters = getParameters(tokens);		
		for(ArrayList<Token> p : parameters) {
			
			for(Token t : p) {
				
				if(t.getType() == Token.IDENTIFIER) {
					
					if(!semanticTable.exists(t.getToken(), SemanticContext.TOKEN_TYPE_ID, SemanticContext.CATEGORY_VAR)) {
						semanticErrors.add(new CompilerError(CompilerError.SEMANTIC, token, "Variavel " + t.getToken() + " não declarada"));
						flag = false;
					}
					else getVar(t.getToken()).setUtilized(true);
										
				}
									
			}
			
			if(!flag) return;
						
		}
						
		if(procedure.equals("write")) callRead(parameters);
		else if(procedure.equals("read")) return;
		else {
						
			int numParameters = getProcedureSignature(procedure).getNumParameters();
												
			if(parameters.size() != numParameters) {
								
				String errorMessage = getProcedureSignature(procedure).getErrorMessage();
								
				if(parameters.size() == 1) errorMessage = errorMessage + " encontrado " + parameters.size() + " parâmetro";
				else errorMessage = errorMessage + " encontrados " + parameters.size() + " parâmetros";
								 
				semanticErrors.add(new CompilerError(CompilerError.SEMANTIC, procedureToken, errorMessage));
				return;
				
			}
						
			int types[] = new int[parameters.size()];
			
			SemanticParser parser = new SemanticParser(this);			
						
			for(int i = 0; i < types.length; ++i) types[i] = parser.parse(parameters.get(i));
						
			ProcedureSignature procedureSignature = getProcedureSignature(procedure);
			
			if(!procedureSignature.verifyParameters(types)) {
				
				String typ[] = new String[]{"indefinido", "int", "real", "boolean"};
				StringBuffer buffer = new StringBuffer("(");
				
				for(int i = 0; i < types.length - 1; ++i) {	
					buffer.append(typ[types[i]] + ", ");
				}
				
				buffer.append(typ[types[types.length - 1]] + ")");
				
				String errorMessage = procedureSignature.getErrorMessage();
				errorMessage = errorMessage + " encontrados os parâmetos " + buffer.toString();
				
				semanticErrors.add(new CompilerError(CompilerError.SEMANTIC, procedureToken, errorMessage));
			}
						
		}
				
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<ArrayList<Token>> getParameters(ArrayList<Token> tokens) {
				
		token = tokens.get(tokensIndex++);
		
		ArrayList<ArrayList<Token>> parameters = new ArrayList<>();
		ArrayList<Token> p = new ArrayList<Token>();
		
		while(token.getType() == Token.IDENTIFIER || token.getType() == Token.INTEGER_NUMBER || token.getType() == Token.REAL_NUMBER ||
				token.getType() == Token.BOOLEAN_VALUE || token.getToken().equals("(") || token.getToken().equals("+") || 
				token.getToken().equals("-") || token.getToken().equals("not")) {
			
			while(token.getType() == Token.IDENTIFIER || token.getType() == Token.INTEGER_NUMBER || token.getType() == Token.REAL_NUMBER || 
					token.getType() == Token.BOOLEAN_VALUE || token.getToken().equals("(") || token.getToken().equals(")") || 
					operators.contains(token.getToken())) {
				
				p.add(token);
				
				if(tokensIndex < tokens.size()) token = tokens.get(tokensIndex++);
			}
			
			parameters.add((ArrayList<Token>) p.clone());
			p.removeAll(p);
						
			if(token.getToken().equals(",")) {
				if(tokensIndex < tokens.size()) token = tokens.get(tokensIndex++);
			}
									
		}
		
		p = parameters.get(parameters.size() - 1);
		
		int openNum = 0;
		int closeNum = 0;
		
		for(Token t : p) {
			if(t.getToken().equals("(")) openNum++;
			if(t.getToken().equals(")")) closeNum++;
		}
		
		if(closeNum > openNum) {
			p.remove(p.size() - 1);
		}
		
		return parameters;
		
	}
				
	private boolean procedureExists(String procedure) {
		
		boolean b = semanticTable.exists(procedure, SemanticContext.TOKEN_TYPE_ID, SemanticContext.CATEGORY_PROC);
		
		if(!b) {
			CompilerError compilerError = new CompilerError(CompilerError.SEMANTIC, token, "O procedimento " + procedure + " não existe");
			semanticErrors.add(compilerError);
		}
		
		return b;
	}
	
	public ProcedureSignature getProcedureSignature(String procedure) {
		
		ProcedureSignature procedureSignature = null;
		
		for(ProcedureSignature ps : procedureSignatures) {
			
			if(ps.getName().equals(procedure)) { 
				procedureSignature = ps;
				break;
			}
			
		}
		
		return procedureSignature; 
	}
	
	private void callRead(ArrayList<ArrayList<Token>> parameters) {
		
		for(ArrayList<Token> parameter : parameters) {
			
			if(parameter.size() > 1) {
				String errorMessage = "O método read aceita apenas uma lista de variáveis";
				semanticErrors.add(new CompilerError(CompilerError.SEMANTIC, token, errorMessage));
				return;
			}
			
			if(parameter.get(0).getType() != Token.IDENTIFIER) {
				String errorMessage = "O método read aceita apenas variáveis como parâmetro";
				semanticErrors.add(new CompilerError(CompilerError.SEMANTIC, token, errorMessage));
				return;
			}
			
		}
		
		int type = getVar(parameters.get(0).get(0).getToken()).getType();
		for(ArrayList<Token> parameter : parameters) {
			
			if(type != getVar(parameter.get(0).getToken()).getType()) {
				String errorMessage = "Os parâmetros do método read devem ser do mesmo tipo";
				semanticErrors.add(new CompilerError(CompilerError.SEMANTIC, token, errorMessage));
				return;
			}
			
		}
								
	}
	
	public SemanticContext getVar(String lexeme) {
		SemanticContext var = semanticTable.get(lexeme, SemanticContext.TOKEN_TYPE_ID, SemanticContext.CATEGORY_VAR, SemanticContext.SCOPE_LOCAL);
		if(var == null) var = semanticTable.get(lexeme, SemanticContext.TOKEN_TYPE_ID, SemanticContext.CATEGORY_VAR, SemanticContext.SCOPE_GLOBAL);
		return var;
	}
	
	public int getTokenSemanticType(Token token) {
		
		int type = SemanticContext.TYPE_UNDEFINED; 
		if(token.getType() == Token.IDENTIFIER) type = getVar(token.getToken()).getType();
		else if(token.getType() == Token.INTEGER_NUMBER) type = SemanticContext.TYPE_INT;
		else if(token.getType() == Token.REAL_NUMBER) type = SemanticContext.TYPE_REAL;
		else if(token.getType() == Token.BOOLEAN_VALUE) type = SemanticContext.TYPE_BOOLEAN;
		
		return type;
	}
				
	public void addSemanticError(String errorMessage, Token token) {
		semanticErrors.add(new CompilerError(CompilerError.SEMANTIC, token, errorMessage));
	}
	
	public void addSemanticWarning(String errorMessage, Token token) {
		semanticErrors.add(new CompilerError(CompilerError.WARNING, token, errorMessage));
	}
	
	private void assignment(ArrayList<Token> tokens) {
			
		ArrayList<Token> expression = new ArrayList<>();
		
		int varType = getTokenSemanticType(token);
		
		token = tokens.get(tokensIndex++);
		
		if(!(tokensIndex < tokens.size())) return;
		
		if(token.getToken().equals(":=")) token = tokens.get(tokensIndex++);
		
		while(tokensIndex < tokens.size() && !token.getToken().equals(";") && !token.getToken().equals("end") && !token.getToken().equals("else")) {
			expression.add(token);
			token = tokens.get(tokensIndex++);
		}
		
		if(token.getToken().equals("end") || token.getToken().equals("else")) --tokensIndex;
		
		int expressionType = new SemanticParser(this).parse(expression);
		
		if(varType == SemanticContext.TYPE_INT && expressionType != SemanticContext.TYPE_INT) {
			addSemanticError("Atribuição de tipo inválido à variavel do tipo inteiro", expression.get(0));
		}
		else if(varType == SemanticContext.TYPE_REAL && expressionType != SemanticContext.TYPE_INT && expressionType != SemanticContext.TYPE_REAL) {
			addSemanticError("Atribuição de tipo inválido à variavel do tipo real", expression.get(0));
		}
		else if(varType == SemanticContext.TYPE_BOOLEAN && expressionType != SemanticContext.TYPE_BOOLEAN) {
			addSemanticError("Atribuição de tipo inválido à variavel do tipo boolean", expression.get(0));
		}
		else if(varType != expressionType) {
			addSemanticError("Atribuição com tipo inválido", expression.get(0));
		}
		
//		System.out.println(token);
//		new Scanner(System.in).nextLine();
		
	}

	private void ifStatement(ArrayList<Token> tokens) {
		
		Token ifToken = token;
		
		ArrayList<Token> expression = new ArrayList<Token>();
		
		if(!(tokensIndex < tokens.size())) return;
		
		token = tokens.get(tokensIndex++);
		
		while(tokensIndex < tokens.size() && !tokens.get(tokensIndex).getToken().equals("then") && !tokens.get(tokensIndex).getToken().equals("begin")) {
			expression.add(token);
			token = tokens.get(tokensIndex++);
		}
		expression.add(token);
		
		if(token.getToken().equals("begin")) --tokensIndex;
		
		if(expression.size() == 0) return;
				
		int type = new SemanticParser(this).parse(expression);
		
		if(type != SemanticContext.TYPE_BOOLEAN) {
			addSemanticError("Bloco if requer uma condição boleana", ifToken);
		}
						
	}
	
	private void whileStatement(ArrayList<Token> tokens) {
				
		Token whileToken = token;
		
		ArrayList<Token> expression = new ArrayList<Token>();
		
		if(!(tokensIndex < tokens.size())) return;
		
		token = tokens.get(tokensIndex++);
		
		while(tokensIndex < tokens.size() && !tokens.get(tokensIndex).getToken().equals("do") && !tokens.get(tokensIndex).getToken().equals("begin")) {
			expression.add(token);
			token = tokens.get(tokensIndex++);
		}
		expression.add(token);
		
		if(token.getToken().equals("begin")) --tokensIndex;
		
		if(expression.size() == 0) return;
		
		int type = new SemanticParser(this).parse(expression);
		
		if(type != SemanticContext.TYPE_BOOLEAN) {
			addSemanticError("Bloco if requer uma condição boleana", whileToken);
		}		
				
	}
	
	public ArrayList<CompilerError> getSemanticErrors() {
		return semanticErrors;
	}
	
	public SemanticTable getSemanticTable() {
		return semanticTable;
	}
}
 