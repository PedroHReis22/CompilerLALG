package compilerLALG.semantic;

public class ProcedureSignature {
	
	private String name;
	private int types[];
	
	public ProcedureSignature(String name, int types[]) {
		this.name = name;
		this.types = types;
	}
	
	public boolean verifyParameters(int types[]) {
		return true;
	}
	
	public String toString() {
		
		String typ[] = new String[]{"undefined", "int", "real", "boolean"};
		
		StringBuffer buffer = new StringBuffer(name + " -> ");
				
		for(int type : types) buffer.append(typ[type] + " ");
		
		
		return buffer.toString();
	}

}
