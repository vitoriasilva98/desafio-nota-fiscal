package br.com.itau.geradornotafiscal.validators;

import br.com.itau.geradornotafiscal.enums.RegimeTributacaoPJ;
import br.com.itau.geradornotafiscal.enums.TipoPessoa;
import br.com.itau.geradornotafiscal.model.Destinatario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegimeTributarioPJValidatorTest {

    private RegimeTributarioPJValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @BeforeEach
    void configuracaoInicial() {
        MockitoAnnotations.openMocks(this);
        validator = new RegimeTributarioPJValidator();
    }

    @Test
    void deveRetornarTrue_QuandoDestinatarioForNull() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    void deveRetornarTrue_QuandoPessoaFisicaNaoTiverRegimeTributario() {
        Destinatario destinatario = new Destinatario();
        destinatario.setTipoPessoa(TipoPessoa.FISICA);
        destinatario.setRegimeTributacao(null);

        assertTrue(validator.isValid(destinatario, context));
    }

    @Test
    void deveRetornarTrue_QuandoPessoaJuridicaTiverRegimeTributario() {
        Destinatario destinatario = new Destinatario();
        destinatario.setTipoPessoa(TipoPessoa.JURIDICA);
        destinatario.setRegimeTributacao(RegimeTributacaoPJ.SIMPLES_NACIONAL);

        assertTrue(validator.isValid(destinatario, context));
    }

    @Test
    void deveRetornarFalse_QuandoPessoaJuridicaNaoTiverRegimeTributario() {
        Destinatario destinatario = new Destinatario();
        destinatario.setTipoPessoa(TipoPessoa.JURIDICA);
        destinatario.setRegimeTributacao(null);

        when(context
                .buildConstraintViolationWithTemplate(anyString()))
                .thenReturn(violationBuilder);
        when(violationBuilder
                .addPropertyNode(anyString()))
                .thenReturn(mock(ConstraintValidatorContext
                        .ConstraintViolationBuilder
                        .NodeBuilderCustomizableContext.class
                        )
                );

        boolean result = validator.isValid(destinatario, context);

        assertFalse(result);
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("O regime tributário é obrigatório para o tipo de pessoa jurídica");
    }
}
