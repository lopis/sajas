package up.fe.liacc.repacl.proto;

import repast.simphony.engine.schedule.ScheduledMethod;
import up.fe.liacc.repacl.Agent;

/**
 * This is the class that all behaviors extend. It doesn't contain any
 * implementation of the behavior itself. Programmer that intend to 
 * implement communication protocols should instead use the classes from
 * the package "proto"
 * @author joaolopes
 *
 */
public abstract class Behavior {

	private Agent owner;

	/**
	 * Behavior default constructor. Always call this constructor.
	 * @param agent The owner, i.e. the agent initiating this behavior.
	 */
	public Behavior(Agent agent) {
		this.setOwner(agent);
	}
	
	/**
	 * This is the body of the behavior and the
	 * method that is executed when the behavior is executed.
	 */
	@ScheduledMethod(start = 1, interval = 1)
	public abstract void action();

	public Agent getOwner() {
		return owner;
	}

	public void setOwner(Agent owner) {
		this.owner = owner;
	}
}
