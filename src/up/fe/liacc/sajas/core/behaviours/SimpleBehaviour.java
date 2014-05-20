package up.fe.liacc.sajas.core.behaviours;

import up.fe.liacc.sajas.core.Agent;

/**
 * An atomic behaviour. This abstract class models behaviours that are
 * made by a single, monolithic task and cannot be interrupted.
 * 
 * @athor Giovanni Rimassa - Universita` di Parma
 * @version $Date: 2000-10-09 09:03:44 +0200 (lun, 09 ott 2000) $ $Revision: 1919 $
 * 
 */
public abstract class SimpleBehaviour extends Behaviour {

	/**
	 * Default constructor. It does not set
	 * the owner agent for this behaviour.
  	 */
	public SimpleBehaviour() {
		super();
	}

	/**
	 * This constructor sets the owner agent for this behaviour.
	 * @param a The agent this behaviour belongs to.
	 */
	public SimpleBehaviour(Agent a) {
		super(a);
	}    

	/**
	 * Resets a <code>SimpleBehaviour</code>. 
	 */
	public void reset() {
		super.reset();
	}
}


