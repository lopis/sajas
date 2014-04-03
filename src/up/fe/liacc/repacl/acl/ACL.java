package up.fe.liacc.repacl.acl;

public class ACL {
	
	/* Protocols */
	
	final static public int FIPA_REQUEST = 1;
	final static public int FIPA_CONTRACT_NET = 2;
	final static public int FIPA_PROPOSE = 3;
	final static public int NO_PROTOCOL = -1;

	
	/* Performatives */
	
	final static public int ACCEPT_PROPOSAL = 1;
	final static public int AGREE = 2;
	final static public int CANCEL = 3;
	final static public int CALL_FOR_PROPOSAL = 4;
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
}
