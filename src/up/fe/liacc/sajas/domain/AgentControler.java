package up.fe.liacc.sajas.domain;

import up.fe.liacc.sajas.core.Agent;

public class AgentControler {

	private Agent a;

	public AgentControler(Agent a) {
		this.a = a;
	}
	
	public void start() {
		a.setup();
	}

	
}
