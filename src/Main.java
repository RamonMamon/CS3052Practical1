import java.io.IOException;

public class Main
{

    public static void main(String args[])
    {
        if(args.length < 1 || args.length > 2)
        {
            System.out.println("undefined");
            System.exit(0);
        }
        // Take in TM description file

        // Take in starting tape descriptor
        TuringMachine tm;
        System.out.println(args[0]);
        try{
            if(args.length == 1)
                tm = new TuringMachine(args[0]);
            else 
                tm = new TuringMachine(args[0], args[1]);
        }catch(IOException e){
            // Exits if file cannot be found.
            System.exit(3);
        }
    }


}