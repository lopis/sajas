package up.fe.liacc.sajas.proto;

import java.util.ArrayList;
import java.util.Vector;

import up.fe.liacc.sajas.MTS;
import up.fe.liacc.sajas.core.AID;
import up.fe.liacc.sajas.core.Agent;
import up.fe.liacc.sajas.core.behaviours.Behaviour;
import up.fe.liacc.sajas.domain.FIPANames;
import up.fe.liacc.sajas.lang.acl.ACLMessage;
import up.fe.liacc.sajas.lang.acl.MessageTemplate;


@SuppressWarnings({ "rawtypes", "unchecked" }) // For compatibility with JADE. I'm so sorry.
public class ContractNetInitiator extends FSMBehaviour {

	private String protocol = FIPANames.InteractionProtocol.FIPA_CONTRACT_NET;
	
	// This vector contains the agents who received the CFP
	private ArrayList<AID> responders;
	protected Vector responses = new Vector();
	protected Vector acceptances = new Vector();

	private MessageTemplate template;

	private FSM protocolState;

	/**
	 * Default super constructor.
	 * Must be called for the behavior to work.
	 * @param agent
	 * @param message
	 */
	public ContractNetInitiator(Agent agent, ACLMessage cfp) {
		myAgent = agent;
		template = new MessageTemplate();
		template.addProtocol(protocol);
		protocolState = State.PROPOSAL;
		protocolState.setTemplate(template);
		responders = cfp.getReceivers();
		
		registerFirstState(new Behaviour() {

			@Override
			public void action() {
				ACLMessage nextMessage = receive();
				if (nextMessage != null) {
					nextState(nextMessage);
				}
			}
		}, "contractnetinit");
		
		MTS.send(prepareCfps(cfp).get(0)); // Send the CFP
	}
	
	protected void nextState(ACLMessage nextMessage) {
		// Update the state
		protocolState = protocolState.nextState(nextMessage, this);
		// Update the template
		protocolState.setTemplate(template);
	}


	protected ACLMessage receive() {
		return this.getAgent().receive(template);
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
	public ArrayList<ACLMessage> prepareCfps(ACLMessage cfp) {
		ArrayList<ACLMessage> cfps = new ArrayList<ACLMessage>();
		cfps.add(cfp);
		return cfps;
	}
	
	

	/**
	 * Verifies if all agents that received the call for proposals
	 * have responded with REFUSE or PROPOSE
	 * @return True if all agents responded.
	 */
	protected boolean isAllResponded() {
		return responders.size() == 1;
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
	protected void handleAllResponses(Vector responses, Vector acceptances) {}
	


	protected void handleRefuse(ACLMessage m) {}
	
	protected void handlePropose(ACLMessage m, Vector acceptances) {}
	
	/**
	 * This enum implements the FSMBehaviour.State interface and
	 * represents the state machine of the Contract Net Initiator.
	 * This protocol has three different states: PROPOSAL, INFORM,
	 * and FINISHED.
	 * <li> PROPOSAL: the CFP was sent and the initiator is waiting
	 * for proposals. When a propose arrives, the initiator calls
	 * the appropriate handler for REFUSE or PROPOSE. That responder
	 * is removed from the repsonders list. Is there are no responders
	 * left, change to INFORM state; otherwise, stay in PROPOSAL.</li>
	 * <li> INFORM: all proposals were sent and the initiator expects
	 * to receive "informs" from the other agents. Stay in this state
	 * until all informs were received. Change state to the FINISHED
	 * when that happens.
	 * @author joaolopes
	 *
	 */
	private enum State implements FSM {

		/**
		 * After sending a call for proposals, expect PROPOSAL
		 * TODO: implement CFP timeout
		 */
		PROPOSAL {
			@Override
			public State nextState(ACLMessage m, Behaviour b) {
				ContractNetInitiator cn = (ContractNetInitiator) b;
				
				// Since we know the message matches the template,
				// we can assume it's a valid propose or refuse.
				cn.responses.add(m);
				
				// Remove the sender from the list	
				// TODO: Should the protocol accept more messages from this sender?
				// 		 Or should further "proposals" from this agent be ignored?
				cn.responders.remove(m.getSender());		
				
				if (m.getPerformative() == ACLMessage.REFUSE) {
					cn.handleRefuse(m);
				} else if (m.getPerformative() == ACLMessage.PROPOSE) {
					cn.handlePropose(m, cn.acceptances);
				}
				
				if (cn.isAllResponded()) {
					
					// This vector will be populated by the "handle all" method
					
					cn.handleAllResponses(cn.responses, cn.acceptances);
					for (Object aclMessage : cn.acceptances) {
						// Send all "ACCEPT PROPOSE" or "REJECT PROPOSE"
						MTS.send((ACLMessage) aclMessage);
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
			public State nextState(ACLMessage m, Behaviour b) {
				if (((ContractNetInitiator) b).isInformed()) {
					return FINISHED;
				} else {
					return INFORM;
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
			public State nextState(ACLMessage m, Behaviour b) {
				return FINISHED;
			}

			@Override
			public void setTemplate(MessageTemplate t) {}
		};


	}


	

}
