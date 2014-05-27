package up.fe.liacc.sajas.proto;

import java.util.ArrayList;

import up.fe.liacc.sajas.MTS;
import up.fe.liacc.sajas.core.Agent;
import up.fe.liacc.sajas.core.behaviours.FSMBehaviour;
import up.fe.liacc.sajas.lang.acl.ACLMessage;
import up.fe.liacc.sajas.lang.acl.MessageTemplate;

public class AchieveREResponder extends FSMBehaviour {

	protected MessageTemplate template;
	private State protocolState;
	protected ACLMessage request;
	protected ACLMessage response;

	public AchieveREResponder(Agent agent, MessageTemplate template) {
		super(agent);

		//template = createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		//template.addProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		this.template = template;

		protocolState = State.RESPONSE;
		protocolState.setTemplate(template, this);
	}

	//	@Override
	//	/**
	//	 * Schedule this method and call super(). This method
	//	 * must be overridden.
	//	 */
	//	public void action() {
	//		/*
	//		 * This method is scheduled in Repast.
	//		 * On each tick, do:
	//		 *  1 - Get one message matching the template
	//		 *  2 - Read the performative in the message
	//		 *  3 - Call the appropriate handler
	//		 *  5 - If wait list is empty, run the appropriate "handle all"
	//		 *  6 - Update the protocol state 
	//		 */
	//		ACLMessage nextMessage = this.getAgent().receive(template);
	//		if (nextMessage != null) {
	//			handleRequest(nextMessage);
	//		}
	//	}

	public static MessageTemplate createMessageTemplate(String protocol) {
		MessageTemplate template = new MessageTemplate();
		template.addProtocol(protocol);
		return template;

		//		if(protocol.equals(FIPANames.InteractionProtocol.FIPA_REQUEST)) {
		//			return MessageTemplate.and(MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
		//					MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
		//		
		//		} else
		//			if(CaseInsensitiveString.equalsIgnoreCase(FIPA_QUERY,iprotocol))
		//				return MessageTemplate.and(MessageTemplate.MatchProtocol(FIPA_QUERY),MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF),MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF)));
		//			else
		//				return MessageTemplate.MatchProtocol(iprotocol);
	}

	/**
	 * This method is called every tick if there is a message to process.
	 * This default implementation does nothing and should be overridden.
	 * @param nextMessage
	 * @return 
	 */
	protected ACLMessage handleRequest(ACLMessage nextMessage) {
		return null;
	}

	/**
	 * This method is called in the last state of the responder. In the
	 * first state, the responder sent a response to the request, containing
	 * AGREE or REFUSE. In the second state, the responder should optionally
	 * reply with and INFORM containing a message informing that some task
	 * was completed. This default implementation does nothing and this method
	 * should be overridden.
	 * @param request2
	 * @param response2
	 * @return
	 */
	protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
		return null;
	}

	private enum State implements FSM<AchieveREResponder> {

		/**
		 * Initially, a REQUEST is expected. Sends a REPLY to
		 * the request and the state changes to INFORM.
		 */
		RESPONSE {
			@Override
			public State nextState(ACLMessage m, AchieveREResponder re) {
				if (m != null) {
					re.request = m;
					ACLMessage response = re.handleRequest(m);
					MTS.send(response);
					return REPLY;
				}

				return RESPONSE;
			}

			@Override
			public void setTemplate(MessageTemplate t, AchieveREResponder re) {
				ArrayList<Integer> performatives = new ArrayList<Integer>();
				performatives.add(ACLMessage.REQUEST);
				performatives.add(ACLMessage.REQUEST_WHEN);
				performatives.add(ACLMessage.REQUEST_WHENEVER);
				performatives.add(ACLMessage.QUERY_IF);
				performatives.add(ACLMessage.QUERY_REF);
				t.setPerformatives(performatives);
			}
		}, 

		/**
		 * Is not expecting a message. Sends an Inform when a task ends.
		 */
		REPLY {
			@Override
			public State nextState(ACLMessage m, AchieveREResponder re) {
				ACLMessage result = re.prepareResultNotification(re.request, re.response);
				MTS.send(result);

				// Reset the responder0
				return RESPONSE;
			}

			@Override
			public void setTemplate(MessageTemplate t, AchieveREResponder re) {
				t = null; // No message is expected
			}
		};

		@Override
		public State nextState(AchieveREResponder re) {
			return this;
		}
	}

}
