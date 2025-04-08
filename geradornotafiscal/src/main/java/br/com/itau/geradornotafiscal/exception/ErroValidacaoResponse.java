package br.com.itau.geradornotafiscal.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class ErroValidacaoResponse {

    private String mensagem;
    private Map<String, String> erros;
}
