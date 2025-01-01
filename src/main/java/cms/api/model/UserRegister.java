package cms.api.model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotEmpty;


@Getter
@Setter
public class UserRegister
{
    @NotEmpty(message="É necessário digitar seu nome.")
    @Size(max=100)
    private String name;
    
    @NotEmpty (message="É necessário digirar seu email.") 
    @Pattern(regexp = "[\\w\\-]+(\\.[\\w\\-]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}", message="O formato do email parece ser inválido.")
    @Size(max=100)
    private String email; 

    @NotEmpty(message="É necessário digirar a nova senha.") 
    @Pattern(regexp = "[^\\s]{6,}", message="A senha deve ter tamanho mínimo 6, sem espaços.")
    @Size(max=50)    
    private String pwd;
}
