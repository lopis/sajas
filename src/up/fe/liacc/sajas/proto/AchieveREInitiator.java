package up.fe.liacc.sajas.proto;

import java.util.ArrayList;
import java.util.Vector;

import up.fe.liacc.sajas.core.AID;
import up.fe.liacc.sajas.core.Agent;
import up.fe.liacc.sajas.core.behaviours.FSMBehaviour;
import up.fe.liacc.sajas.domain.FIPANames;
import up.fe.liacc.sajas.lang.acl.ACLMessage;
import up.fe.liacc.sajas.lang.acl.MessageTemplate;

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
@SuppressWarnings({ "rawtypes" })
public class AchieveREInitiator extends FSMBehaviour {

	private MessageTemplate template;
	private FSM<AchieveREInitiator> protocolState;
	private String protocol;

	/**
	 * List of agents that didn't send the result
	 * of the request yet. 
	 */
	protected ArrayList<AID> waitingList;
	protected ArrayList<AID> responders = new ArrayList<AID>();
	protected Vector responses = new Vector(); //This is a vector for compatibility with JADE


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
		protocol = FIPANames.InteractionProtocol.FIPA_REQUEST;
		protocolState = State.RESPONSE;
		protocolState.setTemplate(template, this);

		waitingList = new ArrayList<AID>();
		for (int i = 0; i < message.getReceivers().size(); i++) {
			waitingList.add(message.getReceivers().get(i));
		}

		responders = new ArrayList<AID>();
		responders.addAll(message.getReceivers());
		myAgent.send(message);
	}
	
	public void action() {
		ACLMessage nextMessage = this.getAgent().receive(template);
		
		// Update the state
		if (nextMessage != null)
			protocolState = protocolState.nextState(nextMessage, this);			
		else
			protocolState = protocolState.nextState(this);
		
		// Update the template
		protocolState.setTemplate(template, this);
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * This method is called when all the responses have been collected or
	 * when the timeout is expired.
	 */ 
	protected void handleAllResponses(Vector responses) {}

	/**
	 * This method is called when all the result notification messages
	 * have been collected.
	 */
	protected void handleAllResultNotifications(Vector notifications) {}

	/**
	 * Handle 'agree' response from one of the receivers of the request.
	 */
	protected void handleAgree(ACLMessage inform) {}

	/**
	 * Handle 'refuse' response from one of the receivers of the request.
	 */
	protected void handleRefuse(ACLMessage inform) {}

	/**
	 * Handle 'inform' result notification from one of
	 * the receivers of the request.
	 */
	protected void handleInform(ACLMessage inform) {}

	/**
	 * Handle 'failure' result notification from one of
	 * the receivers of the request.
	 */
	protected void handleFailure(ACLMessage inform) {}

	/**
	 * Handle 'no-understood' response from one of
	 * the receivers of the request.
	 */
	protected void handleNotUnderstood() {}

	/**
	 * Verifies is all agents already responded with
	 * AGREE/REJECT/NOT_UNDERSTOOD
	 * @return
	 */
	protected boolean isAllResponded() {
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
	private enum State implements FSM<AchieveREInitiator> {

		/**
		 * Initially, a response of Agree/Refuse/Inform is expected
		 */
		RESPONSE {
			@SuppressWarnings("unchecked")
			@Override
			public State nextState(ACLMessage m, AchieveREInitiator re) {
				
				re.responses.add(m);
				
				switch (m.getPerformative()) {
				case ACLMessage.AGREE:
					re.handleAgree(m);
					break;
				case ACLMessage.REFUSE:
					re.handleRefuse(m);
					break;
				case ACLMessage.INFORM:
					re.handleInform(m);
					break;
				default:
					break;
				}
				
				if (re.isAllResponded()) {
					re.handleAllResponses(re.responses);
					return INFORM;
				} else if (re.isAllResulted()) {
					re.myAgent.removeBehaviour(re);
					re.onEnd();
				}

				return RESPONSE;
			}

			@Override
			public void setTemplate(MessageTemplate t, AchieveREInitiator re) {
				ArrayList<Integer> performatives = new ArrayList<Integer>();
				performatives.add(ACLMessage.AGREE);
				performatives.add(ACLMessage.REFUSE);
				performatives.add(ACLMessage.INFORM);
				t.setPerformatives(performatives);
			}
		}, 

		/**
		 * After receiving an Inform or all AGREEs/REFUSEs,
		 * the protocol skips to this state;
		 */
		INFORM {
			@Override
			public State nextState(ACLMessage m, AchieveREInitiator re) {
				if (m.getPerformative() == ACLMessage.INFORM) {
					re.handleInform(m);
				}
				
				if (re.isAllResulted()) {
					re.myAgent.removeBehaviour(re);
					re.onEnd();
				}

				return INFORM;
			}

			@Override
			public void setTemplate(MessageTemplate t, AchieveREInitiator re) {
				ArrayList<Integer> performatives = new ArrayList<Integer>();
				performatives.add(ACLMessage.INFORM);
				t.setPerformatives(performatives);
			}
		};

		@Override
		public State nextState(AchieveREInitiator re) {
			return this;
		}
	}

}
