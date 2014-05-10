package up.fe.liacc.sajas.proto;

import java.util.ArrayList;

import up.fe.liacc.sajas.MTS;
import up.fe.liacc.sajas.core.Agent;
import up.fe.liacc.sajas.core.behaviours.Behaviour;
import up.fe.liacc.sajas.domain.FIPANames;
import up.fe.liacc.sajas.lang.acl.ACLMessage;
import up.fe.liacc.sajas.lang.acl.AID;
import up.fe.liacc.sajas.lang.acl.MessageTemplate;

public class ContractNetInitiator extends Behaviour {

	private MessageTemplate template;
	private State protocolState;
	private String protocol = FIPANames.InteractionProtocol.FIPA_CONTRACT_NET;
	
	// This vector contains the agents who received the CFP
	private ArrayList<AID> responders;
	protected ArrayList<ACLMessage> responses;
	protected ArrayList<ACLMessage> acceptances;

	/**
	 * Default super constructor.
	 * Must be called for the behavior to work.
	 * @param agent
	 * @param message
	 */
	public ContractNetInitiator(Agent agent, ACLMessage cfp) {
		super(agent);
		template = new MessageTemplate();
		template.addProtocol(protocol);
		protocolState = State.PROPOSAL;
		protocolState.setTemplate(template);
		acceptances = new ArrayList<ACLMessage>();
		responses = new ArrayList<ACLMessage>();
		responders = cfp.getReceivers();
		MTS.send(prepareCfps(cfp).get(0)); // Send the CFP
	}

	/**
	 * This method is called in the first state of this behavior
	 * (right after the constructor) and can be used to make changes to
	 * the CFP. This default implementation simply returns an array list
	 * containing the CFP. This implementation follows that of JADE's for 
	 * compatibility's sake.
	 * @param cfp The return value must be an array list containing just 
	 * the CFP.
	 */
	private ArrayList<ACLMessage> prepareCfps(ACLMessage cfp) {
		ArrayList<ACLMessage> cfps = new ArrayList<ACLMessage>();
		cfps.add(cfp);
		return cfps;		
	}

	@Override
	public void action() {
		/*
		 * This method is scheduled in Repast.
		 * On each tick, do:
		 *  1 - Get one message matching the template
		 *  2 - Read the performative in the message
		 *  3 - Call the appropriate handler
		 *  4 - Remove the responding agent from the wait list
		 *  5 - If wait list is empty, run the appropriate "handle all"
		 *  6 - Update the protocol state 
		 */

		// Retrieve one message from the mailbox
		ACLMessage nextMessage = this.getAgent().getMatchingMessage(template);
		if (nextMessage != null) {
			
			// Update the state
			protocolState = protocolState.nextState(nextMessage, this);
			// Update the template
			protocolState.setTemplate(template);
		}
	}
	
	protected ArrayList<ACLMessage> getResponses() {
		if (responses == null) {
			responses = new ArrayList<ACLMessage>();
		}
		return responses;
	}
	

	/**
	 * Verifies if all agents that received the call for proposals
	 * have responded with REFUSE or PROPOSE
	 * @return True if all agents responded.
	 */
	protected boolean isAllResponded() {
		return responders.size() == 1; //TODO HACk. Fix the DF search method
	}

	/**
	 * 
	 * @return
	 */
	protected boolean isInformed() {
		return false;
	}
	
	/**
	 * Called when all agents submitted their Proposals
	 * or when the CFP timeout expires. This default implementation
	 * does nothing and should be overridden when needed.
	 * @param responses Vector containing all responses received by all agents that replied to the CFP.
	 * @param acceptances This method should populate this vector with messages to deliver to the agents
	 * that replied to the CFP. This vector is a list of ACCEPT/REJECT PROPOSAL.
	 */
	protected void handleAllResponses(ArrayList<ACLMessage> responses, ArrayList<ACLMessage> acceptances) {}
	


	protected void handleRefuse(ACLMessage m) {}
	
	protected void handlePropose(ACLMessage m) {}
	
	/**
	 * TODO: this javadocs
	 * @author joaolopes
	 *
	 */
	private enum State {

		/**
		 * After sending a call for proposals, expect PROPOSAL
		 * TODO: implement CFP timeout
		 */
		PROPOSAL {
			@Override
			public State nextState(ACLMessage m, ContractNetInitiator cn) {
				// Since we know the message matches the template,
				// we can assume it's a valid propose or refuse.
				cn.responses.add(m);
				
				// Remove one sender from the list	
				// TODO: Should the protocol accept more messages from this sender?
				// 		 Or should further "proposals" from this agent be ignored?
				cn.responders.remove(m.getSender());		
				
				if (m.getPerformative() == ACLMessage.REFUSE) {
					cn.handleRefuse(m);
				} else if (m.getPerformative() == ACLMessage.PROPOSE) {
					cn.handlePropose(m);
				}
				
				if (cn.isAllResponded()) {
					
					// This vector will be populated by the "handle all" method
					
					cn.handleAllResponses(cn.getResponses(), cn.acceptances);
					for (ACLMessage aclMessage : cn.acceptances) {
						// Send all "ACCEPT PROPOSE" or "REJECT PROPOSE"
						MTS.send(aclMessage);
					}
					
					return INFORM;
					
				} else {
					return PROPOSAL;
				}
			}
			
			@Override
			public void setTemplate(MessageTemplate t) {
				ArrayList<Integer> performatives = new ArrayList<Integer>();
				performatives.add(ACLMessage.PROPOSE);
				performatives.add(ACLMessage.REFUSE);
				t.setPerformatives(performatives);
			}
		}, 
		
		/**
		 * After receiving an Inform or all AGREEs/REFUSEs,
		 * the protocol skips to this state;
		 */
		INFORM {
			@Override
			public State nextState(ACLMessage m, ContractNetInitiator re) {
				if (re.isInformed()) {
					return Dead;
				} else {
					return INFORM;
				}
			}
			
			@Override
			public void setTemplate(MessageTemplate t) {
				ArrayList<Integer> performatives = new ArrayList<Integer>();
				t.setPerformatives(performatives);
			}
		},
		
		/**
		 * Final state. 
		 */
		Dead
		;

		/**
		 * Returns the next state, given a message and the behavior
		 * @param m
		 * @param re
		 * @return
		 */
	    public State nextState(ACLMessage m, ContractNetInitiator re) {
	        return null;
	    }
	    
	    /**
	     * Update the current ACL Message Template.
	     * @param t
	     */
	    public void setTemplate(MessageTemplate t) {}
	}


	

}
