package up.fe.liacc.sajas.core.behaviours;

import up.fe.liacc.sajas.core.Agent;

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
	 * Default constructor. It does not set
	 * the owner agent for this behaviour.
  	 */
	public Behaviour() {}

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

	public void reset() {
		// TODO Auto-generated method stub
	}

	public int onEnd() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
}
