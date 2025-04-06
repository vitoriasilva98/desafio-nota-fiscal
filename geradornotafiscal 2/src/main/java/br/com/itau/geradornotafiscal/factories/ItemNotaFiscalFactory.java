package br.com.itau.geradornotafiscal.factories;

import br.com.itau.geradornotafiscal.model.Item;
import br.com.itau.geradornotafiscal.model.ItemNotaFiscal;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemNotaFiscalFactory {

    public List<ItemNotaFiscal> criar(List<Item> itens, double aliquotaPercentual) {
        return itens.stream()
                .map(item -> criarItem(item, aliquotaPercentual))
                .collect(Collectors.toList());
    }

    private ItemNotaFiscal criarItem(Item item, double aliquotaPercentual) {
        return ItemNotaFiscal.builder()
                .idItem(item.getIdItem())
                .descricao(item.getDescricao())
                .valorUnitario(item.getValorUnitario())
                .quantidade(item.getQuantidade())
                .valorTributoItem(item.getValorUnitario() * aliquotaPercentual)
                .build();
    }
}
