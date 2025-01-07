package cms.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration //nao eh necessario pq a anotacao @EnableWebSecurity deriva de @Configuration
@EnableWebSecurity //permite que nossa configuracao substitua as configurações default de seguranca dos Starters do Spring Security - https://stackoverflow.com/questions/44671457/what-is-the-use-of-enablewebsecurity-in-spring
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain1(HttpSecurity http) throws Exception 
	{
		http
			.authorizeHttpRequests((requests) -> 
				requests
					.requestMatchers("/","/user/open/*","/bootstrap/**","/css/**","/fonts/**","/image/**","/js/**")
					.permitAll()
					.requestMatchers("/user/mng/*").hasAnyRole("Admin","Worker","login")
					.requestMatchers("/worker/*").hasAnyRole("Admin","Worker")
					.requestMatchers("/admin/*").hasRole("Admin")
					.anyRequest().authenticated()
			)
			.formLogin((form) -> 
			    form
					.loginPage("/login")
					//.defaultSuccessUrl("/successLogin", true) //"true" força o redirecionamento para a url indicada. Caso contrario, iria para a url protegida clicada pelo usuario.
					.permitAll()
			)
			.logout((logout) -> 
				logout
				    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				    .logoutSuccessUrl("/")
					.permitAll()
			);
		return http.build();
	}


	
	@Bean
	PasswordEncoder passwordEncoder() {
		//return new BCryptPasswordEncoder();
		return new sha512HexPasswordEncoder();
	}

}