package br.com.itau.geradornotafiscal.service.impl;

import br.com.itau.geradornotafiscal.exception.FalhaAoEnviarParaContasReceberException;
import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.model.Registro;
import br.com.itau.geradornotafiscal.service.IFinanceiroService;
import org.springframework.stereotype.Service;

@Service
public class FinanceiroService extends Registro implements IFinanceiroService {

    @Override
    public void enviarNotaFiscalParaContasReceber(NotaFiscal notaFiscal) {
        logger.info("Iniciando envio para o departamento Contas a Receber da Nota Físcal ID: [{}].", notaFiscal.getIdNotaFiscal());
        try {
            //Simula o envio da nota fiscal para o contas a receber
            Thread.sleep(250);
            logger.info("Foi enviada com sucesso para o departamento de Contas a Receber a Nota Físcal ID: [{}].", notaFiscal.getIdNotaFiscal());
        } catch (InterruptedException e) {
            logger.error("Ocorreu um erro durante o envio para o departamento Contas a Receber da Nota Físcal ID: [{}]", notaFiscal.getIdNotaFiscal(), e);
            Thread.currentThread().interrupt();
            throw new FalhaAoEnviarParaContasReceberException(e);
        }
    }
}
