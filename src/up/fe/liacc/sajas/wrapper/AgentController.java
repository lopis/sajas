package up.fe.liacc.sajas.wrapper;

import up.fe.liacc.sajas.core.Agent;

public class AgentController {

	private Agent a;

	public AgentController(Agent a) {
		this.a = a;
	}
	
	public void start() {
		a.setup();
	}

	
}
