package up.fe.liacc.sajas.domain;

import up.fe.liacc.sajas.lang.acl.ACLMessage;

/**
 * This class represents a generic FIPAException, i.e. one of
 * NotUnderstood,Failure,Refuse, as defined in jade.domain.FIPAAgentManagement.
 * It has two constructors, one based on an ACLMessage, and the second based on its
 * content, i.e. the exception message.
 * The ACL message performative is defaulted to not-understood
 * in the default constructor.
 * @author joaolopes
 *
 */
public class FIPAException extends Exception {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9195324568327896982L;
	
	private ACLMessage aclMessage = new ACLMessage(ACLMessage.NOT_UNDERSTOOD);
	
	/**
	 * Constructs a FIPAException from the given ACL message.
	 * @param message
	 */
	public FIPAException(ACLMessage message) {
		this.aclMessage = message;
	}
	
	/**
	 * Constructs a FIPAException and sets a string as the
	 * ACL message content. The ACL message performative is
	 * defaulted to not-understood
	 * @param message
	 */
	public FIPAException(String message) {
		this.aclMessage.setContent(message);
	}

	public ACLMessage getACLMessage() {
		return aclMessage;
	}
	
	public void setMessage(java.lang.String message) {
		aclMessage.setContent(message);
	}
	
	@Override
	public String getMessage() {
		return aclMessage.getContent();
	}
}
