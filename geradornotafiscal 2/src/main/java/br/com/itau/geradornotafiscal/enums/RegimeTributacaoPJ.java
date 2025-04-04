package br.com.itau.geradornotafiscal.enums;

import br.com.itau.geradornotafiscal.interfaces.Tributavel;
import lombok.Getter;

@Getter
public enum RegimeTributacaoPJ implements Tributavel {
    SIMPLES_NACIONAL(0.03, 0.07, 0.13, 0.19),
    LUCRO_REAL(0.03, 0.09, 0.15, 0.20),
    LUCRO_PRESUMIDO(0.03, 0.09, 0.16, 0.20),
    OUTROS(0.0, 0.0, 0.0, 0.0);

    private double faixa1;
    private double faixa2;
    private double faixa3;
    private double faixa4;

    RegimeTributacaoPJ(double faixa1, double faixa2, double faixa3, double faixa4) {
        this.faixa1 = faixa1;
        this.faixa2 = faixa2;
        this.faixa3 = faixa3;
        this.faixa4 = faixa4;
    }

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

