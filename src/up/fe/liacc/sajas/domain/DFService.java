package up.fe.liacc.sajas.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import up.fe.liacc.sajas.core.AID;
import up.fe.liacc.sajas.core.Agent;
import up.fe.liacc.sajas.domain.FIPAAgentManagement.DFAgentDescription;
import up.fe.liacc.sajas.domain.FIPAAgentManagement.ServiceDescription;


/**
 * This static class provides the Directory Facilitator Service
 * and is used to send messages to agents
 * and to keep a directory of all agents in the application.
 * Agents use the static method send(ACLMessage) to send a message
 * to one or more agents. The ACLMessage object contains
 * the receiver agent and the sender (so the receiver can reply back).
 * 
 * This class needs to be setup initially before registering new agents.
 * To do that, simply call setContext(...);
 * @author joaolopes
 *
 */
public class DFService {

	private static ArrayList<AID> agents = new ArrayList<AID>(); // Contains all agents
	private static HashMap<ServiceDescription, ArrayList<AID>> services = new HashMap<ServiceDescription, ArrayList<AID>>();
	private static HashMap<String, ArrayList<AID>> protocols = new HashMap<String, ArrayList<AID>>();
	private static HashMap<String, ArrayList<AID>> ontologies = new HashMap<String, ArrayList<AID>>();
	private static HashMap<String, ArrayList<AID>> languages = new HashMap<String, ArrayList<AID>>();


	//	/**
	//	 * @return Returns the map of agents. The field is initialized
	//	 * if it wasn't before.
	//	 */
	//	public static HashMap<Integer, Agent> getAgents() {
	//		if (agents == null) {
	//			agents = new HashMap<Integer, Agent>();
	//		}
	//		return agents;
	//	}

	/**
	 * Search an agent with this AID.
	 * @param aid 
	 * @return The agent mapped to this AID or null if
	 * this AID is not registered to any agent in the DF.
	 */
	public static DFAgentDescription[] search(Agent agent, DFAgentDescription dfd) {
		// FIXME: SearchConstraints are not implemented
		ArrayList<AID> results = new ArrayList<AID>();

		// For each non null field in 'dfd', get a list of agents from each map
		// and then intersect those lists.

		if (dfd.getName() != null) {
			if (!agents.contains(dfd.getName())) {
				return new DFAgentDescription[0];
			} 
			results.add(dfd.getName());
		} else {
			results = agents;
		}

		if (!dfd.getLanguages().isEmpty()) 
			results = filterAgents(languages, dfd.getLanguages(), results);

		if (!dfd.getProtocols().isEmpty())
			results = filterAgents(protocols, dfd.getProtocols(), results);

		if (!dfd.getServices().isEmpty()) {
			ArrayList<AID> listAgents = new ArrayList<AID>();
			for (ServiceDescription key : dfd.getServices()) {
				if (services.containsKey(key)) {
					listAgents.addAll(services.get(key));
				}
			}
			listAgents.retainAll(results);
			results = listAgents;
		}

		if (!dfd.getOntologies().isEmpty())
			results = filterAgents(ontologies, dfd.getOntologies(), results);

		DFAgentDescription[] dfdArray = new DFAgentDescription[results.size()];
		for (int i = 0; i < dfdArray.length; i++) {
			dfdArray[i] = new DFAgentDescription();
			dfdArray[i].setName(results.get(i));
		}
		
		return dfdArray;
	}

	/**
	 * For each key in 'list', gets all agents in map with the keys.
	 * Retains only the agents contained in 'previousResults'. 
	 * @param map
	 * @param list
	 * @param previousResults
	 * @return
	 */
	private static ArrayList<AID> filterAgents(HashMap<String, ArrayList<AID>> map,
			ArrayList<String> list, ArrayList<AID> previousResults) {
		
		ArrayList<AID> listAgents = new ArrayList<AID>();
		for (String key : list) {
			if (map.containsKey(key)) {
				listAgents.addAll(map.get(key));
			}
		}
		listAgents.retainAll(previousResults);
		return listAgents;
	}

	/**
	 * Registers the agent in the directory and returns its
	 * AID (freshly generated for it). If the agent is already
	 * registered, returns its current AID.
	 * @param agent The agent to be registered
	 * @return The AID generated for the agent.
	 */
	public static DFAgentDescription register(Agent agent, DFAgentDescription dfd) {
		AID aid = agent.getAID();
		if (dfd.getName() != null)
			agents.add(aid);

		if (dfd.getLanguages() != null)
			addAll(languages, dfd.getLanguages(), aid);

		if (dfd.getProtocols() != null)
			addAll(protocols, dfd.getProtocols(), aid);

		if (dfd.getServices() != null) {
			for (Iterator<ServiceDescription> iterator = dfd.getServices().iterator(); iterator.hasNext();) {
				ServiceDescription key = iterator.next();
				if (!services.containsKey(key)) {
					services.put(key, new ArrayList<AID>());
				}
				services.get(key).add(aid); 
			}
		}

		if (dfd.getOntologies() != null)
			addAll(ontologies, dfd.getOntologies(), aid);

		return dfd;
	}

	/**
	 * Adds to 'map' the pairs aid->list[i] for all members of 'list'.
	 * Allows duplicated values to be added. All repeated values will
	 * be removed when unregistered.
	 * @param map
	 * @param list
	 * @param aid
	 */
	private static void addAll(HashMap<String, ArrayList<AID>> map, ArrayList<String> list, AID aid) {
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			if (!map.containsKey(key)) {
				map.put(key, new ArrayList<AID>());
			}
			map.get(key).add(aid); 
		}
	}

	/**
	 * Deregisters an agent from the DF and resets the
	 * agent's AID (sets its AID to -1). 
	 * @param agent
	 * @return The old agent's AID if the agent was found
	 * in the DF or -1 otherwise.
	 */
	public static void deregister(Agent agent, DFAgentDescription dfd) {
		AID aid = agent.getAID();
		if (dfd.getName() != null)
			agents.remove(aid);

		if (dfd.getLanguages() != null)
			removeAll(languages, dfd.getLanguages(), aid);

		if (dfd.getProtocols() != null)
			removeAll(protocols, dfd.getProtocols(), aid);

		if (dfd.getServices() != null) {
			for (Iterator<ServiceDescription> iterator = dfd.getServices().iterator(); iterator.hasNext();) {
				ServiceDescription key = iterator.next();
				if (services.containsKey(key)) {
					services.get(key).remove(aid);
				}
			}
		}

		if (dfd.getOntologies() != null)
			removeAll(ontologies, dfd.getOntologies(), aid);
	}

	/**
	 * Removes the references of this AID from the map.
	 * @param map The map where to remove from
	 * @param list The keys that point to the lists inside the map
	 * @param aid The value to search for and remove
	 */
	private static void removeAll(HashMap<String, ArrayList<AID>> map, ArrayList<String> list, AID aid) {
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			if (map.containsKey(key)) {
				map.get(key).remove(aid);
			}
		}
	}

	public static void deregisterAgent(Agent agent) {
		AID aid = agent.getAID();
		agents.remove(aid);
		//TODO remove from other maps
	}

}
