package cms.domain.exceptions;

@SuppressWarnings("serial")
public class TokenExpiradoException extends Exception {

    public TokenExpiradoException()
    {
    }
    
    public TokenExpiradoException(Exception e)
    {
        super(e);
    }
    
    public TokenExpiradoException(String msg) {
        super(msg);
    }
    
    public TokenExpiradoException(String msg, Exception e)
    {
        super(msg,e);
    }
}