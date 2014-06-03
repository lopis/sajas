package up.fe.liacc.sajas.core;

import java.io.Serializable;

public class AID implements Serializable{
	
	private String localName;
	
	
	public AID(String localName) {
		this.localName = localName;
	}

	public String getLocalName() {
		return localName;
	}
	
	@Override
	public String toString() {
		return localName + "@";
	}
}
