package br.com.itau.geradornotafiscal.service.impl;

import br.com.itau.geradornotafiscal.exception.FalhaNoRegistroNotaFiscalException;
import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.model.Registro;
import br.com.itau.geradornotafiscal.service.IRegistroService;
import org.springframework.stereotype.Service;

@Service
public class RegistroService extends Registro implements IRegistroService {

    @Override
    public void registrarNotaFiscal(NotaFiscal notaFiscal) {
        logger.info("Iniciando processo de registro da Nota Físcal ID: [{}].", notaFiscal.getIdNotaFiscal());

        try {
            //Simula o registro da nota fiscal
            Thread.sleep(500);
            logger.info("O registro foi realizado com sucesso da Nota Físcal ID: [{}].", notaFiscal.getIdNotaFiscal());
        } catch (InterruptedException e) {
            logger.error("Ocorreu um erro durante o registro da Nota Físcal ID: [{}]", notaFiscal.getIdNotaFiscal(), e);
            throw new FalhaNoRegistroNotaFiscalException(e);
        }
    }
}
