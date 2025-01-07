package cms.lib;

import org.springframework.beans.BeanUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;

/**
 * Imlementação da anoação "ValidaSenhasIguais".
 * Validação customizada para o Bean (não para uma propriedade do bean).
 * Verifica se dois campos de senha do bean são iguais. Simples.
 */

public class ValidaSenhasIguaisValidator implements ConstraintValidator<ValidaSenhasIguais, Object> {

	/** nome do primeiro campo a ser comparado */
	private String senha1Field;
	/** nome do segundo campo a ser comparado */
	private String senha2Field;
	
	@Override
	public void initialize(ValidaSenhasIguais constraint) {
		this.senha1Field = constraint.senha1Field();
		this.senha2Field = constraint.senha2Field();
	}
	
	@Override
	public boolean isValid(Object objetoValidacao, ConstraintValidatorContext context) {
		boolean valido = false;
		
		try {
			String senha1 = (String) BeanUtils.getPropertyDescriptor(objetoValidacao.getClass(), senha1Field)
					.getReadMethod().invoke(objetoValidacao);
			String senha2 = (String) BeanUtils.getPropertyDescriptor(objetoValidacao.getClass(), senha2Field)
					.getReadMethod().invoke(objetoValidacao);
			
			
			if (senha1 != null && senha1.equals(senha2)) {
				valido = true;
			}
			return valido;
		} 
		catch (Exception e) {
			throw new ValidationException(e);
		}
	}

}