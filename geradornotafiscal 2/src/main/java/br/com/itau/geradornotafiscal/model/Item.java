package br.com.itau.geradornotafiscal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Item {

    @NotBlank(message = "O identificador do item é obrigatório")
    @Schema(description = "Identificador do item", example = "63")
    @JsonProperty("id_item")
    private String idItem;

    @NotBlank(message = "A descrição do item é obrigatória")
    @Schema(description = "Descrição do item", example = "TV Samsung 55")
    @JsonProperty("descricao")
    private String descricao;

    @NotNull(message = "O valor unitário do item é obrigatório")
    @Schema(description = "Valor por unidade", example = "5000")
    @JsonProperty("valor_unitario")
    private double valorUnitario;

    @NotNull(message = "A quantidade de itens é obrigatória")
    @Schema(description = "Quantidade do item", example = "1")
    @JsonProperty("quantidade")
    private int quantidade;

}

