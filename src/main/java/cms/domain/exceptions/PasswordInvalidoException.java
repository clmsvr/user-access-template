package cms.domain.exceptions;

@SuppressWarnings("serial")
public class PasswordInvalidoException extends Exception {

    public PasswordInvalidoException()
    {
    }
    
    public PasswordInvalidoException(Exception e)
    {
        super(e);
    }
    
    public PasswordInvalidoException(String msg) {
        super(msg);
    }
    
    public PasswordInvalidoException(String msg, Exception e)
    {
        super(msg,e);
    }
}