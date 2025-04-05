package br.com.itau.geradornotafiscal.service.impl;

import br.com.itau.geradornotafiscal.enums.Finalidade;
import br.com.itau.geradornotafiscal.enums.Regiao;
import br.com.itau.geradornotafiscal.enums.TipoPessoa;
import br.com.itau.geradornotafiscal.enums.TributacaoPessoaFisica;
import br.com.itau.geradornotafiscal.model.*;
import br.com.itau.geradornotafiscal.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GeradorNotaFiscalServiceImpl implements IGeradorNotaFiscalService {

    private final IEstoqueService iEstoqueService;
    private final IRegistroService iRegistroService;
    private final IEntregaService iEntregaService;
    private final IFinanceiroService iFinanceiroService;

    @Override
    public NotaFiscal gerarNotaFiscal(Pedido pedido) {

        double aliquotaPercentual = obterAliquota(pedido);

        NotaFiscal notaFiscal = NotaFiscal.builder()
                .idNotaFiscal(UUID.randomUUID().toString())
                .data(LocalDateTime.now())
                .valorTotalItens(pedido.getValorTotalItens())
                .valorFrete(calcularValorFreteComPercentual(pedido))
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

    private double calcularValorFreteComPercentual(Pedido pedido) {
        double valorFrete = pedido.getValorFrete();

        Optional<Regiao> regiao = pedido.getDestinatario().getEnderecos().stream()
                .filter(endereco -> endereco.getFinalidade() == Finalidade.ENTREGA || endereco.getFinalidade() == Finalidade.COBRANCA_ENTREGA)
                .map(Endereco::getRegiao)
                .findFirst();

        return regiao.map(value -> value.getPercentualRegiao() * valorFrete).orElse(0.0);
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