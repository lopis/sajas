package up.fe.liacc.repacl.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import up.fe.liacc.repacl.acl.ACLMessage;
import up.fe.liacc.repacl.acl.MessageTemplate;
import up.fe.liacc.repacl.acl.Performative;
import up.fe.liacc.repacl.acl.Protocol;

public class MessagingTest {

	/**
	 * Test the integrity of the template class
	 */
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
	
	
	@Test
	public void achieveRETest() {
		
	}
	

	public class TestSubject {
		public String name = "";
		public TestSubject(String name) {
			this.name = name;
		}
	}
	
	@Test
	public void hashMapTest(){
		/*
		 * In this test I want to verify
		 * if saving an object to multiple
		 * keys in a Map makes multiple copies
		 * of that object or just references.
		 */
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		TestSubject subjectA = new TestSubject("Subject A");
		
		ArrayList<TestSubject> testSubjectsArray1 = new ArrayList<TestSubject>();
		ArrayList<TestSubject> testSubjectsArray2 = new ArrayList<TestSubject>();
		testSubjectsArray1.add(subjectA);
		testSubjectsArray2.add(subjectA);

		
		map.put("Key 1", subjectA);
		map.put("Key 2", subjectA);
		((TestSubject) map.get("Key 2")).name = "Change Name";
		map.put("Key 3", testSubjectsArray1);
		map.put("Key 4", testSubjectsArray2);
		
		// Both are the same
		assertEquals(map.get("Key 1"), map.get("Key 2"));
		
		// Changing the name in the second object, changes it in the first one
		assertEquals(((TestSubject) map.get("Key 1")).name, "Change Name");
		
		// ArrayLists also use references
		assertTrue(map.get("Key 3").equals(map.get("Key 4")));
	}
}
