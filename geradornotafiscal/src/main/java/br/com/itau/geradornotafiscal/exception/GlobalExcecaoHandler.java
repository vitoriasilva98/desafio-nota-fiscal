package br.com.itau.geradornotafiscal.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExcecaoHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroValidacaoResponse> handleValidacaoExcecao(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String nomeDoCampo = ((FieldError) error).getField();
            String mensagemErro = error.getDefaultMessage();
            errors.put(nomeDoCampo, mensagemErro);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroValidacaoResponse("Campos inválidos", errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleNaoFoiPossivelLerAhRequisicao(HttpMessageNotReadableException ex) {
        String mensagemErro = "Erro de leitura: verifique se o corpo da requisição está bem formado.";

        Throwable causa = ex.getCause();
        if (causa instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) causa;

            if (ife.getTargetType().equals(LocalDate.class)) {
                Map<String, String> erros = new HashMap<>();
                erros.put("data", "Formato de data inválido. Use o padrão yyyy-MM-dd (ex: 2025-04-02)");

                return ResponseEntity
                        .badRequest()
                        .body(new ErroValidacaoResponse("Erro de formatação", erros));
            }

            mensagemErro = String.format("O valor '%s' não é válido para o campo '%s'. Era esperado: %s",
                    ife.getValue(),
                    ife.getPath().get(ife.getPath().size() - 1).getFieldName(),
                    ife.getTargetType().getSimpleName());
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErroSimplesResponse("Corpo inválido", mensagemErro));
    }

    @ExceptionHandler(EnderecoDeEntregaInvalidoException.class)
    public ResponseEntity<ErroSimplesResponse> handleRegraNegocioException(EnderecoDeEntregaInvalidoException ex) {
        ErroSimplesResponse erro = new ErroSimplesResponse("Regra de negócio violada", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
}
