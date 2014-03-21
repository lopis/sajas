package up.fe.liacc.repacl;

import java.util.LinkedList;

import up.fe.liacc.repacl.acl.ACLMessage;
import up.fe.liacc.repacl.core.BehaviorQ;
import up.fe.liacc.repacl.proto.Behavior;

/**
 * Agents that implement this interface are able to use RepACL to communicate
 * using ACL messages.
 * @author joaolopes
 *
 */
public abstract class IAgent {

	/**
	 * Agent identifier
	 */
	private int aid;
	/**
	 * This agent's mail box can contain ACLMessages from different protocols.
	 * Template matching will be then applied.
	 */
	private MailBox mailBox;
	
	/**
	 * The behavior queue.
	 */
	private BehaviorQ behaviorQ;

	/**
	 * Add a message to this agent's mail box.
	 * The message should be then scheduled for 
	 * later processing.
	 * 
	 * To send messages to an agent, the behavior classes make use
	 * of the MTS service.
	 * @param message
	 */
	public void addMail(ACLMessage message) {
		getMailBox().addMail(message);
	}
	
	/**
	 * @return The mail box
	 */
	private MailBox getMailBox() {
		if (mailBox == null) {
			mailBox = new MailBox();
		}
		return mailBox;
	}
	
	/**
	 * Returns this agent's mail box contents.
	 */
	public LinkedList<ACLMessage> getMail() {
		return getMailBox().getMail();
	}
	
	/**
	 * Returns this agent's mail box contents.
	 */
	public LinkedList<ACLMessage> getMail(ACLMessage template) {
		return getMailBox().getMail(template);
	}

	/**
	 * Sets the agent identifier. It is expected that agents know
	 * their own AID. For instance, the DF class makes use of this
	 * when the agents are registered. Without the AID, no
	 * communication can occur.
	 * @param AID The agent identifier
	 */
	public void setAID(int aid) {
		this.aid = aid;
	}
	
	/**
	 * Returns the agent identifier. It is expected that agents know
	 * their own AID. For instance, the DF class makes use of this
	 * when the agents are registered. Without the AID, no
	 * communication can occur.
	 * @return The agent identifier
	 */
	public int getAID() {
		return aid;
	}

	/**
	 * Adds a behavior to this agent's execution.
	 * @param behavior
	 */
	protected void addBehavior(Behavior behavior) {
		getBehaviorQueue().addBehavior(behavior);
	}

	private BehaviorQ getBehaviorQueue() {
		if (behaviorQ == null) {
			behaviorQ = new BehaviorQ();
		}
		return behaviorQ;
	}
}
