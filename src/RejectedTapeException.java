public class RejectedTapeException extends Exception 
{
    public static final long serialVersionUID = 1L;
    

    public RejectedTapeException(){
        super();
    }

    public RejectedTapeException(String message)
    {
        super(message);
    }

    public RejectedTapeException(String message, Throwable err){
        super(message, err);
    }
}