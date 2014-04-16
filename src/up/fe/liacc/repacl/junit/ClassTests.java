package up.fe.liacc.repacl.junit;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedList;

import org.junit.Test;

import up.fe.liacc.repacl.Agent;
import up.fe.liacc.repacl.MailBox;
import up.fe.liacc.repacl.acl.Performative;
import up.fe.liacc.repacl.acl.ACLMessage;
import up.fe.liacc.repacl.core.DF;


public class ClassTests {

	@Test
	public void test() {
		
		MyAgent agent1 = new MyAgent();
		MyAgent agent2 = new MyAgent();
		
		ACLMessage message = new ACLMessage(Performative.INFORM);
		message.setSender(agent1);
		message.addReceiver(agent2);
		DF.registerAgent(agent1);
		DF.registerAgent(agent2);
		
		agent1.talk();
	}
	
	class MyAgent extends Agent {

		public void talk() {
			
		}
		
	};

}
