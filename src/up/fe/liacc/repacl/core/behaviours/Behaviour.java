package up.fe.liacc.repacl.core.behaviours;

import up.fe.liacc.repacl.core.Agent;

/**
 * This is the class that all behaviors extend. It doesn't contain any
 * implementation of the behavior itself. Programmer that intend to 
 * implement communication protocols should instead use the classes from
 * the package "proto"
 * @author joaolopes
 *
 */
public abstract class Behaviour {

	protected Agent myAgent;

	/**
	 * Behavior default constructor. Always call this constructor.
	 * @param agent The owner, i.e. the agent initiating this behavior.
	 */
	public Behaviour(Agent agent) {
		this.setAgent(agent);
	}
	
	/**
	 * This is the body of the behavior and the
	 * method that is executed when the behavior is executed.
	 */
	public abstract void action();

	public Agent getAgent() {
		return myAgent;
	}

	public void setAgent(Agent owner) {
		this.myAgent = owner;
	}
}
