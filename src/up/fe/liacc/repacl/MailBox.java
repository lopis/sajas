package up.fe.liacc.repacl;

import java.util.HashMap;
import java.util.LinkedList;

import up.fe.liacc.repacl.acl.ACLMessage;

/**
 * This class allows agents to subscribe and unsubscribe
 * to different types of communication.
 * @author joaolopes
 *
 */
public class MailBox {
	
	HashMap<Integer, LinkedList<ACLMessage>> queues;
	
	public MailBox() {
		queues = new HashMap<Integer, LinkedList<ACLMessage>>();
	}
	
	/**
	 * Start accepting messages of a protocol.
	 * Other messages are ignored.
	 * @param protocol
	 */
	public void subscribe(int protocol) {
		queues.put(protocol, new LinkedList<ACLMessage>());
	}

	/**
	 * Stop accepting messages of a protocol.
	 * Further messages will be ignored.
	 * @param protocol
	 */
	public void unsubscribe(int protocol) {
		queues.remove(protocol);
	}
	
	/**
	 * Retrieves pending mail for one protocol.
	 * The queue for this protocol will be emptied.
	 * @param protocolo
	 * @return
	 */
	public LinkedList<ACLMessage> getMail(int protocolo) {
		LinkedList<ACLMessage> mail = queues.get(protocolo);
		queues.put(protocolo, new LinkedList<ACLMessage>());
		return mail;
	}
}
