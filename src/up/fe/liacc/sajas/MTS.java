package up.fe.liacc.sajas;

import java.util.ArrayList;

import up.fe.liacc.sajas.core.AID;
import up.fe.liacc.sajas.core.Agent;
import up.fe.liacc.sajas.domain.AMSService;
import up.fe.liacc.sajas.lang.acl.ACLMessage;

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
		// The message is cloned to prevent modifications of the original one.
		ACLMessage newmessage = message.clone();
		ArrayList<AID> receivers = newmessage.getReceivers();
		//System.out.println(message); //DEBUG
		for (AID aid: receivers) {
			if (aid != null) {
				resolve(aid).addMail(newmessage);
			}
		}
	}

	private static Agent resolve(AID aid) {
		return AMSService.get(aid);
	}


}
