package up.fe.liacc.sajas.proto;

import up.fe.liacc.sajas.core.Agent;
import up.fe.liacc.sajas.core.behaviours.Behaviour;
import up.fe.liacc.sajas.lang.acl.ACLMessage;
import up.fe.liacc.sajas.lang.acl.MessageTemplate;

/**
 * This behaviour is designed to be used together with the Single-Session
 * responder protocol classes. More in details it is aimed at dealing with 
 * protocol initiation messages and dispatching them to responders. The
 * latter are created by means of the createResponder() abstract method
 * that developers must implement.
 * @author joaolopes
 *
 */
public abstract class SSResponderDispatcher extends Behaviour {
	
	private MessageTemplate template;
	

	public SSResponderDispatcher(Agent a, MessageTemplate template) {
		super(a);
		this.template = template;
	}

	@Override
	public void action() {
		ACLMessage message = myAgent.receive(template);
		if (message != null) {
			// Be sure a conversation-id is set. If not create a suitable one
			if (message.getConversationId() == null) {
				message.setConversationId(createConversationId(myAgent.getLocalName()));
			}
			final String convId = message.getConversationId();
			Behaviour ssResponder = createResponder(message);
			addBehaviour(ssResponder);
		}
	}

	/**
	 * Dispaches a new responder, adding it
	 * to the agents' set of behaviours.
	 * @param b
	 */
	protected void addBehaviour(Behaviour b) {
		myAgent.addBehaviour(b);
	}

	/**
	 * Creates a new Responder which will then be dispatched.
	 * @param initiationMsg
	 * @return
	 */
	protected abstract Behaviour createResponder(ACLMessage initiationMsg);
	
	
	
	private static long count = 0;
	private static String createConversationId(String name) {
		return "C-"+name+'-'+System.currentTimeMillis()+'-'+(count++);
	}

}
