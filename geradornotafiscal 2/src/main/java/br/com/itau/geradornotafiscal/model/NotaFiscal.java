package br.com.itau.geradornotafiscal.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class NotaFiscal {

    @Schema(description = "Identificador da nota físcal", example = "97e88ac5-7546-4721-be85-944d605270e3")
    @JsonProperty("id_nota_fiscal")
    private String idNotaFiscal;

    @Schema(description = "Data de emissão da nota físcal", example = "2025-04-02")
    @JsonProperty("data")
    private LocalDateTime data;

    @Schema(description = "Valor total dos itens da nota", example = "5035.90")
    @JsonProperty("valor_total_itens")
    private double valorTotalItens;

    @Schema(description = "Valor do frete", example = "35.90")
    @JsonProperty("valor_frete")
    private double valorFrete;

    @Schema(description = "Itens da nota físcal")
    @JsonProperty("itens")
    private List<ItemNotaFiscal> itens;

    @Schema(description = "Detinatário")
    @JsonProperty("destinatario")
    private Destinatario destinatario;

}