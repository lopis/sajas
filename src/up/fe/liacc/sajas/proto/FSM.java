package up.fe.liacc.sajas.proto;

import up.fe.liacc.sajas.core.behaviours.Behaviour;
import up.fe.liacc.sajas.lang.acl.ACLMessage;
import up.fe.liacc.sajas.lang.acl.MessageTemplate;

/**
 * Basic interface for the State Machine enums in the classes
 * that extend the FSMBehaviour. Those enums are expected to contain
 * multiple states which implement these two methods.
 * @author joaolopes
 *
 */
public interface FSM {
	/**
	 * Returns the next state, given a message and the behavior
	 * @param message The message just received.
	 * @param behaviour This behaviour. Use it to access the behavour state.
	 * @return
	 */
    public abstract FSM nextState(ACLMessage message, Behaviour behaviour);
    
    /**
     * Update the current ACL Message Template.
     * @param t The current template
     */
    public abstract void setTemplate(MessageTemplate t);
}