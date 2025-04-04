package br.com.itau.geradornotafiscal.service.impl;

import br.com.itau.geradornotafiscal.model.*;
import br.com.itau.geradornotafiscal.service.CalculadoraAliquotaProduto;
import br.com.itau.geradornotafiscal.service.GeradorNotaFiscalService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class GeradorNotaFiscalServiceImpl implements GeradorNotaFiscalService {

    @Override
    public NotaFiscal gerarNotaFiscal(Pedido pedido) {

        CalculadoraAliquotaProduto calculadoraAliquotaProduto = new CalculadoraAliquotaProduto();
        double aliquotaPercentual = obterAliquota(pedido);
        double valorFrete = calcularValorFreteComPercentual(pedido);

        NotaFiscal notaFiscal = NotaFiscal.builder()
                .idNotaFiscal(UUID.randomUUID().toString())
                .data(LocalDateTime.now())
                .valorTotalItens(pedido.getValorTotalItens())
                .valorFrete(valorFrete)
                .itens(calculadoraAliquotaProduto.calcularAliquota(pedido.getItens(), aliquotaPercentual))
                .destinatario(pedido.getDestinatario())
                .build();

        new EstoqueService().enviarNotaFiscalParaBaixaEstoque(notaFiscal);
        new RegistroService().registrarNotaFiscal(notaFiscal);
        new EntregaService().agendarEntrega(notaFiscal);
        new FinanceiroService().enviarNotaFiscalParaContasReceber(notaFiscal);

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
}