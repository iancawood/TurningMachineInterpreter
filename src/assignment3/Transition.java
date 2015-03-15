package assignment3;

public class Transition {
	public int nextStateSymbol;
	public char outputSymbol;
	public char headInstruction;
	
	public Transition(int nextState, char output, char head) {
		nextStateSymbol = nextState;
		outputSymbol = output;
		headInstruction = head;
	}
}
