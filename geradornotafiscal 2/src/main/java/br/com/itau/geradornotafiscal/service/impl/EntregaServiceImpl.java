package br.com.itau.geradornotafiscal.service.impl;

import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.adapter.out.EntregaIntegrationAdapter;
import br.com.itau.geradornotafiscal.service.IEntregaService;
import org.springframework.stereotype.Service;

@Service
public class EntregaServiceImpl implements IEntregaService {

    @Override
    public void agendarEntrega(NotaFiscal notaFiscal) {

        try {
            //Simula o agendamento da entrega
            Thread.sleep(150);
            new EntregaIntegrationAdapter().criarAgendamentoEntrega(notaFiscal);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
