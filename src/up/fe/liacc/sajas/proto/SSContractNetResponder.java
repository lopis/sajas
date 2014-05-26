package up.fe.liacc.sajas.proto;

import java.util.ArrayList;

import up.fe.liacc.sajas.MTS;
import up.fe.liacc.sajas.core.Agent;
import up.fe.liacc.sajas.core.behaviours.FSMBehaviour;
import up.fe.liacc.sajas.lang.acl.ACLMessage;
import up.fe.liacc.sajas.lang.acl.MessageTemplate;

public class SSContractNetResponder extends FSMBehaviour {
	
	private State protocolState;
	private MessageTemplate template;
	
	/**
	 * The last CFP received. The protocol only handles one CFP at a time.
	 */
	private ACLMessage cfp; // The last received Call for Proposals
	/**
	 *  The response to the CFP. Should contain a PROPOSE, REFUSE or NOT_UNDERSTAND message.
	 */
	private ACLMessage proposal;
	
	public SSContractNetResponder(Agent a, ACLMessage cfp) {
		super(a);
		protocolState = State.CFP;
		this.template = createMessageTemplate(cfp);
		this.cfp = cfp;
	}

	@Override
	public void action() {
		ACLMessage nextMessage = this.getAgent().receive(template);
		
		// Update the state
		if (nextMessage != null)
			protocolState = protocolState.nextState(nextMessage, this);			
		else
			protocolState = protocolState.nextState(this);
		
		// Update the template
		protocolState.setTemplate(template);
	}
	
	/**
	 * This method is called whenever an ACCEPT_PROPOSAL arrives
	 * that is in sequence. This default implementation does nothing
	 * and should be overridden.
	 * @param cfp The original cfp
	 * @param propose The reply to the original cfp
	 * @param accept The ACLMessage containing the ACCEPT PROPOSAL
	 */
	protected void handleAcceptProposal(ACLMessage cfp,
			ACLMessage propose, ACLMessage accept) {}

	/**
	 * This method is called whenever a REJECT_PROPOSAL arrives
	 * that is in sequence. This default implementation does nothing
	 * and should be overridden.
	 * @param cfp The original cfp
	 * @param propose The reply to the original cfp
	 * @param reject The ACLMessage containing the REJECT PROPOSAL
	 */
	protected void handleRejectProposal(ACLMessage cfp,
			ACLMessage propose, ACLMessage reject) {}
	
	/**
	 * Creates the template and gives it the conversation id
	 * of the CFP.
	 * @param cfp
	 * @return
	 */
	private MessageTemplate createMessageTemplate(ACLMessage cfp) {
		MessageTemplate t = new MessageTemplate();
		t.addPerformative(cfp.getPerformative());
		t.addConversationId(cfp.getConversationId());
		return t;
	}

	/**
	 * Called when a new Call for Proposals arrives. This default
	 * implementation does nothing (returns null).
	 * @param m
	 * @return The reply message to be sent back to the initiator. (PROPOSE)
	 */
	protected ACLMessage handleCfp(ACLMessage m) { return m; }

	/**
	 * This enum implements the FSMBehaviour.State interface and
	 * represents the state machine of the Contract Net Responder.
	 * This protocol has three different states: CFP, NOTIFICATION
	 * and BUSY.
	 * <li> CFP: The first state, emidiately executed. when the
	 * protocol starts. The state changes to NOTIFICATION</li>
	 * <li> NOTIFICATION: The responder is waiting for the notification
	 * of the initiator, containing ACCEPT or REJECT PROPOSAL. When it
	 * arrives, the responder handles the message and the state changes
	 * to BUSY </li>
	 * <li> FINISH: The responder is performing some task requested by the
	 * initiator and ignores further incoming mail. When it finishes, an
	 * INFORM is sent to the initiator, the protocol ends and the behaviour
	 * discarted. </li>
	 * @author joaolopes
	 *
	 */
	private enum State implements FSM<SSContractNetResponder> {

		/**
		 * Initially, Call for Proposals (CFP) is expected
		 */
		CFP {
			@Override
			public State nextState(ACLMessage m, SSContractNetResponder b) {
				ACLMessage prop = b.proposal;
				prop = b.handleCfp(m);
				MTS.send(prop); // Sends Proposal to CFP
				return NOTIFICATION;
			}

			@Override
			public void setTemplate(MessageTemplate t) {
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
			public State nextState(ACLMessage m, SSContractNetResponder cn) {
				if (m.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
					cn.handleRejectProposal(cn.cfp, cn.proposal, m);
					return CFP;
				} else if (m.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
					cn.handleAcceptProposal(cn.cfp, cn.proposal, m);
					return FINISH;
				}

				return NOTIFICATION;
			}

			@Override
			public void setTemplate(MessageTemplate t) {
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
		FINISH {
			@Override
			public State nextState(ACLMessage m, SSContractNetResponder b) {
				return FINISH;
			}

			@Override
			public void setTemplate(MessageTemplate t) {}
		};
		
		@Override
		public State nextState(ACLMessage m, SSContractNetResponder b) {
			return null;
		}
		
		@Override
		public State nextState(
				SSContractNetResponder behaviour) {
			return this;
		}
	}
}
