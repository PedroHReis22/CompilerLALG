package compilerLALG.semantic;

public class ProcedureSignature {
	
	private String name;
	private int types[];
	
	public ProcedureSignature(String name, int types[]) {
		this.name = name;
		this.types = types;
	}
	
	public boolean verifyParameters(int types[]) {
		
		if(types.length != this.types.length) return false;
				
		for(int i = 0; i < types.length; ++i) {
			if(types[i] != this.types[i]) return false;
		}
		
		return true;
	}
	
	public int getNumParameters() {
		return types.length;
	}
	
	public String getName() {
		return name;
	}
	
	public String getErrorMessage() {
		
		String typ[] = new String[]{"indefinido", "int", "real", "boolean"};
		StringBuffer buffer = new StringBuffer();
		
		if(types.length == 1) buffer.append("O método " + name + " espera " + types.length + " parâmetro (");
		else buffer.append("O método " + name + " espera " + types.length + " parâmetros (");
		
		for(int i = 0; i < types.length - 1; ++i) {	
			buffer.append(typ[types[i]] + ", ");
		}
		
		buffer.append(typ[types[types.length - 1]] + ")");
		
		return buffer.toString();
		
	}
	
	public String toString() {
		
		String typ[] = new String[]{"undefined", "int", "real", "boolean"};
		
		StringBuffer buffer = new StringBuffer(name + " -> ");
				
		for(int type : types) buffer.append(typ[type] + " ");
		
		
		return buffer.toString();
	}

}
