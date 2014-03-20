package up.fe.liacc.repacl.core;

import java.util.HashMap;

import repast.simphony.context.Context;
import up.fe.liacc.repacl.IAgent;
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
	 * The Repast context that contains all
	 * scheduled Repast objects.
	 */
	private static Context<Object> context;
	
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
	 * Search an agent with this AID.
	 * @param aid 
	 * @return The agent mapped to this AID or null if
	 * this AID is not registered to any agent in the DF.
	 */
	public static IAgent searchAgent(int aid) {
		return getAgents().get(aid);
	}
	
	/**
	 * 
	 * @param service
	 * @return Returns the group of agents that 
	 * provide a service. Basically it means
	 * the agent activated that behavior.
	 */
	public static IAgent[] searchService(int service) {
		return new IAgent[0];
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
		context.add(agent);
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
	

}
