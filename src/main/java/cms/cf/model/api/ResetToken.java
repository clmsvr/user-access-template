package cms.cf.model.api;

import java.util.Date;

import lombok.Data;

@Data
public class ResetToken
{
    private String token;
    private String email; 
    private Date   sysCreationDate;

}
