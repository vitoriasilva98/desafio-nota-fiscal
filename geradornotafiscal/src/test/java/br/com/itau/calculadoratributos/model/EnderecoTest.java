package br.com.itau.calculadoratributos.model;

import br.com.itau.geradornotafiscal.enums.Finalidade;
import br.com.itau.geradornotafiscal.enums.Regiao;
import br.com.itau.geradornotafiscal.model.Endereco;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EnderecoTest {

    @Test
    void deveRetornarTrueQuandoFinalidadeForENTREGA() {
        Endereco endereco = Endereco.builder()
                .finalidade(Finalidade.ENTREGA)
                .build();

        assertTrue(endereco.ehEnderecoDeEntrega());
    }

    @Test
    void deveRetornarTrueQuandoFinalidadeForCOBRANCA_ENTREGA() {
        Endereco endereco = Endereco.builder()
                .finalidade(Finalidade.COBRANCA_ENTREGA)
                .build();

        assertTrue(endereco.ehEnderecoDeEntrega());
    }

    @Test
    void deveRetornarFalseQuandoFinalidadeNaoForEntrega() {
        Endereco endereco = Endereco.builder()
                .finalidade(Finalidade.COBRANCA)
                .build();

        assertFalse(endereco.ehEnderecoDeEntrega());
    }

    @Test
    void deveCalcularPercentualFreteCorretamente() {
        Regiao regiaoSudeste = Regiao.SUDESTE;
        Endereco endereco = Endereco.builder()
                .regiao(regiaoSudeste)
                .build();

        double freteBase = 100.0;
        double valorComPercentual = endereco.aplicarPercentualFrete(freteBase);

        assertEquals(regiaoSudeste.getPercentualRegiao() * freteBase, valorComPercentual);
    }
}
