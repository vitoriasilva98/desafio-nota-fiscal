package br.com.itau.geradornotafiscal.service;

import br.com.itau.geradornotafiscal.model.NotaFiscal;

public interface IFinanceiroService {
    void enviarNotaFiscalParaContasReceber(NotaFiscal notaFiscal);
}
