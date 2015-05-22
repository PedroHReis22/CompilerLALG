package compilerLALG.codeGeneration;

import java.util.ArrayList;

import compilerLALG.lexical.Token;

public class AddressVector {
	
	public ArrayList<Address> addresses;
	public int addressCount;
	
	public AddressVector() {
		addresses = new ArrayList<Address>();
		addressCount = 0;
	}
	
	public void addAddress(Token token) {
		addresses.add(new Address(token, addressCount++));
	}
	
	public int getAddress(String token) {
		for(Address address : addresses) {
			if(address.token.getToken().equals(token)) return address.address;
		}
		return -1;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		addresses.forEach(a -> buffer.append(a + "\n"));
		return buffer.toString();
	}
	
	class Address {
		
		private Token token;
		private int address;
		
		protected Address(Token var, int address) {
			this.token = var;
			this.address = address;
		}
		
		public Token getVar() {
			return token;
		}
		
		public int getAddress() {
			return address;
		}
		
		@Override
		public String toString() {
			return token.getToken() + " -> " + address;
		}
		
	}

}
