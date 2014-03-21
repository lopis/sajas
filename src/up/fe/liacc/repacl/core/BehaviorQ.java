package up.fe.liacc.repacl.core;

import java.util.LinkedList;

import up.fe.liacc.repacl.proto.Behavior;

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