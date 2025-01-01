package cms.domain.exceptions;

@SuppressWarnings("serial")
public class NotFoundException extends Exception {

    public NotFoundException()
    {
    }
    
    public NotFoundException(Exception e)
    {
        super(e);
    }
    
    public NotFoundException(String msg) {
        super(msg);
    }
    
    public NotFoundException(String msg, Exception e)
    {
        super(msg,e);
    }
}