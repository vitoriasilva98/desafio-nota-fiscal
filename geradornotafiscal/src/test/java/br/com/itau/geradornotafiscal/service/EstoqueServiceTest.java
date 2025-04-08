package br.com.itau.geradornotafiscal.service;

import br.com.itau.geradornotafiscal.model.ItemNotaFiscal;
import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.service.impl.EstoqueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EstoqueServiceTest {

    private EstoqueService estoqueService;

    @BeforeEach
    void configuracaoInicial() {
        estoqueService = new EstoqueService();
    }

    @Test
    void deveDarBaixaNoEstoqueComSucesso() {
        NotaFiscal notaFiscal = NotaFiscal.builder()
                .idNotaFiscal("NF-001")
                .itens(List.of(new ItemNotaFiscal(), new ItemNotaFiscal()))
                .build();

        assertDoesNotThrow(() -> estoqueService.enviarNotaFiscalParaBaixaEstoque(notaFiscal));
    }

    @Test
    void deveLancarExcecao_QuandoInterromperBaixaDeEstoque() {
        NotaFiscal notaFiscal = NotaFiscal.builder()
                .idNotaFiscal("NF123")
                .build();

        Thread.currentThread().interrupt();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> estoqueService.enviarNotaFiscalParaBaixaEstoque(notaFiscal));

        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof InterruptedException);

        Thread.interrupted();
    }
}
