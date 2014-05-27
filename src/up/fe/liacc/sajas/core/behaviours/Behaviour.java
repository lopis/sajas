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

	/**
	 * Empty placeholder fot the method that resets the
	 * behaviour. This method should be overridden.
	 */
	public void reset() {}

	/**
	 * This method is just an empty placeholder for subclasses.
	 * It is invoked just once after this behaviour has ended.
	 * Therefore, it acts as an epilog for the task represented
	 * by this Behaviour. Note that onEnd is called after the
	 * behaviour has been removed from the pool of behaviours
	 * to be executed by an agent. Therefore calling reset()
	 * is not sufficient to cyclically repeat the task represented
	 * by this Behaviour. In order to achieve that, this Behaviour
	 * must be added again to the agent (using myAgent.addBehaviour(this)).
	 * @return
	 */
	public int onEnd() {
		return 0;
	}

	/**
	 * Returns whether this behaviour is done. This default
	 * implementation does nothing and the method should be overridden.
	 * @return
	 */
	public boolean done() {
		return false;
	}
}
