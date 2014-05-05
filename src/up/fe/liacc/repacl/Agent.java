package up.fe.liacc.repacl;

import up.fe.liacc.repacl.acl.ACLMessage;
import up.fe.liacc.repacl.acl.MessageTemplate;
import up.fe.liacc.repacl.core.behaviours.Behaviour;
import up.fe.liacc.repacl.domain.DFService;

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
		DFService.registerAgent(this); //TODO: not mandatory
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
	 * 
	 * @param template The template to filter messages.
	 * @return The first matching message in the Mail box, or
	 * Null if no message in the Mail Box matches the template.
	 */
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
