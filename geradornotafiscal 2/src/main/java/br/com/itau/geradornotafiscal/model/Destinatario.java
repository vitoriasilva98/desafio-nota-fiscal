package br.com.itau.geradornotafiscal.model;

import java.util.List;

import br.com.itau.geradornotafiscal.enums.RegimeTributacaoPF;
import br.com.itau.geradornotafiscal.enums.RegimeTributacaoPJ;
import br.com.itau.geradornotafiscal.enums.TipoPessoa;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Destinatario {
	@JsonProperty("nome")
	private String nome;

	@JsonProperty("tipo_pessoa")
	private TipoPessoa tipoPessoa;

	@JsonProperty("regime_tributacao")
	private RegimeTributacaoPJ regimeTributacao;

	@JsonProperty("documentos")
	private List<Documento> documentos;

	@JsonProperty("enderecos")
	private List<Endereco> enderecos;

	public double obterAliquota(double valorTotalItens) {

		if (this.tipoPessoa == TipoPessoa.FISICA) {
			return RegimeTributacaoPF.IMPOSTO_DE_RENDA.obterTaxaAliquota(valorTotalItens);
		} else {
			return this.regimeTributacao.obterTaxaAliquota(valorTotalItens);
		}
	}
}




