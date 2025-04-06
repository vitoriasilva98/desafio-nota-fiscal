package br.com.itau.geradornotafiscal.service.impl;

import br.com.itau.geradornotafiscal.enums.TipoPessoa;
import br.com.itau.geradornotafiscal.enums.TributacaoPessoaFisica;
import br.com.itau.geradornotafiscal.model.*;
import br.com.itau.geradornotafiscal.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GeradorNotaFiscalServiceImpl implements IGeradorNotaFiscalService {

    private final IEstoqueService iEstoqueService;
    private final IRegistroService iRegistroService;
    private final IEntregaService iEntregaService;
    private final IFinanceiroService iFinanceiroService;
    private final IFreteService iFreteService;

    @Override
    public NotaFiscal gerarNotaFiscal(Pedido pedido) {

        double aliquotaPercentual = obterAliquota(pedido);

        NotaFiscal notaFiscal = NotaFiscal.builder()
                .idNotaFiscal(UUID.randomUUID().toString())
                .data(LocalDateTime.now())
                .valorTotalItens(pedido.getValorTotalItens())
                .valorFrete(iFreteService.calcularValorFreteComPercentual(pedido))
                .itens(criarItensNotaFiscalComTributo(pedido.getItens(), aliquotaPercentual))
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
            aliquota = TributacaoPessoaFisica.FISICA.obterTaxaAliquota(valorTotalItens);
        } else if (tipoPessoa == TipoPessoa.JURIDICA) {
            aliquota = pedido.getDestinatario().getRegimeTributacao().obterTaxaAliquota(valorTotalItens);
        }

        return aliquota;
    }

    private List<ItemNotaFiscal> criarItensNotaFiscalComTributo(List<Item> itens, double aliquotaPercentual) {
        return itens.stream()
                .map(item -> ItemNotaFiscal.builder()
                        .idItem(item.getIdItem())
                        .descricao(item.getDescricao())
                        .valorUnitario(item.getValorUnitario())
                        .quantidade(item.getQuantidade())
                        .valorTributoItem(item.getValorUnitario() * aliquotaPercentual)
                        .build())
                .collect(Collectors.toList());
    }
}