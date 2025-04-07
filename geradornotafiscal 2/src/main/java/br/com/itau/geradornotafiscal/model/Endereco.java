package br.com.itau.geradornotafiscal.model;

import br.com.itau.geradornotafiscal.enums.Finalidade;
import br.com.itau.geradornotafiscal.enums.Regiao;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {

    @Schema(description = "Código de Endereçamento Postal (CEP)", example = "03105003")
    @JsonProperty("cep")
    private String cep;

    @Schema(description = "Logradouro", example = "Avenida Paulista")
    @JsonProperty("logradouro")
    private String logradouro;

    @Schema(description = "Numeração do logradouro", example = "52")
    @JsonProperty("numero")
    private String numero;

    @Schema(description = "Estado", example = "SP")
    @JsonProperty("estado")
    private String estado;

    @Schema(description = "Complemento do endereço", example = "bloco 5, sala 500")
    @JsonProperty("complemento")
    private String complemento;

    @Schema(description = "Finalidade do endereço", example = "ENTREGA")
    @JsonProperty("finalidade")
    private Finalidade finalidade;

    @Schema(description = "Região", example = "SUDESTE")
    @JsonProperty("regiao")
    private Regiao regiao;

    public boolean ehEnderecoDeEntrega() {
        return this.finalidade == Finalidade.ENTREGA || this.finalidade == Finalidade.COBRANCA_ENTREGA;
    }

    public double aplicarPercentualFrete(double valorFrete) {
        return this.regiao.getPercentualRegiao() * valorFrete;
    }
}