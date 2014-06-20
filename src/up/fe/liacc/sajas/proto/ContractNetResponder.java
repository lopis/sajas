package up.fe.liacc.sajas.proto;

import up.fe.liacc.sajas.core.Agent;
import up.fe.liacc.sajas.core.behaviours.FSMBehaviour;
import up.fe.liacc.sajas.domain.FIPANames;
import up.fe.liacc.sajas.lang.acl.ACLMessage;
import up.fe.liacc.sajas.lang.acl.MessageTemplate;

public class ContractNetResponder extends FSMBehaviour {

	private String protocol = FIPANames.InteractionProtocol.FIPA_CONTRACT_NET;

	/**
	 * The last CFP received. The protocol only handles one CFP at a time.
	 */
	private ACLMessage cfp; // The last received Call for Proposals
	/**
	 *  The response to the CFP. Should contain a PROPOSE, REFUSE or NOT_UNDERSTAND message.
	 */
	private ACLMessage proposal;

	private State protocolState;

	private MessageTemplate template;

	public ContractNetResponder(Agent agent, MessageTemplate template) {
		super(agent);

		template.addProtocol(protocol);
		protocolState = State.CFP;
		this.template = template;

		//		registerFirstState(new Behaviour() {
		//
		//			@Override
		//			public void action() {
		//				ACLMessage nextMessage = receive();
		//				if (nextMessage != null) {
		//					nextState(nextMessage);
		//				}
		//			}
		//		}, "contractnetresp");
	}

	public void action() {
		protocolState = protocolState.action(this);
	}


	protected ACLMessage receive(MessageTemplate template) {
		return this.getAgent().receive(template);
	}


	/**
	 * Called when a new Call for Proposals arrives. This default
	 * implementation does nothing (returns null).
	 * @param m
	 * @return The reply message to be sent back to the initiator. (PROPOSE)
	 */
	protected ACLMessage handleCfp(ACLMessage m) { return null; }


	protected ACLMessage handleAcceptProposal(ACLMessage cfp,
			ACLMessage propose, ACLMessage accept) {
				return accept;}

	protected void handleRejectProposal(ACLMessage cfp,
			ACLMessage propose, ACLMessage accept) {}

//	public static MessageTemplate createMessageTemplate(String protocol) {
//		State s = State.CFP;
//		MessageTemplate newMessageTemplate = new MessageTemplate();
//		newMessageTemplate.addProtocol(protocol);
//		s.setTemplate(newMessageTemplate, null);
//		return newMessageTemplate;
//	}

	/**
	 * This enum implements the FSMBehaviour.State interface and
	 * represents the state machine of the Contract Net Responder.
	 * This protocol has three different states: CFP, NOTIFICATION
	 * and BUSY.
	 * <li> CFP: The responder is waiting for a CFP. When one arrives,
	 * the responder prepares a proposal (which may contain a REFUSE,
	 * ACCEPT or NOT_UNDERSTOOD), sends it back to the initiator and
	 * the state changes to NOTIFICATION.</li>
	 * <li> NOTIFICATION: The responder is waiting for the notification
	 * of the initiator, containing ACCEPT or REJECT PROPOSAL. When it
	 * arrives, the responder handles the message and the state changes
	 * to BUSY </li>
	 * <li> BUSY: The responder is performing some task requested by the
	 * initiator and ignores further incoming mail. When it finishes, the
	 * handle  </li>
	 * @author joaolopes
	 *
	 */
	private enum State {

		/**
		 * Initially, Call for Proposals (CFP) is expected
		 */
		CFP {
			@Override
			public State action(ContractNetResponder cn) {
				ACLMessage m = cn.receive(getTemplate(cn));
				if (m == null) {
					return CFP;
				}
				ACLMessage prop = cn.proposal;
				prop = cn.handleCfp(m);
				cn.myAgent.send(prop); // Sends Proposal to CFP
				return NOTIFICATION;
			}

			public MessageTemplate getTemplate(ContractNetResponder cn) {
				MessageTemplate template = MessageTemplate.or(
						MessageTemplate.MatchPerformative(ACLMessage.CFP),
						MessageTemplate.MatchConversationId(cn.cfp.getConversationId()));
				return template;
			}
		}, 

		/**
		 * After receiving a CFP, the agent replies with a proposal or
		 * with reject. If the agent sent a proposal, the following state 
		 * is to keep waiting for  an ACCEPT_ or REJECT_PROPOSAL
		 */
		NOTIFICATION {
			@Override
			public State action(ContractNetResponder cn) {
				ACLMessage m = cn.receive(getTemplate(cn));
				if (m.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
					cn.handleRejectProposal(cn.cfp, cn.proposal, m);
				} else if (m.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
					ACLMessage reply = cn.handleAcceptProposal(cn.cfp, cn.proposal, m);
					
					cn.myAgent.send(reply);
				}
				return BUSY;
				
			}

			public MessageTemplate getTemplate(ContractNetResponder cn) {
				MessageTemplate template = MessageTemplate.or(
						MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL),
						MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL));
				template = MessageTemplate.or(template, 
						MessageTemplate.MatchConversationId(cn.cfp.getConversationId()));
				return template;
			}
		},

		/**
		 * After the proposal is accepted, the agent can do some task
		 * and get back to the CFP issuer later and send an INFORM when
		 * that task is DONE. In this state, messages are ignored.
		 */
		BUSY {
			@Override
			public State action(ContractNetResponder b) {
				return CFP;
			}
		};

		public abstract State action(ContractNetResponder cn);
	}

	@Override
	public boolean done() {
		return false;
	}
}
