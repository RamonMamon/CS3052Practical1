import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class TuringMachine
{
    private static final String WHITESPACES = "\\s+";

    private List<Character> tape = new ArrayList<Character>();
    private List alphabet = new ArrayList<Character>();
    private Map<String,State> states = new HashMap<String, State>();
    private int numMoves;

    private State initialState;
    private State acceptState;
    private State rejectState;
    
    public TuringMachine()
    {

    }

    /**
     * Parses the Turing Machine Description File.
     */
    public void parseDescriptionFile(BufferedReader br) throws IOException, InputErrorException
    {
        try
        {
            String line = br.readLine();
            String[] tokens = line.split(WHITESPACES);
            if(!tokens[0].equals("states")) throw new InputErrorException();

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
            if(alphabetSize != alphabet.size()) throw new InputErrorException();

            line = br.readLine();

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

                // If current state is null set this to the current state.
                State currentState = states.get(state);

                // Saves the new transition.
                char inputChar = getChar(tokens[1]);
                String nextState = tokens[2];
                char outputChar = getChar(tokens[3]);
                char move = checkLength(tokens[4]).charAt(0);
                currentState.addTransition(inputChar, nextState, outputChar, move);
                if(initialState == null) initialState = currentState;

                line = br.readLine();
            }
        }catch(NumberFormatException e){
            // Catches any errors in formatting when dealing with numbers
            throw new InputErrorException();
        }
    }

    public void parseInputTape(BufferedReader br) throws IOException, InputErrorException, RejectedTapeException
    {
        String line = br.readLine();
        while(line != null)
        {
            // Removes all whitespaces from the input.
            line.replaceAll(WHITESPACES, "");
            for(int i = 0; i < line.length(); i++)
            {
                // Adds characters to the tape.
                char currentChar = line.charAt(i);
                if(currentChar != ' ') tape.add(currentChar);
            }
            line = br.readLine();
        }

        int stateIndex = 0;
        while(true)
        {
            // Adds a blank space to the end of the tape.
            if(stateIndex >= tape.size()) tape.add('_');
            char inputChar = tape.get(stateIndex);
            
            if(!alphabetContains(inputChar) && inputChar != '_') throw new RejectedTapeException();

            Transition transition = initialState.getTransition(inputChar);
            // Change the current cell
            tape.set(stateIndex, transition.getOutput());
            // Move tape L or R
            if(transition.getMove() == 'L') stateIndex--;
            else if (transition.getMove() == 'R') stateIndex++;
            // Change current state
            initialState = states.get(transition.getNextState());
            
            if(initialState == rejectState) throw new RejectedTapeException();
            if(initialState == acceptState) break;
            numMoves++;
        }
    }

    public void printTape()
    {
        // Prints the input information.
        System.out.println(numMoves);
        if(tape.size() == 0) System.out.println('_');
        else
        {
            int offset = 0;
            while(true)
            {
                // Increments the offset for each _ at the end of the tape.
                if(tape.get(tape.size() - (offset + 1)) != '_')break;
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

    /**
     * Checks if the inputted string is a single character and in the alphabet and returns it.
     */
    private char getChar(String symbol)throws InputErrorException
    {
        char charSymbol = checkLength(symbol).charAt(0);
        if(!alphabetContains(charSymbol) && charSymbol != '_') throw new InputErrorException();        
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