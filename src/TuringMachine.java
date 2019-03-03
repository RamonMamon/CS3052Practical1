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
    private List tape = new ArrayList<Character>();
    private List alphabet = new ArrayList<Character>();
    private Map<String,State> states = new HashMap<String, State>();

    private State currentState;
    private State acceptState;
    private State rejectState;

    // Can create a State class that stores the transitions of each state and its type
    // Can store all the states in a list.

    public TuringMachine(String description) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(description));
        parseDescriptionFile(br);
    }

    public TuringMachine(String description, String startingTape)throws IOException
    {
        this(description);

        // Parse Starting Tape.
    }

    /**
     * Parses the Turing Machine Description File.
     */
    private void parseDescriptionFile(BufferedReader br) throws IOException
    {
        // Must check if each input is at most one character and separated by spaces

        // First read number of states n.
        // Read n lines corresponding to the states.
        // If not n lines, bad descripition file.
        try{
            String line = br.readLine();
            String[] tokens = line.split("\\s+");

            if(!tokens[0].equals("states")) throw new InputErrorException();

            int numStates = Integer.parseInt(tokens[1]);
            for(int i = 0; i < numStates; i++)
            {
                // Parses the States
                line = br.readLine();
                tokens = line.split("\\s+");
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
            tokens = line.split("\\s+");

            if(!tokens[0].equals("alphabet")) throw new InputErrorException();
            
            int alphabetSize = Integer.parseInt(tokens[1]);
            for(int i = 2; i < tokens.length; i ++)
            {
                // TODO: The token may output a NullPoinbter Exception or Array out of bounds exception.
                
                alphabet.add(tokens[i]);
            }

            if(alphabetSize != alphabet.size()) throw new InputErrorException();

            line = br.readLine();
            // Parse Transitions.
            while(line != null)
            {
                // Divides the line by whitespaces.
                if(line.trim().length() == 0){
                    // Skips lines that are only spaces.
                    line = br.readLine();
                    continue;
                }
                tokens = line.split("\\s+");
                String state = tokens[0];
                if(state.equals(acceptState.getName()) || state.equals(rejectState.getName()))
                {
                    throw new InputErrorException();
                }

                // TODO: Need to do error checking on this. Might be in different order 
                State currentState = states.get(state);
                char inputChar = getChar(tokens[1]);
                String nextState = tokens[2];
                char outputChar = getChar(tokens[3]);
                char move = getChar(tokens[4]);
                currentState.addTransition(inputChar, nextState, outputChar, move);
                line = br.readLine();
            }
            
        }catch(ParseException e){
            System.out.println("input error");
            System.exit(2);
        }catch(InputErrorException e){
            System.out.println("input error");
            System.exit(2);
        }
    }

    private char getChar(String symbol)throws InputErrorException
    {
        if(symbol.length() > 1) throw new InputErrorException();
        char charSymbol = symbol.charAt(0);
        // TODO: change this because the alphabet is stored as a string.
        if(!alphabetContains(charSymbol)) throw new InputErrorException();
        return charSymbol;
    }

    private boolean alphabetContains(char symbol)
    {
        return alphabet.contains(symbol);
    }
}