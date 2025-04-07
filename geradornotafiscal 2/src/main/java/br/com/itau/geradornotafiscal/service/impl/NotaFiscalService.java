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
public class NotaFiscalService extends Registro implements INotaFiscalService {

    private final IEstoqueService estoqueService;
    private final IRegistroService registroService;
    private final IEntregaService entregaService;
    private final IFinanceiroService financeiroService;
    private final ItemNotaFiscalFactory itemNotaFiscalFactory;

    @Override
    public NotaFiscal gerarNotaFiscal(Pedido pedido) {
        logger.info("Iniciando geração da nota fiscal para o pedido - ID: [{}].", pedido.getIdPedido());
        logger.debug("Pedido: [{}]", pedido);
        try {
            NotaFiscal notaFiscal = criarNotaFiscal(pedido);
            logger.info("Nota Físcal criada com sucesso - ID: [{}].", notaFiscal.getIdNotaFiscal());
            logger.debug("Nota Fiscal: [{}].", notaFiscal.getIdNotaFiscal());

            estoqueService.enviarNotaFiscalParaBaixaEstoque(notaFiscal);
            registroService.registrarNotaFiscal(notaFiscal);
            entregaService.agendarEntrega(notaFiscal);
            financeiroService.enviarNotaFiscalParaContasReceber(notaFiscal);

            logger.info("A Nota Físcal foi gerada com sucesso - ID: [{}].", notaFiscal.getIdNotaFiscal());

            return notaFiscal;
        } catch (Exception e) {
            logger.error("Erro ao gerar nota fiscal para o pedido - ID: [{}]", pedido.getIdPedido(), e);
            throw new RuntimeException(e);
        }
    }

    private NotaFiscal criarNotaFiscal(Pedido pedido) {
        double aliquotaPercentual = pedido.getDestinatario().obterAliquota(pedido.getValorTotalItens());
        double freteComPercentual = pedido.getDestinatario().calcularFreteComBaseNaRegiao(pedido.getValorFrete());
        logger.debug("Aliquota percentual: [{}] - Frete com Percentual da Região: [{}]", aliquotaPercentual, freteComPercentual);

        return NotaFiscal.builder()
                .idNotaFiscal(UUID.randomUUID().toString())
                .data(LocalDateTime.now())
                .valorTotalItens(pedido.getValorTotalItens())
                .valorFrete(freteComPercentual)
                .valorTotalNotaFiscal(pedido.getValorTotalItens() + freteComPercentual)
                .itens(itemNotaFiscalFactory.criar(pedido.getItens(), aliquotaPercentual))
                .destinatario(pedido.getDestinatario())
                .build();
    }
}