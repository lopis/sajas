package up.fe.liacc.sajas.proto;

import java.util.ArrayList;
import java.util.Vector;

import up.fe.liacc.sajas.MTS;
import up.fe.liacc.sajas.core.AID;
import up.fe.liacc.sajas.core.Agent;
import up.fe.liacc.sajas.core.behaviours.SimpleBehaviour;
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

	private ACLMessage cfp;

	/**
	 * Default super constructor.
	 * Must be called for the behavior to work.
	 * @param agent
	 * @param message
	 */
	public ContractNetInitiator(Agent agent, ACLMessage cfp) {
		myAgent = agent;
		this.cfp = cfp;
		
		registerFirstState(new StateSendCFP(), StateSendCFP.name);
		registerState(new StatePropose(), StatePropose.name);
		registerState(new StateInform(), StateInform.name);
		registerLastState(new StateFinished(), StateFinished.name);
		
		registerTransition(StateSendCFP.name, StatePropose.name, 1);
		registerTransition(StatePropose.name, StateInform.name, 1);
		registerTransition(StateInform.name, StateFinished.name, 1);
	}
	
	public ACLMessage receive(MessageTemplate template) {
		return myAgent.receive(template);
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

	
	private abstract class State extends SimpleBehaviour {

		protected MessageTemplate template = new MessageTemplate();
		protected static final String name = "";
		protected short isFinished = 0;
		
		public abstract String nextState(ACLMessage m);
		public abstract void setTemplate(MessageTemplate t);
		
		@Override
		public void action() {
			setTemplate(template);
			ACLMessage nextMessage = receive(template);
			if (nextMessage != null) {
				setCurrentState(nextState(nextMessage));
			} else {
				setCurrentState(nextState());
			}	
		}
		
		protected String nextState() {
			isFinished = 0;
			return name;
		}
		
		@Override
		public int onEnd() {
			return isFinished;
		}
		
	}
	
	/**
	 * Initial state. State changes to StateProposal
	 * after issuing the CFP.
	 * @author joaolopes
	 *
	 */
	public class StateSendCFP extends State {
		
		protected static final String name = "sendCFP";

		@Override
		public String nextState(ACLMessage m) {
			responders = cfp.getReceivers();
			MTS.send(prepareCfps(cfp).get(0)); // Send the CFP
			isFinished = 1;
			return StatePropose.name;
		}
		
		@Override
		protected String nextState() {
			return nextState(null);
		}

		@Override
		public void setTemplate(MessageTemplate t) {}
		
	}

	/**
	 * After sending a call for proposals, expects PROPOSAL
	 * TODO: implement CFP timeout
	 */
	public class StatePropose extends State {

		protected static final String name = "propose";

		public String nextState(ACLMessage m) {

			// Since we know the message matches the template,
			// we can assume it's a valid propose or refuse.
			responses.add(m);

			// Remove the sender from the list	
			// TODO: Should the protocol accept more messages from this sender?
			// 		 Or should further "proposals" from this agent be ignored?
			responders.remove(m.getSender());

			if (m.getPerformative() == ACLMessage.REFUSE) {
				handleRefuse(m);
			} else if (m.getPerformative() == ACLMessage.PROPOSE) {
				handlePropose(m, acceptances);
			}

			if (isAllResponded()) {

				// This vector will be populated by the "handle all" method

				handleAllResponses(responses, acceptances);
				for (Object aclMessage : acceptances) {
					// Send all "ACCEPT PROPOSE" or "REJECT PROPOSE"
					MTS.send((ACLMessage) aclMessage);
				}
				isFinished = 1;
				return StateInform.name;

			} else {
				isFinished = 0;
				return name;
			}
		}
		
		protected String nextState() {
			isFinished = 0;
			return name;
		}

		public void setTemplate(MessageTemplate t) {
			t.addPerformative(ACLMessage.PROPOSE);
			t.addPerformative(ACLMessage.REFUSE);
			t.addProtocol(protocol);
		}
	}

	/**
	 * After receiving an Inform or all AGREEs/REFUSEs,
	 * the protocol skips to this state;
	 */
	public class StateInform extends State {
		
		protected static final String name = "inform";
		
		@Override
		public String nextState(ACLMessage m) {
			if (isInformed()) {
				isFinished = 1;
				return StateFinished.name;
			} else {
				isFinished = 0;
				return name;
			}
		}

		@Override
		public void setTemplate(MessageTemplate t) {
			t.addPerformative(ACLMessage.INFORM);
			t.addProtocol(protocol);
		}
	}

	public class StateFinished extends State {
		
		protected static final String name = "finished";

		@Override
		public String nextState(ACLMessage m) {
			isFinished = 0; // the protocol is finished, but this state is technically never finished.
			return name;
		}

		@Override
		public void setTemplate(MessageTemplate t) {
			t = null;
		}

	}

}
