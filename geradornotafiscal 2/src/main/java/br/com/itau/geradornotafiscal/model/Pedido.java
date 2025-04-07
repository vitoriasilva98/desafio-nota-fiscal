package br.com.itau.geradornotafiscal.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Pedido {
    @Schema(description = "Identificador do pedido", example = "29")
    @JsonProperty("id_pedido")
    private int idPedido;

    @Schema(description = "Data da realização do pedido", example = "2025-04-02")
    @JsonProperty("data")
    private LocalDate data;

    @Schema(description = "Valor total dos itens do pedido", example = "5000.0")
    @JsonProperty("valor_total_itens")
    private double valorTotalItens;

    @Schema(description = "Valor do frete", example = "30.00")
    @JsonProperty("valor_frete")
    private double valorFrete;

    @Schema(description = "Itens do pedido")
    @JsonProperty("itens")
    private List<Item> itens;

    @Schema(description = "Detinatário do pedido")
    @JsonProperty("destinatario")
    private Destinatario destinatario;

}
