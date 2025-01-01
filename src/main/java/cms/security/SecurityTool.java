package cms.security;

import java.security.Principal;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

//How to find out the currently logged-in user in Spring Boot?
//https://stackoverflow.com/questions/31159075/how-to-find-out-the-currently-logged-in-user-in-spring-boot

@Component
public class SecurityTool {

	//precisa adicionar nos nomes das roles.
	//varios metodos da api http e spring adicionam isso ao comparar nomes de roles.
	//vc so precisa usar o prefixo ao cadastrar as roles com o usuario.
	//https://www.baeldung.com/spring-security-expressions
	public static final String ROLE_PREFIX = "ROLE_";
	
	public static final String SCOPE_PREFIX = "SCOPE_";
	
	
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/* 
	Sobre Anonymous Authentication (quando a pagina está "Permitida" para no controle de seguranca)
	
	By default anonymous users will be represented with an "AnonymousAuthenticationToken" 
	
	Note that there is no real conceptual difference between a user who is “anonymously authenticated” 
	and an unauthenticated user.
	
	https://docs.spring.io/spring-security/site/docs/4.2.12.RELEASE/apidocs/org/springframework/security/config/annotation/web/builders/HttpSecurity.html#anonymous--
	https://stackoverflow.com/questions/57053736/how-to-check-if-user-is-logged-in-or-anonymous-in-spring-security
    https://docs.spring.io/spring-security/reference/servlet/authentication/anonymous.html
	*/	
	public boolean isAnonymousAuthentication()
	{
		return getAuthentication() instanceof AnonymousAuthenticationToken;
	}
	
	public String getUserName() {		
		//eh um JWT porque estamos configurados como resouce server.
		//Jwt jwt = (Jwt) getAuthentication().getPrincipal();
		
		Object obj = getAuthentication().getPrincipal();
		if (obj instanceof Principal) {
		    return ((Principal) obj).getName();
		}
		if (obj instanceof UserDetails) {
		    return ((UserDetails) obj).getUsername();
		}
		else return "";
	}

	/**
	 * Atenção: 
	 * @return
	 */
	public boolean isAuthenticated() {
		return getAuthentication().isAuthenticated();
	}
	
	public boolean hasAuthority(String authorityName) {
		return getAuthentication().getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(authorityName));
	}	
	public boolean hasScope(String scope) {
		return hasAuthority(SCOPE_PREFIX+scope);
	}
	public boolean hasRole(String role) {
		return hasAuthority(ROLE_PREFIX+role);
	}	

}