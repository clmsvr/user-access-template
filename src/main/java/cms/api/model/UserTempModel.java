package cms.api.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTempModel
{
    @NotEmpty
    @NotNull
    @Size(max = 200)
    private String token;
    
    @Size(max=100)
    private String name;
    
    @NotEmpty (message="É necessário digirar seu email.") 
    @Pattern(regexp = "[\\w\\-]+(\\.[\\w\\-]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}", message="O formato do email parece ser inválido.")
    @Size(max=100)
    private String email; 
    
    @NotEmpty(message="É necessário digirar uma senha.") 
    @Pattern(regexp = "[^\\s]{6,}", message="A senha deve ter tamanho mínimo 6, sem espaços.")
    @Size(max=50)
    private String pwd;
    
    public UserTempModel()
    {
    }

    public UserTempModel(UserModel u, String pwd, String token)
    {
        this.email = u.getEmail();
        this.pwd = pwd;
        this.name = u.getName();
        this.token = token;
    }
}
