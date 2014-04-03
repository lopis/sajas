package up.fe.liacc.repacl.proto;

import java.util.Vector;

import repast.simphony.engine.schedule.ScheduledMethod;
import up.fe.liacc.repacl.Agent;
import up.fe.liacc.repacl.acl.ACLMessage;
import up.fe.liacc.repacl.acl.MessageTemplate;

/**
 * Initiates a "FIPA-REQUEST"-like protocol. Programmers that which to use
 * this protocol should extend this class and override the appropriate handler
 * methods:
 * 
 * <li>handleAllResponses(ACLMessage[] ms)</li>
 * <li>handleAllResults(ACLMessage[] ms)</li>
 * <li>handleAgree(ACLMessage m)</li>
 * <li>handleReject(ACLMessage m)</li>
 * <li>handleInform(ACLMessage m)</li>
 * <li>handleFailure(ACLMessage m)</li>
 * <li>handleNotUnderstood(ACLMessage m)</li>
 *  
 * The complement to this class is the AchieveREResponder which will respond
 * to the request sent by the agent that initiates the protocol. So the agents
 * that are supposed to respond to this reply should implement the handler
 * methods in the AchieveREResponder class.
 * 
 * @see AchieveREResponder
 * @author joaolopes
 *
 */
public class AchieveREInitiator extends Behavior {
	
	MessageTemplate template;

	/**
	 * Initiates the protocol and sends the message using this protocol.
	 * The agent that runs this behavior is the initiator and will send
	 * a FIPA REQUEST to the receivers. The receivers may then reply with
	 * AGREE or REJECT and then eventually send the result of the request
	 * as INFORM, FAILURE or NOT UNDERSTOOD.
	 * @param agent The message to be sent by this behavior.
	 */
	public AchieveREInitiator(Agent agent, ACLMessage message) {
		super(agent);
		
	}
	
	/**
	 * This method is called when all the responses have been collected or
	 * when the timeout is expired.
	 * TODO implement timeout
	 */ 
	protected void handleAllResponses(Vector<ACLMessage> responses) {}
	
	/**
	 * This method is called when all the result notification messages
	 * have been collected.
	 */
	protected void handleAllResultNotifications(Vector<ACLMessage> notifications) {}
	
	/**
	 * Handle 'agree' response from one of the receivers of the request.
	 */
	protected void handleAgree(ACLMessage agree) {}
	
	/**
	 * Handle 'refuse' response from one of the receivers of the request.
	 */
	protected void handleRefuse(ACLMessage agree) {}

	/**
	 * Handle 'inform' result notification from one of
	 * the receivers of the request.
	 */
	protected void handleInform(ACLMessage agree) {}
	
	/**
	 * Handle 'failure' result notification from one of
	 * the receivers of the request.
	 */
	protected void handleFailure(ACLMessage agree) {}
	
	/**
	 * Handle 'no-understood' response from one of
	 * the receivers of the request.
	 */
	protected void handleNotUnderstood() {}

	@Override
	@ScheduledMethod(start = 1, interval = 5)
	public void action() {
		/*
		 * This method is scheduled in Repast.
		 * On each tick, do:
		 *  1 - Get one message matching the template
		 *  2 - Read the performative in the message
		 *  3 - Call the appropriate handler
		 *  4 - Remove the responder from the wait list
		 *  5 - If wait list is empty, run the appropriate "handle all"
		 */
		
	}

}
