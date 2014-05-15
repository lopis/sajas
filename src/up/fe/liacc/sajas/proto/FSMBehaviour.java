package up.fe.liacc.sajas.proto;

import up.fe.liacc.sajas.core.Agent;
import up.fe.liacc.sajas.core.behaviours.Behaviour;
import up.fe.liacc.sajas.lang.acl.ACLMessage;
import up.fe.liacc.sajas.lang.acl.MessageTemplate;

/**
 * Common superclass of all state-machine-based protocols.
 * The children of this behaviour are expected to 
 * @author joaolopes
 *
 */
public abstract class FSMBehaviour extends Behaviour {

	protected State protocolState;
	protected MessageTemplate template;

	/**
	 * Default constructor for the FSMBehaviour. Sets the owner agent.
	 * @param agent The agent this behaviour belongs to.
	 */
	public FSMBehaviour(Agent agent) {
		super(agent);
	}

	@Override
	public void action() {
		ACLMessage nextMessage = this.getAgent().receive(template);
		if (nextMessage != null) {
			
			// Update the state
			protocolState = protocolState.nextState(nextMessage, this);
			// Update the template
			protocolState.setTemplate(template);
		}
	}

	/**
	 * Basic interface for 
	 * @author joaolopes
	 *
	 */
	public interface State {
		/**
		 * Returns the next state, given a message and the behavior
		 * @param message The message just received.
		 * @param behaviour This behaviour. Use it to access the behavour state.
		 * @return
		 */
	    public abstract State nextState(ACLMessage message, Behaviour behaviour);
	    
	    /**
	     * Update the current ACL Message Template.
	     * @param t The current template
	     */
	    public abstract void setTemplate(MessageTemplate t);
	}
}
