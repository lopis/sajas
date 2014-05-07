package up.fe.liacc.repacl;

import java.util.ArrayList;
import java.util.HashMap;

import up.fe.liacc.repacl.core.Agent;
import up.fe.liacc.repacl.lang.acl.ACLMessage;
import up.fe.liacc.repacl.lang.acl.AID;

/**
 * The MTS, or  Message Transport Service, handles
 * the delivery of ACL messages.
 * @author joaolopes
 *
 */
public class MTS {
	
	private static HashMap<AID, Agent> agents;

	/**
	 * Sends a message to another agent.
	 * The receiver and sender should be
	 * specified in the message, as well as
	 * the performative.
	 * @param message
	 */
	public static void send(ACLMessage message) {
		ACLMessage newmessage = message.clone();
		ArrayList<AID> receivers = newmessage.getReceivers();
		
		for (AID aid: receivers) {
			if (aid != null) {
				resolve(aid).addMail(newmessage);
			}
		}
	}

	private static Agent resolve(AID aid) {
		return agents.get(aid);
	}

	/**
	 * Convenience method that allows the MTS to create
	 * a map of AIDs to Agents.
	 * @param agent
	 */
	public static void addAddress(Agent agent) {
		getAgents().put(agent.getAID(), agent);
	}

	private static HashMap<AID, Agent> getAgents() {
		if (agents == null) {
			agents = new HashMap<AID, Agent>();
		}
		return agents;
	}
}
