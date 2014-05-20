package up.fe.liacc.sajas.proto;

import java.util.ArrayList;

import up.fe.liacc.sajas.core.Agent;
import up.fe.liacc.sajas.core.behaviours.Behaviour;
import up.fe.liacc.sajas.lang.acl.ACLMessage;
import up.fe.liacc.sajas.lang.acl.MessageTemplate;

public class AchieveREResponder extends FSMBehaviour {

	protected MessageTemplate template;

	public AchieveREResponder(Agent agent, MessageTemplate template) {
		super(agent);

		//template = createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		//template.addProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		
		this.template = template;

		protocolState = State.RESPONSE;
		protocolState.setTemplate(template);
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

	private enum State implements FSMBehaviour.State {

		/**
		 * Initially, a response of Agree/Refuse/Inform is expected
		 */
		RESPONSE {
			@Override
			public State nextState(ACLMessage m, Behaviour b) {
				AchieveREInitiator re = (AchieveREInitiator) b;
				if (re.isAllResponded()) {
					return In;
				} else if (re.isAllResulted()) {
					return FINISHED;
				}
				else {
					return RESPONSE;
				}
			}

			@Override
			public void setTemplate(MessageTemplate t) {
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
		In {
			@Override
			public State nextState(ACLMessage m, Behaviour b) {
				AchieveREInitiator re = (AchieveREInitiator) b;
				if (re.isAllResulted()) {
					return FINISHED;
				}
				else {
					return In;
				}
			}

			@Override
			public void setTemplate(MessageTemplate t) {
				ArrayList<Integer> performatives = new ArrayList<Integer>();
				performatives.add(ACLMessage.INFORM);
				t.setPerformatives(performatives);
			}
		},

		/**
		 * Final state. 
		 */
		FINISHED {

			@Override
			public up.fe.liacc.sajas.proto.FSMBehaviour.State nextState(
					ACLMessage message, Behaviour behaviour) {
				return FINISHED;
			}

			@Override
			public void setTemplate(MessageTemplate t) {}

		}
		;

	}

}
