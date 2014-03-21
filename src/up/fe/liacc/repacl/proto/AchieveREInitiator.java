package up.fe.liacc.repacl.proto;

import up.fe.liacc.repacl.IAgent;
import up.fe.liacc.repacl.acl.ACLMessage;

/**
 * 
 * @author joaolopes
 *
 */
public class AchieveREInitiator extends Behavior {
	
	ACLMessage template;

	/**
	 * Initiates the protocol and sends the message using this protocol.
	 * The agent that runs this behavior is the initiator and will send
	 * a FIPA REQUEST to the receivers. The receivers may then reply with
	 * AGREE or REJECT and then eventually send the result of the request
	 * as INFORM, FAILURE or NOT UNDERSTOOD.
	 * @param agent The message to be sent by this behavior.
	 */
	public AchieveREInitiator(IAgent agent, ACLMessage message) {
		super(agent);
		
	}
	
	protected void handleAllResponses() {}
	
	protected void handleAllResults() {}
	
	protected void handleAgree() {}
	
	protected void handleReject() {}

	protected void handleInform() {}
	
	protected void handleFailure() {}
	
	protected void handleNotUnderstood() {}

	@Override
	public void action() {
		 /* on each tick
			for each behavior
				1. get matching mail
				2. use mail
					If INFORM->handle inform, etc.
				3. remove mail from box
				4. if behavior finished, remove behavior
					Behavior ends if all receivers sent a reply.
		*/
		getOwner().getMail(template);
	}

}
