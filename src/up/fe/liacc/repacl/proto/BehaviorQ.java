package up.fe.liacc.repacl.proto;

import java.util.LinkedList;

/**
 * This class is a simple behavior queue for the agents.
 * 
 * @author joaolopes
 *
 */
public class BehaviorQ {

	/**
	 * Queue of behaviors for this agent.
	 */
	private LinkedList<Behavior> behaviors;

	public void addBehavior(Behavior b) {
		behaviors.add(b);
	}
}
