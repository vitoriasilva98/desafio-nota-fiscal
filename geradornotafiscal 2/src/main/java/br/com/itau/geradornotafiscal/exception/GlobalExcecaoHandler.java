package br.com.itau.geradornotafiscal.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
            mensagemErro = String.format("O valor '%s' não é válido para o campo '%s'. Era esperado: %s",
                    ife.getValue(),
                    ife.getPath().get(ife.getPath().size() - 1).getFieldName(),
                    ife.getTargetType().getSimpleName());
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErroSimplesResponse("Corpo inválido", mensagemErro));
    }
}
