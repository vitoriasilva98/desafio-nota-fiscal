package br.com.itau.geradornotafiscal.model;

import br.com.itau.geradornotafiscal.enums.Finalidade;
import br.com.itau.geradornotafiscal.enums.Regiao;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("cep")
    private String cep;

    @JsonProperty("logradouro")
    private String logradouro;

    @JsonProperty("numero")
    private String numero;

    @JsonProperty("estado")
    private String estado;

    @JsonProperty("complemento")
    private String complemento;

    @JsonProperty("finalidade")
    private Finalidade finalidade;

    @JsonProperty("regiao")
    private Regiao regiao;

    public boolean ehEnderecoDeEntrega() {
        return this.finalidade == Finalidade.ENTREGA || this.finalidade == Finalidade.COBRANCA_ENTREGA;
    }

    public double aplicarPercentualFrete(double valorFrete) {
        return this.regiao.getPercentualRegiao() * valorFrete;
    }
}