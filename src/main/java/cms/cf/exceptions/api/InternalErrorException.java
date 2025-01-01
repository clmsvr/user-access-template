package cms.cf.exceptions.api;

@SuppressWarnings("serial")
//@ResponseStatus(value=HttpStatus.BAD_REQUEST)
public class InternalErrorException extends RuntimeException 
{
    public InternalErrorException(Exception e)
    {
        super(e);
    }
    
    public InternalErrorException(String msg) {
        super(msg);
    }
    
    public InternalErrorException(String msg, Exception e)
    {
        super(msg,e);
    }

}