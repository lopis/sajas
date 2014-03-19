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
		this.owner = agent;
	}
}
