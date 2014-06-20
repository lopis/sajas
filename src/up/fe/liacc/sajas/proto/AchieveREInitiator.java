package up.fe.liacc.sajas.proto;

import java.util.ArrayList;
import java.util.Vector;

import up.fe.liacc.sajas.core.AID;
import up.fe.liacc.sajas.core.Agent;
import up.fe.liacc.sajas.core.behaviours.FSMBehaviour;
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

	private State protocolState;

	protected ArrayList<AID> responders = new ArrayList<AID>();
	protected ArrayList<AID> informers = new ArrayList<AID>();
	protected Vector responses = new Vector(); //This is a vector for compatibility with JADE
	
	protected ACLMessage request;


	/**
	 * Initiates the protocol and sends the message using this protocol.
	 * The agent that runs this behavior is the initiator and will send
	 * a FIPA REQUEST to the receivers. The receivers may then reply with
	 * AGREE or REJECT and then eventually send the result of the request
	 * as INFORM, FAILURE or NOT UNDERSTOOD.
	 * @param agent The message to be sent by this behavior.
	 */
	public AchieveREInitiator(Agent agent, ACLMessage request) {
		super(agent);
//		if (request.getConversationId() == null) {
//			request.setConversationId(System.currentTimeMillis() + "_" + myAgent);
//		}
		this.request = request;
		this.responders = new ArrayList<AID>(request.getReceivers());
		this.protocolState = State.SEND_REQUEST;
	}
	
	public void action() {
		protocolState = protocolState.action(this);	
//		ACLMessage nextMessage = this.getAgent().receive(template);
//		
//		// Update the state
//		if (nextMessage != null)
//			protocolState = protocolState.nextState(nextMessage, this);			
//		else
//			protocolState = protocolState.nextState(this);
//		
//		// Update the template
//		protocolState.setTemplate(template, this);
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
		return responders.isEmpty();
	}

	/**
	 * Verifies if all agents sent the result message
	 * with INFORM or FAILURE.
	 * @return
	 */
	protected boolean isAllResulted() {
		return informers.isEmpty();
	}


	protected ACLMessage receive(MessageTemplate template) {
		return myAgent.receive(template);
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
		
		SEND_REQUEST {

			@Override
			public State action(AchieveREInitiator re) {
				if (re.request.getConversationId() == null) {
					re.request.setConversationId(re.myAgent.getName() + System.nanoTime());
				}
				re.responders = new ArrayList<AID>();
				re.responders.addAll(re.request.getReceivers());
				re.myAgent.send(re.request);
				return RESPONSE;
			}
		},

		/**
		 * Initially, a response of Agree/Refuse/Inform is expected
		 */
		RESPONSE {
			@SuppressWarnings("unchecked")
			@Override
			public State action(AchieveREInitiator re) {
				
				ACLMessage m = re.receive(getTemplate(re));
				if (m == null) {
					return RESPONSE;
				}
				
				re.responses.add(m);
				re.responders.remove(m.getSender());
				
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
				}
				
				return RESPONSE;
			}

			public MessageTemplate getTemplate(AchieveREInitiator re) {
				MessageTemplate template = new MessageTemplate();
				template.addPerformative(ACLMessage.AGREE);
				template.addPerformative(ACLMessage.REFUSE);
				template.addPerformative(ACLMessage.INFORM);
				return template;
			}
		}, 

		/**
		 * After receiving an Inform or all AGREEs/REFUSEs,
		 * the protocol skips to this state;
		 */
		INFORM {
			@Override
			public State action(AchieveREInitiator re) {
				ACLMessage m = re.receive(getTemplate(re));
				if (m!=null &&m.getPerformative() == ACLMessage.INFORM) {
					re.handleInform(m);
				}
				if (re.isAllResulted()) {
					return FINISHED;
				}
				return INFORM;
			}

			public MessageTemplate getTemplate(AchieveREInitiator re) {
				MessageTemplate template = new MessageTemplate();
				template.addPerformative(ACLMessage.INFORM);
				return template;
			}
		},
		
		FINISHED {

			@Override
			protected State action(AchieveREInitiator re) {
				return FINISHED;
			}
			
		};
		
		protected abstract State action(AchieveREInitiator re);
	}
	
	@Override
	public boolean done() {
		return this.protocolState == State.FINISHED;
	}
}
