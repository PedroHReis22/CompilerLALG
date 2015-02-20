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
		errors.put("<com_comp>", "Esperado a palavra reservada 'begin'"); 
		errors.put("<dec_proc>", "Esperado a palavra reservada 'procedure'");
		errors.put("<dec_proc_>", "Esperado o final da declaração de procedimento {;} ou os parâmtros formais");
		errors.put("<par_form>", "Esperado o simbolo (");
		errors.put("<par_form_>", "Esperado o final do parâmetro formal {;} ou o final dos parâmetros formais {)}");
		errors.put("<sec_par_form>", "Esperado a seção de parâmetros formais");
		errors.put("<list_ident>", "Esperado um identificador");
		errors.put("<list_ident_>", "Esperado {,} para um novo identificador ou : para o tipo de variavel");
		errors.put("<relac>", "Esperado um símbolo de relação: =, <>, <, <=, > ou >=");
		errors.put("<exp_simp>", "Esperado um fator");
		errors.put("<tipo>", "Esperado o tipo: integer ou real");
		errors.put("<int>", "Esperado um número inteiro");
		errors.put("<real>", "Esperado um número real");
		errors.put("<cond>", "Esperado a palavra reservada 'if'");
		errors.put("<rep>", "Esperado a palavra reservada 'while'");
		errors.put("<fator>", "Esperado: (, identificador, número inteiro, número real ou a palvra reservada 'not'");
		errors.put("<cond_>", "Esperado o final da expressão {;} ou a palavra reservada 'end' ou um bloco else");
		errors.put("<part_dec_var>", "Esperado a palavra reservada 'var'");
		errors.put("<exp>", "Esperado uma expressão");
		errors.put("<com_comp_>", "Esperado o fim da expressão: ; ou 'end'");
		errors.put("<part_dec_var_>", "Esperado o fim de declaração de varíaveis");
		errors.put("<dec_var>", "Esperado declaração de variáveis");
		errors.put("<exp_>", "Esperado uma expressão");
		errors.put("<exp_simp_>", "Esperado +, - ou 'or'");
		errors.put("<termo_>", "Esperado *, 'div' ou 'and'");
		errors.put("<list_exp>", "Esperado uma lista de expressões");
		errors.put("<list_exp_>", "Esperado uma lista de expressões");
		errors.put("<com>", "Esperado um comando");
		errors.put("<com_>", "Esperado um comando"); 
		errors.put("<com__>", "Esperado um comando");
		errors.put("<cham_proc>", "Esperado uma chamada de procedimento");
		errors.put("<termo>", "Esperado um termo");
		errors.put("<boolean>", "Esperando um valor booleano: true ou false");		
		
		return errors.get(notTerminalSimbol);
		
	}	

}
