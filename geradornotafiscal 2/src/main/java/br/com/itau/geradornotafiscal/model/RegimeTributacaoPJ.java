package br.com.itau.geradornotafiscal.model;

import br.com.itau.geradornotafiscal.interfaces.Tributavel;

public enum RegimeTributacaoPJ implements Tributavel {
    SIMPLES_NACIONAL {
        @Override
        public double obterTaxaAliquota(double valorTotalItens) {
            if (valorTotalItens < 1000) {
                return 0.03;
            } else if (valorTotalItens <= 2000) {
                return 0.07;
            } else if (valorTotalItens <= 5000) {
                return 0.13;
            } else {
                return 0.19;
            }
        }
    },
    LUCRO_REAL {
        @Override
        public double obterTaxaAliquota(double valorTotalItens) {
            if (valorTotalItens < 1000) {
                return 0.03;
            } else if (valorTotalItens <= 2000) {
                return 0.09;
            } else if (valorTotalItens <= 5000) {
                return 0.15;
            } else {
                return 0.20;
            }
        }
    },
    LUCRO_PRESUMIDO {
        @Override
        public double obterTaxaAliquota(double valorTotalItens) {
            if (valorTotalItens < 1000) {
                return 0.03;
            } else if (valorTotalItens <= 2000) {
                return 0.09;
            } else if (valorTotalItens <= 5000) {
                return 0.16;
            } else {
                return 0.20;
            }
        }
    },
    OUTROS {
        @Override
        public double obterTaxaAliquota(double valorTotalItens) {
            return 0.0;
        }
    };
}

