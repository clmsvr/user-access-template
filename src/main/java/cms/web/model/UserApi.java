package cms.web.model;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserApi
{
    @NotEmpty (message="É necessário digirar seu email.") 
    @Pattern(regexp = "[\\w\\-]+(\\.[\\w\\-]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}", message="O formato do email parece ser inválido.")
    @Size(max=100)
    private String email; 

// o formulario de usuario nao atualiza sennha    
//    @NotEmpty(message="É necessário digirar uma senha.") 
//    @Pattern(regexp = "[^\\s]{6,}", message="A senha deve ter tamanho mínimo 6, sem espaços.")
//    @Size(max=50)
//    private String pwd;
    
    @NotEmpty(message="É necessário digitar seu nome.")
    @Size(max=100)
    private String name;
    
    @Size(max=100)
    private String city;
    
    @Size(message="Valor do Estado incorreto", min=0, max=2)
    private String state;
    
    private int    numBlocksSubtitled;
    private int    numBlocksTranslated;
    private String comment;        //descricao do proprio usuario
    private LocalDateTime   creationDate;
    private LocalDateTime   updateDate;

    //@DateTimeFormat(pattern="MM/dd/yyyy")
    //@Pattern(regexp = "\\(?\\b([0-9]{2})\\)?[-. ]?([0-9]{4})[-. ]?([0-9]{4})\\b", message="Telefone em formato incorreto")
    //private String telefone;
    //@Range(min = 1, max = 150)
    //int age;
}
