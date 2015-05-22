package compilerLALG.semantic;

import java.util.ArrayList;
import java.util.Arrays;

import compilerLALG.lexical.Token;

public class SemanticParser {
	
	private ArrayList<String> operators;
	private Semantic semantic;
	
	public SemanticParser(Semantic semantic) {
		
		this.semantic = semantic;
		
		operators = new ArrayList<>();
		operators.addAll(Arrays.asList(new String[]{"+", "-", "*", "div", ">", ">=", "<", "<=", "<>", "=", "or", "and"}));
	}
		
	public int parse(ArrayList<Token> tokens) {
														
		while(tokens.get(0).getToken().equals("(") && tokens.get(tokens.size() - 1).getToken().equals(")")) {
			tokens.remove(0);
			tokens.remove(tokens.size() - 1);
		}
		
		if(tokens.size() == 1) {
			return semantic.getTokenSemanticType(tokens.get(0));
		}
		
		if(tokens.size() == 2) {
			return resolveUnitaryExpression(tokens);
		}
		
		while(contains(tokens, "(")) {
																		
			int start = indexOf(tokens, "(");
			int end = lastIndexOf(tokens, ")");
			if(end == -1) end = tokens.size() - 1;
						
			int type = resolveExpression(new ArrayList<Token>(tokens.subList(start + 1, end)));
						
			if(type == SemanticContext.TYPE_UNDEFINED) return type;
			
			updateExpression(tokens, start, end, type);
						
		}
		
		int type = resolveExpression(tokens);
						
		return type;
	}
	
	private int resolveUnitaryExpression(ArrayList<Token> tokens) {
		
		Token operator = tokens.get(0);
		Token operand = tokens.get(1);
		
		if(operator.getToken().equals("+") || operator.getToken().equals("-")) {
			
			int type = semantic.getTokenSemanticType(operand);
			
			if(type == SemanticContext.TYPE_INT || type == SemanticContext.TYPE_REAL) {
				return type;
			}
			else {
				semantic.addSemanticError("O operador " + operator.getToken() + " só está definido para valores numéricos", operator);
				return SemanticContext.TYPE_UNDEFINED;
			}
			
		}
		else if(operator.getToken().equals("not")) {
			
			int type = semantic.getTokenSemanticType(operand);
			
			if(type == SemanticContext.TYPE_BOOLEAN) {
				return type;
			}
			else {
				semantic.addSemanticError("O operador not só está definido para valores booleanos", operator);
				return SemanticContext.TYPE_UNDEFINED;
			}
		}
		
		return SemanticContext.TYPE_UNDEFINED;
	}
	
	private int resolveExpression(ArrayList<Token> tokens) {
						
		int index;
		int type = -1;
		boolean flag;
		boolean not;
		
		while(tokens.size() > 1) {
						
			index = -1;
			String operator = "";
			flag = false;
			not = false;
			
			if(contains(tokens, "not")) {
				index = indexOf(tokens, "not");
				not = true;
			}
			else if(contains(tokens, "*") || contains(tokens, "div")) {
				index = indexOf(tokens, new String[]{"*", "div"});
			}
			else if(contains(tokens, "+") || contains(tokens, "-")) {
								
				index = indexOf(tokens, new String[]{"+", "-"});
				flag = true;
				
				if(index == 0) {
					index = indexOf(tokens, new String[]{"+", "-"}, index + 1);
					flag = false;
				}
				else {
					
					Token t = tokens.get(index - 1);
					if(t.getType() == Token.SPECIAL_SYMBOL || t.getType() == Token.RESERVED_WORD) {
						index = indexOf(tokens, new String[]{"+", "-"}, index + 1);
						if(index != -1) flag = true;
					}
					
				}
				
			}
			
			if(!flag && (contains(tokens, ">") || contains(tokens, ">=") || contains(tokens, "<") || contains(tokens, "<="))) {
				index = indexOf(tokens, new String[]{">", ">=", "<", "<="});
			}
			else if(!flag && (contains(tokens, "=") || contains(tokens, "<>"))) {
				index = indexOf(tokens, new String[]{"=", "<>"});
			}
			else if(!flag && (contains(tokens, "or") || contains(tokens, "and"))) {
				index = indexOf(tokens, new String[]{"or", "and"});
			}
					
			operator = tokens.get(index).getToken();
			
			ArrayList<Token> op1 = null;
			ArrayList<Token> op2 = null;
			
			if(!not) {
				
				ArrayList<ArrayList<Token>> operands = getOperands(tokens, index);
				
				if(operands == null) return SemanticContext.TYPE_UNDEFINED;
				
				op1 = operands.get(0);
				op2 = operands.get(1);
			}
			else {
				op1 = new ArrayList<>();
				op1.add(tokens.get(index + 1));
			}			
				
			if(operator.equals("not")) {
				type = resolveNot(op1, tokens.get(index));
			}
			else if(operator.equals("*")) {
				type = resolveMultiplication(op1, op2, tokens.get(index));
			}
			else if(operator.equals("div")) {
				type = resolveDivision(op1, op2, tokens.get(index));
			}
			else if(operator.equals("+") || operator.equals("-")) {
				type = resolveSum(op1, op2, tokens.get(index));
			}
			else if(operator.equals("<=") || operator.equals("<") || operator.equals(">") || operator.equals(">=") || operator.equals("=") || operator.equals("<>")) {
				type = resolveRelational(op1, op2, tokens.get(index));
			}
			else if(operator.equals("or") || operator.equals("and")) {
				type = resolveLogical(op1, op2, tokens.get(index));
			}
												
			if(type == SemanticContext.TYPE_UNDEFINED) return type;
			
			int start;
			int end;
							
			if(!not) {
				start = index - op1.size();
				end = index + op2.size();
			}
			else {
				start = index;
				end = index + op1.size();
			}
			
			updateExpression(tokens, start, end, type);
		}
		
		return semantic.getTokenSemanticType(tokens.get(0));
				
	}
	
	private boolean contains(ArrayList<Token> tokens, String token) {
		for(Token t : tokens) {
			if(t.getToken().equals(token)) return true;
		}
		return false;
	}
	
	private int lastIndexOf(ArrayList<Token> tokens, String token) {
		
		int lastIndex = -1;
		
		for(int i = 0; i < tokens.size(); ++i) {
			if(tokens.get(i).getToken().equals(token)) lastIndex = i;
		}
		
		return lastIndex;
	}
	
	private int indexOf(ArrayList<Token> tokens, String token) {
		for(int i = 0; i < tokens.size(); ++i) {
			if(tokens.get(i).getToken().equals(token)) return i;
		}
		return -1;
	}
	
	private int indexOf(ArrayList<Token> tokens, String token[]) {
		
		ArrayList<String> tokenAux = new ArrayList<>(Arrays.asList(token));
		
		for(int i = 0; i < tokens.size(); ++i) {
			if(tokenAux.contains((tokens.get(i).getToken()))) return i;
		}
		
		return -1;
	}
	
	private int indexOf(ArrayList<Token> tokens, String token[], int startIndex) {
		
		ArrayList<String> tokenAux = new ArrayList<>(Arrays.asList(token));
		
		for(int i = ++startIndex; i < tokens.size(); ++i) {
			if(tokenAux.contains((tokens.get(i).getToken()))) return i;
		}
		
		return -1;
		
	}
	
	public String toString(ArrayList<Token> tokens) {
		StringBuffer buffer = new StringBuffer();
		for(Token t : tokens) buffer.append(t.getToken() + " ");
		return buffer.toString();
	}
	
	private ArrayList<ArrayList<Token>> getOperands(ArrayList<Token> tokens, int indexOperator) {
		
		ArrayList<ArrayList<Token>> operands = new ArrayList<>();
		ArrayList<Token> op1 = new ArrayList<>();
		ArrayList<Token> op2 = new ArrayList<>();
		Token t;
		
		//primeiro operando
		
		if(!((indexOperator + 1) < tokens.size())) return null;
		
		t = tokens.get(indexOperator + 1);
		
		if(t.getToken().equals("+") || t.getToken().equals("-")) {
			op1.add(t);
			if(!((indexOperator + 2) < tokens.size())) return null;
			t = tokens.get(indexOperator + 2);
		}
		
		if(t.getType() != Token.IDENTIFIER && t.getType() != Token.INTEGER_NUMBER && t.getType() != Token.REAL_NUMBER && t.getType() != Token.BOOLEAN_VALUE) {
			return null;
		}
		
		op1.add(t);
				
		//segundo operando
		
		if((indexOperator - 1) < 0) return null;
		
		t = tokens.get(indexOperator - 1);		
		
		if(t.getType() != Token.IDENTIFIER && t.getType() != Token.INTEGER_NUMBER && t.getType() != Token.REAL_NUMBER && t.getType() != Token.BOOLEAN_VALUE) {
			return null;
		}		
		
		if((indexOperator - 2) >= 0) {
			
			Token t1 = tokens.get(indexOperator - 2);
			
			if(t1.getToken().equals("+") || t1.getToken().equals("-")) {
				
				if((indexOperator - 3) >= 0) {
					
					Token t2 = tokens.get(indexOperator - 3);
										
					if(operators.contains(t2.getToken())) {
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
		
	private int resolveMultiplication(ArrayList<Token> operand1, ArrayList<Token> operand2, Token operator) {
		
		int type1;
		int type2;
		
		Token op1 = operand1.get(0);
		Token op2 = operand2.get(0);
		
		if(op1.getToken().equals("+") || op1.getToken().equals("-")) {
			op1 = operand1.get(1);
		}
		
		if(op2.getToken().equals("+") || op2.getToken().equals("-")) {
			op2 = operand2.get(1);
		}
		
		type1 = semantic.getTokenSemanticType(op1);
		type2 = semantic.getTokenSemanticType(op2);
		
		if(type1 == SemanticContext.TYPE_UNDEFINED || type2 == SemanticContext.TYPE_UNDEFINED ||
				type1 == SemanticContext.TYPE_BOOLEAN || type2 == SemanticContext.TYPE_BOOLEAN) {
			
			semantic.addSemanticError("A operação de multiplicação só está definida para valores numéricos", operator);
			return SemanticContext.TYPE_UNDEFINED; 
		}
		if(type1 == type2) return type1;
				
		return SemanticContext.TYPE_REAL;
	}
	
	private int resolveDivision(ArrayList<Token> operand1, ArrayList<Token> operand2, Token operator) {
		
		int type1;
		int type2;
		
		Token op1 = operand1.get(0);
		Token op2 = operand2.get(0);
		
		if(op1.getToken().equals("+") || op1.getToken().equals("-")) {
			op1 = operand1.get(1);
		}
		
		if(op2.getToken().equals("+") || op2.getToken().equals("-")) {
			op2 = operand2.get(1);
		}
		
		type1 = semantic.getTokenSemanticType(op1);
		type2 = semantic.getTokenSemanticType(op2);
		
		if(type1 != SemanticContext.TYPE_INT || type2 != SemanticContext.TYPE_INT) {
			semantic.addSemanticError("A operção de divisão só está definida para valores inteiros", operator);
			return SemanticContext.TYPE_UNDEFINED; 
		}
		
		return SemanticContext.TYPE_INT;
	}

	private int resolveSum(ArrayList<Token> operand1, ArrayList<Token> operand2, Token operator) {
			
		int type1;
		int type2;
		
		Token op1 = operand1.get(0);
		Token op2 = operand2.get(0);
		
		if(op1.getToken().equals("+") || op1.getToken().equals("-")) {
			op1 = operand1.get(1);
		}
		
		if(op2.getToken().equals("+") || op2.getToken().equals("-")) {
			op2 = operand2.get(1);
		}
		
		type1 = semantic.getTokenSemanticType(op1);
		type2 = semantic.getTokenSemanticType(op2);
		
		if(type1 == SemanticContext.TYPE_UNDEFINED || type2 == SemanticContext.TYPE_UNDEFINED ||
				type1 == SemanticContext.TYPE_BOOLEAN || type2 == SemanticContext.TYPE_BOOLEAN) {
			
			semantic.addSemanticError("A operção de soma só está definida para valores numéricos", operator);
			return SemanticContext.TYPE_UNDEFINED; 
		}
		if(type1 == type2) return type1;
				
		return SemanticContext.TYPE_REAL;
	}

	private int resolveRelational(ArrayList<Token> operand1, ArrayList<Token> operand2, Token operator) {
		
		int type1;
		int type2;
		
		Token op1 = operand1.get(0);
		Token op2 = operand2.get(0);
		
		if(op1.getToken().equals("+") || op1.getToken().equals("-")) {
			op1 = operand1.get(1);
		}
		
		if(op2.getToken().equals("+") || op2.getToken().equals("-")) {
			op2 = operand2.get(1);
		}
		
		type1 = semantic.getTokenSemanticType(op1);
		type2 = semantic.getTokenSemanticType(op2);
				
		if(operator.equals("<=") || operator.equals("<") || operator.equals(">") || operator.equals(">=")) {
			
			if(type1 == SemanticContext.TYPE_UNDEFINED || type2 == SemanticContext.TYPE_UNDEFINED ||
					type1 == SemanticContext.TYPE_BOOLEAN || type2 == SemanticContext.TYPE_BOOLEAN) {
				
				semantic.addSemanticError("O operador " + operator + " só está definido para valores numéricos", operator);
				return SemanticContext.TYPE_UNDEFINED; 
			}			
			
		}
		else {
			
			if(type1 == SemanticContext.TYPE_UNDEFINED || type2 == SemanticContext.TYPE_UNDEFINED)  {
				semantic.addSemanticError("Não é possível utilizar o operador " + operator + " para o tipo indefinido", operator);
				return SemanticContext.TYPE_UNDEFINED; 
			}
			else if(type1 == SemanticContext.TYPE_BOOLEAN || type2 == SemanticContext.TYPE_BOOLEAN) {
				if(type1 != type2) {
					semantic.addSemanticError("Comparação entre um booleano e um valor numérico", operator);
					return SemanticContext.TYPE_UNDEFINED;
				}
			}
			
		}
		
		return SemanticContext.TYPE_BOOLEAN;
	}

	private int resolveLogical(ArrayList<Token> operand1, ArrayList<Token> operand2, Token operator) {
		
		int type1;
		int type2;
		
		Token op1 = operand1.get(0);
		Token op2 = operand2.get(0);
		
		if(op1.getToken().equals("+") || op1.getToken().equals("-")) {
			op1 = operand1.get(1);
		}
		
		if(op2.getToken().equals("+") || op2.getToken().equals("-")) {
			op2 = operand2.get(1);
		}
		
		type1 = semantic.getTokenSemanticType(op1);
		type2 = semantic.getTokenSemanticType(op2);
		
		if(type1 != SemanticContext.TYPE_BOOLEAN || type2 != SemanticContext.TYPE_BOOLEAN) {
			semantic.addSemanticError("Operação lógica só é definida para valores boleanos", operator);
			return SemanticContext.TYPE_UNDEFINED;
		}
		
		return SemanticContext.TYPE_BOOLEAN;
	}
	
	private int resolveNot(ArrayList<Token> operand, Token operator) {
		
		int type = semantic.getTokenSemanticType(operand.get(0));
		
		if(type != SemanticContext.TYPE_BOOLEAN) {
			semantic.addSemanticError("Operação not só é definida para valores boleanos", operator);
			return SemanticContext.TYPE_UNDEFINED;
		}
		
		return SemanticContext.TYPE_BOOLEAN;
	}
	
	private void updateExpression(ArrayList<Token> tokens, int start, int end, int type) {
		
		tokens.removeAll(tokens.subList(start, end));
		
		switch (type) {
		
			case SemanticContext.TYPE_BOOLEAN: 
				tokens.set(start, new Token("true", -1, -1));
				break;
				
			case SemanticContext.TYPE_INT:
				tokens.set(start, new Token("1", -1, -1));
				break;
				
			case SemanticContext.TYPE_REAL:
				tokens.set(start, new Token("1.1", -1, -1));
				break;

		}
		
	}

}
