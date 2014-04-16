package up.fe.liacc.repacl.acl;

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
	private ArrayList<Integer> performatives;
	private ArrayList<Integer> protocols;
	private ArrayList<Object> contents;
	private ArrayList<String> replyWiths;
	private ArrayList<String> inReplyTos;

	public ArrayList<Integer> getPerformatives() {
		return performatives;
	}

	public void setPerformatives(ArrayList<Integer> performatives) {
		this.performatives = performatives;
	}

	public ArrayList<Integer> getProtocols() {
		if (protocols == null) {
			protocols = new ArrayList<Integer>();
		}
		return protocols;
	}

	public void setProtocols(ArrayList<Integer> protocols) {
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

	public void setInReplyTos(ArrayList<String> inReplyTos) {
		this.inReplyTos = inReplyTos;
	}

	public boolean matchesPerformative(int performative) {
		return getPerformatives() == null
		|| getPerformatives().contains(performative);
	}

	public boolean matchesProtocol(int protocol) {
		return getProtocols() == null
		|| getProtocols().contains(protocol);
	}

	public boolean matchesInReplyTo(String inReplyTo) {
		return getInReplyTos() == null
		|| getInReplyTos().contains(inReplyTo);
	}

	public boolean matchesReplyWith(String replyWith) {
		return getReplyWiths() == null
		|| getReplyWiths().contains(replyWith);
	}

	public boolean matchesContent(Object content) {
		return getContents() == null
		|| getContents().contains(content);
	}

	public void addProtocol(Integer protocol) {
		getProtocols().add(protocol);
	}

}
