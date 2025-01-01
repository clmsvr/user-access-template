package cms.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cms.domain.model.Permission;
import cms.domain.model.Role;
import cms.domain.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRep;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) 
	throws UsernameNotFoundException 
	{
		cms.domain.model.User u = null;
		
//		u =  userRep.findById(10L)
//				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com id informado"));
        
		u = userRep.findByEmail(username); 
		if (u == null)
			throw new UsernameNotFoundException("Usuário não encontrado com e-mail informado");
		
		return new User(username, u.getPwd(), getAuthorities(u) );
	}
	
	private Collection<GrantedAuthority> getAuthorities(cms.domain.model.User u) 
	{
		List<Role> roles = u.getRoles();

		Collection<GrantedAuthority> authorities =  roles.stream()
				.map(r -> new SimpleGrantedAuthority(SecurityTool.ROLE_PREFIX+r.getName()))
				.collect(Collectors.toSet());
		
		for (Role role : roles) {
			for (Permission p : role.getPermissions()) {
				authorities.add(new SimpleGrantedAuthority(p.getName()));
			}
		}
		
		return authorities;
	}

}