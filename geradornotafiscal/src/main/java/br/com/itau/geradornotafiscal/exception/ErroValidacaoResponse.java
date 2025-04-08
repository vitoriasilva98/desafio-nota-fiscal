package br.com.itau.geradornotafiscal.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ErroValidacaoResponse {

    private String mensagem;
    private Map<String, String> erros;
}
