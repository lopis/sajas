package up.fe.liacc.repacl.proto;

import up.fe.liacc.repacl.IAgent;

public abstract class Behavior {

	private IAgent owner;

	/**
	 * Behavior creation
	 * @param agent The owner, i.e. the
	 * agent initiating this behavior.
	 */
	public Behavior(IAgent agent) {
		this.setOwner(agent);
	}
	
	/**
	 * This is the body of the behavior and the
	 * method that is executed when the behavior is executed.
	 */
	public abstract void action();

	public IAgent getOwner() {
		return owner;
	}

	public void setOwner(IAgent owner) {
		this.owner = owner;
	}
}
