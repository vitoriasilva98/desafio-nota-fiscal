package br.com.itau.geradornotafiscal.model;

import lombok.Getter;

@Getter
public enum Regiao {
    NORTE(1.08),
    NORDESTE(1.085),
    CENTRO_OESTE(1.07),
    SUDESTE(1.048),
    SUL(1.06);

    private final Double percentualRegiao;

    Regiao(Double percentualRegiao) {
        this.percentualRegiao = percentualRegiao;
    }
}
