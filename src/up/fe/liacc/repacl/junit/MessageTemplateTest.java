package up.fe.liacc.repacl.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import up.fe.liacc.repacl.acl.ACLMessage;
import up.fe.liacc.repacl.acl.MessageTemplate;
import up.fe.liacc.repacl.acl.Performative;
import up.fe.liacc.repacl.acl.Protocol;

public class MessageTemplateTest {

	@Test
	public void messageTemplateTest() {
		
		MessageTemplate template = new MessageTemplate();
		
		ArrayList<Integer> protocols = new ArrayList<Integer>();
		protocols.add(Protocol.FIPA_REQUEST);
		template.setProtocols(protocols);
		
		// This little message matches the template
		ACLMessage message1 = new ACLMessage(Performative.AGREE);
		message1.setProtocol(Protocol.FIPA_REQUEST);
		
		// This little message does not
		ACLMessage message2 = new ACLMessage(Performative.REJECT_PROPOSAL);
		message2.setProtocol(Protocol.FIPA_PROPOSE);
		
		assertTrue(message1.match(template));
		assertFalse(message2.match(template));
	}
}