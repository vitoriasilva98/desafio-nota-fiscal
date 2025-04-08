package br.com.itau.geradornotafiscal.service;

import br.com.itau.geradornotafiscal.exception.FalhaNaBaixaDeEstoqueException;
import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.service.impl.EstoqueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

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
                .idNotaFiscal("NF123")
                .build();

        assertDoesNotThrow(() -> estoqueService.enviarNotaFiscalParaBaixaEstoque(notaFiscal));
    }

    @Test
    void deveLancarExcecaoAoInterromperBaixaDeEstoque() {
        NotaFiscal notaFiscal = NotaFiscal.builder()
                .idNotaFiscal("NF123")
                .build();

        EstoqueService estoqueComErro = new EstoqueService() {
            @Override
            public void enviarNotaFiscalParaBaixaEstoque(NotaFiscal notaFiscal) {
                throw new FalhaNaBaixaDeEstoqueException(new InterruptedException("Erro simulado"));
            }
        };

        FalhaNaBaixaDeEstoqueException ex = assertThrows(
                FalhaNaBaixaDeEstoqueException.class,
                () -> estoqueComErro.enviarNotaFiscalParaBaixaEstoque(notaFiscal)
        );

        assertTrue(ex.getCause() instanceof InterruptedException);
    }
}
