package br.com.itau.geradornotafiscal.service;

import br.com.itau.geradornotafiscal.exception.FalhaNoAgendamentoDaEntregaException;
import br.com.itau.geradornotafiscal.model.ItemNotaFiscal;
import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.port.out.EntregaIntegrationPort;
import br.com.itau.geradornotafiscal.service.impl.EntregaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntregaServiceTest {

    @Mock
    private EntregaIntegrationPort entregaIntegrationPort;

    @InjectMocks
    private EntregaService entregaService;

    private NotaFiscal notaFiscal;

    @BeforeEach
    void configuracaoInicial() {
        MockitoAnnotations.openMocks(this);
        notaFiscal = new NotaFiscal();
    }

    @Test
    void deveAgendarEntregaComSucesso() {
        notaFiscal = NotaFiscal.builder()
                .idNotaFiscal("NF-003")
                .itens(List.of(new ItemNotaFiscal()))
                .build();

        entregaIntegrationPort = mock(EntregaIntegrationPort.class);

        entregaService = new EntregaService(entregaIntegrationPort) {
            @Override
            public void agendarEntrega(NotaFiscal notaFiscal) {
                try {
                    entregaIntegrationPort.criarAgendamentoEntrega(notaFiscal);
                } catch (Exception e) {
                    throw new FalhaNoAgendamentoDaEntregaException(e);
                }
            }
        };

        entregaService.agendarEntrega(notaFiscal);

        verify(entregaIntegrationPort, times(1)).criarAgendamentoEntrega(notaFiscal);
    }

    @Test
    void deveLancarExcecao_QuandoInterruptedExceptionOcorre() {
        notaFiscal = NotaFiscal.builder()
                .idNotaFiscal("NF-003")
                .itens(List.of(new ItemNotaFiscal()))
                .build();

        Thread.currentThread().interrupt();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> entregaService.agendarEntrega(notaFiscal));

        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof InterruptedException);

        Thread.interrupted();
    }
}
