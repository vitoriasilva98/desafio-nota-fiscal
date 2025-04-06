package br.com.itau.geradornotafiscal.service.impl;

import br.com.itau.geradornotafiscal.enums.TipoPessoa;
import br.com.itau.geradornotafiscal.enums.RegimeTributacaoPF;
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
    private final IFreteService iFreteService;
    private final ItemNotaFiscalFactory itemNotaFiscalFactory;

    @Override
    public NotaFiscal gerarNotaFiscal(Pedido pedido) {

        double aliquotaPercentual = obterAliquota(pedido);

        NotaFiscal notaFiscal = NotaFiscal.builder()
                .idNotaFiscal(UUID.randomUUID().toString())
                .data(LocalDateTime.now())
                .valorTotalItens(pedido.getValorTotalItens())
                .valorFrete(iFreteService.calcularValorFreteComPercentual(pedido))
                .itens(itemNotaFiscalFactory.criar(pedido.getItens(), aliquotaPercentual))
                .destinatario(pedido.getDestinatario())
                .build();

        iEstoqueService.enviarNotaFiscalParaBaixaEstoque(notaFiscal);
        iRegistroService.registrarNotaFiscal(notaFiscal);
        iEntregaService.agendarEntrega(notaFiscal);
        iFinanceiroService.enviarNotaFiscalParaContasReceber(notaFiscal);

        return notaFiscal;
    }

    private double obterAliquota(Pedido pedido) {

        double aliquota = 0.0;
        double valorTotalItens = pedido.getValorTotalItens();
        TipoPessoa tipoPessoa = pedido.getDestinatario().getTipoPessoa();

        if (tipoPessoa == TipoPessoa.FISICA) {
            aliquota = RegimeTributacaoPF.IMPOSTO_DE_RENDA.obterTaxaAliquota(valorTotalItens);
        } else if (tipoPessoa == TipoPessoa.JURIDICA) {
            aliquota = pedido.getDestinatario().getRegimeTributacao().obterTaxaAliquota(valorTotalItens);
        }

        return aliquota;
    }
}