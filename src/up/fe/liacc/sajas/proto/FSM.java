package up.fe.liacc.sajas.proto;

import up.fe.liacc.sajas.lang.acl.ACLMessage;
import up.fe.liacc.sajas.lang.acl.MessageTemplate;

/**
 * Basic interface for the State Machine enums in the classes
 * that extend the FSMBehaviour. Those enums are expected to contain
 * multiple states which implement these two methods.
 * @author joaolopes
 *
 */
public interface FSM<T> {
	/**
	 * Returns the next state, given a message and the behavior
	 * @param message The message just received.
	 * @param behaviour This behaviour. Use it to access the behavour state.
	 * @return
	 */
    public abstract FSM<T> nextState(ACLMessage message, T behaviour);
    
    /**
     * Update the current ACL Message Template.
     * @param t The current template
     */
    public abstract void setTemplate(MessageTemplate t);

	public abstract FSM<T> nextState(T behaviour);
}