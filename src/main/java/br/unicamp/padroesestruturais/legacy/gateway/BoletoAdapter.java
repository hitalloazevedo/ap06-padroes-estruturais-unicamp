package br.unicamp.padroesestruturais.legacy.gateway;

import br.unicamp.padroesestruturais.legacy.domain.FormaPagamento;
import br.unicamp.padroesestruturais.legacy.domain.ResultadoCobranca;

public class BoletoAdapter implements PaymentGateway {

    private GatewayPagamentoInterno gatewayInterno;

    public BoletoAdapter(GatewayPagamentoInterno gatewayInterno) {
        this.gatewayInterno = gatewayInterno;
    }

    @Override
    public ResultadoCobranca processPayment(String orderId, String customer, double amount) {
        return gatewayInterno.cobrar(orderId, customer, amount, FormaPagamento.BOLETO);
    }
    
}
