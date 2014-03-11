package up.fe.liacc.repacl;

import up.fe.liacc.repacl.acl.ACLMessage;

/**

 * @author joaolopes
 *
 */
public interface IRepACLAgent {

	/**
	 * Send message to this agent.
	 * The message will be read and processed but a response
	 * may only occur a few ticks later.
	 * @param message
	 */
	void send(ACLMessage message);
}
