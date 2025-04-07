package br.com.itau.geradornotafiscal.web.controller;

import br.com.itau.geradornotafiscal.model.NotaFiscal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import br.com.itau.geradornotafiscal.model.Pedido;
import br.com.itau.geradornotafiscal.service.INotaFiscalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Notas Fiscais", description = "Operações relacionadas a notas fiscais")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pedido")
public class GeradorNFController {

    private final INotaFiscalService notaFiscalService;

    @Operation(summary = "Gera uma nova nota fiscal")
    @ApiResponse(responseCode = "201", description = "Nota fiscal criada com sucesso")
    @PostMapping("/gerarNotaFiscal")
    public ResponseEntity<NotaFiscal> gerarNotaFiscal(@RequestBody Pedido pedido) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(notaFiscalService.gerarNotaFiscal(pedido));
    }

}
