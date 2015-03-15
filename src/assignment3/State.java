package assignment3;

import java.util.HashMap;

public class State {
	public boolean isFinalState = false;
	int stateNumber;
	HashMap<Character, Transition> transitions = new HashMap<Character, Transition>();
	
	public State(int stateNum) {
		stateNumber = stateNum;
	}
	
	public State(int stateNum, boolean finalState) {
		stateNumber = stateNum;
		isFinalState = finalState;		
	}
	
	void addTransition(char inputSymbol, int nextState, char outputSymbol, char headInstruction) {
		transitions.put(inputSymbol, new Transition(nextState, outputSymbol, headInstruction));
	}
	
	Transition transition(char inputSymbol) {
		return transitions.get(inputSymbol);		
	}

}
