package br.com.itau.geradornotafiscal.validators;

import br.com.itau.geradornotafiscal.enums.TipoDocumento;
import br.com.itau.geradornotafiscal.model.Documento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentoValidoValidatorTest {

    private DocumentoValidoValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintViolationBuilder violationBuilder;

    @BeforeEach
    void configuracaoInicial() {
        MockitoAnnotations.openMocks(this);
        validator = new DocumentoValidoValidator();
    }

    @Test
    void deveRetornarTrue_QuandoDocumentoForNull() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    void deveRetornarTrue_QuandoNumeroForNull() {
        Documento doc = new Documento(null, TipoDocumento.CPF);
        assertTrue(validator.isValid(doc, context));
    }

    @Test
    void deveRetornarTrue_QuandoTipoForNull() {
        Documento doc = new Documento("12345678900", null);
        assertTrue(validator.isValid(doc, context));
    }

    @Test
    void deveRetornarFalse_QuandoCpfForInvalido() {
        Documento doc = new Documento("12345678900", TipoDocumento.CPF);

        when(context.buildConstraintViolationWithTemplate("CPF inválido")).thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode("numero")).thenReturn(
                mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

        boolean result = validator.isValid(doc, context);

        assertFalse(result);
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("CPF inválido");
    }

    @Test
    void deveRetornarFalse_QuandoCnpjForInvalido() {
        Documento doc = new Documento("12345678000199", TipoDocumento.CNPJ);

        when(context.buildConstraintViolationWithTemplate("CNPJ inválido")).thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode("numero")).thenReturn(
                mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

        boolean result = validator.isValid(doc, context);

        assertFalse(result);
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("CNPJ inválido");
    }

    @Test
    void deveRetornarTrue_QuandoCpfForValido() {
        Documento doc = new Documento("39053344705", TipoDocumento.CPF);
        assertTrue(validator.isValid(doc, context));
    }

    @Test
    void deveRetornarTrue_QuandoCnpjForValido() {
        Documento doc = new Documento("49695613000180", TipoDocumento.CNPJ);
        assertTrue(validator.isValid(doc, context));
    }
}
