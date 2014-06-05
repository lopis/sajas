package up.fe.liacc.sajas.lang.acl;

import java.util.ArrayList;

/**
 * A template for matching incoming messages.
 * To use create a template, simply use the constructor to provide the
 * required filters. Alternatively, use the default constructor and
 * then the setters for each of these fields:
 * <li>performative
 * <li>protocol
 * <li>reply-with
 * <li>in-reply-to
 * <li>content
 * When creating a template, some fields can be left null. Null fields
 * won't be considered when matching a message.
 * 
 * @author joaolopes
 *
 */
public class MessageTemplate {
	
	/*
	 * The following lists are used for matching message fields.
	 * A message template allows each field to have more than
	 * one possible value.
	 */
	private ArrayList<Integer> performatives = new ArrayList<Integer>();
	private ArrayList<String> protocols = new ArrayList<String>();
	private ArrayList<Object> contents = new ArrayList<Object>();
	private ArrayList<String> replyWiths = new ArrayList<String>();
	private ArrayList<String> inReplyTos = new ArrayList<String>();
	private ArrayList<String> conversationIds = new ArrayList<String>();

	public ArrayList<Integer> getPerformatives() {
		return performatives;
	}

	public void setPerformatives(ArrayList<Integer> performatives) {
		this.performatives = performatives;
	}

	public ArrayList<String> getProtocols() {
		return protocols;
	}

	public void setProtocols(ArrayList<String> protocols) {
		this.protocols = protocols;
	}

	public ArrayList<Object> getContents() {
		
		return contents;
	}

	public void setContents(ArrayList<Object> contents) {
		this.contents = contents;
	}

	public ArrayList<String> getReplyWiths() {
		return replyWiths;
	}

	public void setReplyWiths(ArrayList<String> replyWiths) {
		this.replyWiths = replyWiths;
	}

	public ArrayList<String> getInReplyTos() {
		return inReplyTos;
	}

	public ArrayList<String> getConversationIds() {
		return conversationIds;
	}

	public void setConversationIds(ArrayList<String> conversationIds) {
		this.conversationIds = conversationIds;
	}

	public void setInReplyTos(ArrayList<String> inReplyTos) {
		this.inReplyTos = inReplyTos;
	}

	public boolean matchesPerformative(int performative) {
		return getPerformatives().isEmpty()
		|| getPerformatives().contains(performative);
	}

	public boolean matchesProtocol(String protocol) {
		return getProtocols().isEmpty()
		|| getProtocols().contains(protocol);
	}

	public boolean matchesInReplyTo(String inReplyTo) {
		return getInReplyTos().isEmpty()
		|| getInReplyTos().contains(inReplyTo);
	}

	public boolean matchesReplyWith(String replyWith) {
		return getReplyWiths().isEmpty()
		|| getReplyWiths().contains(replyWith);
	}

	public boolean matchesContent(Object content) {
		return getContents().isEmpty()
		|| getContents().contains(content);
	}
	
	public boolean matchesConversationIds(String conversationId) {
		return getConversationIds().isEmpty()
		|| getConversationIds().contains(conversationId);
	}

	public void addProtocol(String protocol) {
		if (!getProtocols().contains(protocol)) {
			getProtocols().add(protocol);
		}
	}
	
	public void addPerformative(int performative) {
		if (!getPerformatives().contains(performative)) {
			getPerformatives().add(performative);
		}
	}

	public static MessageTemplate MatchProtocol(String protocol) {
		MessageTemplate mt = new MessageTemplate();
		mt.addProtocol(protocol);
		return mt;
	}

	public static MessageTemplate MatchPerformative(int performative) {
		MessageTemplate mt = new MessageTemplate();
		mt.addPerformative(performative);
		return mt;
	}

	public void addConversationId(String conversationId) {
		if (!getConversationIds().contains(conversationId))
			getConversationIds().add(conversationId);
	}

	public static MessageTemplate MatchConversationId(String conversationId) {
		MessageTemplate mt = new MessageTemplate();
		mt.addConversationId(conversationId);
		return mt;
	}

	public static MessageTemplate or(MessageTemplate mt1, MessageTemplate mt2) {
		MessageTemplate mt3 = new MessageTemplate();
		
		mt3.getPerformatives().addAll(mt1.getPerformatives());
		mt3.getPerformatives().addAll(mt2.getPerformatives());

		mt3.getProtocols().addAll(mt1.getProtocols());
		mt3.getProtocols().addAll(mt2.getProtocols());

		mt3.getConversationIds().addAll(mt1.getConversationIds());
		mt3.getConversationIds().addAll(mt2.getConversationIds());
		
		//TODO: copy the rest of the things.
		
		return mt3;
	}
}
