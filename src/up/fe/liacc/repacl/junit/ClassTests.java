package up.fe.liacc.repacl.junit;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedList;

import org.junit.Test;

import up.fe.liacc.repacl.IAgent;
import up.fe.liacc.repacl.MailBox;
import up.fe.liacc.repacl.acl.ACL;
import up.fe.liacc.repacl.acl.ACLMessage;
import up.fe.liacc.repacl.core.DF;


public class ClassTests {

	@Test
	public void test() {
		
		MyAgent agent1 = new MyAgent();
		MyAgent agent2 = new MyAgent();
		
		ACLMessage message = new ACLMessage(ACL.INFORM);
		message.setSender(agent1);
		message.setReceiver(agent2);
		
		DF.registerAgent(agent1);
		DF.registerAgent(agent2);
		
		
	}
	
	class MyAgent implements IAgent {
		
		private int aid;
		private MailBox mailBox;
		HashMap<Class<?>, LinkedList<ACLMessage>> messages;
		
		public LinkedList<ACLMessage> getQueue(Class<?> c) {
			if (messages == null) {
				messages = new HashMap<Class<?>, LinkedList<ACLMessage>>();
			}
			if (!messages.containsKey(c)) {
				messages.put(c, new LinkedList<ACLMessage>());
			}
			
			return messages.get(c);
		}

		@Override
		public void setAID(int aid) {
			this.aid = aid;
		}
		
		@Override
		public void addMail(ACLMessage message) {
			System.out.println("Received: " + message.getPerformative());
			getQueue(message.getClass()).add(message);
		}
		
		@Override
		public int getAID() {
			return aid;
		}
	};

}
