import java.util.Map;
import java.util.HashMap;

public class State
{
    private String name;

    // Have a hashmap that contains the next state which is accessed by an 
    // input alphabet that will be used as a key.
    private Map<Character, Transition> transitions = new HashMap<Character, Transition>();

    public State(String name)
    {
        this.name = name;
    }

    public void addTransition(char input, String next, char output, char move) throws InputErrorException
    {
        if(transitions.containsKey(input)) throw new InputErrorException();
        Transition newTransition = new Transition(next, output, move);
        
        
        transitions.put(input, newTransition);
    }

    public String getName()
    {
        return name;
    }
}