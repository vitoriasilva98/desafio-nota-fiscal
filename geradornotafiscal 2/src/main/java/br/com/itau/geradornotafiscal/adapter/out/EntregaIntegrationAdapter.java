package br.com.itau.geradornotafiscal.adapter.out;

import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.port.out.EntregaIntegrationPort;

public class EntregaIntegrationAdapter implements EntregaIntegrationPort {

    @Override
    public void criarAgendamentoEntrega(NotaFiscal notaFiscal) {

        try {
            //Simula o agendamento da entrega
            if (notaFiscal.getItens().size() > 5) {
                Thread.sleep(0);
            }
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
