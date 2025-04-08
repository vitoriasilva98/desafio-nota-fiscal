package br.com.itau.calculadoratributos.exception;

import br.com.itau.geradornotafiscal.exception.EnderecoDeEntregaInvalidoException;
import br.com.itau.geradornotafiscal.exception.ErroSimplesResponse;
import br.com.itau.geradornotafiscal.exception.ErroValidacaoResponse;
import br.com.itau.geradornotafiscal.exception.GlobalExcecaoHandler;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GlobalExcecaoHandlerTeset {

    private GlobalExcecaoHandler handler;

    @BeforeEach
    void configuracaoInicial() {
        handler = new GlobalExcecaoHandler();
    }

    @Test
    void testHandleValidacaoExcecao() {
        // Cria um FieldError simulando uma violação (por exemplo, para o campo "nome")
        FieldError fieldError = new FieldError("destinatario", "nome", "Campo obrigatório");

        // Cria um BeanPropertyBindingResult e adiciona o erro
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "destinatario");
        bindingResult.addError(fieldError);

        // Cria a exceção de validação
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        // Chama o handler
        ResponseEntity<ErroValidacaoResponse> response = handler.handleValidacaoExcecao(ex);

        // Verifica o status e o conteúdo do corpo da resposta
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErroValidacaoResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("Campos inválidos", body.getMensagem());
        Map<String, String> errors = body.getErros();
        assertEquals("Campo obrigatório", errors.get("nome"));
    }

    @Test
    void testHandleNaoFoiPossivelLerAhRequisicao_ComLocalDateInvalidFormat() {
        // Cria uma InvalidFormatException que simule erro no formato de uma data
        InvalidFormatException ife = new InvalidFormatException(null, "Formato inválido", "abc", LocalDate.class);
        // Adiciona um caminho (path) para que o ife possua informação do campo "data"
        ife.prependPath(new JsonMappingException.Reference(new Object(), "data"));

        // Cria a exceção HttpMessageNotReadableException com ife como causa
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Erro de leitura", ife);

        ResponseEntity<Object> response = handler.handleNaoFoiPossivelLerAhRequisicao(ex);

        // Verifica que o status é 400 e o corpo é do tipo ErroValidacaoResponse
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErroValidacaoResponse);
        ErroValidacaoResponse body = (ErroValidacaoResponse) response.getBody();
        assertEquals("Erro de formatação", body.getMensagem());
        Map<String, String> erros = body.getErros();
        assertEquals("Formato de data inválido. Use o padrão yyyy-MM-dd (ex: 2025-04-02)", erros.get("data"));
    }

    @Test
    void testHandleNaoFoiPossivelLerAhRequisicao_OtherCause() {
        // Cria uma exceção genérica como causa (não InvalidFormatException)
        Exception cause = new Exception("Erro genérico");
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Erro de leitura", cause);

        ResponseEntity<Object> response = handler.handleNaoFoiPossivelLerAhRequisicao(ex);

        // Verifica que o status é 400 e o corpo é um ErroSimplesResponse com a mensagem esperada
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErroSimplesResponse);
        ErroSimplesResponse body = (ErroSimplesResponse) response.getBody();
        assertEquals("Corpo inválido", body.getTitulo());
        assertEquals("Erro de leitura: verifique se o corpo da requisição está bem formado.", body.getDetalhe());
    }

    @Test
    void testHandleRegraNegocioException() {
        // Cria uma EnderecoDeEntregaInvalidoException com mensagem
        String mensagem = "O destinatário deve possuir pelo menos um endereço com a finalidade de ENTREGA ou COBRANCA_ENTREGA.";
        EnderecoDeEntregaInvalidoException ex = new EnderecoDeEntregaInvalidoException(mensagem);

        ResponseEntity<ErroSimplesResponse> response = handler.handleRegraNegocioException(ex);

        // Verifica que o status é 400 e o corpo é do tipo ErroSimplesResponse com a mensagem correta
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErroSimplesResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("Regra de negócio violada", body.getTitulo());
        assertEquals(mensagem, body.getDetalhe());
    }
}
