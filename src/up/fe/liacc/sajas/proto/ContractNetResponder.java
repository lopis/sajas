package up.fe.liacc.sajas.proto;

import java.util.ArrayList;

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

	private FSM<ContractNetResponder> protocolState;

	private MessageTemplate template;

	public ContractNetResponder(Agent agent, MessageTemplate template) {
		super(agent);

		template.addProtocol(protocol);
		protocolState = State.CFP;
		protocolState.setTemplate(template, this);
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
		ACLMessage nextMessage = this.getAgent().receive(template);
		
		// Update the state
		if (nextMessage != null)
			protocolState = protocolState.nextState(nextMessage, this);			
		else
			protocolState = protocolState.nextState(this);
		
		// Update the template
		protocolState.setTemplate(template, this);
	}


	protected void nextState(ACLMessage nextMessage) {
		// Update the state
		protocolState = protocolState.nextState(nextMessage, this);
		// Update the template
		protocolState.setTemplate(template, this);
	}


	protected ACLMessage receive() {
		return this.getAgent().receive(template);
	}


	/**
	 * Called when a new Call for Proposals arrives. This default
	 * implementation does nothing (returns null).
	 * @param m
	 * @return The reply message to be sent back to the initiator. (PROPOSE)
	 */
	protected ACLMessage handleCfp(ACLMessage m) { return null; }


	protected void handleAcceptProposal(ACLMessage cfp,
			ACLMessage propose, ACLMessage accept) {}

	protected void handleRejectProposal(ACLMessage cfp,
			ACLMessage propose, ACLMessage accept) {}

	public static MessageTemplate createMessageTemplate(String protocol) {
		State s = State.CFP;
		MessageTemplate newMessageTemplate = new MessageTemplate();
		newMessageTemplate.addProtocol(protocol);
		s.setTemplate(newMessageTemplate, null);
		return newMessageTemplate;
	}

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
	private enum State implements FSM<ContractNetResponder> {

		/**
		 * Initially, Call for Proposals (CFP) is expected
		 */
		CFP {
			@Override
			public State nextState(ACLMessage m, ContractNetResponder cn) {
				ACLMessage prop = cn.proposal;
				prop = cn.handleCfp(m);
				cn.myAgent.send(prop); // Sends Proposal to CFP
				return NOTIFICATION;
			}

			@Override
			public void setTemplate(MessageTemplate t, ContractNetResponder c) {
				ArrayList<Integer> performatives = new ArrayList<Integer>();
				performatives.add(ACLMessage.CFP);
				t.setPerformatives(performatives);
			}
		}, 

		/**
		 * After receiving a CFP, the agent replies with a proposal or
		 * with reject. If the agent sent a proposal, the following state 
		 * is to keep waiting for  an ACCEPT_ or REJECT_PROPOSAL
		 */
		NOTIFICATION {
			@Override
			public State nextState(ACLMessage m, ContractNetResponder b) {
				if (m.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
					b .handleRejectProposal(b.cfp, b.proposal, m);
					return CFP;
				} else if (m.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
					b.handleAcceptProposal(b.cfp, b.proposal, m);
					return BUSY;
				}

				return NOTIFICATION;
			}

			@Override
			public void setTemplate(MessageTemplate t, ContractNetResponder c) {
				ArrayList<Integer> performatives = new ArrayList<Integer>();
				performatives.add(ACLMessage.ACCEPT_PROPOSAL);
				performatives.add(ACLMessage.REJECT_PROPOSAL);
				t.setPerformatives(performatives);
			}
		},

		/**
		 * After the proposal is accepted, the agent can do some task
		 * and get back to the CFP issuer later and send an INFORM when
		 * that task is DONE. In this state, messages are ignored.
		 */
		BUSY {
			@Override
			public State nextState(ACLMessage m, ContractNetResponder b) {
				return BUSY;
			}

			@Override
			public void setTemplate(MessageTemplate t, ContractNetResponder c) {
				ArrayList<Integer> performatives = new ArrayList<Integer>();
				performatives.add(ACLMessage.INFORM);
				t.setPerformatives(performatives);
			}
		};
		
		public State nextState(ContractNetResponder behaviour) {
			return this;
		}
	}


}
