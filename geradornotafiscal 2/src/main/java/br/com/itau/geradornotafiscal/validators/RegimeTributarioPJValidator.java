package br.com.itau.geradornotafiscal.validators;

import br.com.itau.geradornotafiscal.annotations.RegimeTributarioPJValido;
import br.com.itau.geradornotafiscal.enums.TipoPessoa;
import br.com.itau.geradornotafiscal.model.Destinatario;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RegimeTributarioPJValidator implements ConstraintValidator<RegimeTributarioPJValido, Destinatario> {

    @Override
    public boolean isValid(Destinatario destinatario, ConstraintValidatorContext constraintValidatorContext) {
        if (destinatario == null) return true;

        if (destinatario.getTipoPessoa() == TipoPessoa.JURIDICA && destinatario.getRegimeTributacao() == null) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("O regime tributário é obrigatório para o tipo de pessoa jurídica")
                    .addPropertyNode("regimeTributacao")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
