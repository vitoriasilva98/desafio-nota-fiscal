package br.com.itau.geradornotafiscal.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErroSimplesResponse {

    private String titulo;
    private String detalhe;
}
