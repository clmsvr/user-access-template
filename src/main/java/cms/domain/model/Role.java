package cms.domain.model;

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
@Entity
public class Role
{
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;	
    private String name;
    private String description;
    
    @ManyToMany
	@JoinTable(name = "role_has_permission", 
	  joinColumns = @JoinColumn(name = "role_id"), 
	  inverseJoinColumns = @JoinColumn(name = "permission_id"))	
	private List<Permission> permissions = new ArrayList<>();
}
