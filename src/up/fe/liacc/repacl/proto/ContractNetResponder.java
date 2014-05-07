package up.fe.liacc.repacl.proto;

import java.util.ArrayList;

import up.fe.liacc.repacl.MTS;
import up.fe.liacc.repacl.core.Agent;
import up.fe.liacc.repacl.core.behaviours.Behaviour;
import up.fe.liacc.repacl.domain.FIPANames;
import up.fe.liacc.repacl.lang.acl.ACLMessage;
import up.fe.liacc.repacl.lang.acl.MessageTemplate;

public class ContractNetResponder extends Behaviour {
	
	private MessageTemplate template;
	private State protocolState;
	private String protocol = FIPANames.InteractionProtocol.FIPA_CONTRACT_NET;
	
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
		ACLMessage nextMessage = this.getAgent().getMatchingMessage(template);
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
				performatives.add(ACLMessage.CALL_FOR_PROPOSAL);
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
				if (m.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
					cn.handleRejectProposal(cn.cfp, cn.proposal, m);
					return CFP;
				} else if (m.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
					cn.handleAcceptProposal(cn.cfp, cn.proposal, m);
					return Busy;
				}
				
				return ACRJ;
			}

			@Override
			public void setTemplate(MessageTemplate t) {
				ArrayList<Integer> performatives = new ArrayList<Integer>();
				performatives.add(ACLMessage.INFORM);
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
				performatives.add(ACLMessage.INFORM);
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
