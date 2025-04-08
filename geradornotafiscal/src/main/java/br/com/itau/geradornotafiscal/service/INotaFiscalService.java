package br.com.itau.geradornotafiscal.service;

import br.com.itau.geradornotafiscal.model.NotaFiscal;
import br.com.itau.geradornotafiscal.model.Pedido;

public interface INotaFiscalService {

	NotaFiscal gerarNotaFiscal(Pedido pedido);
	
}
