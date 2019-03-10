import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Main
{
    private static final int ACCEPTED = 0;
    private static final int NOT_ACCEPTED = 1;
    private static final int INPUT_ERROR = 2;
    private static final int FILE_NOT_FOUND = 3;
    public static void main(String args[])
    {
        if(args.length < 1 || args.length > 2)
        {
            System.out.println("undefined");
            System.exit(0);
        }
        
        TuringMachine  tm = new TuringMachine();
        int status = ACCEPTED;
        boolean isParenTM = false;
        boolean isBinAdd = false;
        try{
            BufferedReader descReader = null;
            BufferedReader startTape = null;
            
            try{
                String description = args[0];
                isParenTM = description.contains("paren.tm");
                isBinAdd = description.contains("binadd.tm");
                descReader = new BufferedReader(new FileReader(description));
                if(args.length == 2){
                    String startingTape = args[1];
                    startTape = new BufferedReader(new FileReader(startingTape));
                }
                tm.parseDescriptionFile(descReader);
                tm.parseInputTape(startTape);
                tm.processTape();
                System.out.println("accepted");
            }catch(RejectedTapeException e)
            {
                System.out.println("not accepted");
                status = NOT_ACCEPTED;
            }catch(InputErrorException e)
            {
                System.out.println("input error");
                status = INPUT_ERROR;
            }finally
            {
                if(descReader != null)descReader.close();
                if(startTape != null)startTape.close();
            }
        }catch(IOException e){
            // Exits if file cannot be found.
            status = FILE_NOT_FOUND;
        }finally{
            // TODO: Uncomment this later.
            if(status != INPUT_ERROR && status != FILE_NOT_FOUND && !isParenTM && !isBinAdd){
                tm.printTape();
            }
            System.exit(status);
        }
    }
}