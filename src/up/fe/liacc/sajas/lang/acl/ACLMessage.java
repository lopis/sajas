package up.fe.liacc.sajas.lang.acl;

import java.io.Serializable;
import java.util.ArrayList;

import up.fe.liacc.sajas.core.AID;
import up.fe.liacc.sajas.domain.FIPANames;

/**
 * This is the generic class for all types of messages.
 * The type of protocol is set in the field "
 * 
 * @author joaolopes
 *
 */
public class ACLMessage {
	
	final static public int ACCEPT_PROPOSAL = 1;
	final static public int AGREE = 2;
	final static public int CANCEL = 3;
	final static public int CFP = 4;
	final static public int CONFIRM = 5;
	final static public int DISCONFIRM = 6;
	final static public int FAILURE = 7;
	final static public int INFORM = 8;
	final static public int INFORM_IF = 9;
	final static public int INFORM_REF = 10;
	final static public int NOT_UNDERSTOOD = 11;
	final static public int PROPAGATE = 12;
	final static public int PROPOSE = 13;
	final static public int PROXY = 14;
	final static public int QUERY_IF = 15;
	final static public int QUERY_REF = 16;
	final static public int REFUSE = 17;
	final static public int REJECT_PROPOSAL = 18;
	final static public int REQUEST = 19;
	final static public int REQUEST_WHEN = 20;
	final static public int REQUEST_WHENEVER = 21;
	final static public int SUBSCRIBE = 22;
	final static public int NO_PERFORMATIVE = -1;
	
	private int performative = NO_PERFORMATIVE;	// The intent of the message
	private String protocol = FIPANames.InteractionProtocol.FIPA_REQUEST;	// The intent of the message defaults to REQUEST
	private Serializable contentObject; // Any object can be attached to the message
	private AID sender; // The sender must be set so the receiver can reply
	private ArrayList<AID> receivers;
	private String replyWith; // Tag to identify a "thread" of communication
	private String inReplyTo; // This value comes from "replyWith"
	private long when = 0; // Deadline for the response.
	private StringBuffer contentString;
	public static final long NO_WHEN = 0;
	
	/**
	 * Creates a new ACL Message. The fields replyWith and inReplyTo are not
	 * needed, but all other should be not null.
	 * @param performative A valid ACL_* value
	 */
	public ACLMessage(int performative) {
		this.setPerformative(performative);
	}
	
	/**
	 * Communication performative represents the intent of the message.
	 * The value of this field is one of the following:
	 * <li>ACL_ACCEPT_PROPOSAL</li>
	 * <li>ACL_AGREE</li>
	 * <li>ACL_CANCEL</li>
	 * <li>ACL_CALL_FOR_PROPOSAL</li>
	 * <li>ACL_CONFIRM</li>
	 * <li>ACL_DISCONFIRM</li>
	 * <li>ACL_FAILURE</li>
	 * <li>ACL_INFORM</li>
	 * <li>ACL_INFORM_IF</li>
	 * <li>ACL_INFORM_REF</li>
	 * <li>ACL_NOT_UNDERSTOOD</li>
	 * <li>ACL_PROPAGATE</li>
	 * <li>ACL_PROPOSE</li>
	 * <li>ACL_PROXY</li>
	 * <li>ACL_QUERY_IF</li>
	 * <li>ACL_QUERY_REF</li>
	 * <li>ACL_REFUSE</li>
	 * <li>ACL_REJECT_PROPOSAL</li>
	 * <li>ACL_REQUEST</li>
	 * <li>ACL_REQUEST_WHEN</li>
	 * <li>ACL_REQUEST_WHENEVER</li>
	 * <li>ACL_SUBSCRIBE</li>
	 * @return The performative
	 */
	public int getPerformative() {
		return performative;
	}
	
	/**
	 * Sets the performative of this message. The value defaults to 
	 * ACL_NO_PERFORMATIVE if the param "performative" is not valid.
	 * @param performative
	 */
	public void setPerformative(int performative) {
		this.performative = 
				(performative > 0 && performative <= SUBSCRIBE)
				? performative
				: NO_PERFORMATIVE;
	}
	
	/**
	 * Returns the protocol to which this message belongs.
	 * @return The protocol is represented 
	 */
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	/**
	 * Returns the content of this Message in the form of a string.
	 * This string may be null.
	 * @return
	 */
	public String getContent() {
		if(contentString != null) {
			return new String(contentString);
		} else { 
			return null;
		}
	}

	/**
	 * @return Returns the content of this message. This object can be null.
	 */
	public Serializable getContentObject() {
		return contentObject;
	}
	
	/**
	 * Attaches an object to this message. This message's content
	 * can be left null.
	 * @param content
	 */
	public void setContentObject(Serializable content) {
		this.contentObject = content;
	}
	
	/**
	 * @return Reference to the issuer of this message.
	 */
	public AID getSender() {
		return sender;
	}
	
	/**
	 * Sets the issuer if this message.
	 * @param sender
	 */
	public void setSender(AID sender) {
		this.sender = sender;
	}
	
	/**
	 * @return Reference to the receiver of this message.
	 */
	public ArrayList<AID> getReceivers() {
		if (receivers == null) {
			receivers = new ArrayList<AID>();
		}
		return receivers;
	}

	protected void setReceivers(ArrayList<AID> receivers) {
		this.receivers = receivers;
	}
	
	/**
	 * Sets the receiver of this message.
	 * @param receiver
	 */
	public void addReceiver(AID receiver) {
		getReceivers().add(receiver);
	}

	/**
	 * @return An identifier for this communication.
	 * If this field is set, the responder should use it
	 * to set the value of "inReplyTo" in the response.
	 */
	public String getReplyWith() {
		return replyWith;
	}

	/**
	 * @param replyWith An identifier for this communication.
	 * If this field is set, the responder should use it
	 * to set the value of "inReplyTo" in the response.
	 */
	public void setReplyWith(String replyWith) {
		this.replyWith = replyWith;
	}

	/**
	 * @return An identifier for this communication.
	 * The value of this field should be the value
	 * received in the "replyWith" field in the request.
	 */
	public String getInReplyTo() {
		return inReplyTo;
	}

	/**
	 * @param inReplyTo An identifier for this communication.
	 * The value of this field should be the value
	 * received in the "replyWith" field in the request.
	 */
	public void setInReplyTo(String inReplyTo) {
		this.inReplyTo = inReplyTo;
	}

	/**
	 
	 * @return Deadline for the response.
	 * The time is formatted as unix time, in seconds. //FIXME: confirm if seconds or millis
	 */
	public long getWhen() {
		return when;
	}

	/**
	 * 
	 * @param when Deadline for the response.
	 * The time is formatted as unix time, in seconds. //FIXME: confirm if seconds or millis
	 */
	public void setWhen(long when) {
		this.when = when;
	}
	
	/**
	 * This methods expects a "template", which is basically an ACLMessage
	 * with part or all of its fields set. All template's fields are compared
	 * to this message's. Null values on the template are ignored.
	 * The performative "null" value, since it's an integer, is "-1" or 
	 * ACL.NO_PERFORMATIVE.
	 * @param template
	 * @return True if all fields (expected those ignored) match the template's. 
	 */
	public boolean match(MessageTemplate template) {
		return template.matchesPerformative(this.getPerformative()) 
			&& template.matchesProtocol(this.getProtocol())
			&& template.matchesInReplyTo(this.getInReplyTo())
			&& template.matchesReplyWith(this.getReplyWith())
			&& template.matchesContent(this.getContentObject());
	}
	
	public ACLMessage clone() {
		ACLMessage newMessage = new ACLMessage(this.performative);
		newMessage.setContentObject(this.contentObject);
		newMessage.setInReplyTo(this.inReplyTo);
		newMessage.setPerformative(this.performative);
		newMessage.setProtocol(this.protocol);
		newMessage.setReplyWith(this.replyWith);
		newMessage.setSender(this.sender);
		newMessage.setWhen(this.when);
		
		newMessage.setReceivers(this.receivers);
		newMessage.setSender(this.sender);
		
		return newMessage;
	}

}
