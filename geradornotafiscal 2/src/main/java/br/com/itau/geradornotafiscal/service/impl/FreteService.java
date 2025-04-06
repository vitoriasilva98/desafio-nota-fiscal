package br.com.itau.geradornotafiscal.service.impl;

import br.com.itau.geradornotafiscal.enums.Finalidade;
import br.com.itau.geradornotafiscal.enums.Regiao;
import br.com.itau.geradornotafiscal.model.Endereco;
import br.com.itau.geradornotafiscal.model.Pedido;
import br.com.itau.geradornotafiscal.service.IFreteService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FreteService implements IFreteService {

    @Override
    public double calcularValorFreteComPercentual(Pedido pedido) {
        double valorFrete = pedido.getValorFrete();

        Optional<Regiao> regiao = pedido.getDestinatario().getEnderecos().stream()
                .filter(endereco -> endereco.getFinalidade() == Finalidade.ENTREGA || endereco.getFinalidade() == Finalidade.COBRANCA_ENTREGA)
                .map(Endereco::getRegiao)
                .findFirst();

        return regiao.map(value -> value.getPercentualRegiao() * valorFrete).orElse(0.0);
    }
}
