package br.com.itau.geradornotafiscal.factories;

import br.com.itau.geradornotafiscal.model.Item;
import br.com.itau.geradornotafiscal.model.ItemNotaFiscal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class ItemNotaFiscalFactoryTest {

    @Test
    void deveCriarListaDeItemNotaFiscalComValoresCorretos() {
        // Arrange
        Item item1 = new Item("1", "Produto A", 10.0, 2);
        Item item2 = new Item("2", "Produto B", 20.0, 1);
        List<Item> itens = List.of(item1, item2);
        double aliquota = 0.1;

        ItemNotaFiscalFactory factory = new ItemNotaFiscalFactory();
        List<ItemNotaFiscal> itensNota = factory.criar(itens, aliquota);

        assertThat(itensNota).hasSize(2);

        ItemNotaFiscal nota1 = itensNota.get(0);
        assertThat(nota1.getIdItem()).isEqualTo("1");
        assertThat(nota1.getDescricao()).isEqualTo("Produto A");
        assertThat(nota1.getValorUnitario()).isEqualTo(10.0);
        assertThat(nota1.getQuantidade()).isEqualTo(2);
        assertThat(nota1.getValorTributoItem()).isEqualTo(1.0);

        ItemNotaFiscal nota2 = itensNota.get(1);
        assertThat(nota2.getIdItem()).isEqualTo("2");
        assertThat(nota2.getDescricao()).isEqualTo("Produto B");
        assertThat(nota2.getValorUnitario()).isEqualTo(20.0);
        assertThat(nota2.getQuantidade()).isEqualTo(1);
        assertThat(nota2.getValorTributoItem()).isEqualTo(2.0);
    }
}
