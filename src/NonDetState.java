import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class NonDetState
{
    private String name;

    // Have a hashmap that contains the next state which is accessed by an 
    // input alphabet that will be used as a key.
    private Map<Character, List<Transition>> transitions = new HashMap<Character, List<Transition>>();
    private List<Character> tape;
    private int tapeIndex;

    public NonDetState(String name)
    {
        this.name = name;
    }

    public void addTransition(char input, String next, char output, char move)
    {
        List<Transition> newTransitions = new ArrayList<Transition>();
        Transition newTransition = new Transition(next, output, move);
        
        if(transitions.containsKey(input)) 
            newTransitions = transitions.get(input);

        newTransitions.add(newTransition);
        transitions.put(input, newTransitions);
    }

    /**
     * Checks if the input symbol has a transition from the current state.
     */
    public List<Transition> getTransitions(char input) throws RejectedTapeException
    {
        if(!transitions.containsKey(input)) throw new RejectedTapeException();
        
        return transitions.get(input);
    }

    public String getName()
    {
        return name;
    }

    public void setTape(List<Character> tape)
    {
        this.tape = tape;
    }

    public void setTapeVal(char outputChar)
    {
        if(tapeIndex >= tape.size()) tape.add('_');
        tape.set(tapeIndex, outputChar);
    }

    public char getInputChar()
    {
        // Adds a blank space to the end of the tape.
        if(tapeIndex >= tape.size()) tape.add('_');
        return tape.get(tapeIndex);
    }

    public void setTapeIndex(int tapeIndex)
    {
        this.tapeIndex = new Integer(tapeIndex);
    }

    public int getTapeIndex()
    {
        return tapeIndex;
    }

    public void incrementTapeIndex()
    {
        tapeIndex++;
    }

    public void decrementTapeIndex()
    {
        tapeIndex--;
    }

    public List<Character> getTape()
    {
        return tape;
    }

    public void addToTape(char character)
    {
        tape.add(character);
    }
    
}