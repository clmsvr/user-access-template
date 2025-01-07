package cms.lib;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Validação customizada para o Bean (não para uma propriedade do bean).
 * Verifica se dois campos de senha do bean são iguais. Simples.
 */

@Target({ ElementType.TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = { ValidaSenhasIguaisValidator.class })
public @interface ValidaSenhasIguais {

	String message() default "Novas Senhas devem ser Iguais.";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
	
	/** nome do primeiro campo a ser comparado */
	String senha1Field();
	
	/** nome do segundo campo a ser comparado */
	String senha2Field();
}
