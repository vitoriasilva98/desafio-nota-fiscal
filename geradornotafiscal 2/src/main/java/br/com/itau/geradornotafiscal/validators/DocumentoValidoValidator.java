package br.com.itau.geradornotafiscal.validators;

import br.com.itau.geradornotafiscal.annotations.DocumentoValido;
import br.com.itau.geradornotafiscal.enums.TipoDocumento;
import br.com.itau.geradornotafiscal.model.Documento;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CNPJValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DocumentoValidoValidator implements ConstraintValidator<DocumentoValido, Documento> {

    @Override
    public boolean isValid(Documento documento, ConstraintValidatorContext context) {
        if (documento == null) return true;

        String numero = documento.getNumero();
        TipoDocumento tipo = documento.getTipo();

        if (numero == null || tipo == null) return true;

        boolean valido = true;
        String mensagem = null;

        if (tipo == TipoDocumento.CPF) {
            CPFValidator cpfValidator = new CPFValidator();
            cpfValidator.initialize(null);

            valido = cpfValidator.isValid(numero, context);
            if (!valido) mensagem = "CPF inválido";
        } else if (tipo == TipoDocumento.CNPJ) {
            CNPJValidator cnpjValidator = new CNPJValidator();
            cnpjValidator.initialize(null);
            valido = cnpjValidator.isValid(numero, context);
            if (!valido) mensagem = "CNPJ inválido";
        }

        if (!valido) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(mensagem)
                    .addPropertyNode("numero")
                    .addConstraintViolation();
        }

        return valido;
    }
}
