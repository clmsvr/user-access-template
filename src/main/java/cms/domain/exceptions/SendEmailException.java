package cms.domain.exceptions;

@SuppressWarnings("serial")
public class SendEmailException extends Exception {

    public SendEmailException()
    {
    }
    
    public SendEmailException(Exception e)
    {
        super(e);
    }
    
    public SendEmailException(String msg) {
        super(msg);
    }
    
    public SendEmailException(String msg, Exception e)
    {
        super(msg,e);
    }
}