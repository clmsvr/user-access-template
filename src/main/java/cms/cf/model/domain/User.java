package cms.cf.model.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class User
{
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;
	
    private String email; 
    private String pwd;
    private String name;
    private String city;
    private String state;
    
    private int    numBlocksSubtitled;
    private int    numBlocksTranslated;
    private String comment;        //descricao do proprio usuario
    
    private LocalDateTime   creationDate = LocalDateTime.now() ;
    private LocalDateTime   updateDate = LocalDateTime.now() ; 
    
	@ManyToMany
	@JoinTable(name = "user_has_role", 
			  joinColumns = @JoinColumn(name = "user_id"), 
			  inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles = new ArrayList<>();
}
