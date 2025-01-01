package cms.cf.service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cms.cf.exceptions.domain.AlreadyExistException;
import cms.cf.exceptions.domain.EmailInvalidoException;
import cms.cf.exceptions.domain.NotFoundException;
import cms.cf.exceptions.domain.PasswordInvalidoException;
import cms.cf.exceptions.domain.SendEmailException;
import cms.cf.exceptions.domain.TokenExpiradoException;
import cms.cf.exceptions.domain.TokenInvalidoException;
import cms.cf.lib.MailTool;
import cms.cf.model.api.PwdChange;
import cms.cf.model.api.PwdReset;
import cms.cf.model.api.UserModel;
import cms.cf.model.api.UserRegister;
import cms.cf.model.domain.ResetToken;
import cms.cf.model.domain.Role;
import cms.cf.model.domain.User;
import cms.cf.model.domain.UserTemp;
import cms.cf.repository.ResetTokenRepository;
import cms.cf.repository.RoleRepository;
import cms.cf.repository.UserRepository;
import cms.cf.repository.UserTempRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserMngService
{
	@Autowired
	private UserRepository userRep;
	@Autowired
	private RoleRepository roleRep;
	@Autowired
	private ResetTokenRepository resetTokenRep;
	@Autowired
	private UserTempRepository userTempRep;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	MailTool mailTool;
	
	
	/**
	 * Cria o novo usuario na tabela "user" com a role "Worker".
	 * Remove a entrada na tabela "user_temp".
	 */
	@Transactional
    public void confirmUser(String email, String pwd, String token)
    throws TokenInvalidoException, TokenExpiradoException, 
           PasswordInvalidoException, EmailInvalidoException
    {
        //obter dados do token no banco
        UserTemp stored;
        Optional<UserTemp> opt = userTempRep.findById(token);
        
        if (opt.isPresent())
        	stored = opt.get();
        else
        {
            log.warn("tentativa de confirmar token nao encontrado: " + token);
            throw new TokenInvalidoException();
        }
        
        if (email.trim().equals(stored.getEmail()) == false)
        {
            log.warn("tentativa de confirmar token [" + token + "] com email invalido ["+email+"]");
            throw new EmailInvalidoException();
        }
        
        //verificar senha
        String pwdhash = null;
        if (pwd != null ) 
        {
            pwdhash = passwordEncoder.encode(pwd);
        }
            
        if (pwdhash == null || pwdhash.equals(stored.getPwd()) == false)
        {
            if (stored.getAcessos() == 3)
            {
            	userTempRep.deleteById(stored.getToken()); //remover token do banco.
            	userTempRep.flush();
                log.warn("Excesso de tentativa de confirmar token [" + token + "]");
                throw new TokenExpiradoException();
            }

          	//atualizar a base para indicar nota tentativa de acesso
            stored.setAcessos(stored.getAcessos()+1);
            userTempRep.save(stored);
            userTempRep.flush();
            
			throw new PasswordInvalidoException();
        }

        //ok a senha confere.
		
        User newUser = new User();
        newUser.setEmail(stored.getEmail());
        newUser.setPwd(stored.getPwd());
        newUser.setName(stored.getName());

        Role role = roleRep.findByName("Worker");
        newUser.getRoles().add(role);

        userRep.save(newUser);
        userRep.flush();
        userTempRep.deleteById(token);
        userTempRep.flush();
    }
    
	/**
	 * Gera um Token
	 * Cria uma entrada para o token/usuario na tabela "user_temp"
	 * Envia um email para o usuario com o link de confirmação do email/conta.
	 */
	@Transactional
    public void createUserTempAndSendEmail(UserRegister user)
    throws AlreadyExistException, SendEmailException
    {
		if (userRep.findByEmail(user.getEmail()) != null)
		{
			throw new AlreadyExistException();
		}
		
        //gerar token para email de confirmacao
        String token = makeToken();
        
        //Adicionar Usuario Temporario a espera da confirmacao
        UserTemp ut = new UserTemp(
        		user.getName(),
        		user.getEmail(),
        		passwordEncoder.encode(user.getPwd()),
        		token);
        userTempRep.save(ut);
        userTempRep.flush();
        
        //Enviar email de confirmacao
    	String url = mailTool.getConfigProperties().getConfirmUserUrl();
    	String message = "Confirmação do email do cadasstro. <a href='"+url+"?token="+token+"'>Link</a>";
    	String subject = "Template - Nova Conta.";
    	mailTool.sendEmail(user.getEmail(), subject, message); 
    }
    
	/**
	 * Gera um Token
	 * Cria uma entrada para o token/usuario na tabela "reset_token"
	 * Envia um email para o usuario com o link para a pagina de reset de senha, contendo o token de controle.
	 */    
    @Transactional
    public void createResetTokenAndSendEmail(String email)
    throws SendEmailException
    {
        //gerar token para email de confirmacao
        String token = makeToken();
       
        ResetToken rt = new ResetToken();
        rt.setEmail(email);
        rt.setToken(passwordEncoder.encode(token));

        //Adicionar Usuario Temporario a espera da confirmacao
        resetTokenRep.save(rt);
        resetTokenRep.flush();
        
        //Enviar email de confirmacao
    	String url = mailTool.getConfigProperties().getConfirmResetUrl();
    	String message = "Link para atualização de sua senha. <a href='"+url+"?token="+token+"'>Link</a>";
    	String subject = "Template - Reset de Senha.";
    	mailTool.sendEmail(email, subject, message);         
    }  
    
    private String makeToken()
    {
        return makeToken2();
    }
    
    static final String AB  = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%&*()_+-=[]{}<>:,.;?/|";
    static SecureRandom rnd = new SecureRandom();
    
    @SuppressWarnings("unused")
    private String makeToken1(int len)
    {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
    
    private String makeToken2()
    {
        return new BigInteger(130, rnd).toString(32);
    }

	public void verifyResetToken(String token) 
	throws TokenInvalidoException, TokenExpiradoException
	{
        //preparar o token
        String htoken = passwordEncoder.encode(token);
        
        //obter dados do token no banco
        Optional<ResetToken> opt = resetTokenRep.findById(htoken);
        ResetToken rt = null;
        if (opt.isPresent())  rt = opt.get(); 
        else {
            log.warn("tentativa de reset com token nao encontrado: " + token);
            throw new TokenInvalidoException();
        }
        
        //verificar se o token está expirado por tempo.
        long diff = System.currentTimeMillis() - rt.getSysCreationDate().getTime();
        if (diff > 24*60*60*1000) //24horas
        {
            log.warn("tentativa de reset com token EXPIRADO: " + token);
            throw new TokenExpiradoException();
        }
	}

	public void finalizaResetPassword(PwdReset pwd) 
    throws TokenInvalidoException, NotFoundException
	{
        //preparar o token
        String htoken = passwordEncoder.encode(pwd.getToken());
        
        //obter dados do token no banco
        Optional<ResetToken> opt = resetTokenRep.findById(htoken);
        ResetToken rt = null;
        if (opt.isPresent())  rt = opt.get(); 
        else {
            log.warn("tentativa de reset com token nao encontrado: " + pwd.getToken());
            throw new TokenInvalidoException();
        }     
        
    	User user = userRep.findByEmail(rt.getEmail());
    	if (user == null) {
            log.warn("tentativa de alterar senha de usuario nao encontrado: " + rt.getEmail());
            throw new NotFoundException("Inconsistencia: usuario nao encotrado: "+rt.getEmail());
    	}
        user.setPwd(passwordEncoder.encode(pwd.getNewpwd1()));
        userRep.save(user);
        userRep.flush();
	}

	public void changeUserPassword(PwdChange pwd, String userEmail) 
	throws NotFoundException, PasswordInvalidoException
	{
        User user = userRep.findByEmail(userEmail);
        if (user == null)
        {
            log.warn("tentativa de alterar senha de usuario nao encontrado: " + userEmail);
            throw new NotFoundException();
        }
        
        //verificar senha com o PasswordEncoder padrao da seguranca
        //String pwdhash = DigestUtils.sha512Hex(pwd.getPwd());
        String pwdhash = passwordEncoder.encode(pwd.getPwd());
        
        if (pwdhash == null || pwdhash.equals(user.getPwd()) == false)
        {
			throw new PasswordInvalidoException();
        }

        //ok a senha confere.

        //user.setPwd(DigestUtils.sha512Hex(pwd.getNewpwd1()));
    	user.setPwd(passwordEncoder.encode(pwd.getNewpwd1()));
        
        userRep.save(user);
        userRep.flush();
	}

	public void updateUser(@Valid UserModel userModel, String email) {

        User userdb = userRep.findByEmail(email);
        //User userdb = userRep.getByEmail(userModel.getEmail()); //brecha de seguranca
        //modelMapper.map(userModel, userdb);
        BeanUtils.copyProperties(userModel, userdb, "id", "pwd", "email", "creationDate"); //iguinorar props "id" e "pwd"
                                 // o formulario de usuario nao atualiza sennha 
        userdb.setUpdateDate(LocalDateTime.now());
    	userRep.save(userdb);
    	userRep.flush();
		
	}
}
