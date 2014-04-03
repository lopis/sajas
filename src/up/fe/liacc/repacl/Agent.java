package up.fe.liacc.repacl;

import java.util.LinkedList;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.util.ContextUtils;
import up.fe.liacc.repacl.acl.ACLMessage;
import up.fe.liacc.repacl.acl.MessageTemplate;
import up.fe.liacc.repacl.core.BehaviorQ;
import up.fe.liacc.repacl.core.DF;
import up.fe.liacc.repacl.proto.Behavior;

/**
 * Agents that implement this interface are able to use RepACL to communicate
 * using ACL messages.
 * @author joaolopes
 *
 */
public abstract class Agent {

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
	 * Default constructor. Always call this constructor
	 * when extending this class, as it registers the
	 * agent in the DF and makes communication possible.
	 * The setup() method is called in the end of this
	 * constructor.
	 */
	public Agent() {
		DF.registerAgent(this);
		setup();
	}

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
	 * Returns this agent's mail box
	 */
	public MailBox getMail() {
		return getMailBox();
	}
	
	public ACLMessage getMatchingMessage(MessageTemplate template) {
		return getMailBox().getMatchingMessage(template);
	}

	/**
	 * Sets the agent identifier. It is expected that agents know
	 * their own AID. For instance, the DF class makes use of this
	 * when the agents are registered. Without the AID, no
	 * communication can occur.
	 * @param aid The agent identifier.
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
	@SuppressWarnings("unchecked")
	protected void addBehavior(Behavior behavior) {
		getBehaviorQueue().addBehavior(behavior);
		DF.getContext().add(behavior);
	}

	private BehaviorQ getBehaviorQueue() {
		if (behaviorQ == null) {
			behaviorQ = new BehaviorQ();
		}
		return behaviorQ;
	}
	
	/**
	 * 
	 */
	public void setup(){}
	
	@ScheduledMethod(start=1, interval=50)
	public void step(){
		System.err.println("[A] Default step");
	}
}
