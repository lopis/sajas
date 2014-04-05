package up.fe.liacc.repacl.proto;

import java.util.ArrayList;
import java.util.Vector;

import up.fe.liacc.repacl.Agent;
import up.fe.liacc.repacl.acl.ACLMessage;
import up.fe.liacc.repacl.acl.MessageTemplate;
import up.fe.liacc.repacl.acl.Performative;
import up.fe.liacc.repacl.acl.Protocol;
import up.fe.liacc.repacl.core.MTS;

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

	private MessageTemplate template;
	private State protocolState;
	private Integer protocol;
	
	/**
	 * List of agents that didn't send the result
	 * of the request yet. 
	 */
	ArrayList<Integer> waitingList;


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
		
		// Set the template that will filter the responses
		template = new MessageTemplate();
		protocol = Protocol.FIPA_REQUEST; //FIXME this shouldn't be fixed
		protocolState = State.ARI;
		protocolState.setTemplate(template);
		
		waitingList = new ArrayList<Integer>();
		for (int i = 0; i < message.getReceivers().size(); i++) {
			waitingList.add(message.getReceivers().get(i).getAID());
		}

		send(message);
	}

	public Integer getProtocol() {
		return protocol;
	}

	public void setProtocol(Integer protocol) {
		this.protocol = protocol;
	}

	/**
	 * This method is called when all the responses have been collected or
	 * when the timeout is expired.
	 * TODO implement timeout for result
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
	public void action() {
		/*
		 * This method is scheduled in Repast.
		 * On each tick, do:
		 *  1 - Get one message matching the template
		 *  2 - Read the performative in the message
		 *  3 - Call the appropriate handler
		 *  4 - Remove the responding agent from the wait list TODO: implement wait list
		 *  5 - If wait list is empty, run the appropriate "handle all"
		 *  6 - Update the protocol state 
		 */

		// Retrieve one message from the mailbox
		ACLMessage nextMessage = this.getOwner().getMatchingMessage(template);
		if (nextMessage != null) {
			if (nextMessage.getPerformative() == Performative.INFORM
				|| nextMessage.getPerformative() == Performative.FAILURE) {
				
			}
			// Update the state
			protocolState = protocolState.nextState(nextMessage, this);
			// Update the template
			protocolState.setTemplate(template);
		}
		
		
	}
	
	/**
	 * Verifies is all agents already responded with
	 * AGREE/REJECT/NOT_UNDERSTOOD
	 * @return
	 */
	protected boolean isAllResponded() {
		// TODO implement wait list for AGREE/REFUSE
		return true;
	}

	/**
	 * Verifies if all agents sent the result message
	 * with INFORM or FAILURE.
	 * @return
	 */
	protected boolean isAllResulted() {
		return waitingList.isEmpty();
	}
	
	/**
	 * 
	 * @param message
	 */
	public void send(ACLMessage message) {
		MTS.send(message);
	}
	
	/**
	 * The state machine for this protocol.
	 * Each state implements "nextState(ACLMessage, AchieveREInitiator re)"
	 * that returns the next state given the current state (in 're') and
	 * the new message being processed. The AchieveREInitiator has three
	 * states:
	 * <li>ARI: expected Agree, Refuse and Inform and skips to
	 * state "In" when all agents send their response.
	 * <li>In: expects Inform and skips to state "Dead" after
	 * all agents send their results.
	 * <li>Dead: final state; triggers the disposal of the behavior.
	 * 
	 * @author joaolopes
	 *
	 */
	private enum State {

		/**
		 * Initially, Agree/Refuse/Inform are expected
		 */
		ARI {
			@Override
			public State nextState(ACLMessage m, AchieveREInitiator re) {
				if (re.isAllResponded()) {
					return In;
				} else if (re.isAllResulted()) {
					return Dead;
				}
				else {
					return ARI;
				}
			}
			
			@Override
			public void setTemplate(MessageTemplate t) {
				ArrayList<Integer> performatives = new ArrayList<Integer>();
				performatives.add(Performative.AGREE);
				performatives.add(Performative.REFUSE);
				performatives.add(Performative.INFORM);
				t.setPerformatives(performatives);
			}
		}, 
		
		/**
		 * After receiving an Inform or all AGREEs/REFUSEs,
		 * the protocol skips to this state;
		 */
		In {
			@Override
			public State nextState(ACLMessage m, AchieveREInitiator re) {
				if (re.isAllResulted()) {
					return Dead;
				}
				else {
					return In;
				}
			}
			
			@Override
			public void setTemplate(MessageTemplate t) {
				ArrayList<Integer> performatives = new ArrayList<Integer>();
				performatives.add(Performative.INFORM);
				t.setPerformatives(performatives);
			}
		},
		
		/**
		 * Final state. 
		 */
		Dead
		;

		/**
		 * Returns the next state, given a message and the behavior
		 * @param m
		 * @param re
		 * @return
		 */
	    public State nextState(ACLMessage m, AchieveREInitiator re) {
	        return null;
	    }
	    
	    /**
	     * 
	     * @param t
	     */
	    public void setTemplate(MessageTemplate t) {}
	}

}
