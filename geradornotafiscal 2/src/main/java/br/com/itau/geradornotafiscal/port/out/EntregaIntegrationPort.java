package br.com.itau.geradornotafiscal.port.out;

import br.com.itau.geradornotafiscal.model.NotaFiscal;
import org.springframework.stereotype.Component;

@Component
public interface EntregaIntegrationPort {
    void criarAgendamentoEntrega(NotaFiscal notaFiscal);
}
