package cms.domain.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class ResetToken
{
	@EqualsAndHashCode.Include
	@Id	
    private String token;
    private String email; 
    private Date   sysCreationDate = new Date();

}
