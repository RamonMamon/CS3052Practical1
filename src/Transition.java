public class Transition
{

    private String nextState;
    private char output;
    private char move;

    public Transition(String nextState, char output, char move)
    {
        this.output = output;
        this.nextState = nextState;
        this.move = move;
    }

    public char getOutput()
    {
        return output;
    }

    public String getNextState()
    {
        return nextState;
    }

    public char getMove()
    {
        return move;
    }

    @Override
    public String toString()
    {
        return nextState + " " + output + " " + move;
    }
}