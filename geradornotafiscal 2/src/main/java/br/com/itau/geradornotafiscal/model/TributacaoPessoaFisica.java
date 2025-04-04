package br.com.itau.geradornotafiscal.model;

import br.com.itau.geradornotafiscal.interfaces.Tributavel;

public enum TributacaoPessoaFisica implements Tributavel {

    FISICA {
        @Override
        public double obterTaxaAliquota(double valorTotalItens) {
            if (valorTotalItens < 500) {
                return 0;
            } else if (valorTotalItens <= 2000) {
                return 0.12;
            } else if (valorTotalItens <= 3500) {
                return 0.15;
            } else {
                return 0.17;
            }
        }
    }
}
