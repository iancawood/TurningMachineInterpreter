package assignment3;

// This object represents a transition and exists strictly to group data
public class Transition {
	public int nextStateNumber;
	public char outputSymbol;
	public char headInstruction;
	
	public Transition(int nextState, char output, char head) {
		nextStateNumber = nextState;
		outputSymbol = output;
		headInstruction = head;
	}
}
