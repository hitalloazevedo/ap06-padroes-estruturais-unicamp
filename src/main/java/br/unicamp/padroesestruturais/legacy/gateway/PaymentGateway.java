package br.unicamp.padroesestruturais.legacy.gateway;

import br.unicamp.padroesestruturais.legacy.domain.ResultadoCobranca;

public interface PaymentGateway {
    public ResultadoCobranca processPayment(String orderId, String customer, double amount);
}
