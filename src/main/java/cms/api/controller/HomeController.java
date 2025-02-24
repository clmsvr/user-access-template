package cms.api.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cms.domain.model.User;
import cms.domain.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("")
public class HomeController 
{
	@Autowired
	private UserRepository userRep;
    
    /**
     * O path "/login" é configurado no Spring em "SecurityConfig".
     * Para customizar a pagina do formulario de login, nós devemos implementar a ação 
     * para quando o Spring redirecionar o usuario para o formulario de login.
     * A ação do POST do formulario de login não eh implementada por nós.        
     * @return
     */
    @GetMapping({ "/login" })
    public String login()
    {
    	return "login";
    }

//    /**
//     * URL configurada em "SecurityConfig.class" como a url redirecionada pelo Spring
//     * em caso de sucesso no processo de login.
//     * Default seria "/", mas assim nos dá o controle dinâmico do processo.
//     * @return
//     */
//	@GetMapping("/successLogin")
//	public String successLogin() {
//		//poderia verificar as roles e redirecionar para a pagina correta.
//		return "redirect:/";
//	}
    
    //public String root(Model model, HttpServletRequest request)
    //public String root(Model model, @Authentication Principal User user) //this will work even in WebFlux reactive environment versus the SecurityContextHolder.getContext().getAuthentication() which won't work because of paradigm shift from thread per request model to multiple requests per thread.
    //public String root(Model model, Authentication  a)
    //public String root(Model model, Principal  p)
    //
    
    @GetMapping
    public String root(Model model, Principal p, HttpServletRequest request) 
    {
    	User u = null;
    	
    	//verificar se o cookie de sessao recebido ainda tem um usuario cadastrado no banco.
    	if (p != null){
    		u = userRep.findByEmail(p.getName());
    		if (u == null) { // existe sessão mas sem usuario cadastrado
    			try{request.logout();}catch(Exception e) {} //remover a sessao.
    			System.out.println("SESSAO removida.");
    		}
    	}
    	
    	if (u == null )
    		return welcome(model);
    	else
    		return home(model);
    }
    
    private String welcome(Model model)
    { 	
        return "welcome";
    }

    private String home(Model model) 
    {
        return "home";
    }
}
