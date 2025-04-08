package br.com.itau.geradornotafiscal.enums;

import br.com.itau.geradornotafiscal.interfaces.Tributavel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegimeTributacaoPJ implements Tributavel {
    SIMPLES_NACIONAL(0.03, 0.07, 0.13, 0.19),
    LUCRO_REAL(0.03, 0.09, 0.15, 0.20),
    LUCRO_PRESUMIDO(0.03, 0.09, 0.16, 0.20),
    OUTROS(0.0, 0.0, 0.0, 0.0);

    private final double faixa1;
    private final double faixa2;
    private final double faixa3;
    private final double faixa4;

    @Override
    public double obterTaxaAliquota(double valorTotalItens) {
        if (valorTotalItens < 1000) {
            return this.getFaixa1();
        } else if (valorTotalItens <= 2000) {
            return this.getFaixa2();
        } else if (valorTotalItens <= 5000) {
            return this.getFaixa3();
        } else {
            return this.getFaixa4();
        }
    }
}

