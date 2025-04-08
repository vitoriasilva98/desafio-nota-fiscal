package br.com.itau.calculadoratributos.service;

import br.com.itau.geradornotafiscal.exception.EnderecoDeEntregaInvalidoException;
import br.com.itau.geradornotafiscal.exception.FalhaNaGeracaoDaNotaFiscalException;
import br.com.itau.geradornotafiscal.factories.ItemNotaFiscalFactory;
import br.com.itau.geradornotafiscal.model.*;
import br.com.itau.geradornotafiscal.service.IEntregaService;
import br.com.itau.geradornotafiscal.service.IEstoqueService;
import br.com.itau.geradornotafiscal.service.IFinanceiroService;
import br.com.itau.geradornotafiscal.service.IRegistroService;
import br.com.itau.geradornotafiscal.service.impl.NotaFiscalService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotaFiscalServiceTest {

    @Mock
    private IEstoqueService estoqueService;

    @Mock
    private IRegistroService registroService;

    @Mock
    private IEntregaService entregaService;

    @Mock
    private IFinanceiroService financeiroService;

    @Mock
    private ItemNotaFiscalFactory itemNotaFiscalFactory;

    @InjectMocks
    private NotaFiscalService notaFiscalService;

    @Test
    void deveGerarNotaFiscalComSucesso() {
        Destinatario destinatario = mock(Destinatario.class);
        when(destinatario.possuiEnderecoValidoParaEntrega()).thenReturn(true);
        when(destinatario.obterAliquota(anyDouble())).thenReturn(0.1);
        when(destinatario.calcularFreteComBaseNaRegiao(anyDouble())).thenReturn(50.0);

        ItemNotaFiscal item = new ItemNotaFiscal();
        when(itemNotaFiscalFactory.criar(anyList(), anyDouble())).thenReturn(List.of(item));

        Pedido pedido = new Pedido();
        pedido.setIdPedido(123);
        pedido.setDestinatario(destinatario);
        pedido.setValorTotalItens(1000.0);
        pedido.setValorFrete(50.0);
        pedido.setItens(List.of(new Item()));

        NotaFiscal notaFiscal = notaFiscalService.gerarNotaFiscal(pedido);

        assertNotNull(notaFiscal);
        assertEquals(1000.0, notaFiscal.getValorTotalItens());
        assertEquals(50.0, notaFiscal.getValorFrete());
        assertEquals(1050.0, notaFiscal.getValorTotalNotaFiscal());
        verify(estoqueService).enviarNotaFiscalParaBaixaEstoque(notaFiscal);
        verify(registroService).registrarNotaFiscal(notaFiscal);
        verify(entregaService).agendarEntrega(notaFiscal);
        verify(financeiroService).enviarNotaFiscalParaContasReceber(notaFiscal);
    }

    @Test
    void deveLancarExcecao_QuandoDestinatarioNaoTemEnderecoValido() {
        Destinatario destinatario = mock(Destinatario.class);
        when(destinatario.possuiEnderecoValidoParaEntrega()).thenReturn(false);

        Pedido pedido = new Pedido();
        pedido.setIdPedido(123);
        pedido.setDestinatario(destinatario);

        assertThrows(EnderecoDeEntregaInvalidoException.class, () -> {
            notaFiscalService.gerarNotaFiscal(pedido);
        });

        verifyNoInteractions(estoqueService, registroService, entregaService, financeiroService);
    }

    @Test
    void deveLancarExcecaoGenerica_SeAlgoFalhar() {
        Destinatario destinatario = mock(Destinatario.class);
        when(destinatario.possuiEnderecoValidoParaEntrega()).thenReturn(true);
        when(destinatario.obterAliquota(anyDouble())).thenReturn(0.1);
        when(destinatario.calcularFreteComBaseNaRegiao(anyDouble())).thenReturn(50.0);
        when(itemNotaFiscalFactory.criar(anyList(), anyDouble())).thenThrow(new RuntimeException("Erro simulado"));

        Pedido pedido = new Pedido();
        pedido.setIdPedido(123);
        pedido.setDestinatario(destinatario);
        pedido.setValorTotalItens(1000.0);
        pedido.setValorFrete(50.0);
        pedido.setItens(List.of(new Item()));

        assertThrows(FalhaNaGeracaoDaNotaFiscalException.class, () -> {
            notaFiscalService.gerarNotaFiscal(pedido);
        });

        verifyNoInteractions(estoqueService, registroService, entregaService, financeiroService);
    }

}