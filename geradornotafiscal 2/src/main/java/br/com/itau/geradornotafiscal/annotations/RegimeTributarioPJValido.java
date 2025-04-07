package br.com.itau.geradornotafiscal.annotations;

import br.com.itau.geradornotafiscal.validators.RegimeTributarioPJValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RegimeTributarioPJValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RegimeTributarioPJValido {
    String message() default "RegimeTributárioPJ é obrigatório para pessoa jurídica";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
