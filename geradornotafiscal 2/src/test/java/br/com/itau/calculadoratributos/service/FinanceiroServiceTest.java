package br.com.itau.calculadoratributos.service;

import br.com.itau.geradornotafiscal.exception.FalhaAoEnviarParaContasReceberException;
import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.service.impl.FinanceiroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FinanceiroServiceTest {

    private FinanceiroService financeiroService;

    @BeforeEach
    void configuracaoInicial() {
        financeiroService = new FinanceiroService();
    }

    @Test
    void deveEnviarNotaFiscalParaContasReceberComSucesso() {
        NotaFiscal notaFiscal = NotaFiscal.builder()
                .idNotaFiscal("123")
                .build();

        assertDoesNotThrow(() -> financeiroService.enviarNotaFiscalParaContasReceber(notaFiscal));
    }

    @Test
    void deveLancarExcecao_AoInterromperEnvioDeNotaFiscal() {
        NotaFiscal notaFiscal = NotaFiscal.builder()
                .idNotaFiscal("123")
                .build();

        FinanceiroService serviceComErro = new FinanceiroService() {
            @Override
            public void enviarNotaFiscalParaContasReceber(NotaFiscal notaFiscal) {
                throw new FalhaAoEnviarParaContasReceberException(new InterruptedException("Erro simulado"));
            }
        };

        FalhaAoEnviarParaContasReceberException ex = assertThrows(
                FalhaAoEnviarParaContasReceberException.class,
                () -> serviceComErro.enviarNotaFiscalParaContasReceber(notaFiscal)
        );

        assertTrue(ex.getCause() instanceof InterruptedException);
    }
}
