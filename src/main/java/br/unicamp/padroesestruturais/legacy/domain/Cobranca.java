package br.unicamp.padroesestruturais.legacy.domain;

public interface Cobranca {
    double getValorCobrado();
    String getPedidoId();
    String getStatus();
    String getReferencia();
    FormaPagamento getFormaPagamento();
}