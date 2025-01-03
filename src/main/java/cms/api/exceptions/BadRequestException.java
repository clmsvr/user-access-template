package cms.api.exceptions;

@SuppressWarnings("serial")
//@ResponseStatus(value=HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException 
{
    public BadRequestException()
    {
    }
    
    public BadRequestException(Exception e)
    {
        super(e);
    }
    
    public BadRequestException(String msg) {
        super(msg);
    }
    
    public BadRequestException(String msg, Exception e)
    {
        super(msg,e);
    }
}