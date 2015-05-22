package compilerLALG.codeGeneration;

import java.util.ArrayList;
import java.util.Arrays;

import compilerLALG.lexical.Token;
import compilerLALG.semantic.SemanticTable;

public class CodeGenerationParser {
	
	private ArrayList<CodeGenerationToken> tokens;
	private ArrayList<String> C;
		
	public CodeGenerationParser(ArrayList<Token> tokens) {
		C = new ArrayList<String>();
		createTokens(tokens);
	}
	
	private void createTokens(ArrayList<Token> tokens) { 
		this.tokens = new ArrayList<>();
		for(Token token : tokens) this.tokens.add(new CodeGenerationToken(token));
	}
	
	public void parse(SemanticTable semanticTable, AddressVector addressVector) {
				
		while(tokens.get(0).getToken().equals("(") && tokens.get(tokens.size() - 1).getToken().equals(")")) {
			tokens.remove(0);
			tokens.remove(tokens.size() - 1);
		}
		
		if(tokens.size() == 1) {
			singleExpression(addressVector, tokens);
			return;
		}
		else if(tokens.size() == 2) {
			binaryExpression(addressVector, tokens);
			return;
		}
		
		while(contains("(", tokens)) {
						
			int start = indexOf("(", tokens);
			int end = lastIndexOf(")", tokens);
			if(end == -1) end = tokens.size() - 1;
			
			resolveExpression(new ArrayList<CodeGenerationToken>(tokens.subList(start + 1, end)), addressVector);
			
			tokens.get(start).setUsed(true);
			tokens.get(end).setUsed(true);
						
		}
				
		resolveExpression(tokens, addressVector);
										
	}
	
	private void resolveExpression(ArrayList<CodeGenerationToken> tokens, AddressVector addressVector) {
				
		int index = -1;
		boolean flag;
		
		while(hasNotUsed(tokens)) {
			
			flag = false;
			
			
			if(contains("not", tokens)) {
				
				index = indexOf("not", tokens);
				
				ArrayList<CodeGenerationToken> expression = new ArrayList<CodeGenerationToken>();
				expression.add(tokens.get(index));
				expression.add(tokens.get(index + 1));
				
				binaryExpression(addressVector, expression);
				
				expression.get(0).setUsed(true);
				expression.get(1).setUsed(true);
				
			}
			else if(contains("*", tokens) || contains("div", tokens)) {
				
				index = indexOf(new String[]{"*", "div"}, tokens);
				CodeGenerationToken operator = tokens.get(index);
				
				if(operator.getToken().getToken().equals("div")) { 
					numericExpression(index, tokens, addressVector, "DIVI");
				}
				else {
					numericExpression(index, tokens, addressVector, "MULT");
				}
				
			}
			else if(contains("+", tokens) || contains("-", tokens)) {
				
				index = indexOf(new String[]{"+", "-"}, tokens);
				flag = true;
							
				if(index == 0) {
					index = indexOf(tokens, new String[]{"+", "-"}, index + 1);
					flag = false;
				}
				else {
					Token t = tokens.get(index - 1).getToken();
					if(t.getType() == Token.SPECIAL_SYMBOL || t.getType() == Token.RESERVED_WORD) {
						index = indexOf(tokens, new String[]{"+", "-"}, index + 1);
						if(index != -1) flag = true;
					}
				}
				
			}
			
			if(!flag && (contains(">", tokens) || contains(">=", tokens) || contains("<", tokens) || contains("<=", tokens))) {
								
				index = indexOf(new String[]{">", ">=", "<", "<="}, tokens);
				CodeGenerationToken operator = tokens.get(index);
								
				if(operator.getToken().getToken().equals("<")) {
					relationalExpression(index, tokens, addressVector, "CMME");
				}
				else if(operator.getToken().getToken().equals("<=")) {
					relationalExpression(index, tokens, addressVector, "CMEG");
				}
				else if(operator.getToken().getToken().equals(">")) {
					relationalExpression(index, tokens, addressVector, "CMMA");
				}
				else if(operator.getToken().getToken().equals(">=")) {
					relationalExpression(index, tokens, addressVector, "CMAG");
				}
								
			}
			else if(!flag && (contains("=", tokens) || contains("<>", tokens))) {
				
				index = indexOf(new String[]{"=", "<>"}, tokens);
				CodeGenerationToken operator = tokens.get(index);
								
				if(operator.getToken().getToken().equals("=")) {
					equalityExpression(index, tokens, addressVector, "CMIG");
				}
				else if(operator.getToken().getToken().equals("<>")) {
					equalityExpression(index, tokens, addressVector, "CMDG");
				}
								
			}
			else if(!flag && (contains("or", tokens) || contains("and", tokens))) {
				
				index = indexOf(new String[]{"or", "and"}, tokens);
				CodeGenerationToken operator = tokens.get(index);
				
				if(operator.getToken().getToken().equals("or")) {					
					logicalExpression(index, tokens, addressVector, "DISJ");
				}
				else if(operator.getToken().getToken().equals("and")) {
					logicalExpression(index, tokens, addressVector, "CONJ");
				}
			}
			
			if(flag) {
				
				CodeGenerationToken operator = tokens.get(index);
				
				if(operator.getToken().getToken().equals("+")) { 
					numericExpression(index, tokens, addressVector, "SOMA");
				}
				else numericExpression(index, tokens, addressVector, "SUBT");
				
			}
			
		}
				
	}
	
	private void numericExpression(int index, ArrayList<CodeGenerationToken> tokens, AddressVector addressVector, String operation) {
		
		ArrayList<ArrayList<CodeGenerationToken>> operands = getOperands(tokens, index);
		
		ArrayList<CodeGenerationToken> op1 = operands.get(0);
		ArrayList<CodeGenerationToken> op2 = operands.get(1);
		
		if(!op1.get(0).isUsed()) {
			
			if(op1.size() == 1) singleExpression(addressVector, op1);
			else binaryExpression(addressVector, op1);
			
			for(CodeGenerationToken t : op1) t.setUsed(true);
			
		}
		
		if(!op2.get(0).isUsed()) {
			
			if(op2.size() == 1) singleExpression(addressVector, op2);
			else binaryExpression(addressVector, op2);
			
			for(CodeGenerationToken t : op2) t.setUsed(true);
			
		}
						
		C.add(operation);
		
		tokens.get(index).setUsed(true);

	}
	
	private void relationalExpression(int index, ArrayList<CodeGenerationToken> tokens, AddressVector addressVector, String operation) {
		
		ArrayList<ArrayList<CodeGenerationToken>> operands = getOperands(tokens, index);
		
		ArrayList<CodeGenerationToken> op1 = operands.get(0);
		ArrayList<CodeGenerationToken> op2 = operands.get(1);
		
		if(!op1.get(0).isUsed()) {
			
			if(op1.size() == 1) singleExpression(addressVector, op1);
			else binaryExpression(addressVector, op1);
			
			for(CodeGenerationToken t : op1) t.setUsed(true);
			
		}
		
		if(!op2.get(0).isUsed()) {
			
			if(op2.size() == 1) singleExpression(addressVector, op2);
			else binaryExpression(addressVector, op2);
			
			for(CodeGenerationToken t : op2) t.setUsed(true);
			
		}
						
		C.add(operation);
		
		tokens.get(index).setUsed(true);
		
	}
	
	private void equalityExpression(int index, ArrayList<CodeGenerationToken> tokens, AddressVector addressVector, String operation) {
				
		ArrayList<ArrayList<CodeGenerationToken>> operands = getOperands(tokens, index);
		
		ArrayList<CodeGenerationToken> op1 = operands.get(0);
		ArrayList<CodeGenerationToken> op2 = operands.get(1);
		
		if(!op1.get(0).isUsed()) {
			
			if(op1.size() == 1) singleExpression(addressVector, op1);
			else binaryExpression(addressVector, op1);
			
			for(CodeGenerationToken t : op1) t.setUsed(true);
			
		}
		
		if(!op2.get(0).isUsed()) {
			
			if(op2.size() == 1) singleExpression(addressVector, op2);
			else binaryExpression(addressVector, op2);
			
			for(CodeGenerationToken t : op2) t.setUsed(true);
			
		}
						
		C.add(operation);
		
		tokens.get(index).setUsed(true);
		
	}
	
	private void logicalExpression(int index, ArrayList<CodeGenerationToken> tokens, AddressVector addressVector, String operation) {
		
		ArrayList<ArrayList<CodeGenerationToken>> operands = getOperands(tokens, index);
		
		ArrayList<CodeGenerationToken> op1 = operands.get(0);
		ArrayList<CodeGenerationToken> op2 = operands.get(1);
		
		if(!op1.get(0).isUsed()) {
			
			if(op1.size() == 1) singleExpression(addressVector, op1);
			else binaryExpression(addressVector, op1);
			
			for(CodeGenerationToken t : op1) t.setUsed(true);
			
		}
		
		if(!op2.get(0).isUsed()) {
			
			if(op2.size() == 1) singleExpression(addressVector, op2);
			else binaryExpression(addressVector, op2);
			
			for(CodeGenerationToken t : op2) t.setUsed(true);
			
		}
						
		C.add(operation);
		
		tokens.get(index).setUsed(true);
		
	}
	
	private void singleExpression(AddressVector addressVector, ArrayList<CodeGenerationToken> tokens) {
		
		if(tokens.get(0).getToken().getType() == Token.IDENTIFIER) {						
			int address = addressVector.getAddress(tokens.get(0).getToken().getToken());
			C.add("CRVL " + address);
		}
		else if(tokens.get(0).getToken().getType() == Token.REAL_NUMBER || tokens.get(0).getToken().getType() == Token.INTEGER_NUMBER) {
			C.add("CRCT " + tokens.get(0).getToken().getToken());
		}
		else if(tokens.get(0).getToken().getType() == Token.BOOLEAN_VALUE) {
			if(tokens.get(0).getToken().getToken().equals("true")) C.add("CRCT 1");
			else C.add("CRCT 0");
		}
		
	}
	
	private void binaryExpression(AddressVector addressVector, ArrayList<CodeGenerationToken> tokens) {
		
		singleExpression(addressVector, new ArrayList<>(tokens.subList(1, 2)));
						
		if(tokens.get(0).getToken().getToken().equals("-")) {
			C.add("INVR");
		}
		if(tokens.get(0).getToken().getToken().equals("not")) {
			C.add("NEGA");
		}		
		
	}
	
	public ArrayList<String> getC() {
		return C;
	}
	
	private boolean contains(String token, ArrayList<CodeGenerationToken> tokens) {
		for(CodeGenerationToken t : tokens) {
			if(t.getToken().getToken().equals(token) && !t.isUsed()) return true;
		}
		return false;
	}
		
	private int lastIndexOf(String token, ArrayList<CodeGenerationToken> tokens) {
		
		int lastIndex = -1;
		
		for(int i = 0; i < tokens.size(); ++i) {
			if(tokens.get(i).getToken().getToken().equals(token) && !tokens.get(i).isUsed()) lastIndex = i;
		}
		
		return lastIndex;
	}
	
	private int indexOf(String token, ArrayList<CodeGenerationToken> tokens) {
		for(int i = 0; i < tokens.size(); ++i) {
			if(tokens.get(i).getToken().getToken().equals(token)) return i;
		}
		return -1;
	}
	
	private int indexOf(String token[], ArrayList<CodeGenerationToken> tokens)  {
		
		ArrayList<String> tokenAux = new ArrayList<>(Arrays.asList(token));
		
		for(int i = 0; i < tokens.size(); ++i) {
			if(tokenAux.contains((tokens.get(i).getToken().getToken())) && !tokens.get(i).isUsed()) return i;
		}
		
		return -1;
	}
	
	private int indexOf(ArrayList<CodeGenerationToken> tokens, String token[], int startIndex) {
		
		ArrayList<String> tokenAux = new ArrayList<>(Arrays.asList(token));
		
		for(int i = ++startIndex; i < tokens.size(); ++i) {
			if(tokenAux.contains((tokens.get(i).getToken().getToken())) && !tokens.get(i).isUsed()) return i;
		}
		
		return -1;
		
	}
	
	private boolean hasNotUsed(ArrayList<CodeGenerationToken> tokens) {
		
		for (CodeGenerationToken token : tokens) {
			if(!token.isUsed()) return true;
		}
		
		return false;
	}
	
	private ArrayList<ArrayList<CodeGenerationToken>> getOperands(ArrayList<CodeGenerationToken> tokens, int indexOperator) {
		
		ArrayList<ArrayList<CodeGenerationToken>> operands = new ArrayList<>();
		ArrayList<CodeGenerationToken> op1 = new ArrayList<>();
		ArrayList<CodeGenerationToken> op2 = new ArrayList<>();
		CodeGenerationToken t;
		
		ArrayList<String> operators = new ArrayList<>();
		operators.addAll(Arrays.asList(new String[]{"+", "-", "*", "div", ">", ">=", "<", "<=", "<>", "=", "or", "and"}));
		
		//primeiro operando
				
		t = tokens.get(indexOperator + 1);
		
		if(t.getToken().getToken().equals("+") || t.getToken().getToken().equals("-")) {
			op1.add(t);
			t = tokens.get(indexOperator + 2);
		}
		op1.add(t);
				
		//segundo operando
				
		t = tokens.get(indexOperator - 1);		
				
		if((indexOperator - 2) >= 0) {
			
			CodeGenerationToken t1 = tokens.get(indexOperator - 2);
			
			if(t1.getToken().getToken().equals("+") || t1.getToken().getToken().equals("-")) {
				
				if((indexOperator - 3) >= 0) {
					
					CodeGenerationToken t2 = tokens.get(indexOperator - 3);
										
					if(operators.contains(t2.getToken().getToken())) {
						op2.add(t1);
					}
					
				}
				else op2.add(t1);
				
			}
			
		}
		
		op2.add(t);
		
		operands.add(op2);
		operands.add(op1);
				
		return operands;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		tokens.forEach(t -> buffer.append(t + "\n"));
		return buffer.toString();
	}

	class CodeGenerationToken {
		
		private boolean used;
		private Token token;
		
		public CodeGenerationToken(Token token) {
			used = false;
			this.token = token;
		}
		
		public boolean isUsed() {
			return used;
		}
		
		public void setUsed(boolean used) {
			this.used = used;
		}
		
		public Token getToken() {
			return token;
		}
		
		@Override
		public String toString() {
			return token + "  " + used;
		}
		
	}

}

