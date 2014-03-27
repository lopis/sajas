package up.fe.liacc.repacl;

import java.util.LinkedList;

import up.fe.liacc.repacl.acl.ACLMessage;

/**
 * This class stores all mail addressed to one agent.
 * @author joaolopes
 *
 */
public class MailBox {
	
	LinkedList<ACLMessage> mail;
	
	public MailBox() {
		mail = new LinkedList<ACLMessage>();
	}

	/**
	 * Adds a new message to the mail box. 
	 * @param message
	 */
	public void addMail(ACLMessage message) {
		mail.add(message);
	}
	
	/**
	 * 
	 * @param template 
	 * @return Returns the messages that match the template.
	 */
	public LinkedList<ACLMessage> getMail(ACLMessage template) {
		LinkedList<ACLMessage> matchingMail = new LinkedList<ACLMessage>();
		for (int i = 0; i < mail.size(); i++) {
			if (mail.get(i).match(template)) {
				matchingMail.add(mail.get(i));
			}
		}
		
		return matchingMail;
	}
	
	/**
	 * @return Returns all mail in this box
	 */
	public LinkedList<ACLMessage> getMail() {	
		return mail;
	}
	
	/**
	 * Removes all mail from the mail box, including all
	 * indexes.
	 */
	public void clearMailBox() {
		mail.clear();
	}
	
	
}
