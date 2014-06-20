package up.fe.liacc.sajas.lang.acl;

/**
* Signals that an error occured during the decoding of the content 
* of an ACLMessage using Base64. 
*
* @author Tiziana Trucco - CSELT S.p.A.
* @version $Date: 2000-09-12 15:24:08 +0200 (mar, 12 set 2000) $ $Revision: 1857 $
*/

public class UnreadableException extends Exception {
/**
	 * 
	 */
	private static final long serialVersionUID = -120763548135198677L;

/**
* Constructs an <code>UnreadableException</code> with the specified detail
* message.
* @param the detail message.
*/
  public UnreadableException(String msg) {
    super(msg);
  }

}
