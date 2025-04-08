package br.com.itau.geradornotafiscal.service.impl;

import br.com.itau.geradornotafiscal.exception.FalhaNaBaixaDeEstoqueException;
import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.model.Registro;
import br.com.itau.geradornotafiscal.service.IEstoqueService;
import org.springframework.stereotype.Service;

@Service
public class EstoqueService extends Registro implements IEstoqueService {

    @Override
    public void enviarNotaFiscalParaBaixaEstoque(NotaFiscal notaFiscal) {
        logger.info("Iniciando processo de baixa do estoque para à Nota Físcal ID: [{}].", notaFiscal.getIdNotaFiscal());
        try {
            //Simula envio de nota fiscal para baixa de estoque
            Thread.sleep(380);
            logger.info("O estoque foi atualizado com sucesso para à Nota Físcal ID: [{}].", notaFiscal.getIdNotaFiscal());
        } catch (InterruptedException e) {
            logger.error("Ocorreu um erro ao tentar dar baixa no estoque para a Nota Físcal ID: [{}]", notaFiscal.getIdNotaFiscal(), e);
            Thread.currentThread().interrupt();
            throw new FalhaNaBaixaDeEstoqueException(e);
        }
    }
}
