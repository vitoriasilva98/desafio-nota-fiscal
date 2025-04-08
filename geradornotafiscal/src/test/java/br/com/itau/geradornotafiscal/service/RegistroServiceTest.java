package br.com.itau.geradornotafiscal.service;

import br.com.itau.geradornotafiscal.model.ItemNotaFiscal;
import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.service.impl.RegistroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegistroServiceTest {

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
        NotaFiscal notaFiscal = NotaFiscal.builder()
                .idNotaFiscal("NF-003")
                .itens(List.of(new ItemNotaFiscal()))
                .build();

        Thread.currentThread().interrupt();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> registroService.registrarNotaFiscal(notaFiscal));

        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof InterruptedException);

        Thread.interrupted();
    }
}
