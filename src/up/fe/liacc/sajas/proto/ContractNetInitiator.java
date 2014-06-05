package up.fe.liacc.sajas.proto;

import java.util.ArrayList;
import java.util.Vector;

import up.fe.liacc.sajas.core.AID;
import up.fe.liacc.sajas.core.Agent;
import up.fe.liacc.sajas.core.behaviours.FSMBehaviour;
import up.fe.liacc.sajas.domain.FIPANames;
import up.fe.liacc.sajas.lang.acl.ACLMessage;
import up.fe.liacc.sajas.lang.acl.MessageTemplate;


@SuppressWarnings({ "rawtypes", "unchecked" }) // For compatibility with JADE. I'm so sorry.
public class ContractNetInitiator extends FSMBehaviour {

	private static String protocol = FIPANames.InteractionProtocol.FIPA_CONTRACT_NET;
	
	// This vector contains the agents who received the CFP
	private ArrayList<AID> responders = new ArrayList<AID>();
	protected Vector responses = new Vector();
	protected Vector acceptances = new Vector();

	private State protocolState;
	private ACLMessage cfp;
	private long replyTimeout;

	protected ArrayList<AID> respondersToInform = new ArrayList<AID>();

	/**
	 * Default super constructor.
	 * Must be called for the behavior to work.
	 * @param agent
	 * @param message
	 */
	public ContractNetInitiator(Agent agent, ACLMessage cfp) {
		super(agent);
		
		protocolState = State.SEND_CFP;
		this.cfp = cfp;
		responders = cfp.getReceivers();
		
//		registerFirstState(new Behaviour() {
//
//			@Override
//			public void action() {
//				ACLMessage nextMessage = receive();
//				if (nextMessage != null) {
//					nextState(nextMessage);
//				}
//			}
//		}, "contractnetinit");

	}
	
	public void action() {
		protocolState = protocolState.action(this);
	}


	protected ACLMessage receive(MessageTemplate template) {
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
	public Vector prepareCfps(ACLMessage cfp) {
		Vector cfps = new Vector();
		cfps.add(cfp);
		return cfps;
	}
	
	

	/**
	 * Verifies if all agents that received the call for proposals
	 * have responded with REFUSE or PROPOSE
	 * @return True if all agents responded.
	 */
	protected boolean isAllResponded() {
		return responders.isEmpty();
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
	

	/**
	 * This method is called every time a refuse message is received.
	 * This default implementation does nothing; programmers might wish
	 * to override the method in case they need to react to this event.
	 * @param m The received refuse message
	 */
	protected void handleRefuse(ACLMessage m) {}
	
	/**
	 * This method is called every time a propose message is received.
	 * This default implementation does nothing; programmers might wish
	 * to override the method in case they need to react to this event.
	 * @param m
	 * @param acceptances the list of ACCEPT/REJECT_PROPOSAL to be sent back.
	 * This list can be filled step by step redefining this method, or it can
	 * be filled at once redefining the handleAllResponses method.
	 */
	protected void handlePropose(ACLMessage m, Vector acceptances) {}
	
	/**
	 * The handler for the final message of the protocol.
	 * This default implementation does nothing and should be
	 * overridden if needed.
	 */
	protected void handleInform(ACLMessage inform) {}
	
	private static long count = 0;
	private static String createConversationId(String name) {
		return "C-"+name+'-'+System.currentTimeMillis()+'-'+(count++);
	}
	
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
	private enum State{
		
		SEND_CFP {

			public State action(ContractNetInitiator cn) {
				
				ACLMessage cfps = (ACLMessage) cn.prepareCfps(cn.cfp).get(0);
				if (cfps.getConversationId() == null || cfps.getConversationId().equals("")) {
					cfps.setConversationId(createConversationId(cn.myAgent.getLocalName()));
				}
				cn.myAgent.send(cfps); // Send the CFP;
				return PROPOSAL;
			}
			
		},

		/**
		 * After sending a call for proposals, expect PROPOSAL
		 * TODO: implement CFP timeout
		 */
		PROPOSAL {
			@Override
			public State action(ContractNetInitiator cn) {
				
				ACLMessage m = cn.receive(getTemplate(cn));
				if (m == null) {
					return PROPOSAL;
				}
				
				// Since we know the message matches the template,
				// we can assume it's a valid propose or refuse.
				cn.responses.add(m);
				
				// Remove the sender from the list	
				// TODO: Should the protocol accept more messages from this sender?
				// 		 Or should further "proposals" from this agent be ignored?
				cn.responders.remove(m.getSender());
				System.err.println("\t\t#" + cn.responders.size());
				
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
						cn.myAgent.send((ACLMessage) aclMessage);
						cn.respondersToInform .addAll(((ACLMessage) aclMessage).getReceivers());
					}
					
					cn.replyTimeout = System.currentTimeMillis();
					
					
					return INFORM;
					
				} else {
					return PROPOSAL;
				}
			}
			
			public MessageTemplate getTemplate(ContractNetInitiator cn) {
				MessageTemplate template = MessageTemplate.or(
						MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
						MessageTemplate.MatchPerformative(ACLMessage.REFUSE));
				template = MessageTemplate.or(template, 
						MessageTemplate.MatchConversationId(cn.cfp.getConversationId()));
				return template;
			}
		}, 
		
		/**
		 * After receiving an Inform or all AGREEs/REFUSEs,
		 * the protocol skips to this state;
		 */
		INFORM {
			private MessageTemplate getTemplate(ContractNetInitiator cn) {
				MessageTemplate template = MessageTemplate.or(
						MessageTemplate.MatchPerformative(ACLMessage.INFORM),
						MessageTemplate.MatchConversationId(cn.cfp.getConversationId()));
				return template;
			}

			@Override
			public State action(ContractNetInitiator cn) {
				ACLMessage m = cn.receive(getTemplate(cn));
				if (m == null) {
					return INFORM;
				}
				
				cn.handleInform(m);
				cn.respondersToInform.remove(m.getSender());
				
				MessageTemplate template = getTemplate(cn);
				if (cn.respondersToInform.isEmpty()) {
					return FINISHED;
				} else {
					return INFORM;
				}
			}
		},
		
		FINISHED {

			@Override
			public State action(ContractNetInitiator behaviour) {
				return FINISHED;
			}
			
		};
		
		public abstract State action(ContractNetInitiator behaviour);
	}
	
	@Override
	public boolean done() {
		return protocolState == State.FINISHED;
	}
	
}
