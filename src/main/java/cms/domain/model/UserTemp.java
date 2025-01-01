package cms.domain.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class UserTemp
{
	@EqualsAndHashCode.Include
	@Id
    private String token;
   
    private String email; 
    private String pwd;
    private String name;
    
    private int acessos = 0;
    private boolean emailSent = false;
    
    private Date   sysCreationDate = new Date();

    public UserTemp()
    {
    }

    public UserTemp(String name, String email, String pwd, String token)
    {
        this.email = email;
        this.pwd = pwd;
        this.name = name;
        this.token = token;
    }
}
