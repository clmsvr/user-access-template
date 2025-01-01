package cms.api.model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotEmpty;

public class PwdChange
{
    @NotEmpty(message="É necessário digirar sua senha.") 
    @Size(max=50)
    private String pwd;
    
    @NotEmpty(message="É necessário digirar a nova senha.") 
    @Pattern(regexp = "[^\\s]{6,}", message="A senha deve ter tamanho mínimo 6, sem espaços.")
    @Size(max=50)
    private String newpwd1;
    
    @NotEmpty(message="É necessário confirmar a senha.") 
    @Size(max=50)
    private String newpwd2;
    
    public PwdChange()
    {
    }


    public void reset()
    {
        pwd = "";
        newpwd1 = "";
        newpwd2 = "";
    }
    
    public String getPwd()
    {
        return pwd;
    }

    public void setPwd(String pwd)
    {
        this.pwd = pwd;
    }

    public String getNewpwd1()
    {
        return newpwd1;
    }

    public void setNewpwd1(String newpwd1)
    {
        this.newpwd1 = newpwd1;
    }

    public String getNewpwd2()
    {
        return newpwd2;
    }

    public void setNewpwd2(String newpwd2)
    {
        this.newpwd2 = newpwd2;
    }


}
