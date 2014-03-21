package up.fe.liacc.repacl.core;

import java.util.ArrayList;

import up.fe.liacc.repacl.IAgent;
import up.fe.liacc.repacl.acl.ACLMessage;

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
		ArrayList<IAgent> receivers = message.getReceivers();
		for (IAgent agent: receivers) {
			agent.addMail(message);
		}
	}
}
