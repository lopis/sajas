package up.fe.liacc.repacl;

import java.util.Collection;


public class ContextWrapper {
	
	/**
	 * The Repast context that contains all
	 * scheduled Repast objects.
	 */
	static Collection<Object> context;

	/**
	 * Set the context for the simulation.
	 * @param c The type is the interface "Collection" to 
	 * preserve the independence from the Repast library.
	 * @throws IllegalArgumentException The type of the parameter must
	 * be "Context"
	 */
	public static void setContext(Collection<Object> c) {
		context = c;
	}
	
	/**
	 * @return Returns the wrapper.
	 */
	public static Collection<Object> getContext() {
		return context;
	}

}
