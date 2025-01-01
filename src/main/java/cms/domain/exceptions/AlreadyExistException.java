package cms.domain.exceptions;

@SuppressWarnings("serial")
public class AlreadyExistException extends Exception {

    public AlreadyExistException()
    {
    }
    
    public AlreadyExistException(Exception e)
    {
        super(e);
    }
    
    public AlreadyExistException(String msg) {
        super(msg);
    }
    
    public AlreadyExistException(String msg, Exception e)
    {
        super(msg,e);
    }
}