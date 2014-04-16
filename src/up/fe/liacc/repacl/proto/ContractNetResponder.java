package up.fe.liacc.repacl.proto;

import java.util.ArrayList;

import up.fe.liacc.repacl.Agent;
import up.fe.liacc.repacl.acl.ACLMessage;
import up.fe.liacc.repacl.acl.MessageTemplate;
import up.fe.liacc.repacl.acl.Performative;
import up.fe.liacc.repacl.acl.Protocol;
import up.fe.liacc.repacl.core.MTS;

public class ContractNetResponder extends Behavior {
	
	private MessageTemplate template;
	private State protocolState;
	private Integer protocol = Protocol.FIPA_CONTRACT_NET;
	
	private ACLMessage cfp; // The last received Call for Proposals
	private ACLMessage proposal; // The proposal sent to this.cfp

	public ContractNetResponder(Agent agent) {
		super(agent);
		
		template = new MessageTemplate();
		template.addProtocol(protocol);
		protocolState = State.CFP;
		protocolState.setTemplate(template);
	}

	@Override
	public void action() {
		// Retrieve one message from the mailbox
		ACLMessage nextMessage = this.getOwner().getMatchingMessage(template);
		if (nextMessage != null) {

			// Update the state
			protocolState = protocolState.nextState(nextMessage, this);
			// Update the template
			protocolState.setTemplate(template);
		}
	}


	/**
	 * Called when a new Call for Proposals arrives. This default
	 * implementation does nothing (returns null).
	 * @param m
	 * @return 
	 */
	protected ACLMessage handleCfp(ACLMessage m) { return null; }


	protected void handleAcceptProposal(ACLMessage cfp,
			ACLMessage propose, ACLMessage accept) {}

	protected void handleRejectProposal(ACLMessage cfp,
			ACLMessage propose, ACLMessage accept) {}

	/**
	 * TODO: this javadocs
	 * @author joaolopes
	 *
	 */
	private enum State {

		/**
		 * Initially, Call for Proposals (CFP) is expected
		 */
		CFP {
			@Override
			public State nextState(ACLMessage m, ContractNetResponder cn) {
				cn.proposal = cn.handleCfp(m);
				MTS.send(cn.proposal); // Sends Proposal to CFP
				return ACRJ;
			}

			@Override
			public void setTemplate(MessageTemplate t) {
				ArrayList<Integer> performatives = new ArrayList<Integer>();
				performatives.add(Performative.CALL_FOR_PROPOSAL);
				t.setPerformatives(performatives);
			}
		}, 

		/**
		 * After receiving a CFP, the agent replies with a proposal or
		 * with reject. If the agent sent a proposal, the following state 
		 * is to keep waiting for  an ACCEPT_ or REJECT_PROPOSAL
		 */
		ACRJ {
			@Override
			public State nextState(ACLMessage m, ContractNetResponder cn) {
				if (m.getPerformative() == Performative.REJECT_PROPOSAL) {
					cn.handleRejectProposal(cn.cfp, cn.proposal, m);
					return CFP;
				} else if (m.getPerformative() == Performative.ACCEPT_PROPOSAL) {
					cn.handleAcceptProposal(cn.cfp, cn.proposal, m);
					return Busy;
				}
				
				return ACRJ;
			}

			@Override
			public void setTemplate(MessageTemplate t) {
				ArrayList<Integer> performatives = new ArrayList<Integer>();
				performatives.add(Performative.INFORM);
				t.setPerformatives(performatives);
			}
		},

		/**
		 * After the proposal is accepted, the agent can do some task
		 * and get back to the CFP issuer later and send an INFORM when
		 * that task is DONE. In this state, messages are ignored.
		 */
		Busy {
			@Override
			public State nextState(ACLMessage m, ContractNetResponder cn) {
				return Busy;
			}

			@Override
			public void setTemplate(MessageTemplate t) {
				ArrayList<Integer> performatives = new ArrayList<Integer>();
				performatives.add(Performative.INFORM);
				t.setPerformatives(performatives);
			}
		};

		/**
		 * Returns the next state, given a message and the behavior
		 * @param m
		 * @param re
		 * @return
		 */
		public State nextState(ACLMessage m, ContractNetResponder cn) {
			return null;
		}

		/**
		 * 
		 * @param t
		 */
		public void setTemplate(MessageTemplate t) {}
	}


}
