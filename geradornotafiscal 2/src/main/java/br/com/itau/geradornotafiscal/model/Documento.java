package br.com.itau.geradornotafiscal.model;

import br.com.itau.geradornotafiscal.annotations.DocumentoValido;
import br.com.itau.geradornotafiscal.enums.TipoDocumento;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DocumentoValido
public class Documento {

    @Pattern(regexp = "\\d+", message = "O campo deve conter apenas números")
    @NotBlank(message = "O número do documento é obrigatório")
    @Schema(description = "Numeração do documento", example = "53765188093")
    @JsonProperty("numero")
    private String numero;

    @NotNull(message = "O tipo do documento é obrigatório")
    @Schema(description = "Tipo do documento", example = "CPF")
    @JsonProperty("tipo")
    private TipoDocumento tipo;

}
