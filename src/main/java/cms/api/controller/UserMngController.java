package cms.api.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cms.api.exceptions.BadRequestException;
import cms.api.model.PwdChange;
import cms.api.model.UserApi;
import cms.domain.exceptions.NotFoundException;
import cms.domain.exceptions.PasswordInvalidoException;
import cms.domain.model.User;
import cms.domain.repository.UserRepository;
import cms.domain.service.UserMngService;
import cms.lib.ValidaSenhasIguais;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user/mng")
public class UserMngController 
{ 
	@Autowired
	private UserRepository userRep;
	@Autowired
	private UserMngService userService;
	@Autowired
	private ModelMapper modelMapper;

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

    @GetMapping({"/account"})
    public String account(Model model, Principal principal) 
    {
//        //Usuario TEM que estar logado. Acesso restrito
//        String email = securityTool.getUserName();

        //Usuario TEM que estar logadob(principal !- null). Acesso restrito
        String email = principal.getName();
    	
        //aqui o username é o email.
        User user = userRep.findByEmail(email);
        
        UserApi userModel = new UserApi();
        modelMapper.map(user, userModel);
        
        //userModel.setPwd(""); //nao tem senha nessa view
        model.addAttribute("user", userModel);
        model.addAttribute("states", states);
        return "user/account-form";
    }
    
    @PostMapping({"/account"})
    @Transactional
    public String accountPost(
    		Model model, 
    		@Valid @ModelAttribute("user") UserApi userModel, 
    		BindingResult result, 
    		Principal p) 
    {
        if (result.hasErrors())
        {
            log.debug("erro no formulario");
            
//			for (Object object : result.getAllErrors())
//			{
//				if (object instanceof FieldError)
//				{
//				    FieldError fieldError = (FieldError) object;
//				    System.out.println("FieldError"+ fieldError.getField() + ":" + fieldError.getCode());
//				}
//				if (object instanceof ObjectError)
//				{
//				      ObjectError objectError = (ObjectError) object;
//				      System.out.println("ObjectError"+ objectError.getObjectName() + ":" + objectError.getCode());
//				}
//			}
         
            model.addAttribute("states", states);
            
            return "user/account-form";
        }

    	userService.updateUser(userModel,p.getName());
    	
        return "redirect:/user/mng/account?message=1";
    }    
    
    @GetMapping({"/change-pwd"})
    public String changePwd(Model model) 
    {
        model.addAttribute("pwd", new PwdChange());
        return "user/change-pwd-form";
    }
    
    @PostMapping({"/change-pwd"})
    public String changePwdPost(Model model,@Valid @ModelAttribute("pwd") PwdChange pwd, 
    		BindingResult result, Principal principal) 
    {
        if (result.hasErrors())
        {
            pwd.reset();
            log.debug("erro no formulario");
            return "user/change-pwd-form";
        }
        
        //Com a anotação "@ValidaSenhasIguais" no Bean, nao precisa deste teste.
        if (pwd.getNewpwd1().equals(pwd.getNewpwd2()) == false)
        {
            pwd.reset();
            //result.rejectValue("newpwd2", "newpwd2", "Senhas não conferem."); 
            result.reject("senhas-diff", "Senhas não conferem."); 
            return "user/change-pwd-form";
        }
        
        //String username = securityTool.getUserName();
        if (principal ==  null)
        {
            log.warn("usuario NAO logado tentando alterar senha.");
            throw new BadRequestException();
        }
        
 	
        try {
			userService.changeUserPassword(pwd, principal.getName());
			return "redirect:/user/mng/change-pwd?message=1"; 
		} 
        catch (NotFoundException e) {
        	throw new BadRequestException("Inconsistencia: usuario logado nao encontrado: "+principal.getName());
		} 
        catch (PasswordInvalidoException e) {
            //enviar erro de senha invalida
            pwd.reset();
            result.rejectValue("pwd", "pwd", "Senha Incorreta."); 
            return "user/change-pwd-form";
		}
    }       
}
