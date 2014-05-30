package up.fe.liacc.sajas.proto;

import java.util.ArrayList;

import up.fe.liacc.sajas.core.Agent;
import up.fe.liacc.sajas.core.behaviours.FSMBehaviour;
import up.fe.liacc.sajas.lang.acl.ACLMessage;
import up.fe.liacc.sajas.lang.acl.MessageTemplate;

public class AchieveREResponder extends FSMBehaviour {

	protected MessageTemplate template;
	private FSM<AchieveREResponder> protocolState;
	protected ACLMessage request;
	protected ACLMessage response;

	public AchieveREResponder(Agent agent, MessageTemplate template) {
		super(agent);

		this.template = template;

		protocolState = State.RESPONSE;
		protocolState.setTemplate(template, this);
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

	public static MessageTemplate createMessageTemplate(String protocol) {
		MessageTemplate template = new MessageTemplate();
		template.addProtocol(protocol);
		return template;
	}

	/**
	 * This method is called every tick if there is a message to process.
	 * This default implementation does nothing and should be overridden.
	 * @param nextMessage
	 * @return 
	 */
	protected ACLMessage handleRequest(ACLMessage nextMessage) {
		return null;
	}

	/**
	 * This method is called in the last state of the responder. In the
	 * first state, the responder sent a response to the request, containing
	 * AGREE or REFUSE. In the second state, the responder should optionally
	 * reply with and INFORM containing a message informing that some task
	 * was completed. This default implementation does nothing and this method
	 * should be overridden.
	 * @param request2
	 * @param response2
	 * @return
	 */
	protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
		return null;
	}

	private enum State implements FSM<AchieveREResponder> {

		/**
		 * Initially, a REQUEST is expected. Sends a REPLY to
		 * the request and the state changes to INFORM.
		 */
		RESPONSE {
			@Override
			public State nextState(ACLMessage m, AchieveREResponder re) {
				re.request = m;
				ACLMessage response = re.handleRequest(m);
				re.myAgent.send(response);
				return REPLY;
			}

			@Override
			public void setTemplate(MessageTemplate t, AchieveREResponder re) {
				ArrayList<Integer> performatives = new ArrayList<Integer>();
				performatives.add(ACLMessage.REQUEST);
				performatives.add(ACLMessage.REQUEST_WHEN);
				performatives.add(ACLMessage.REQUEST_WHENEVER);
				performatives.add(ACLMessage.QUERY_IF);
				performatives.add(ACLMessage.QUERY_REF);
				t.setPerformatives(performatives);
			}

			@Override
			public State nextState(AchieveREResponder behaviour) {
				return RESPONSE;
			}
		}, 

		/**
		 * Is not expecting a message. Sends an Inform when a task ends.
		 */
		REPLY {
			@Override
			public State nextState(ACLMessage m, AchieveREResponder re) {
				re.myAgent.addMail(m); // This message was not parsed. Back to the mail box.
				return nextState(re);
			}

			@Override
			public void setTemplate(MessageTemplate t, AchieveREResponder re) {
				t = new MessageTemplate();
				t.addPerformative(-1);
			}

			@Override
			public State nextState(AchieveREResponder re) {
				ACLMessage result = re.prepareResultNotification(re.request, re.response);
				re.myAgent.send(result);

				// Reset the responder0
				return RESPONSE;
			}
		};
	}

}
