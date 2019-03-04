import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

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
        TuringMachine  tm = new TuringMachine();
        try{
            BufferedReader descReader = null;
            BufferedReader startTape = null;
            int status = 0;
            try{
                String description = args[0];
                descReader = new BufferedReader(new FileReader(description));
                
                tm.parseDescriptionFile(descReader);
                if(args.length == 2){
                    String startingTape = args[1];
                    startTape = new BufferedReader(new FileReader(startingTape));
                    tm.parseInputTape(startTape);
                }

                System.out.println("accepted");
            }catch(RejectedTapeException e)
            {
                System.out.println("not accepted");
                status = 1;
            }catch(InputErrorException e)
            {
                System.out.println("input error");
                status = 2;
            }finally
            {
                // find a way to return the status.
                tm.printTape();
                descReader.close();
                startTape.close();
                System.exit(status);
            }
        }catch(IOException e){
            // Exits if file cannot be found.
            System.exit(3);
        }
    }
}