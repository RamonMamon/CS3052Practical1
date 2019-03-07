import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class TuringMachine
{
    private static final String WHITESPACES = "\\s+";
    private static final char BLANK = '_';

    private List<Character> tape = new ArrayList<Character>();
    private List<Character> alphabet = new ArrayList<Character>();
    private Map<String,State> states = new HashMap<String, State>();
    private int numMoves;

    private State initialState;
    private State acceptState;
    private State rejectState;
    
    /**
     * Parses the Turing Machine Description File.
     */
    public void parseDescriptionFile(BufferedReader br) throws IOException, InputErrorException, RejectedTapeException
    {
        try
        {
            String line = br.readLine();
            if(line == null) throw new InputErrorException();

            String[] tokens = line.split(WHITESPACES);
            if(!tokens[0].equals("states") || tokens.length != 2) throw new InputErrorException();

            // Parses the States
            int numStates = Integer.parseInt(tokens[1]);
            for(int i = 0; i < numStates; i++)
            {
                // Add the states to the list.
                line = br.readLine();
                tokens = line.split(WHITESPACES);
                if(line == null || tokens.length > 2) throw new InputErrorException();

                State newState = new State(tokens[0]);
                
                if(tokens.length == 2)
                {
                    // Checks if the state is an accept or reject state.
                    String type = tokens[1];
                    if(type.equals("+"))
                        acceptState = newState;
                    else if (type.equals("-"))
                        rejectState = newState;
                    else throw new InputErrorException();
                }
                if(initialState == null) initialState = newState;
                states.put(newState.getName(), newState);
            }
            if(acceptState == null || rejectState == null) throw new InputErrorException();

            // Parse the alphabet 
            line = br.readLine();
            tokens = line.split(WHITESPACES);
            if(!tokens[0].equals("alphabet")) throw new InputErrorException();
            
            int alphabetSize = Integer.parseInt(tokens[1]);
            for(int i = 2; i < tokens.length; i ++)
            {
                // Adds symbols to the alphabet
                alphabet.add(checkLength(tokens[i]).charAt(0));
            }
            if(alphabetSize != alphabet.size() || alphabetSize == 0) throw new InputErrorException();

            line = br.readLine();

            // If there are no transition, the machine is then put in a reject state.
            if (line == null) throw new RejectedTapeException();

            // Parse Transitions
            while(line != null)
            {
                // Adds the transitions to their respective states.
                if(line.trim().length() == 0){
                    // Skips lines that are only spaces.
                    line = br.readLine();
                    continue;
                }
                tokens = line.split(WHITESPACES);
                String state = tokens[0];
                if(state.equals(acceptState.getName()) || state.equals(rejectState.getName()) || tokens.length != 5)
                {
                    throw new InputErrorException();
                }

                State currentState = getState(state);

                // Saves the new transition.
                char inputChar = getChar(tokens[1]);
                String nextState = getState(tokens[2]).getName();
                
                char outputChar = getChar(tokens[3]);
                char move = checkLength(tokens[4]).charAt(0);
                if(move != 'R' && move != 'L') throw new InputErrorException();
                currentState.addTransition(inputChar, nextState, outputChar, move);
                
                line = br.readLine();
            }
        }catch(NumberFormatException e){
            // Catches any errors in formatting when dealing with numbers
            throw new InputErrorException();
        }
    }

    /**
     * Parses the Input tape and converts values on it according to the Description file.
     */
    public void parseInputTape(BufferedReader br) throws IOException, InputErrorException, RejectedTapeException
    {
        if(br == null) return;
        String line = br.readLine();
        while(line != null)
        {
            // Removes all whitespaces from the input.
            line = line.replaceAll(WHITESPACES, "");
            for(int i = 0; i < line.length(); i++)
            {
                // Adds characters to the tape.
                char currentChar = line.charAt(i);
                if(!alphabetContains(currentChar) && currentChar != '_') throw new InputErrorException();
                if(currentChar != ' ') tape.add(currentChar);
            }
            line = br.readLine();
        }
    }

    public void processTape() throws RejectedTapeException, InputErrorException
    {
        int tapeIndex = 0;
        while(true)
        {
            // Adds a blank space to the end of the tape.
            if(tapeIndex >= tape.size()) tape.add(BLANK);
            
            char inputChar = tape.get(tapeIndex);            
            Transition transition = initialState.getTransition(inputChar);
            
            // Change the current cell
            tape.set(tapeIndex, transition.getOutput());

            // FOR TESTING PURPOSES.
            // System.out.println("Tape Index " + tapeIndex + ":" + initialState.getName() + " " + inputChar + " " + transition.toString());
            // Move tape L or R
            if(transition.getMove() == 'L' && tapeIndex != 0) tapeIndex--;
            else if (transition.getMove() == 'R') tapeIndex++;
            
            // Change current state
            initialState = getState(transition.getNextState());           
            
            if(initialState == rejectState) throw new RejectedTapeException();
            if(initialState == acceptState) break;
            numMoves++;
        }
    }

    /**
     * Prints the Tape.
     */
    public void printTape()
    {
        // Prints the input information.
        System.out.println(numMoves);
        if(tape.size() == 0) System.out.print(BLANK);
        else{
            int offset = 0;
            while(true)
            {
                int ceil = offset + 1;
                // Boolean ensures that there is at least one blank 
                boolean atStart = (tape.size() - ceil) == 0;
                boolean notBlank = tape.get(tape.size() - (offset + 1)) != BLANK;
                // Increments the offset for each _ at the end of the tape.
                if(atStart || notBlank)break;
                offset++;
            }

            for(int i = 0; i < tape.size()-offset; i++)
                System.out.print(tape.get(i));
        }
    }

    /**
     * Checks if the string is a single character
     */
    private String checkLength(String symbol) throws InputErrorException
    {
        if(symbol.length() > 1) throw new InputErrorException();
        return symbol;
    }

    private State getState(String state) throws InputErrorException
    {
        if(!states.containsKey(state)) throw new InputErrorException();
        return states.get(state);
    }

    /**
     * Checks if the input string is a single character and in the alphabet and returns it.
     */
    private char getChar(String symbol)throws InputErrorException
    {
        char charSymbol = checkLength(symbol).charAt(0);
        if(!alphabetContains(charSymbol) && charSymbol != BLANK) throw new InputErrorException();        
        return charSymbol;
    }

    /**
     * Checks if the symbol is contained in the alphabet.
     */
    private boolean alphabetContains(char symbol)
    {
        return alphabet.contains(symbol);
    }

}