package up.fe.liacc.sajas.core;

public class AID {
	
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
