package up.fe.liacc.repacl.core;

import up.fe.liacc.repacl.MTS;
import up.fe.liacc.repacl.core.behaviours.Behaviour;
import up.fe.liacc.repacl.lang.acl.ACLMessage;
import up.fe.liacc.repacl.lang.acl.AID;
import up.fe.liacc.repacl.lang.acl.MessageTemplate;

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
	private AID aid;
	/**
	 * This agent's mail box can contain ACLMessages from different protocols.
	 * Template matching will be then applied.
	 */
	private MessageQueue mailBox;
	
//	/**
//	 * Queue of behaviors for this agent.
//	 */
//	private LinkedList<Behaviour> behaviours;
//	private ContextScheduler contextScheduler = new ContextScheduler();
	
	/**
	 * Default constructor. Always call this constructor
	 * when extending this class, as it registers the
	 * agent in the DF and makes communication possible.
	 * The setup() method is called in the end of this
	 * constructor.
	 */
	public Agent() {
		//DFService.registerAgent(this); // Not mandatory for agents to register
		aid = new AID("");
		MTS.addAddress(this);
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
		getMailBox().addFirst(message);
	}
	
	/**
	 * @return The mail box
	 */
	private MessageQueue getMailBox() {
		if (mailBox == null) {
			mailBox = new MessageQueue();
		}
		return mailBox;
	}

	
	/**
	 * 
	 * @param template The template to filter messages.
	 * @return The first matching message in the Mail box, or
	 * Null if no message in the Mail Box matches the template.
	 */
	public ACLMessage getMatchingMessage(MessageTemplate template) {
		return getMailBox().receive(template);
	}

	/**
	 * Sets the agent identifier. It is expected that agents know
	 * their own AID. For instance, the DF class makes use of this
	 * when the agents are registered. Without the AID, no
	 * communication can occur.
	 * @param aid The agent identifier.
	 */
	public void setAID(AID aid) {
		this.aid = aid;
	}
	
	/**
	 * Returns the agent identifier. It is expected that agents know
	 * their own AID. For instance, the DF class makes use of this
	 * when the agents are registered. Without the AID, no
	 * communication can occur.
	 * @return The agent identifier
	 */
	public AID getAID() {
		return aid;
	}

	/**
	 * Adds a behavior to this agent's execution.
	 * @param behavior
	 */
	protected abstract void addBehavior(Behaviour behaviour);
//	{
//		//contextScheduler.add(behaviour);
//	}
	
	/**
	 * Method executed after the agent is created.
	 * This default implementation is empty and should be extended
	 * if needed.
	 */
	public void setup(){}
	
	/**
	 * This default implementation is empty and should be extended 
	 * if needed.
	 * @ScheduledMethod
	 */
	public void step(){}
	
	@Override
	public String toString() {
		return "[Agent#" + getAID() + "]";
	}
}
