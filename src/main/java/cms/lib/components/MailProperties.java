package cms.lib.components;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Validated
@Component
@ConfigurationProperties("mail")
public class MailProperties {

	@NotNull
	private String sender;
	
	@NotBlank
	private String password;
	
	@NotBlank
	private String smtpHost;

	private String smtpPort = "465";
	
	@NotBlank
	private String confirmUserUrl;
	
	@NotBlank
	private String confirmResetUrl;
				
}