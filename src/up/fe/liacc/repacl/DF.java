package up.fe.liacc.repacl;

import java.util.HashMap;

import up.fe.liacc.repacl.acl.ACLMessage;


/**
 * This static class provides the Directory Facilitator Service
 * and is used to send messages to agents
 * and to keep a directory of all agents in the application.
 * Agents use the static method send(ACLMessage) to send a message
 * to one or more agents. The ACLMessage object contains
 * the receiver agent and the sender (so the receiver can reply back).
 * @author joaolopes
 *
 */
public class DF {

	private static int lastAID = 0; // Just to help generate new identifiers
	private static HashMap<Integer, IAgent> agents; // Contains all agents
	
	/**
	 * Returns the map of agents. The field is initialized
	 * if it wasn't before.
	 * @return
	 */
	public static HashMap<Integer, IAgent> getAgents() {
		if (agents == null) {
			agents = new HashMap<Integer, IAgent>();
		}
		return agents;
	}
	
	/**
	 * Returns the agent with this AID.
	 * @param aid 
	 * @return The agent mapped to this AID or null if
	 * this AID is not registered to any agent in the DF.
	 */
	public static IAgent getAgent(int aid) {
		return getAgents().get(aid);
	}
	
	/**
	 * Registers the agent in the directory and returns its
	 * AID (freshly generated for it). If the agent is already
	 * registered, returns its current AID.
	 * @param agent The agent to be registered
	 * @return The AID generated for the agent.
	 */
	public static int registerAgent(IAgent agent) {
		// If this agent is already in the hashMap,
		// just return its key.
		if (getAgents().containsValue(agent)) {
			return agent.getAID();
		}
		
		// Quick way to find a new ID for this agent
		// that is not in use at the moment.
		while (getAgents().containsKey(lastAID)) {
			lastAID++;
		}
		
		// The agent must know their own ID.
		agent.setAID(lastAID);
		agents.put(lastAID, agent);
		return lastAID;
	}
	
	/**
	 * Deregisters an agent from the DF and resets the
	 * agent's AID. 
	 * @param agent
	 * @return The agent's AID if the agent was found
	 * in the DF or -1 otherwise.
	 */
	public static int unregisterAgent(IAgent agent) {
		if (getAgents().containsKey(agent.getAID())) {
			getAgents().remove(agent.getAID());
			int aid = agent.getAID();
			agent.setAID(-1);
			return aid;
		}
		return -1;
	}
	
	/**
	 * Sends an ACL message. The receiver of the message
	 * is contained in the message.
	 * @param message
	 */
	public static void send(ACLMessage message) {
		
		message.getReceiver().send(message);
	}
}
