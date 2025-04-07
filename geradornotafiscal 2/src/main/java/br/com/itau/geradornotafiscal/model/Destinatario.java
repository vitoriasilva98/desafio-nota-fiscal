package br.com.itau.geradornotafiscal.model;

import java.util.List;

import br.com.itau.geradornotafiscal.enums.RegimeTributacaoPF;
import br.com.itau.geradornotafiscal.enums.RegimeTributacaoPJ;
import br.com.itau.geradornotafiscal.enums.TipoPessoa;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Destinatario {

	@Schema(description = "Nome do destinatário", example = "1")
	@JsonProperty("nome")
	private String nome;

	@Schema(description = "Tipo de Pessoa", example = "FISICA")
	@JsonProperty("tipo_pessoa")
	private TipoPessoa tipoPessoa;

	@Schema(description = "Regime de Tributação (Pessoa Jurídica)", example = "SIMPLES_NACIONAL")
	@JsonProperty("regime_tributacao")
	private RegimeTributacaoPJ regimeTributacao;

	@Schema(description = "Lista de documentos")
	@JsonProperty("documentos")
	private List<Documento> documentos;

	@Schema(description = "Lista de endereços")
	@JsonProperty("enderecos")
	private List<Endereco> enderecos;

	public double obterAliquota(double valorTotalItens) {

		if (this.tipoPessoa == TipoPessoa.FISICA) {
			return RegimeTributacaoPF.IMPOSTO_DE_RENDA.obterTaxaAliquota(valorTotalItens);
		} else {
			return this.regimeTributacao.obterTaxaAliquota(valorTotalItens);
		}
	}

	public double calcularFreteComBaseNaRegiao(double valorFrete) {
		return enderecos.stream()
				.filter(Endereco::ehEnderecoDeEntrega)
				.map(e -> e.aplicarPercentualFrete(valorFrete))
				.findFirst()
				.orElse(0.0);
	}
}




