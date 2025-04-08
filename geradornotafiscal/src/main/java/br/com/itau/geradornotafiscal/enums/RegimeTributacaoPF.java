package br.com.itau.geradornotafiscal.enums;

import br.com.itau.geradornotafiscal.interfaces.Tributavel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegimeTributacaoPF implements Tributavel {

    IMPOSTO_DE_RENDA(0.0, 0.12, 0.15, 0.17);

    private final double faixa1;
    private final double faixa2;
    private final double faixa3;
    private final double faixa4;

    @Override
    public double obterTaxaAliquota(double valorTotalItens) {
        if (valorTotalItens < 500) {
            return getFaixa1();
        } else if (valorTotalItens <= 2000) {
            return getFaixa2();
        } else if (valorTotalItens <= 3500) {
            return getFaixa3();
        } else {
            return getFaixa4();
        }
    }
}
