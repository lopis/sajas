package up.fe.liacc.sajas.proto;

import up.fe.liacc.sajas.core.Agent;
import up.fe.liacc.sajas.core.behaviours.FSMBehaviour;
import up.fe.liacc.sajas.lang.acl.ACLMessage;
import up.fe.liacc.sajas.lang.acl.MessageTemplate;

public class SSContractNetResponder extends FSMBehaviour {
	
	private State protocolState;
	
	/**
	 * The last CFP received. The protocol only handles one CFP at a time.
	 */
	ACLMessage cfp; // The last received Call for Proposals
	/**
	 *  The response to the CFP. Should contain a PROPOSE, REFUSE or NOT_UNDERSTAND message.
	 */
	ACLMessage proposal;
	
	public SSContractNetResponder(Agent a, ACLMessage cfp) {
		super(a);
		protocolState = State.CFP;
		this.cfp = cfp;
	}

	@Override
	public void action() {
		protocolState = protocolState.action(this);
	}
	
	/**
	 * This method is called whenever an ACCEPT_PROPOSAL arrives
	 * that is in sequence. This default implementation does nothing
	 * and should be overridden.
	 * @param cfp The original cfp
	 * @param propose The reply to the original cfp
	 * @param accept The ACLMessage containing the ACCEPT PROPOSAL
	 * @return The INFORM reply message to send back to the initiator.
	 */
	protected ACLMessage handleAcceptProposal(ACLMessage cfp,
			ACLMessage propose, ACLMessage accept) {return accept;}

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
	 * Called when a new Call for Proposals arrives. This default
	 * implementation does nothing (returns null).
	 * @param m
	 * @return The reply message to be sent back to the initiator. (PROPOSE)
	 */
	protected ACLMessage handleCfp(ACLMessage m) { return m; }
	
	protected ACLMessage receive(MessageTemplate template) {
		return this.getAgent().receive(template);
	}

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
	private enum State{

		/**
		 * Initially, Call for Proposals (CFP) is expected
		 */
		CFP {
			@Override
			public State action(SSContractNetResponder cn) {
				
				if (cn.cfp == null) {
					return FINISHED;
				}
				
				ACLMessage prop = cn.proposal;
				prop = cn.handleCfp(cn.cfp);
				cn.myAgent.send(prop); // Sends Proposal to CFP
				return NOTIFICATION;
			}

			public MessageTemplate getTemplate(SSContractNetResponder cn) {
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
			public State action(SSContractNetResponder cn) {
				ACLMessage m = cn.receive(getTemplate(cn));
				if (m == null) {
					return NOTIFICATION;
				}
				if (m.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
					cn.handleRejectProposal(cn.cfp, cn.proposal, m);
				} else if (m.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
					ACLMessage reply = cn.handleAcceptProposal(cn.cfp, cn.proposal, m);
					
					cn.myAgent.send(reply);
				}
				return FINISHED;
				
			}

			public MessageTemplate getTemplate(SSContractNetResponder cn) {
				MessageTemplate template = MessageTemplate.or(
						MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL),
						MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL));
				template = MessageTemplate.or(template, 
						MessageTemplate.MatchConversationId(cn.cfp.getConversationId()));
				return template;
			}
		},
		
		FINISHED {
			@Override
			public State action(SSContractNetResponder behaviour) {
				return FINISHED;
			}
			
		};
		public State action(SSContractNetResponder behaviour) {
			return null;
		}
	}
	
	@Override
	public boolean done() {
		return protocolState == State.FINISHED;
	}
	
}
