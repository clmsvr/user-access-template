package cms.cf.controller;

import java.security.Principal;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cms.cf.model.domain.User;
import cms.cf.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("")
public class HomeController 
{
	
//	@Autowired
//	private SecurityTool securityTool;  
	
	@Autowired
	private UserRepository userRep;
	
   
    /**
     * inicia a acao de logout.
     * O path "/logout" é configurado no Spring em "SecurityConfig".
     * O spring implementa a acao de "/logout" e redireciona para o path configurado: "/"
     */
    @GetMapping({"/initlogout"})
    public String initlogout() 
    {
        return "redirect:/logout"; //está configurado no "SecurityConfig"
    }  
    
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
    
    /**
     * inicia a acao de login.
     * "/initlogin" poderia estar junto de root (""), mas no final a url no browser 
     * teria o parh "/initlogin" e fica feio. 
     */
    @GetMapping({"/initlogin"})
    public String initlogin() 
    {
        return "redirect:/"; 
    }  
    
    //public String root(Model model, HttpServletRequest request)
    //public String root(Model model, @AuthenticationPrincipal User user) //this will work even in WebFlux reactive environment versus the SecurityContextHolder.getContext().getAuthentication() which won't work because of paradigm shift from thread per request model to multiple requests per thread.
    //public String root(Model model, Authentication  a)
    //public String root(Model model, Principal  p)
    //"initlogin" esta protegico e o spring vai iniciar o processo de login
    
    //@GetMapping({ "" })
    //@GetMapping("")
    @GetMapping
    public String root(Model model, Principal p, HttpServletRequest request) 
    throws SQLException
    {
    	//if (securityTool.isAuthenticated() == false)  errado, pois o usuario pode ser anonimo, já que a pagina raiz está "permitida"
    	
    	//verificatt se o cokie de sessao recebido ainda tem um usuario cadastrado no banco.
    	User u = null;
    	if (p != null) {
    		u = userRep.findByEmail(p.getName());
    		if (u == null) {
    			try{request.logout();}catch(Exception e) {} //remover a sessao.
    			System.out.println("SESSAO removida.");
    		}
    	}
    	
    	
    	if (u == null )//|| securityTool.isAnonymousAuthentication())
    		return welcome(model);
    	else
    		return home(model);
    }
    
    private String welcome(Model model)
    { 	
        return "welcome";
    }

    private String home(Model model) 
    throws SQLException 
    {
        //if (securityTool.hasRole("worker") || securityTool.hasRole("admin"))
    	//tring email = securityTool.getUserName();
    	

        return "home";
    }
}
