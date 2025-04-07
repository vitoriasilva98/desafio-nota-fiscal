package br.com.itau.geradornotafiscal.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Pedido {

    @NotNull(message = "O identificador do pedido é obrigatório")
    @Schema(description = "Identificador do pedido", example = "29")
    @JsonProperty("id_pedido")
    private int idPedido;

    @NotNull(message = "A data do pedido é obrigatória")
    @Schema(description = "Data da realização do pedido", example = "2025-04-02")
    @JsonProperty("data")
    private LocalDate data;

    @NotNull(message = "O valor total dos itens é obrigatório")
    @Schema(description = "Valor total dos itens do pedido", example = "5000.0")
    @JsonProperty("valor_total_itens")
    private double valorTotalItens;

    @NotNull(message = "O valor do frete é obrigatório")
    @Schema(description = "Valor do frete", example = "30.00")
    @JsonProperty("valor_frete")
    private double valorFrete;

    @NotEmpty(message = "É obrigatório que pelos menos tenha um item")
    @Schema(description = "Itens do pedido")
    @JsonProperty("itens")
    private List<@Valid Item> itens;

    @Valid
    @NotNull(message = "O destinatário é obrigatório")
    @Schema(description = "Detinatário do pedido")
    @JsonProperty("destinatario")
    private Destinatario destinatario;

}
