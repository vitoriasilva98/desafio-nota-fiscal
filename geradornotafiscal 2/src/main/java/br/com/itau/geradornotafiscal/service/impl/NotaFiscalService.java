package br.com.itau.geradornotafiscal.service.impl;

import br.com.itau.geradornotafiscal.factories.ItemNotaFiscalFactory;
import br.com.itau.geradornotafiscal.model.*;
import br.com.itau.geradornotafiscal.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class NotaFiscalService implements INotaFiscalService {

    private final IEstoqueService estoqueService;
    private final IRegistroService registroService;
    private final IEntregaService entregaService;
    private final IFinanceiroService financeiroService;
    private final ItemNotaFiscalFactory itemNotaFiscalFactory;

    @Override
    public NotaFiscal gerarNotaFiscal(Pedido pedido) {

        NotaFiscal notaFiscal = criarNotaFiscal(pedido);

        estoqueService.enviarNotaFiscalParaBaixaEstoque(notaFiscal);
        registroService.registrarNotaFiscal(notaFiscal);
        entregaService.agendarEntrega(notaFiscal);
        financeiroService.enviarNotaFiscalParaContasReceber(notaFiscal);

        return notaFiscal;
    }

    private NotaFiscal criarNotaFiscal(Pedido pedido) {
        double aliquotaPercentual = pedido.getDestinatario().obterAliquota(pedido.getValorTotalItens());
        double freteComPercentual = pedido.getDestinatario().calcularFreteComBaseNaRegiao(pedido.getValorFrete());

        return NotaFiscal.builder()
                .idNotaFiscal(UUID.randomUUID().toString())
                .data(LocalDateTime.now())
                .valorTotalItens(pedido.getValorTotalItens())
                .valorFrete(freteComPercentual)
                .itens(itemNotaFiscalFactory.criar(pedido.getItens(), aliquotaPercentual))
                .destinatario(pedido.getDestinatario())
                .build();
    }
}