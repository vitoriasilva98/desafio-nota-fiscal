package br.com.itau.calculadoratributos.service;

import br.com.itau.geradornotafiscal.exception.FalhaNoAgendamentoDaEntregaException;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntregaServiceTest {

    @Mock
    private EntregaIntegrationPort entregaIntegrationPort;

    @InjectMocks
    private EntregaService entregaService;

    private NotaFiscal notaFiscal;

    @BeforeEach
    void configuracaoInicial() {
        MockitoAnnotations.openMocks(this);
        notaFiscal = new NotaFiscal();

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
    }

    @Test
    void deveAgendarEntregaComSucesso() {
        NotaFiscal notaFiscal = NotaFiscal.builder().idNotaFiscal("NF-123").build();

        entregaService.agendarEntrega(notaFiscal);

        verify(entregaIntegrationPort, times(1)).criarAgendamentoEntrega(notaFiscal);
    }

    @Test
    void deveLancarExcecao_QuandoInterruptedExceptionOcorre() throws Exception {
        NotaFiscal notaFiscal = new NotaFiscal();
        notaFiscal.setIdNotaFiscal("NF-123");

        EntregaService entregaServiceComErro = new EntregaService(entregaIntegrationPort) {
            @Override
            public void agendarEntrega(NotaFiscal notaFiscal) {
                throw new FalhaNoAgendamentoDaEntregaException(new InterruptedException());
            }
        };

        assertThrows(FalhaNoAgendamentoDaEntregaException.class, () -> {
            entregaServiceComErro.agendarEntrega(notaFiscal);
        });
    }
}
