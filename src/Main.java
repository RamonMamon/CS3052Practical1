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
        
        if(args.length < 1 || args.length > 3)
        {
            System.out.println("undefined");
            System.exit(0);
        }

        String description = args[0];
        boolean nd = false;
        boolean hasTape = false;
        
        if(args[0].equals("-n"))
        {
            nd = true;
            description = args[1];
            if(args.length == 3) hasTape = true;
        }else
        {
            if(args.length == 2) hasTape = true;
        }
        
        TuringMachine tm = new TuringMachine();
        NonDetTuringMachine ndtm = new NonDetTuringMachine();
        int status = ACCEPTED;
        boolean isParenTM = false;
        boolean isBinAdd = false;
        try{
            BufferedReader descReader = null;
            BufferedReader startTape = null;
            
            try{
                
                isParenTM = description.contains("paren.tm");
                isBinAdd = description.contains("binadd.tm");
                descReader = new BufferedReader(new FileReader(description));
                if(hasTape){
                    String startingTape = (nd)? args[2] : args[1];
                    startTape = new BufferedReader(new FileReader(startingTape));
                }

                if(nd)
                {
                    ndtm.parseDescriptionFile(descReader);
                    ndtm.parseInputTape(startTape);
                    ndtm.processTape();
                }else
                {
                    tm.parseDescriptionFile(descReader);
                    tm.parseInputTape(startTape);
                    tm.processTape();
                }
                System.out.println("accepted");
            }catch(RejectedTapeException e)
            {
                System.out.println("not accepted");
                status = NOT_ACCEPTED;
            }catch(InputErrorException e)
            {
                System.out.println("input error");
                status = INPUT_ERROR;
            }
            finally
            {
                if(descReader != null)descReader.close();
                if(startTape != null)startTape.close();
            }
        }catch(IOException e){
            // Exits if file cannot be found.
            status = FILE_NOT_FOUND;
        }finally{
            if(status != INPUT_ERROR && status != FILE_NOT_FOUND && !isParenTM && !isBinAdd){
                if(nd) ndtm.printTape();
                else tm.printTape();
            }
            System.exit(status);
        }
    }
}