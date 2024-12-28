package cms.cf.security;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

// https://www.baeldung.com/spring-security-expressions
// Now let's explore the security expressions:
//
// hasRole, hasAnyRole
// hasAuthority, hasAnyAuthority
// permitAll, denyAll
// isAnonymous, isRememberMe, isAuthenticated, isFullyAuthenticated
// principal, authentication
// hasPermission



//23.23. Simplificando o controle de acesso em métodos com meta-anotações
public @interface SecurityAnnotations {

	public @interface Cozinhas {
		
		//@PreAuthorize("hasAnyRole('ADMIN', 'CLIENT') and hasAuthority('SCOPE_read')")
		
		@PreAuthorize("hasAuthority('SCOPE_write') and hasAuthority('EDITAR_COZINHAS')")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeEditar { }

		@PreAuthorize("hasAuthority('SCOPE_read') and isAuthenticated()")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeConsultar { }
		
	}
	
	public @interface Restaurantes {
		
		@PreAuthorize("hasAuthority('SCOPE_write') and hasAuthority('EDITAR_RESTAURANTES')")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeEditar { }

		@PreAuthorize("hasAuthority('SCOPE_read') and isAuthenticated()")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeConsultar { }
		
		//23.28. Restringindo acessos de forma contextual (sensível à informação)
		@PreAuthorize("hasAuthority('SCOPE_write') and "
				+ "(hasAuthority('EDITAR_RESTAURANTES') or "
				+ "@algaSecurity.gerenciaRestaurante(#restauranteId))")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeGerenciarFuncionamento { }
	}
	
    public @interface Pedidos {
		
    	//23.29. Restringindo acessos com @PostAuthorize
		@PreAuthorize("hasAuthority('SCOPE_read') and isAuthenticated()")
		@PostAuthorize("""
				hasAuthority('CONSULTAR_PEDIDOS') or
				@algaSecurity.getUsuarioId() == returnObject.cliente.id or
				@algaSecurity.gerenciaRestaurante(returnObject.restaurante.id) """)
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeBuscar { }
		
		
		@PreAuthorize("""
				      hasAuthority('SCOPE_read') and 
				      (hasAuthority('CONSULTAR_PEDIDOS') or 
				      @algaSecurity.getUsuarioId() == #filtro.clienteId or 
				      @algaSecurity.gerenciaRestaurante(#filtro.restauranteId) ) """)
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodePesquisar { }
	
		@PreAuthorize("hasAuthority('SCOPE_write') and isAuthenticated()")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeCriar { }		
		
		//poderia ficar assim apos aula 23.39: Gerando links do HAL dinamicamente de acordo com permissões do usuário
		//@PreAuthorize("@algaSecurity.podeGerenciarPedidos(#codigoPedido) )")
		@PreAuthorize("""
			      hasAuthority('SCOPE_write') and 
			      (hasAuthority('GERENCIAR_PEDIDOS') or 
			      @algaSecurity.gerenciaRestauranteDoPedido(#codigoPedido) ) """)
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeGerenciarPedido { }
		
		@PreAuthorize("hasAuthority('SCOPE_write') and hasAuthority('GERENCIAR_PEDIDOS') ")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeEditar { }
	}
    
    public @interface FormasPagamento {

        @PreAuthorize("hasAuthority('SCOPE_write') and hasAuthority('EDITAR_FORMAS_PAGAMENTO')")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface PodeEditar { }

        @PreAuthorize("hasAuthority('SCOPE_read') and isAuthenticated()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface PodeConsultar { }
        
    }
    
    public @interface Cidades {

        @PreAuthorize("hasAuthority('SCOPE_write') and hasAuthority('EDITAR_CIDADES')")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface PodeEditar { }

        @PreAuthorize("hasAuthority('SCOPE_read') and isAuthenticated()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface PodeConsultar { }
        
    }

    public @interface Estados {
        
        @PreAuthorize("hasAuthority('SCOPE_write') and hasAuthority('EDITAR_ESTADOS')")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface PodeEditar { }

        @PreAuthorize("hasAuthority('SCOPE_read') and isAuthenticated()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface PodeConsultar { }
        
    }

    //para testes - leberado com token client_credential
    public @interface UsuariosGruposPermissoes____testes {

        @PreAuthorize("hasAuthority('SCOPE_write')")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface PodeAlterarPropriaSenha { }
        
        @PreAuthorize("hasAuthority('SCOPE_write')")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface PodeAlterarUsuario { }

        @PreAuthorize("hasAuthority('SCOPE_write') ")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface PodeEditar { }
        

        @PreAuthorize("hasAuthority('SCOPE_read')")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface PodeConsultar { }
        
    }  
    
    public @interface UsuariosGruposPermissoes {

        @PreAuthorize("hasAuthority('SCOPE_write') and "
                + "@algaSecurity.getUsuarioId() == #usuarioId")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface PodeAlterarPropriaSenha { }
        
        @PreAuthorize("hasAuthority('SCOPE_write') and (hasAuthority('EDITAR_USUARIOS_GRUPOS_PERMISSOES') or "
                + "@algaSecurity.getUsuarioId() == #usuarioId)")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface PodeAlterarUsuario { }

        @PreAuthorize("hasAuthority('SCOPE_write') and hasAuthority('EDITAR_USUARIOS_GRUPOS_PERMISSOES')")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface PodeEditar { }
        

        @PreAuthorize("hasAuthority('SCOPE_read') and hasAuthority('CONSULTAR_USUARIOS_GRUPOS_PERMISSOES')")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface PodeConsultar { }
        
    }    
    
    public @interface Estatisticas {

        @PreAuthorize("hasAuthority('SCOPE_read') and "
                + "hasAuthority('GERAR_RELATORIOS')")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface PodeConsultar { }
        
    }    
}