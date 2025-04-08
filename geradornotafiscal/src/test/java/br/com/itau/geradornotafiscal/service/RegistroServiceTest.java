package br.com.itau.geradornotafiscal.service;

import br.com.itau.geradornotafiscal.exception.FalhaNoRegistroNotaFiscalException;
import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.service.impl.RegistroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RegistroServiceTest {

    private RegistroService registroService;

    @BeforeEach
    void configuracaoInicial() {
        registroService = new RegistroService();
    }

    @Test
    void deveRegistrarNotaFiscalComSucesso() {
        NotaFiscal notaFiscal = NotaFiscal.builder()
                .idNotaFiscal("123")
                .build();

        assertDoesNotThrow(() -> registroService.registrarNotaFiscal(notaFiscal));
    }

    @Test
    void deveLancarFalhaNoRegistroNotaFiscalException_QuandoInterruptedExceptionOcorre() {
        registroService = new RegistroService() {
            @Override
            public void registrarNotaFiscal(NotaFiscal notaFiscal) {
                throw new FalhaNoRegistroNotaFiscalException(new InterruptedException("Simulado"));
            }
        };

        NotaFiscal notaFiscal = new NotaFiscal();
        notaFiscal.setIdNotaFiscal("NF-123");

        FalhaNoRegistroNotaFiscalException exception = assertThrows(
                FalhaNoRegistroNotaFiscalException.class,
                () -> registroService.registrarNotaFiscal(notaFiscal)
        );

        assertTrue(exception.getCause() instanceof InterruptedException);
        assertEquals("Simulado", exception.getCause().getMessage());
    }
}
