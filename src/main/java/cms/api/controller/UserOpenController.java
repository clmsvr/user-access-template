package cms.api.controller;

import java.security.Principal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cms.api.exceptions.BadRequestException;
import cms.api.exceptions.InternalErrorException;
import cms.api.model.Email;
import cms.api.model.PwdReset;
import cms.api.model.UserRegister;
import cms.api.model.UserTempApi;
import cms.domain.exceptions.AlreadyExistException;
import cms.domain.exceptions.EmailInvalidoException;
import cms.domain.exceptions.NotFoundException;
import cms.domain.exceptions.PasswordInvalidoException;
import cms.domain.exceptions.SendEmailException;
import cms.domain.exceptions.TokenExpiradoException;
import cms.domain.exceptions.TokenInvalidoException;
import cms.domain.model.User;
import cms.domain.repository.UserRepository;
import cms.domain.repository.UserTempRepository;
import cms.domain.service.UserMngService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user/open")
public class UserOpenController 
{
	@Autowired
	private UserMngService userService;
	
	@Autowired
	private UserRepository userRep;
	
	@Autowired
	private UserTempRepository userTempRep;
	
    private static HashMap<String, String> states = new HashMap<>();
    static{
        states.put("AC","Acre");
        states.put("AL","Alagoas");
        states.put("AP","Amapá");
        states.put("AM","Amazonas");
        states.put("BA","Bahia");
        states.put("CE","Ceará");
        states.put("DF","Distrito Federal");
        states.put("ES","Espirito Santo");
        states.put("GO","Goiás");
        states.put("MA","Maranhão");
        states.put("MS","Mato Grosso do Sul");
        states.put("MT","Mato Grosso");
        states.put("MG","Minas Gerais");
        states.put("PA","Pará");
        states.put("PB","Paraíba");
        states.put("PR","Paraná");
        states.put("PE","Pernambuco");
        states.put("PI","Piauí");
        states.put("RJ","Rio de Janeiro");
        states.put("RN","Rio Grande do Norte");
        states.put("RS","Rio Grande do Sul");
        states.put("RO","Rondônia");
        states.put("RR","Roraima");
        states.put("SC","Santa Catarina");
        states.put("SP","São Paulo");
        states.put("SE","Sergipe");
        states.put("TO","Tocantins");
    }

	/**
	 * Inicio do processo para Registrar novo usuário
	 */
    @GetMapping({"/register"})
    public String register(Model model) 
    {
        model.addAttribute("user", new UserRegister());
        return "user/register-form";
    } 

    @PostMapping({ "/register" })
    public String registerPost(
    		Model model, 
    		@Valid @ModelAttribute("user") UserRegister user, 
    		BindingResult result)
    {      
        if (result.hasErrors())
        {
            return "user/register-form";
        }

        try
        {
            userService.createUserTempAndSendEmail(user);
            return "redirect:/user/open/register-resp";
        }
        catch (AlreadyExistException e) {
        	result.rejectValue("email", "email", "Email indisponível.");
        	return "user/register-form";
		}
        catch (SendEmailException e)
        {
        	log.error("ERRO ao enviar EMAIL de confirmcao ao usuario ["+user.getEmail()+"]:  "+e.toString());        	
            throw new InternalErrorException(e);
        }
    }

    @GetMapping({"/register-resp"})
    public String registerResp(Model model) 
    {
        return "user/register-resp";
    }
    
    
    @GetMapping({"/confirm"})
    public String confirm(Model model, @RequestParam("token") String token) 
    {
        //obter dados do token no banco
        if (userTempRep.findById(token).isPresent() == false)
        {      	
            log.warn("tentativa de confirmar token nao encontrado: " + token);
            throw new BadRequestException("Token Inválido !");
        }
        
		//sem dados pois precisa confirma-los
        UserTempApi utm = new UserTempApi();
        utm.setToken(token);
        
        model.addAttribute("user", utm);
        return "user/confirm-form";
    } 

    /**
     * Finaliza a criação do novo usuario.
     */
    @PostMapping({"/confirm"})
    @Transactional
    public String confirmPost(@ModelAttribute("user") UserTempApi user, 
    		BindingResult result, Model model, Principal principal,
    		HttpServletRequest request) 
    {
    	//nao valida "UserTempModel" o formm pois os dados ja foram validados no registro.
        try
        {
            //Verificar se o usuario está logado. NAO PODE
            if (principal !=  null)
            {
                log.warn("usuario logado ["+principal.getName()+"] tentando confirmar token ["+user.getToken()+"]");
                throw new BadRequestException();
            }
                         
            userService.confirmUser(user.getEmail(),user.getPwd(),user.getToken());
            //Logar o usuario
            request.login(user.getEmail(), user.getPwd());
            //redirecionar para home login
            return "redirect:/";
        }
        catch (TokenInvalidoException e) {
        	throw new BadRequestException("Token Inválido !");
		}
        catch (TokenExpiradoException e) {
        	throw new BadRequestException("Token Expirado !");
		} 
        catch (EmailInvalidoException e) {
            user.setPwd("");
            model.addAttribute("user", user);
            result.rejectValue("email", "email", "Email incorreto."); 
            return "user/confirm-form";
		}
        catch (PasswordInvalidoException e) {
            //enviar erro de senha invalida
            user.setPwd("");
            model.addAttribute("user", user);
            result.rejectValue("pwd", "pwd", "Senha Incorreta."); 
            return "user/confirm-form";
		}
        catch (Exception e)
        {
            throw new InternalErrorException("falha inesperada",e);
        }
    }     
    

    /**
     * Inicia o processo de Reset de Senha
     */
    @GetMapping({"/reset-pwd"})
    public String resetPwd(Model model) 
    {
        model.addAttribute("email", new Email());
        return "user/reset-pwd-form";
    }
    
    @PostMapping({"/reset-pwd"})
    public String resetPwdPost(Model model, @Valid @ModelAttribute("email") Email email, BindingResult result) 
    {
        if (result.hasErrors())
        {
            log.debug("erro no formulario");
            return "user/reset-pwd-form";
        }
        
        //verificar existencia do email
        User userdb = userRep.findByEmail(email.getEmail());
        if (userdb == null)
        {
            log.warn("tentatica de reset de senha de email inesistente ["+email.getEmail()+"].");
            
            //NAO indicar erro ao usuario (1 - indica sucesso)
            //return "redirect:/user/open/reset-pwd?message=1";
            
            result.rejectValue("email", "email", "Email invalido.");
            return "user/reset-pwd-form";
        }
        
        try
        {
            userService.createResetTokenAndSendEmail(email.getEmail());
        }
        catch(SendEmailException e)
        {       	
            log.warn("Falhas enviando email de reset de senha: "+ e.getMessage(), e);
            //nao indicar erro ao usuario
            return "redirect:/user/open/reset-pwd?message=2";            
        }
        return "redirect:/user/open/reset-pwd?message=1";
    }
        
    @GetMapping({"/reset-confirm"})
    public String receiveResetToken(Model model, @RequestParam("token") String token) 
    {
    	try {
			userService.verifyResetToken(token);
		}
    	catch (TokenInvalidoException e) {
    		throw new BadRequestException("Token Inválido !");
		}
    	catch (TokenExpiradoException e) {
    		throw new BadRequestException("Token Expirado !");
		}
    	
        PwdReset pwd = new PwdReset();
        pwd.setToken(token);
        
        model.addAttribute("pwd", pwd);
        return "user/reset-confirm-form";
    }
    
    /**
     * Finaliza o processo de Reset de Senha
     */
    @PostMapping({"/reset-confirm"})
    @Transactional
    public String resetConfirmPost(Model model, @Valid @ModelAttribute("pwd")  PwdReset pwd, 
    		BindingResult result, HttpServletRequest request) 
    {
        if (pwd.getToken() == null)
        {
            log.warn("token nao encontrado");
            throw new BadRequestException("Operaçãp inválida.");
        }
        
        if (result.hasErrors())
        {
            log.debug("erro no formulario");
            return "user/reset-confirm-form";
        }
        
        if (pwd.getNewpwd1().equals(pwd.getNewpwd2()) == false)
        {
            result.rejectValue("newpwd2", "newpwd2", "Senhas não conferem."); //!!!! NAO funciona sem a declaracao "@ModelAttribute("user")"  acima.
            return "user/reset-confirm-form";
        }
        
        Principal principal = request.getUserPrincipal(); 
        if (principal !=  null)
        {
            log.warn("usuario logado tentando reset senha.");
            throw new BadRequestException("Operaçãp inválida.");
        }
        
		try {
			userService.finalizaResetPassword(pwd);
			return "redirect:/user/open/reset-confirm-resp"; 
		} 
		catch (TokenInvalidoException e) {
			throw new BadRequestException("Token Inválido !");
		}
		catch (NotFoundException e) {
			throw new BadRequestException("Usuário não encontrado.");
		}
    }   
    
    @GetMapping({"/reset-confirm-resp"})
    public String resetConfirm(Model model) 
    {
        return "user/reset-confirm-resp";
    }    
}
