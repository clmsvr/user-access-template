package cms.domain.exceptions;

@SuppressWarnings("serial")
public class EmailInvalidoException extends Exception {

    public EmailInvalidoException()
    {
    }
    
    public EmailInvalidoException(Exception e)
    {
        super(e);
    }
    
    public EmailInvalidoException(String msg) {
        super(msg);
    }
    
    public EmailInvalidoException(String msg, Exception e)
    {
        super(msg,e);
    }
}