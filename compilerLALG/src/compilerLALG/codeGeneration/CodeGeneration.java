package compilerLALG.codeGeneration;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Stack;

import compilerLALG.lexical.Token;
import compilerLALG.semantic.Semantic;
import compilerLALG.semantic.SemanticContext;
import compilerLALG.semantic.SemanticParser;
import compilerLALG.semantic.SemanticTable;

public class CodeGeneration {
	
	private ArrayList<String> C;
	private AddressVector addressVector;
	
	private Token token;
	private int tokenIndex;
	private int deviation;
	private Stack stack;
	private Stack stack2;
		
	private String programName;
	
	private Semantic semantic;
	
	public CodeGeneration() {
		addressVector = new AddressVector();
	}
	
	public boolean execute(ArrayList<Token> tokens, SemanticTable semanticTable, Semantic semantic) {
				
		if(!verifyProcedure(tokens)) return false;
				
		C = new ArrayList<>();
		this.semantic = semantic;
		
		deviation = 0;
		stack = new Stack();
		stack2 = new Stack();
		
		token = tokens.get(tokenIndex++);
		
		initProgram(tokens);
		addVars(tokens);
		
		generateBytecodes(tokens, semanticTable);
		saveInFile();
		
		return true;
	}
	
	private void saveInFile() {
		
		try{
			
			File file = new File(programName + ".bc");
			FileWriter fileWriter = new FileWriter(file);
			
			for(String s : C) {
				fileWriter.write(s + "\n");
			}
			
			fileWriter.flush();
			fileWriter.close();
						
		} catch(Exception e) { e.printStackTrace();}
		
	}
	
	private void generateBytecodes(ArrayList<Token> tokens, SemanticTable semanticTable) {
		
		while(tokenIndex < tokens.size()) {
						
			if(token.getToken().equals("read")) read(tokens, semanticTable);
			else if(token.getToken().equals("write")) write(tokens, semanticTable);
			else if(token.getToken().equals("if")) {
				ifStatement(tokens, semanticTable);
				stack2.forEach(i -> C.add(i + " NADA"));
			}
			else if(token.getToken().equals("while")) {
				whileStatement(tokens, semanticTable);
				C.add("DSVS " + stack2.pop());
				C.add(stack.pop() + " NADA ");
				
			}
			else if(token.getType() == Token.IDENTIFIER) identifier(tokens, semanticTable);
			else token = tokens.get(tokenIndex++);
		}
		
		C.add("PARA");
		
		
	}
	
	private void initProgram(ArrayList<Token> tokens) {
		
		C.add("INPP");
		
		token = tokens.get(tokenIndex++);
		programName = token.getToken();
		token = tokens.get(tokenIndex++);
		
	}
	
	private void addVars(ArrayList<Token> tokens) {
				
		while(!token.getToken().equals("begin")) {
			
			if(token.getType() == Token.IDENTIFIER) {
				addressVector.addAddress(token);
				C.add("AMEM 1");
			}
			
			token = tokens.get(tokenIndex++);
		}
		
	}

	private boolean verifyProcedure(ArrayList<Token> tokens) {
		
		for(Token t : tokens) {
			if(t.getToken().equals("procedure")) return false;
		}
		
		return true;
		
	}
	
	private void read(ArrayList<Token> tokens, SemanticTable semanticTable) {
		
		ArrayList<Token> parameters = new ArrayList<Token>();
		
		token = tokens.get(tokenIndex++);
		
		while(!token.getToken().equals("if") && !token.getToken().equals("while") && !token.getToken().equals(";") && !token.getToken().equals("end")) {
			if(token.getType() == Token.IDENTIFIER) parameters.add(token);
			token = tokens.get(tokenIndex++);
		}
		
		if(token.getToken().equals("if") || token.getToken().equals("while") || token.getToken().equals("end")) --tokenIndex;
		
		for(Token t : parameters) {
			
			int address = addressVector.getAddress(t.getToken());
			int type = semanticTable.get(t.getToken(), SemanticContext.TOKEN_TYPE_ID, SemanticContext.CATEGORY_VAR).getType();
			
			if(type == SemanticContext.TYPE_INT) C.add("LEIT");
			else if(type == SemanticContext.TYPE_REAL) C.add("LERL");
			else if(type == SemanticContext.TYPE_REAL) C.add("LEBL");
			
			C.add("ARMZ " + address);
			
		}
		
	}
	
	private void identifier(ArrayList<Token> tokens, SemanticTable semanticTable) {
		
		if(tokens.get(tokenIndex).getToken().equals(":=")) {
			assigment(tokens, semanticTable);
		}
		
		
	}
	
	private void assigment(ArrayList<Token> tokens, SemanticTable semanticTable) {
				
		int address = addressVector.getAddress(token.getToken());
		
		token = tokens.get(tokenIndex++); //:=
		token = tokens.get(tokenIndex++);
		
		ArrayList<Token> expression = new ArrayList<>();
				
		while(!token.getToken().equals("if") && !token.getToken().equals("while") && !token.getToken().equals(";") && !token.getToken().equals("end") && !token.getToken().equals("else")) {
			expression.add(token);
			token = tokens.get(tokenIndex++);	
		}
				
		if(token.getToken().equals("if") || token.getToken().equals("while") || token.getToken().equals("end") || token.getToken().equals("else")) --tokenIndex;
		
		CodeGenerationParser codeGenerationParser = new CodeGenerationParser(expression);
		codeGenerationParser.parse(semanticTable, addressVector);
		C.addAll(codeGenerationParser.getC());
		
		C.add("ARMZ " + address);
						
	}
	
	@SuppressWarnings("unchecked")
	private void write(ArrayList<Token> tokens, SemanticTable semanticTable) {
				
		ArrayList<Token> expression = new ArrayList<Token>();
		ArrayList<ArrayList<Token>> parameters = new ArrayList<ArrayList<Token>>();
		
		token = tokens.get(tokenIndex++);
		if(token.getToken().equals("(")) token = tokens.get(tokenIndex++);
		
		while(!token.getToken().equals("if") && !token.getToken().equals("while") && !token.getToken().equals(";") && !token.getToken().equals("end") && !token.getToken().equals(")")) {
						
			if(!token.getToken().equals(",")) expression.add(token);
			else {
				parameters.add((ArrayList<Token>) expression.clone());
				expression.removeAll(expression);
			}
			
			token = tokens.get(tokenIndex++);
			
		}
		
		parameters.add(expression);
		
		if(token.getToken().equals("if") || token.getToken().equals("while") || token.getToken().equals("end")) --tokenIndex;
				
		for(ArrayList<Token> t : parameters) {
			
			CodeGenerationParser codeGenerationParser = new CodeGenerationParser(t); 
			codeGenerationParser.parse(semanticTable, addressVector);
			C.addAll(codeGenerationParser.getC());
			
			int type = new SemanticParser(semantic).parse(t);
			
			if(type == SemanticContext.TYPE_BOOLEAN) {
				C.add("IMPB");
			}
			else C.add("IMPE");
			
		}
		
	}
	
	private void ifStatement(ArrayList<Token> tokens, SemanticTable semanticTable) {
				
		ArrayList<Token> expression = new ArrayList<>();
		ArrayList<Token> block = extractBlock(tokens, token, tokenIndex);
		
		int max = tokenIndex + block.size() - 1;
					
		token = tokens.get(tokenIndex++);
		
		while(!token.getToken().equals("then")) {
			expression.add(token);
			token = tokens.get(tokenIndex++);
		}
		token = tokens.get(tokenIndex++);
		
		CodeGenerationParser codeGenerationParser = new CodeGenerationParser(expression); //condição do if
		codeGenerationParser.parse(semanticTable, addressVector);
		C.addAll(codeGenerationParser.getC());
		C.add("DSVF " + deviation);
		stack.push(deviation++);
				
		while(tokenIndex < max) {
			resolve(tokens, semanticTable);
			if(tokenIndex < max) token = tokens.get(tokenIndex++);			
		}
				
	}
	
	private void whileStatement(ArrayList<Token> tokens, SemanticTable semanticTable) {
		
		ArrayList<Token> expression = new ArrayList<>();
		ArrayList<Token> block = extractBlock(tokens, token, tokenIndex);
		
		int max = tokenIndex + block.size() - 1;
					
		C.add(deviation + " NADA");
		stack2.push(deviation++);
		
		token = tokens.get(tokenIndex++);
		
		while(!token.getToken().equals("do")) {
			expression.add(token);
			token = tokens.get(tokenIndex++);
		}
		token = tokens.get(tokenIndex++);
		
		
		
		CodeGenerationParser codeGenerationParser = new CodeGenerationParser(expression); //condição do if
		codeGenerationParser.parse(semanticTable, addressVector);
		C.addAll(codeGenerationParser.getC());
		
		C.add("DSVF " + deviation);
		stack.push(deviation++);
						
		while(tokenIndex < max) {
			resolve(tokens, semanticTable);
			if(tokenIndex < max) token = tokens.get(tokenIndex++);			
		}
				
	}	
	
	
	private void resolve(ArrayList<Token> tokens, SemanticTable semanticTable) {
				
		if(token.getToken().equals("if")) {
			ifStatement(tokens, semanticTable);
		}
		else if(token.getToken().equals("while")) {
			whileStatement(tokens, semanticTable);
		}
		else if(token.getType() == Token.IDENTIFIER){
			identifier(tokens, semanticTable);
		}
		else if(token.getToken().equals("else")) {
			C.add("DSVS " + deviation);
			stack2.push(deviation++);
			C.add(stack.pop() + " NADA");
		}
		
	}
	
	private ArrayList<Token> extractBlock(ArrayList<Token> tokens, Token token, int tokenIndex) {
				
		ArrayList<Token> block = new ArrayList<>();
		
		if(token.getToken().equals("if")) {
						
			while(!token.getToken().equals("then")) {
				block.add(token);
				token = tokens.get(tokenIndex++);
			}
			block.add(token);
			
			token = tokens.get(tokenIndex++);			
			
			block.addAll(extractBlock(tokens, token, tokenIndex));
									
		}
		else if(token.getToken().equals("while")) {
			
			while(!token.getToken().equals("do")) {
				block.add(token);
				token = tokens.get(tokenIndex++);
			}
			block.add(token);
			
			token = tokens.get(tokenIndex++);
						
			block.addAll(extractBlock(tokens, token, tokenIndex));
			
		}
		else if(token.getToken().equals("else")) {
			block.add(token);
			token = tokens.get(tokenIndex++);
			block.addAll(extractBlock(tokens, token, tokenIndex));
		}
		else if(token.getToken().equals("begin")) {
			
			while(!token.getToken().equals("end")) {
				block.add(token);
				token = tokens.get(tokenIndex++);
			}
			
		}
		else {
			
			while(!token.getToken().equals("if") && !token.getToken().equals("while") && !token.getToken().equals("end") && !token.getToken().equals("else")) {				
				block.add(token);
				token = tokens.get(tokenIndex++);
			}
			if(token.getToken().equals("else")) block.addAll(extractBlock(tokens, token, tokenIndex));
			
		}
		
		return block;
		
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		C.forEach(c -> buffer.append(c + "\n"));
		return buffer.toString();
	}
	
	public ArrayList<String> getC() {
		return C;
	}
	
}
