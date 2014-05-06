package up.fe.liacc.repacl.domain;

import java.util.ArrayList;

import up.fe.liacc.repacl.core.Agent;
import up.fe.liacc.repacl.lang.acl.ACLMessage;

/**
 * The MTS, or  Message Transport Service, handles
 * the delivery of ACL messages.
 * @author joaolopes
 *
 */
public class MTS {
	
	/**
	 * Sends a message to another agent.
	 * The receiver and sender should be
	 * specified in the message, as well as
	 * the performative.
	 * @param message
	 */
	public static void send(ACLMessage message) {
		ArrayList<Agent> receivers = message.getReceivers();
		for (Agent agent: receivers) {
			if (agent != null) {
				agent.addMail(message);
			}
		}
	}
}