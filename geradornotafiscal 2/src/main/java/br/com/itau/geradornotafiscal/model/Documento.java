package br.com.itau.geradornotafiscal.model;

import br.com.itau.geradornotafiscal.enums.TipoDocumento;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Documento {

    @NotBlank(message = "O número do documento é obrigatório")
    @Schema(description = "Numeração do documento", example = "12345678")
    @JsonProperty("numero")
    private String numero;

    @NotNull(message = "O tipo do documento é obrigatório")
    @Schema(description = "Tipo do documento", example = "CPF")
    @JsonProperty("tipo")
    private TipoDocumento tipo;

}
