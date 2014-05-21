package up.fe.liacc.sajas.proto;

import up.fe.liacc.sajas.MTS;
import up.fe.liacc.sajas.core.Agent;
import up.fe.liacc.sajas.core.behaviours.SimpleBehaviour;
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
	private MessageTemplate template;

	public ContractNetResponder(Agent agent, MessageTemplate template) {
		myAgent = agent;
		this.template = template;

		registerFirstState(new StateCFP(), StateCFP.name);
		registerState(new StateNotification(), StateNotification.name);
		registerLastState(new StateBusy(), StateBusy.name);
		
		registerTransition(StateCFP.name, StateNotification.name, 1);
		registerTransition(StateNotification.name, StateBusy.name, 1);
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

	public static MessageTemplate createMessageTemplate(String protocol) {
		MessageTemplate newMessageTemplate = new MessageTemplate();
		newMessageTemplate.addProtocol(protocol);
		return newMessageTemplate;
	}
	
	public ACLMessage receive(MessageTemplate template) {
		return myAgent.receive(template);
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
	private abstract class State extends SimpleBehaviour {

		protected MessageTemplate t;
		protected static final String name = "";
		protected short isFinished = 0;

		public abstract String nextState(ACLMessage m);
		public abstract void setTemplate(MessageTemplate t);

		@Override
		public void action() {
			if (t == null) {
				t = template;
			}
			setTemplate(template);
			ACLMessage nextMessage = receive(template);
			if (nextMessage != null) {
				setCurrentState(nextState(nextMessage));
			}
			isFinished = 0;
		}

		@Override
		public int onEnd() {
			return isFinished;
		}

	}

	/**
	 * Initially, Call for Proposals (CFP) is expected
	 */
	public class StateCFP extends State {
		
		protected static final String name = "cfp";
		
		@Override
		public String nextState(ACLMessage m) {

			ACLMessage prop = proposal;
			prop = handleCfp(m);
			MTS.send(prop); // Sends Proposal to CFP
			isFinished = 1;
			return StateNotification.name;
		}

		@Override
		public void setTemplate(MessageTemplate t) {
			if (t == null) {
				t = template;
			}
			t.addPerformative(ACLMessage.CFP);
			t.addProtocol(protocol);
		}
	}

	/**
	 * After receiving a CFP, the agent replies with a proposal or
	 * with reject. If the agent sent a proposal, the following state 
	 * is to keep waiting for  an ACCEPT_ or REJECT_PROPOSAL
	 */
	public class StateNotification extends State {
		
		protected static final String name = "notify";
		
		@Override
		public String nextState(ACLMessage m) {

			if (m.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
				handleRejectProposal(cfp, proposal, m);
				isFinished = 1;
				return StateCFP.name;
			} else if (m.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
				handleAcceptProposal(cfp, proposal, m);
				isFinished = 1;
				return StateBusy.name;
			}

			isFinished = 0;
			return name;
		}

		@Override
		public void setTemplate(MessageTemplate t) {
			t = new MessageTemplate();
			t.addPerformative(ACLMessage.INFORM);
			t.addProtocol(protocol);
		}
	}

	/**
	 * After the proposal is accepted, the agent can do some task
	 * and get back to the CFP issuer later and send an INFORM when
	 * that task is DONE. In this state, messages are ignored.
	 */
	public class StateBusy extends State {
		
		protected static final String name = "busy";
		
		@Override
		public String nextState(ACLMessage m) {
			isFinished = 0;
			return name;
		}

		@Override
		public void setTemplate(MessageTemplate t) {
			t = new MessageTemplate();
			t.addPerformative(ACLMessage.INFORM);
			t.addProtocol(protocol);
		}
	}



}
