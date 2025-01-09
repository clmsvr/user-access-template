package cms.lib.components;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cms.domain.exceptions.SendEmailException;
import cms.lib.ConfigHelper;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Component
public class MailTool {

	@Autowired
	MailProperties props;

    public MailProperties getConfigProperties()
    {
    	return props;
    }
    
    public void sendEmail(String recipentEmail, String subject, String textMessage)
    throws SendEmailException
    {
    	// como preparar a conta do google para enviar email
    	//https://support.google.com/accounts/answer/185833?hl=pt-BR
    	
        String pwd = ConfigHelper.decode64(props.getPassword());
        
        if (props.getSender() == null || pwd == null) 
        	throw new SendEmailException("Erro lendo credenciais para enviar EMAILs"); 
        
        /* Parametros de conexao com servidor Gmail */
        //SSL
        Properties properties = new Properties();
        properties.put("mail.smtp.host", props.getSmtpHost());
        properties.put("mail.smtp.socketFactory.port", props.getSmtpPort());
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", props.getSmtpPort());

//        //TTLS
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
        
        
        //https://stackoverflow.com/questions/4184204/what-is-the-difference-between-getdefaultinstance-and-getinstance-in-session
        //https://stackoverflow.com/questions/11566772/java-mail-api-exception-thrown-saying-java-lang-securityexception-access-to-d        
        //eu estava usando getDefaultInstance()  e tendo muitas falhas ao enviar email.
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(props.getUserId(),pwd);
            }
        });

        /* Ativa Debug para sess�o */
        //session.setDebug(true);
        
        Message message = new MimeMessage(session);
        
        try {
			//Remetente
			message.setFrom(new InternetAddress(props.getSender())); 

			//Destinatário(s)
			//Address[] toUser = InternetAddress.parse("x@yahoo.com, y@gmail.com");
			//message.setRecipients(Message.RecipientType.TO, toUser);
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipentEmail));
			message.setSubject(subject);
			message.setText(textMessage);

			Transport.send(message);
//ou
//        Transport transport = session.getTransport("smtp");
//        transport.connect("smtp.gmail.com", "email", "senha");
//        transport.sendMessage(message, message.getAllRecipients());
//        transport.close();            
		} catch (Exception e) {
			throw new SendEmailException(e);
		}
        
    } 
}
