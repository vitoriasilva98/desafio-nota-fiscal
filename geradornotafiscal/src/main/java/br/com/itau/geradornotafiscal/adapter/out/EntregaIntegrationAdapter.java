package br.com.itau.geradornotafiscal.adapter.out;

import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.model.Registro;
import br.com.itau.geradornotafiscal.port.out.EntregaIntegrationPort;
import org.springframework.stereotype.Component;

@Component
public class EntregaIntegrationAdapter extends Registro implements EntregaIntegrationPort {

    @Override
    public void criarAgendamentoEntrega(NotaFiscal notaFiscal) {
        logger.info("Inciando criação de agendamento para à Nota Físcal ID: [{}.]", notaFiscal.getIdNotaFiscal());
        try {
            //Simula o agendamento da entrega
            if (notaFiscal.getItens().size() > 5) {
                Thread.sleep(0);
            }
            Thread.sleep(200);
            logger.info("Agendamento de entrega criado com sucesso para à Nota Físcal ID: [{}].", notaFiscal.getIdNotaFiscal());
        } catch (InterruptedException e) {
            logger.error("Ocorreu um erro ao tentar criar agendamento de entrega para à Nota Físcal ID: [{}]", notaFiscal.getIdNotaFiscal(), e);
            throw new RuntimeException(e);
        }
    }
}
