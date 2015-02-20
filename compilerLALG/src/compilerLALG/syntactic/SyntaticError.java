package compilerLALG.syntactic;

import java.util.HashMap;
import java.util.Map;

/**
 * Retorna o erro sintático de acordo com o simbolo não terminal
 */
public class SyntaticError {
	
	/**
	 * Retorna o erro sintático de acordo com o símbolo não terminal
	 * 
	 * @param notTerminalSimbol O simbolo não terminal para o qual deseja-se o erro
	 * 
	 * @return O erro sintático de acordo com o símbolo não terminal
	 */
	public static String getErrorMessage(String notTerminalSimbol) {
		
		Map<String, String> errors = new HashMap<String, String>();
		
		errors.put("<prog>", "Esperado a palavra reservada 'program'");
		errors.put("<ident>", "Esperado um identificador");
		errors.put("<bloco>", "Esperado um bloco de instruções");
		errors.put("<bloco_>", "Esperado um bloco de instruções");
		errors.put("<part_dec_sub>", "Esperado declaração de procedimento");
		errors.put("<com_comp>", "Esperado um comando composto"); 
		errors.put("<dec_proc>", "Esperado a declaração de procedimento");
		errors.put("<dec_proc_>", "Esperado a declaração de procedimento");
		errors.put("<par_form>", "Esperado a lista de paramêtros formais");
		errors.put("<par_form_>", "Esperado a lista de paramêtros formais");
		errors.put("<sec_par_form>", "Esperado a seção de parâmetros formais");
		errors.put("<list_ident>", "Esperado uma lista de identificadores");
		errors.put("<list_ident_>", "Esperado uma lista de identificadores");
		errors.put("<relac>", "Esperado um símbolo de relação");
		errors.put("<exp_simp>", "Esperado uma expressão simples");
		errors.put("<tipo>", "Esperado o tipo: integer ou real");
		errors.put("<int>", "Esperado um número inteiro");
		errors.put("<real>", "Esperado um número real");
		errors.put("<cond>", "Esperado um comando condicional");
		errors.put("<rep>", "Esperado um comando de repetição");
		errors.put("<fator>", "Esperado um fator");
		errors.put("<cond_>", "Esperado uma expressão condicional");
		errors.put("<part_dec_var>", "Esperado declaração de variáveis");
		errors.put("<exp>", "Esperado uma expressão");
		errors.put("<com_comp_>", "Esperado o comando composto");
		errors.put("<part_dec_var_>", "Esperado declaração de variáveis");
		errors.put("<dec_var>", "Esperado declaração de variáveis");
		errors.put("<exp_>", "Esperado uma expressão");
		errors.put("<exp_simp_>", "Esperado uma expressão simples");
		errors.put("<termo_>", "Esperado um termo");
		errors.put("<list_exp>", "Esperado uma lista de expressões");
		errors.put("<list_exp_>", "Esperado uma lista de expressões");
		errors.put("<com>", "Esperado um comando");
		errors.put("<com_>", "Esperado um comando"); 
		errors.put("<com__>", "Esperado um comando");
		errors.put("<cham_proc>", "Esperado uma chamada de procedimento");
		errors.put("<termo>", "Esperado um termo");
		errors.put("<boolean>", "Esperado um valor booleano");
		errors.put("$", "Esperado o fim do arquivo");
		
		return errors.get(notTerminalSimbol);
		
	}	

}
