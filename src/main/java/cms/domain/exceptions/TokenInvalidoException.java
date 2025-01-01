package cms.domain.exceptions;

@SuppressWarnings("serial")
public class TokenInvalidoException extends Exception {

    public TokenInvalidoException()
    {
    }
    
    public TokenInvalidoException(Exception e)
    {
        super(e);
    }
    
    public TokenInvalidoException(String msg) {
        super(msg);
    }
    
    public TokenInvalidoException(String msg, Exception e)
    {
        super(msg,e);
    }
}