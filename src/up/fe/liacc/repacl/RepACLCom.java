package up.fe.liacc.repacl;

import java.util.LinkedList;

import repast.simphony.engine.schedule.ScheduledMethod;

/**
 * Agents that wish to communicate using ACL can use this class'
 * methods. This class contains a queue of messages from other agents.
 * On each tick, the method step() is executed and all the message in the
 * queue are sent.
 * There are three kind of communcative acts in this class: 
 * <li> Request </li>
 * <li> Propose </li>
 * <li> Contract Net </li>
 * Further details about the communication act are contained in the ACLMessage 
 * class, like its performative and content.
 * @author joaolopes
 *
 */
public class RepACLCom {

	/**
	 * Queue with all the messages. Should be empty on after each step().
	 */
	private LinkedList<ACLMessage> messageQueue;
	
	private static RepACLCom instance;

	
	/**
	 * This is a singleton class, so the constructor is private.
	 */
	private RepACLCom() {
		
	}
	
	public static RepACLCom getInstance() {
		if (instance == null) {
			instance = new RepACLCom();
		}
		return instance;
	}

	/**
	 * 
	 */
	
	public void initiateRequest(ACLMessage message) {

	}

	/**
	 * 
	 */
	public void initiatePropose(ACLMessage message) {

	}

	/**
	 * 
	 */
	public void initiateContractNet(ACLMessage message) {

	}
	
	@ScheduledMethod
	public void step() {
		
	}
}
