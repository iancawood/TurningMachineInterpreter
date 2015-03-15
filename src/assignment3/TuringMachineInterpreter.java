package assignment3;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

public class TuringMachineInterpreter {
	
	HashMap<Integer, State> states = new HashMap<Integer, State>();
	ArrayList<String> inputTapes = new ArrayList<String>();
	ArrayList<String> outputTapes = new ArrayList<String>();
	
	static String inputPath = "";
	static String outputPath = "";
	
	public static void main(String[] args) {
		System.out.println("Welcome to my Turing Machine Interpreter");
		
		// validate bash arguments
//		if (args.length == 2) {
//			inputPath = args[0];
//			outputPath = args[1];
//		} else {
//			System.out.println("Invalid number of arguments.");
//		}
		
		TuringMachineInterpreter interpreter = new TuringMachineInterpreter();
		
		interpreter.input("input.txt"); 
		
		for(String tape: interpreter.inputTapes) {
			for (int i = 0; i<tape.length(); i++) {
				System.out.println(tape.charAt(i));
				
				
			}
		}
		
//		interpreter.output("output.txt");		
		
	}
	
	void input(String path) {
		try {
			Scanner scanner = new Scanner(new File(path));
			
			while (scanner.hasNext()) {
				String[] tuple = scanner.nextLine().split(" ");
				
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
					
				default:
					System.out.println("Unrecognized line in input file.");
					break;
				}
			}
			
			scanner.close();
		} catch (Exception e) {
			System.out.println("Failed to extract data from input file.");
		}
	}
	
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
	
	void setFinalStates(String[] tuple) {
		// tuple[i] is the state number of a final state
		for (int i = 1; i<tuple.length; i++) {
			int stateNumber = Integer.parseInt(tuple[1]);
			if (states.get(stateNumber) != null) { // check if state exists
				states.get(stateNumber).isFinalState = true;
			} else {
				states.put(stateNumber, new State(stateNumber, true));
			}
		}			
	}
	
	void newInputTape(String[] tuple) {	
		// tuple[1] is the input tape
		inputTapes.add(tuple[1]);
	}
}
