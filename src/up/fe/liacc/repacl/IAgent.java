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
	 * Send message <b>to</b> this agent.
	 * The message will be read and processed but a response
	 * may only occur a few ticks later.
	 * 
	 * To send messages to an agent, you should use the DF class 
	 * through the method "initiateProtocol()".
	 * @param message
	 */
	void send(ACLMessage message);
	
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
	
	/**
	 * This method initiates the interaction protocol.
	 * When the agent initiates new communication, 
	 * the mailbox should be prepared to receive the responses.
	 * @param message
	 */
	void initiateProtocol(ACLMessage message);
	
	/**
	 * 
	 * @return This agent's mail box.
	 */
	MailBox getMailBox();
}
