package up.fe.liacc.repacl.proto;

import up.fe.liacc.repacl.Agent;

public abstract class Behavior {

	private Agent owner;

	/**
	 * Behavior creation
	 * @param agent The owner, i.e. the
	 * agent initiating this behavior.
	 */
	public Behavior(Agent agent) {
		this.setOwner(agent);
	}
	
	/**
	 * This is the body of the behavior and the
	 * method that is executed when the behavior is executed.
	 */
	public abstract void action();

	public Agent getOwner() {
		return owner;
	}

	public void setOwner(Agent owner) {
		this.owner = owner;
	}
}
