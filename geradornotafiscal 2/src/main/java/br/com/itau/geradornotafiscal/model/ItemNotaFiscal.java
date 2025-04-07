package br.com.itau.geradornotafiscal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ItemNotaFiscal {

    @Schema(description = "Identificador do item da nota físcal", example = "74")
    @JsonProperty("id_item")
    private String idItem;

    @Schema(description = "Descrição do item", example = "TV Samsung 55")
    @JsonProperty("descricao")
    private String descricao;

    @Schema(description = "Valor por unidade", example = "5000")
    @JsonProperty("valor_unitario")
    private double valorUnitario;

    @Schema(description = "Quantidade do item", example = "1")
    @JsonProperty("quantidade")
    private int quantidade;

    @Schema(description = "Valor de tributo pago sobre o item", example = "118.0")
    @JsonProperty("valor_tributo_item")
    private double valorTributoItem;

}