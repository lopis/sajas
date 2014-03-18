package up.fe.liacc.repacl;

import up.fe.liacc.repacl.acl.ACLMessage;

/**
 * Agents that implement this interface are able to use RepACL to communicate
 * using ACL messages.
 * @author joaolopes
 *
 */
public interface IAgent {

	/**
	 * Add a message to this agent's mail box.
	 * The message should be then scheduled for 
	 * later processing.
	 * 
	 * To send messages to an agent, you should use the DF class 
	 * through the method "initiateProtocol()".
	 * @param message
	 */
	void addMail(ACLMessage message);
	
	/**
	 * Sets the agent identifier. It is expected that agents know
	 * their own AID. For instance, the DF class makes use of this
	 * when the agents are registered. Without the AID, no
	 * communication can occur.
	 * @param AID The agent identifier
	 */
	void setAID(int aid);
	
	/**
	 * Returns the agent identifier. It is expected that agents know
	 * their own AID. For instance, the DF class makes use of this
	 * when the agents are registered. Without the AID, no
	 * communication can occur.
	 * @return The agent identifier
	 */
	int getAID();


}
