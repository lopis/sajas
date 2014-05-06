package up.fe.liacc.repacl.proto;

import java.util.ArrayList;

import up.fe.liacc.repacl.core.Agent;
import up.fe.liacc.repacl.core.behaviours.Behaviour;
import up.fe.liacc.repacl.domain.FIPANames;
import up.fe.liacc.repacl.lang.acl.ACLMessage;
import up.fe.liacc.repacl.lang.acl.MessageTemplate;

public class AchieveREResponder extends Behaviour {

	protected MessageTemplate template;

	public AchieveREResponder(Agent agent) {
		super(agent);

		// Set the template that will filter the responses
		template = new MessageTemplate();
		ArrayList<String> protocols = new ArrayList<String>();
		
		//FIXME this shouldn't be a fixed value. This behaviour
		// supports more than one protocol.
		protocols.add(FIPANames.InteractionProtocol.FIPA_REQUEST);
		template.setProtocols(protocols );
	}

	@Override
	/**
	 * Schedule this method and call super(). This method
	 * must be overridden.
	 */
	public void action() {
		/*
		 * This method is scheduled in Repast.
		 * On each tick, do:
		 *  1 - Get one message matching the template
		 *  2 - Read the performative in the message
		 *  3 - Call the appropriate handler
		 *  5 - If wait list is empty, run the appropriate "handle all"
		 *  6 - Update the protocol state 
		 */
		ACLMessage nextMessage = this.getAgent().getMatchingMessage(template);
		if (nextMessage != null) {
			handleRequest(nextMessage);
		}
	}

	/**
	 * This method is called every tick if there is a message to process.
	 * This default implementation does nothing and should be overridden.
	 * @param nextMessage
	 */
	public void handleRequest(ACLMessage nextMessage) {}
	
	

}
