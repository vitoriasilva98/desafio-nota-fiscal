package br.com.itau.calculadoratributos.model;

import br.com.itau.geradornotafiscal.enums.RegimeTributacaoPF;
import br.com.itau.geradornotafiscal.enums.RegimeTributacaoPJ;
import br.com.itau.geradornotafiscal.enums.TipoPessoa;
import br.com.itau.geradornotafiscal.model.Destinatario;
import br.com.itau.geradornotafiscal.model.Endereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class DestinatarioTest {

    @Mock
    private Endereco enderecoEntrega;

    @InjectMocks
    private Destinatario destinatario;

    @BeforeEach
    void configuracaoInicial() {
        destinatario = new Destinatario();
        destinatario.setEnderecos(List.of(enderecoEntrega));
    }

    @Test
    void deveRetornarAliquota_ParaPessoaFisica() {
        destinatario.setTipoPessoa(TipoPessoa.FISICA);
        List<Double> valorTotalItensList = List.of(500.0, 1000.0, 3500.0, 3501.0);

        for (Double valorTotalItens : valorTotalItensList) {
            double aliquotaEsperada = RegimeTributacaoPF.IMPOSTO_DE_RENDA.obterTaxaAliquota(valorTotalItens);
            double aliquota = destinatario.obterAliquota(valorTotalItens);
            assertEquals(aliquotaEsperada, aliquota);
        }
    }

    @Test
    void deveRetornarAliquotaDeTresPorCentoNoRegimeSimplesNacional_ParaPessoaJuridica() {
        destinatario.setTipoPessoa(TipoPessoa.JURIDICA);
        destinatario.setRegimeTributacao(RegimeTributacaoPJ.SIMPLES_NACIONAL);

        double aliquota = destinatario.obterAliquota(500.0);

        assertEquals(0.03, aliquota);
    }

    @Test
    void deveRetornarAliquotaDeTresPorCentoNoRegimeLucroReal_ParaPessoaJuridica() {
        destinatario.setTipoPessoa(TipoPessoa.JURIDICA);
        destinatario.setRegimeTributacao(RegimeTributacaoPJ.LUCRO_REAL);

        double aliquota = destinatario.obterAliquota(999.0);

        assertEquals(0.03, aliquota);
    }

    @Test
    void deveRetornarAliquotaDeTresPorCentoNoRegimeLucroPresumido_ParaPessoaJuridica() {
        destinatario.setTipoPessoa(TipoPessoa.JURIDICA);
        destinatario.setRegimeTributacao(RegimeTributacaoPJ.LUCRO_PRESUMIDO);

        double aliquota = destinatario.obterAliquota(200.0);

        assertEquals(0.03, aliquota);
    }

    @Test
    void deveCalcularFreteComBaseNaRegiao() {
        double valorFrete = 100.0;

        when(enderecoEntrega.ehEnderecoDeEntrega()).thenReturn(true);
        when(enderecoEntrega.aplicarPercentualFrete(valorFrete)).thenReturn(110.0);

        double resultado = destinatario.calcularFreteComBaseNaRegiao(valorFrete);

        assertEquals(110.0, resultado);
    }

    @Test
    void deveRetornarZero_QuandoNaoHaEnderecoDeEntrega() {
        when(enderecoEntrega.ehEnderecoDeEntrega()).thenReturn(false);

        double resultado = destinatario.calcularFreteComBaseNaRegiao(100.0);

        assertEquals(0.0, resultado);
    }

    @Test
    void deveRetornarTrue_QuandoHaEnderecoValidoParaEntrega() {
        when(enderecoEntrega.ehEnderecoDeEntrega()).thenReturn(true);

        boolean resultado = destinatario.possuiEnderecoValidoParaEntrega();

        assertTrue(resultado);
    }

    @Test
    void deveRetornarFalse_QuandoNaoHaEnderecoValidoParaEntrega() {
        when(enderecoEntrega.ehEnderecoDeEntrega()).thenReturn(false);

        boolean resultado = destinatario.possuiEnderecoValidoParaEntrega();

        assertFalse(resultado);
    }

}
