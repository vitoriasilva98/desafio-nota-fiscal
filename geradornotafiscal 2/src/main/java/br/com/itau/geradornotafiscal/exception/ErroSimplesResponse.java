package br.com.itau.geradornotafiscal.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ErroSimplesResponse {

    private String titulo;
    private String detalhe;
}
