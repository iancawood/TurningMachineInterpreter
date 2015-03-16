package assignment3;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.StringBuilder;

public class TuringMachineInterpreter {
	
	HashMap<Integer, State> states = new HashMap<Integer, State>();
	ArrayList<String> inputTapes = new ArrayList<String>();
	ArrayList<String> outputTapes = new ArrayList<String>();
	
	static String inputPath = ""; // the path to the file where data is read from
	static String outputPath = ""; // the path to the file where data is written to
	
	public static void main(String[] args) {
		System.out.println("Welcome to my Turing Machine Interpreter");
		
		// validate bash arguments
		if (args.length != 2) {
			System.out.println("Invalid number of arguments.");

		} else {
			System.out.println("Computing...");
			
			inputPath = args[0];
			outputPath = args[1];
			
			TuringMachineInterpreter interpreter = new TuringMachineInterpreter();
			
			interpreter.input(inputPath); // builds a turning machine in accords to the input file
			
			System.out.println("Turning machine created. Beginning to execute input tapes...");
			
			for (String tape: interpreter.inputTapes) { // execute all input tapes
				if (tape.equals("Z#Z")) {
					System.out.println("here");
				}				
				
				StringBuilder output = new StringBuilder(tape);
				State currentState = interpreter.states.get(0);
				int headPosition = 1; // this is 1, not 0 because I added "Z" to the front of the tape
				
				while (true) {				
					Transition transition = currentState.transition(output.charAt(headPosition)); // read the tape and get the appropriate transition
					
					if (transition == null) { // no valid transition exists
						// reject and halt
						interpreter.outputTapes.add(output.substring(1, output.length()-1));
						interpreter.outputTapes.add("REJECT");
						System.out.println("REJECT: " + tape);
						break;
					} else {
						// write to output tape
						output.replace(headPosition, headPosition+1, Character.toString(transition.outputSymbol));
					}
					
					currentState = interpreter.states.get(transition.nextStateNumber); // transition to new state
					
					// execute head instructions
					if (transition.headInstruction == 'R') { // move right
						headPosition++;
					} else if (transition.headInstruction == 'L') { // move left
						headPosition--;
					} else if (transition.headInstruction == 'H') { // halt
						interpreter.outputTapes.add(output.substring(1, output.length()-1));
						if (currentState.isFinalState) {
							interpreter.outputTapes.add("ACCEPT");
							System.out.println("ACCEPT: " + tape);
						} else {
							interpreter.outputTapes.add("REJECT");
							System.out.println("REJECT: " + tape);
						}					
						break;
					}
					
				} 
			}
			
			interpreter.output(outputPath);
			
			System.out.println("Done.");
		}
	}
	
	// this function opens the input file and constructs a turning machine accordingly
	void input(String path) {
		try {
			Scanner scanner = new Scanner(new File(path));
			
			while (scanner.hasNext()) {
				String[] tuple = scanner.nextLine().split(" "); // parse line by removing all white space
				
				switch(tuple[0].charAt(0)) {
				case 't': // transition
					newTransition(tuple);
					break;
					
				case 'f': // final state
					setFinalStates(tuple);
					break;
					
				case 'i': // input tape
					newInputTape(tuple);
					break;
					
				default: // something went wrong
					System.out.println("Unrecognized line in input file.");
					break;
				}
			}
			
			scanner.close();
		} catch (Exception e) {
			System.out.println("Failed to extract data from input file.");
		}
	}
	
	// this function opens the output file and writes all output tapes to it
	void output(String path) {
		try {
			PrintWriter writer = new PrintWriter(new File(path));	
			for (String s: outputTapes) {
				writer.println(s);
			}			
			writer.close();
		} catch (Exception e) {
			System.out.println("Failed to output data into file.");
		}
	}
	
	// create a new transition object with a state object
	void newTransition(String[] tuple) {
		// tuple[1] is the state number
		// tuple[2] is the input symbol
		// tuple[3] is the next state number
		// tuple[4] is the output symbol
		// tuple[5] is the head instructions
		
		int stateNumber = Integer.parseInt(tuple[1]);
		if (states.get(stateNumber) == null) { // check if state doesn't exists
			states.put(stateNumber, new State(stateNumber));
		}
		states.get(stateNumber).addTransition(
				tuple[2].charAt(0),
				Integer.parseInt(tuple[3]),
				tuple[4].charAt(0),
				tuple[5].charAt(0));
	}
	
	// mark the appropriate states to be accept states
	void setFinalStates(String[] tuple) {
		// tuple[i] is the state number of a final state
		for (int i = 1; i<tuple.length; i++) {
			int stateNumber = Integer.parseInt(tuple[i]);
			if (states.get(stateNumber) != null) { // check if state exists
				states.get(stateNumber).isFinalState = true;
			} else {
				states.put(stateNumber, new State(stateNumber, true));
			}
		}			
	}
	
	// parse a new input tape
	void newInputTape(String[] tuple) {	
		// tuple[1] is the input tape
		inputTapes.add("Z" + tuple[1] + "Z"); // *** IMPORTATNT *** Note the addition of the Z's to indicate the ends of the tape
	}
}
