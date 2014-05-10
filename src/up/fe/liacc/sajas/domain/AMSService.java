package up.fe.liacc.sajas.domain;

import java.util.HashMap;

import up.fe.liacc.sajas.core.Agent;
import up.fe.liacc.sajas.lang.acl.AID;

public class AMSService {

	private static HashMap<AID, Agent> agents = new HashMap<AID, Agent>();
	
	/**
	 * @param agent
	 */
	public static void register(Agent agent) {
		agents.put(agent.getAID(), agent);
	}
	
	public static void deregister(Agent agent) {
		agents.remove(agent.getAID());
	}

	public static Agent get(AID aid) {
		return agents.get(aid);
	}
}
