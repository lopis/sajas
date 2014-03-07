package up.fe.liacc.repacl;

public class ACLMessage {
	
	final static public int ACL_ACCEPT_PROPOSAL = 1;
	final static public int ACL_AGREE = 2;
	final static public int ACL_CANCEL = 3;
	final static public int ACL_CALL_FOR_PROPOSAL = 4;
	final static public int ACL_CONFIRM = 5;
	final static public int ACL_DISCONFIRM = 6;
	final static public int ACL_FAILURE = 7;
	final static public int ACL_INFORM = 8;
	final static public int ACL_INFORM_IF = 9;
	final static public int ACL_INFORM_REF = 10;
	final static public int ACL_NOT_UNDERSTOOD = 11;
	final static public int ACL_PROPAGATE = 12;
	final static public int ACL_PROPOSE = 13;
	final static public int ACL_PROXY = 14;
	final static public int ACL_QUERY_IF = 15;
	final static public int ACL_QUERY_REF = 16;
	final static public int ACL_REFUSE = 17;
	final static public int ACL_REJECT_PROPOSAL = 18;
	final static public int ACL_REQUEST = 19;
	final static public int ACL_REQUEST_WHEN = 20;
	final static public int ACL_REQUEST_WHENEVER = 21;
	final static public int ACL_SUBSCRIBE = 22;
	
	final static public int ACL_NO_PERFORMATIVE = -1;
	
	
	private int performative = ACL_NO_PERFORMATIVE;
	private Object content;
	private RepACLCom sender;
	private RepACLCom receiver;
	private String replyWith;
	private String inReplyTo;
	
	/**
	 * Creates a new ACL Message. The fields replyWith and inReplyTo are not
	 * needed, but all other should be not null.
	 * @param performative A valid ACL_* value
	 * @param sender Reference to the agent that sent this message
	 * @param receiver Reference to the agent that will receive this message
	 * @param replyWith The sender can include a tag or ID to this message,
	 * so it can identify the reply in case of assynchronous comunication.
	 * @param inReplyTo The responder can fill this field to let the original
	 * sender identify which request is being replied to.
	 */
	public ACLMessage(int performative, RepACLCom sender, RepACLCom receiver,
			String replyWith, String inReplyTo) {
		this.performative = performative;
		this.sender = sender;
		this.receiver = receiver;
		this.replyWith = replyWith;
		this.inReplyTo = inReplyTo;
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
				(performative > 0 && performative <= ACL_SUBSCRIBE)
				? performative
				: ACL_NO_PERFORMATIVE;
	}
	
	/**
	 * Returns the content of this message. This object can be null.
	 * @return
	 */
	public Object getContent() {
		return content;
	}
	
	/**
	 * Attaches an object to this message. This message's content
	 * can be left null.
	 * @param content
	 */
	public void setContent(Object content) {
		this.content = content;
	}
	
	/**
	 * @return Reference to the issuer of this message.
	 */
	public RepACLCom getSender() {
		return sender;
	}
	
	/**
	 * Sets the issuer if this message.
	 * @param sender
	 */
	public void setSender(RepACLCom sender) {
		this.sender = sender;
	}
	
	/**
	 * @return Reference to the receiver of this message.
	 */
	public RepACLCom getReceiver() {
		return receiver;
	}
	
	/**
	 * Sets the receiver of this message.
	 * @param receiver
	 */
	public void setReceiver(RepACLCom receiver) {
		this.receiver = receiver;
	}

}
