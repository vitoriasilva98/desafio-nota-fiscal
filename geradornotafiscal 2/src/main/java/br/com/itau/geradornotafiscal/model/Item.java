package br.com.itau.geradornotafiscal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Item {

    @Schema(description = "Identificador do item", example = "63")
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

}

