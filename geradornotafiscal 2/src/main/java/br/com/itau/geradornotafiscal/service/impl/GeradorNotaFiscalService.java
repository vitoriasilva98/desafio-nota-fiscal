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
public class GeradorNotaFiscalService implements IGeradorNotaFiscalService {

    private final IEstoqueService iEstoqueService;
    private final IRegistroService iRegistroService;
    private final IEntregaService iEntregaService;
    private final IFinanceiroService iFinanceiroService;
    private final ItemNotaFiscalFactory itemNotaFiscalFactory;

    @Override
    public NotaFiscal gerarNotaFiscal(Pedido pedido) {

        double aliquotaPercentual = pedido.getDestinatario().obterAliquota(pedido.getValorTotalItens());
        double frenteComPercentual = pedido.getDestinatario().calcularFreteComBaseNaRegiao(pedido.getValorFrete());

        NotaFiscal notaFiscal = NotaFiscal.builder()
                .idNotaFiscal(UUID.randomUUID().toString())
                .data(LocalDateTime.now())
                .valorTotalItens(pedido.getValorTotalItens())
                .valorFrete(frenteComPercentual)
                .itens(itemNotaFiscalFactory.criar(pedido.getItens(), aliquotaPercentual))
                .destinatario(pedido.getDestinatario())
                .build();

        iEstoqueService.enviarNotaFiscalParaBaixaEstoque(notaFiscal);
        iRegistroService.registrarNotaFiscal(notaFiscal);
        iEntregaService.agendarEntrega(notaFiscal);
        iFinanceiroService.enviarNotaFiscalParaContasReceber(notaFiscal);

        return notaFiscal;
    }
}