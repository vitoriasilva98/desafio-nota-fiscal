package br.com.itau.geradornotafiscal.service.impl;

import br.com.itau.geradornotafiscal.model.*;
import br.com.itau.geradornotafiscal.service.CalculadoraAliquotaProduto;
import br.com.itau.geradornotafiscal.service.GeradorNotaFiscalService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GeradorNotaFiscalServiceImpl implements GeradorNotaFiscalService{
	@Override
	public NotaFiscal gerarNotaFiscal(Pedido pedido) {

        Destinatario destinatario = pedido.getDestinatario();
        TipoPessoa tipoPessoa = destinatario.getTipoPessoa();
        List<ItemNotaFiscal> itemNotaFiscalList = new ArrayList<>();
        CalculadoraAliquotaProduto calculadoraAliquotaProduto = new CalculadoraAliquotaProduto();

        double aliquota = 0.0;
        double valorTotalItens = pedido.getValorTotalItens();

        if (tipoPessoa == TipoPessoa.FISICA) {
            aliquota = TributacaoPessoaFisica.FISICA.obterTaxaAliquota(valorTotalItens);
        } else if (tipoPessoa == TipoPessoa.JURIDICA) {
            aliquota = destinatario.getRegimeTributacao().obterTaxaAliquota(valorTotalItens);
        }

        itemNotaFiscalList = calculadoraAliquotaProduto.calcularAliquota(pedido.getItens(), aliquota);
        //Regras diferentes para frete

        Regiao regiao = destinatario.getEnderecos().stream()
                .filter(endereco -> endereco.getFinalidade() == Finalidade.ENTREGA || endereco.getFinalidade() == Finalidade.COBRANCA_ENTREGA)
                .map(Endereco::getRegiao)
                .findFirst()
                .orElse(null);

        double valorFrete = pedido.getValorFrete();
        double valorFreteComPercentual = regiao.getPercentualRegiao() * valorFrete;

        // Create the NotaFiscal object
        String idNotaFiscal = UUID.randomUUID().toString();

        NotaFiscal notaFiscal = NotaFiscal.builder()
                .idNotaFiscal(idNotaFiscal)
                .data(LocalDateTime.now())
                .valorTotalItens(pedido.getValorTotalItens())
                .valorFrete(valorFreteComPercentual)
                .itens(itemNotaFiscalList)
                .destinatario(pedido.getDestinatario())
                .build();

        new EstoqueService().enviarNotaFiscalParaBaixaEstoque(notaFiscal);
        new RegistroService().registrarNotaFiscal(notaFiscal);
        new EntregaService().agendarEntrega(notaFiscal);
        new FinanceiroService().enviarNotaFiscalParaContasReceber(notaFiscal);

        return notaFiscal;
    }
}