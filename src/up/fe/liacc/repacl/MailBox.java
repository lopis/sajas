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
	 * @return Returns all mail in this box. The mail
	 * box is NOT cleared when accessed this way.
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

	/**
	 * Uses a template to retrieve a matching message from
	 * the mail box. This message will be then removed from
	 * the box.
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
				mail.remove(message);
				return message;
			}
		}
		
		return null;
	}
	
	
}
