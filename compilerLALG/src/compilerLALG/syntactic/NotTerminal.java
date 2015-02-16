package compilerLALG.syntactic;

/**
 * Representação dos símbolos não terminais da linguagem
 */
public class NotTerminal {
	
	public static final int PROG = 0;
	public static final int BLOCO = 1;
	public static final int BLOCO_ = 2;
	public static final int TIPO = 3;
	public static final int PART_DEC_VAR = 4;
	public static final int PART_DEC_VAR_ = 5;
	public static final int DEC_VAR = 6;
	public static final int LIST_IDENT = 7;
	public static final int LIST_IDENT_ = 8;
	public static final int PART_DEC_SUB = 9;
	public static final int DEC_PROC = 10;
	public static final int DEC_PROC_ = 11;
	public static final int PAR_FORM = 12;
	public static final int PAR_FORM_ = 13;
	public static final int SEC_PAR_FORM = 14;
	public static final int COM_COMP = 15;
	public static final int COM_COMP_ = 16;
	public static final int COM = 17;
	public static final int COM_ = 18;
	public static final int COM__ = 19;
	public static final int CHAM_PROC = 20;
	public static final int COND = 21;
	public static final int COND_ = 22;
	public static final int REP = 23;
	public static final int EXP = 24;
	public static final int EXP_ = 25;
	public static final int RELAC = 26; 
	public static final int EXP_SIMP = 27;
	public static final int EXP_SIMP_ = 28;
	public static final int TERMO = 29;
	public static final int TERMO_ = 30;
	public static final int FATOR = 31;
	public static final int LIST_EXP = 32;
	public static final int LIST_EXP_ = 33;
	public static final int IDENT = 34;
	public static final int INT = 35;
	public static final int REAL = 36;
	
	public static final int TOTAL = 37;
	
	/**
	 * Retorna o index na tabela de gramática para o simbolo não terminal passado por parâmetro
	 * 
	 * @param notTerminal O simbolo não terminal que deseja-se o index
	 * 
	 * @return O index do símbolo não terminal
	 */
	public static int getIndex(String notTerminal) {
		
		switch (notTerminal.trim()) {
		
			case "<prog>":          return PROG;				
			case "<bloco>":         return BLOCO;
			case "<bloco_>":        return BLOCO_;
			case "<tipo>":          return TIPO;
			case "<part_dec_var>":  return PART_DEC_VAR;
			case "<part_dec_var_>": return PART_DEC_VAR_;
			case "<dec_var>":       return DEC_VAR;
			case "<list_ident>":    return LIST_IDENT;
			case "<list_ident_>":   return LIST_IDENT_;
			case "<part_dec_sub>":  return PART_DEC_SUB;
			case "<dec_proc>":      return DEC_PROC;
			case "<dec_proc_>":     return DEC_PROC_;
			case "<par_form>":      return PAR_FORM;
			case "<par_form_>":     return PAR_FORM_;
			case "<sec_par_form>":  return SEC_PAR_FORM;
			case "<com_comp>":      return COM_COMP;
			case "<com_comp_>":     return COM_COMP_;
			case "<com>":           return COM;
			case "<com_>":          return COM_;
			case "<com__>":         return COM__;
			case "<cham_proc>":     return CHAM_PROC;
			case "<cond>":          return COND;
			case "<cond_>":         return COND_;
			case "<rep>":           return REP;
			case "<exp>":           return EXP;
			case "<exp_>":          return EXP_;
			case "<relac>":         return RELAC;
			case "<exp_simp>":      return EXP_SIMP;
			case "<exp_simp_>":     return EXP_SIMP_;
			case "<termo>":         return TERMO;
			case "<termo_>":        return TERMO_;
			case "<fator>":         return FATOR;
			case "<list_exp>":      return LIST_EXP;
			case "<list_exp_>":     return LIST_EXP_;
			case "<ident>":         return IDENT;
			case "<int>":           return INT;
			case "<real>":          return REAL;		
			default:                return -1;
		}
		
	}

}
