package up.fe.liacc.sajas.core;

import java.util.ArrayList;

import up.fe.liacc.sajas.MTS;
import up.fe.liacc.sajas.core.behaviours.Behaviour;
import up.fe.liacc.sajas.lang.acl.ACLMessage;
import up.fe.liacc.sajas.lang.acl.MessageTemplate;

/**
 * Agents that implement this interface are able to use RepACL to communicate
 * using ACL messages.
 * @author joaolopes
 *
 */
public abstract class Agent {
	
	private String name;

	/**
	 * Agent identifier
	 */
	private AID aid;
	/**
	 * This agent's mail box can contain ACLMessages from different protocols.
	 * Template matching will be then applied.
	 */
	private MessageQueue mailBox;
	
	private ArrayList<Behaviour> behaviours = new ArrayList<Behaviour>();


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
	protected MessageQueue getMailBox() {
		if (mailBox == null) {
			mailBox = new MessageQueue();
		}
		return mailBox;
	}
	
	public String getLocalName() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	public void send(ACLMessage message) {
		if (message != null) {
			message.setSender(getAID());
			MTS.send(message);
		}
	}

	/**
	 * 
	 * @param template The template to filter messages.
	 * @return The first matching message in the Mail box, or
	 * Null if no message in the Mail Box matches the template.
	 */
	public ACLMessage receive(MessageTemplate template) {
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
		this.name = aid.getLocalName();
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
	public abstract void addBehaviour(Behaviour behaviour);

	public abstract void removeBehaviour(Behaviour behaviour);
	
	/**
	 * Method executed after the agent is created.
	 * This default implementation is empty and should be extended
	 * if needed.
	 */
	protected void setup(){}

	public void doDelete() {
		for (Behaviour behaviour : behaviours) {
			removeBehaviour(behaviour);
		}
		takeDown();
	}
	
	
	protected void takeDown() {}
	
	@Override
	public String toString() {
		return "[Agent#" + getAID() + "]";
	}

	public ArrayList<Behaviour> getBehaviours() {
		return behaviours;
	}

	public void setBehaviours(ArrayList<Behaviour> behaviours) {
		this.behaviours = behaviours;
	}

	public void setupAgent() {
		setup();
	}

	public boolean hasMail() {
		return !mailBox.isEmpty();
	}
}
