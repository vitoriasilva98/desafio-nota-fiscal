package br.com.itau.geradornotafiscal.adapter.out;

import br.com.itau.geradornotafiscal.model.ItemNotaFiscal;
import br.com.itau.geradornotafiscal.model.NotaFiscal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EntregaIntegrationAdapterTest {

    private EntregaIntegrationAdapter entregaIntegrationAdapter;

    @BeforeEach
    void configuracaoInicial() {
        entregaIntegrationAdapter = new EntregaIntegrationAdapter();
    }

    @Test
    void deveCriarAgendamentoEntregaComSucesso() {
        NotaFiscal notaFiscal = NotaFiscal.builder()
                .idNotaFiscal("NF-001")
                .itens(List.of(new ItemNotaFiscal(), new ItemNotaFiscal()))
                .build();

        assertDoesNotThrow(() -> entregaIntegrationAdapter.criarAgendamentoEntrega(notaFiscal));
    }

    @Test
    void deveCriarAgendamentoEntregaComMaisDeCincoItens() {
        List<ItemNotaFiscal> itens = List.of(
                new ItemNotaFiscal(), new ItemNotaFiscal(), new ItemNotaFiscal(),
                new ItemNotaFiscal(), new ItemNotaFiscal(), new ItemNotaFiscal()
        );

        NotaFiscal notaFiscal = NotaFiscal.builder()
                .idNotaFiscal("NF-002")
                .itens(itens)
                .build();

        assertDoesNotThrow(() -> entregaIntegrationAdapter.criarAgendamentoEntrega(notaFiscal));
    }

    @Test
    void deveLancarExcecao_QuandoThreadForInterrompida() {
        NotaFiscal notaFiscal = NotaFiscal.builder()
                .idNotaFiscal("NF-003")
                .itens(List.of(new ItemNotaFiscal()))
                .build();

        Thread.currentThread().interrupt();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> entregaIntegrationAdapter.criarAgendamentoEntrega(notaFiscal));

        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof InterruptedException);

        Thread.interrupted();
    }
}
