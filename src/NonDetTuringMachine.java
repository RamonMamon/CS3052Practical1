import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class NonDetTuringMachine
{
    private static final String WHITESPACES = "\\s+";
    private static final char BLANK = '_';
    private static final char SPACE = ' ';
    private static final char RIGHT = 'R';
    private static final char LEFT = 'L';

    private List<Character> alphabet = new ArrayList<Character>();
    private Map<String,NonDetState> states = new HashMap<String, NonDetState>();
    private int numMoves;

    private NonDetState initialState;
    private NonDetState acceptState;
    private NonDetState rejectState;
    
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
                
                NonDetState newState = new NonDetState(tokens[0]);
                
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
                if(initialState == null)
                {
                    newState.setTape(new ArrayList<Character>());
                    initialState = newState;
                }
                
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

                NonDetState currentState = getState(state);

                // Saves the new transition.
                char inputChar = getChar(tokens[1]);
                String nextState = getState(tokens[2]).getName();
                
                char outputChar = getChar(tokens[3]);
                char move = checkLength(tokens[4]).charAt(0);
                if(move != RIGHT && move != LEFT) throw new InputErrorException();
                
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
                if(!alphabetContains(currentChar) && currentChar != BLANK) throw new InputErrorException();
                if(currentChar != SPACE) initialState.addToTape(currentChar);
            }
            line = br.readLine();
        }
    }

    public void processTape() throws RejectedTapeException, InputErrorException
    {
        List<NonDetState> nodeStates = new ArrayList<NonDetState>();
        nodeStates.add(initialState);
        while(true)
        {
            int stateIndex = 0;
            List<NonDetState> newStates = new ArrayList<NonDetState>();
            while(stateIndex < nodeStates.size())
            {                
                // Iterates through the list of states and changing their respective tapes.
                NonDetState currentState = nodeStates.get(stateIndex);
                
                char inputChar = currentState.getInputChar();          
                List<Transition> transitions = currentState.getTransitions(inputChar);
                for(int i = 0; i < transitions.size(); i++)
                {
                    // Iterates through all the transitions of the current state and adds the next state to a list.
                    Transition transition = transitions.get(i);
                    
                    // FOR TESTING PURPOSES.
                    // printTransition(tapeIndex);

                    // Change the current cell
                    NonDetState tempState = getState(transition.getNextState());
                    tempState.setTapeIndex(currentState.getTapeIndex());
                    tempState.setTape(currentState.getTape());
                    tempState.setTapeVal(transition.getOutput());

                    
                    // FOR TESTING PURPOSES.
                    // System.out.println("Tape Index " + tempState.getTapeIndex() + ":" + currentState.getName() + " " + inputChar + " " + transition.toString());
                    
                    // Move tape L or R
                    if(transition.getMove() == LEFT && tempState.getTapeIndex()!= 0) tempState.decrementTapeIndex();
                    else if (transition.getMove() == RIGHT) tempState.incrementTapeIndex();
                    
                    // Adds the state to a list of states
                    newStates.add(tempState);
                    
                    if(tempState == rejectState) newStates.remove(newStates.size()-1);
                    if(tempState == acceptState) return;
                }
                
                stateIndex++;
            }
            numMoves++;
            nodeStates = newStates;
        }
    }

    /**
     * Prints the tape with an arrow referring to where the head is.
     */
    private void printTransition(int tapeIndex, ArrayList<Character> tape)
    {
        for(int i = 0; i < tape.size(); i++)
            System.out.print(tape.get(i));

        System.out.println();
        for(int i = 0; i < tapeIndex; i++)
            System.out.print(' ');

        System.out.println('^');
    }

    /**
     * Prints the Number of Moves.
     */
    public void printTape()
    {
        System.out.println(numMoves);
    }

    /**
     * Checks if the string is a single character
     */
    private String checkLength(String symbol) throws InputErrorException
    {
        if(symbol.length() > 1) throw new InputErrorException();
        return symbol;
    }

    private NonDetState getState(String state) throws InputErrorException
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