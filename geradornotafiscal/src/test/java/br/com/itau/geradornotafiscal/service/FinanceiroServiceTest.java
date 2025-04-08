package br.com.itau.geradornotafiscal.service;

import br.com.itau.geradornotafiscal.model.ItemNotaFiscal;
import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.service.impl.FinanceiroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FinanceiroServiceTest {

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
    void deveLancarExcecao_QuandoInterromperEnvioDeNotaFiscal() {
        NotaFiscal notaFiscal = NotaFiscal.builder()
                .idNotaFiscal("NF-003")
                .itens(List.of(new ItemNotaFiscal()))
                .build();

        Thread.currentThread().interrupt();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> financeiroService.enviarNotaFiscalParaContasReceber(notaFiscal));

        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof InterruptedException);

        Thread.interrupted();
    }
}
