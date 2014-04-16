package up.fe.liacc.repacl;

import java.util.LinkedList;

import up.fe.liacc.repacl.acl.ACLMessage;
import up.fe.liacc.repacl.acl.MessageTemplate;

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
	 * @return Returns all mail in this box
	 */
	public LinkedList<ACLMessage> getMail() {
		LinkedList<ACLMessage> messages = (LinkedList<ACLMessage>) mail.clone();
		mail.clear();
		return messages;
	}
	
	/**
	 * Removes all mail from the mail box, including all
	 * indexes.
	 */
	public void clearMailBox() {
		mail.clear();
	}

	/**
	 * Returns the first matching mail the 
	 * @param template
	 * @return
	 */
	public ACLMessage getMatchingMessage(MessageTemplate template) {
		//FIXME: create mail hash table with templates.
		// Then, see if the template already exists
		// If yes, pop the first message in that list!
		// If not, try searching the messages that don't belong to any template.
		
		// Quick and dirty search:
		for (ACLMessage message : mail) {
			if (message.match(template)) {
				return message;
			}
		}
		
		return null;
	}
	
	
}
