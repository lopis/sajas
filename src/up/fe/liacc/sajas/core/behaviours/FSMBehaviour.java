package up.fe.liacc.sajas.core.behaviours;

import java.util.HashMap;
import java.util.Map;

import up.fe.liacc.sajas.core.Agent;

/**
 * Common superclass of all state-machine-based protocols.
 * The children of this behaviour are expected to 
 * @author joaolopes
 *
 */
public abstract class FSMBehaviour extends Behaviour {

	Map<String, Behaviour> states = new HashMap<String, Behaviour>();
	Map<String, Map<Integer, String>> transitions = new HashMap<String, Map<Integer,String>>();
	Map<String, String> defaultTransitions = new HashMap<String, String>();
	
	protected String currentState;
	protected int currentResult;
	private String lastState;
	
	public FSMBehaviour(Agent a) {
		super(a);
	}
	
	@Override
	public void action() {
		if (states.get(currentState) != null)
			scheduleNext(currentResult); // Updates the current state
		else return;
			
		if (states.get(currentState) != null)
			currentResult = states.get(currentState).onEnd();
		
	}
	
	private void scheduleNext(int currentResult) {
		states.get(currentState).action();
	}

	/**
	 * Register a new state in the FSM.
	 * @param state The behaviour representing the state
	 * @param name The identifier of the state
	 */
	public void registerState(Behaviour state, String name) {
		states.put(name, state);
	}
	
	/**
	 * Remove a state from the FSM, as well as
	 * all transitions from it. FIXME: remove 
	 * transition from other states to this one.
	 * @param name
	 */
	public void deregisterState(String name) {
		states.remove(name);
		transitions.remove(name);
	}
	
	public void registerFirstState(Behaviour state, String name) {
		states.put(name, state);
		currentState = name;
	}
	
	public void registerLastState(Behaviour state, String name) {
		states.put(name, state);
		lastState = name;
	}
	
	/**
	 * Register a new transition from `currentState` to `nextState` when the
	 * result of returned by the `onEnd()` method of the last state was `event`.
	 * @param currentState The name of the current state
	 * @param nextState The name of the state this transition leads to
	 * @param event The value returns by `onEnd()` that triggers this transition.
	 */
	public void registerTransition(String currentState, String nextState, int event) {
		if (!transitions.containsKey(currentState)) {
			transitions.put(currentState, new HashMap<Integer, String>());
		}
		transitions.get(currentState).put(event, nextState);
	}
	
	public void registerDefaultTransition(String currentState, String nextState) {
		defaultTransitions.put(currentState, nextState);
	}
	
	/**
	 * Removes a transition from this FSM.
	 * @param source
	 * @param event
	 */
	public void deregisterTransition(String source, int event) {
		if (transitions.containsKey(source)) {
			transitions.get(source).remove(event);
		}
	}
	
	public void setCurrentState(String nextState) {
		currentState = nextState;
	}
}
