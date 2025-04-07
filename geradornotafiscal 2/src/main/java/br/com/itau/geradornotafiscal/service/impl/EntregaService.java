package br.com.itau.geradornotafiscal.service.impl;

import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.model.Registro;
import br.com.itau.geradornotafiscal.port.out.EntregaIntegrationPort;
import br.com.itau.geradornotafiscal.service.IEntregaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EntregaService extends Registro implements IEntregaService {

    private final EntregaIntegrationPort entregaIntegrationPort;

    @Override
    public void agendarEntrega(NotaFiscal notaFiscal) {
        logger.info("Iniciando processo de agendamento de entrega para à Nota Físcal ID: [{}].", notaFiscal.getIdNotaFiscal());
        try {
            //Simula o agendamento da entrega
            Thread.sleep(150);
            entregaIntegrationPort.criarAgendamentoEntrega(notaFiscal);
        } catch (InterruptedException e) {
            logger.error("Ocorreu um erro ao tentar agendar a entrega da Nota Físcal ID: [{}]", notaFiscal.getIdNotaFiscal(), e);
            throw new RuntimeException(e);
        }

    }
}
