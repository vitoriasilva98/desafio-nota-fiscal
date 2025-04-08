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
    void deveRetornarAliquotaDeZeroPorCento_ParaPessoaFisica() {
        destinatario.setTipoPessoa(TipoPessoa.FISICA);
        double valorTotalItens = 500.0;

        double aliquotaEsperada = RegimeTributacaoPF.IMPOSTO_DE_RENDA.obterTaxaAliquota(valorTotalItens);
        double aliquota = destinatario.obterAliquota(valorTotalItens);

        assertEquals(aliquotaEsperada, aliquota);
    }

    @Test
    void deveRetornarAliquotaDeDozePorCento_ParaPessoaFisica() {
        destinatario.setTipoPessoa(TipoPessoa.FISICA);
        double valorTotalItens = 1000.0;

        double aliquotaEsperada = RegimeTributacaoPF.IMPOSTO_DE_RENDA.obterTaxaAliquota(valorTotalItens);
        double aliquota = destinatario.obterAliquota(valorTotalItens);

        assertEquals(aliquotaEsperada, aliquota);
    }

    @Test
    void deveRetornarAliquotaDeQuinzePorCento_ParaPessoaFisica() {
        destinatario.setTipoPessoa(TipoPessoa.FISICA);
        double valorTotalItens = 3500.0;

        double aliquotaEsperada = RegimeTributacaoPF.IMPOSTO_DE_RENDA.obterTaxaAliquota(valorTotalItens);
        double aliquota = destinatario.obterAliquota(valorTotalItens);

        assertEquals(aliquotaEsperada, aliquota);
    }

    @Test
    void deveRetornarAliquotaDeDezessetePorCento_ParaPessoaFisica() {
        destinatario.setTipoPessoa(TipoPessoa.FISICA);
        double valorTotalItens = 3501.0;

        double aliquotaEsperada = RegimeTributacaoPF.IMPOSTO_DE_RENDA.obterTaxaAliquota(valorTotalItens);
        double aliquota = destinatario.obterAliquota(valorTotalItens);

        assertEquals(aliquotaEsperada, aliquota);
    }

    @Test
    void deveRetornarAliquotaDeTresPorCentoNoRegimeSimplesNacional_ParaPessoaJuridica() {
        destinatario.setTipoPessoa(TipoPessoa.JURIDICA);
        destinatario.setRegimeTributacao(RegimeTributacaoPJ.SIMPLES_NACIONAL);

        double aliquota = destinatario.obterAliquota(500.0);

        assertEquals(0.03, aliquota);
    }

    @Test
    void deveRetornarAliquotaDeSetePorCentoNoRegimeSimplesNacional_ParaPessoaJuridica() {
        destinatario.setTipoPessoa(TipoPessoa.JURIDICA);
        destinatario.setRegimeTributacao(RegimeTributacaoPJ.SIMPLES_NACIONAL);

        double aliquota = destinatario.obterAliquota(1000.0);

        assertEquals(0.07, aliquota);
    }

    @Test
    void deveRetornarAliquotaDeTrezePorCentoNoRegimeSimplesNacional_ParaPessoaJuridica() {
        destinatario.setTipoPessoa(TipoPessoa.JURIDICA);
        destinatario.setRegimeTributacao(RegimeTributacaoPJ.SIMPLES_NACIONAL);

        double aliquota = destinatario.obterAliquota(5000.0);

        assertEquals(0.13, aliquota);
    }

    @Test
    void deveRetornarAliquotaDeDezenovePorCentoNoRegimeSimplesNacional_ParaPessoaJuridica() {
        destinatario.setTipoPessoa(TipoPessoa.JURIDICA);
        destinatario.setRegimeTributacao(RegimeTributacaoPJ.SIMPLES_NACIONAL);

        double aliquota = destinatario.obterAliquota(5001.0);

        assertEquals(0.19, aliquota);
    }

    @Test
    void deveRetornarAliquotaDeTresPorCentoNoRegimeLucroReal_ParaPessoaJuridica() {
        destinatario.setTipoPessoa(TipoPessoa.JURIDICA);
        destinatario.setRegimeTributacao(RegimeTributacaoPJ.LUCRO_REAL);

        double aliquota = destinatario.obterAliquota(999.0);

        assertEquals(0.03, aliquota);
    }

    @Test
    void deveRetornarAliquotaDeNovePorCentoNoRegimeLucroReal_ParaPessoaJuridica() {
        destinatario.setTipoPessoa(TipoPessoa.JURIDICA);
        destinatario.setRegimeTributacao(RegimeTributacaoPJ.LUCRO_REAL);

        double aliquota = destinatario.obterAliquota(1500.0);

        assertEquals(0.09, aliquota);
    }

    @Test
    void deveRetornarAliquotaDeQuinzePorCentoNoRegimeLucroReal_ParaPessoaJuridica() {
        destinatario.setTipoPessoa(TipoPessoa.JURIDICA);
        destinatario.setRegimeTributacao(RegimeTributacaoPJ.LUCRO_REAL);

        double aliquota = destinatario.obterAliquota(5000.0);

        assertEquals(0.15, aliquota);
    }

    @Test
    void deveRetornarAliquotaDeVintePorCentoNoRegimeLucroReal_ParaPessoaJuridica() {
        destinatario.setTipoPessoa(TipoPessoa.JURIDICA);
        destinatario.setRegimeTributacao(RegimeTributacaoPJ.LUCRO_REAL);

        double aliquota = destinatario.obterAliquota(5050.0);

        assertEquals(0.20, aliquota);
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
