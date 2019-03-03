public class InputErrorException extends Exception 
{
    public static final long serialVersionUID = 1L;
    

    public InputErrorException(){
        super();
    }

    public InputErrorException(String message)
    {
        super(message);
    }

    public InputErrorException(String message, Throwable err){
        super(message, err);
    }
}