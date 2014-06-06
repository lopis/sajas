package up.fe.liacc.sajas.core;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import up.fe.liacc.sajas.lang.acl.ACLMessage;
import up.fe.liacc.sajas.lang.acl.MessageTemplate;

/**
 * This class stores all mail addressed to one agent.
 * @author joaolopes
 *
 */
public class MessageQueue {
	
	LinkedList<ACLMessage> messages;
	
	public MessageQueue() {
		messages = new LinkedList<ACLMessage>();
	}

	/**
	 * Adds a new message to the mail box. 
	 * @param message
	 */
	public void add(ACLMessage message) {
		messages.add(message);
		Collections.shuffle(messages);
	}

	
//	/**
//	 * @return Returns all mail in this box. The mail
//	 * box is NOT cleared when accessed this way.
//	 */
//	public LinkedList<ACLMessage> getMail() {
//		return mail;
//	}
	
	/**
	 * Removes all mail from the mail box, including all
	 * indexes.
	 */
	public void clearMailBox() {
		messages.clear();
	}

	/**
	 * Uses a template to retrieve the first matching message from
	 * the queue. This message will be then removed from the box.
	 * @param template
	 * @return
	 */
	public ACLMessage receive(MessageTemplate template) {
		//FIXME: create mail hash table with templates.
		// Then, see if the template already exists
		// If yes, pop the first message in that list!
		// If not, try searching the messages that don't belong to any template.
		
		// Quick and dirty search:
		for (ACLMessage message : messages) {
			if (message.match(template)) {
				messages.remove(message);
				return message;
			}
		}
		
		return null;
	}
	
	/**
	 * Copy all messages to a given list.
	 * A non generic Collection type is used for JADE compatibility.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void copyTo(Collection list) {
		for (Iterator iterator = messages.iterator(); iterator.hasNext();) {
			ACLMessage message = (ACLMessage) iterator.next();
			list.add(message.clone());
		}
	}

	public boolean isEmpty() {
		return messages.isEmpty();
	}
	
}
