package br.unicamp.padroesestruturais.legacy.service;

import br.unicamp.padroesestruturais.legacy.domain.FormaPagamento;
import br.unicamp.padroesestruturais.legacy.domain.Pedido;
import br.unicamp.padroesestruturais.legacy.domain.ResultadoCobranca;
import br.unicamp.padroesestruturais.legacy.domain.Taxa;
import br.unicamp.padroesestruturais.legacy.gateway.PaymentGateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CobrancaService {

    private final HashMap<FormaPagamento, PaymentGateway> gateways;

    public CobrancaService(HashMap<FormaPagamento, PaymentGateway> gateways) {
        this.gateways = gateways;
    }

    public ResultadoCobranca cobrar(
        Pedido pedido, 
        FormaPagamento forma,
        Taxa taxa
    ) {

        PaymentGateway gateway = this.gateways.get(forma);

        if (gateway == null) {
            throw new IllegalArgumentException("Forma de pagamento não suportada: " + forma);
        }
        
        double valorFinal = this.calcularValorFinal(pedido.getValorBase(), taxa);

        return gateway.processPayment(pedido.getId(), pedido.getCliente(), valorFinal);
    }

    public List<ResultadoCobranca> cobrarEmLote(
        List<Pedido> pedidos, FormaPagamento forma, Taxa taxa
    ) {

        List<ResultadoCobranca> resultados = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            resultados.add(
                this.cobrar(
                    pedido, 
                    forma,
                    taxa
                )
            );
        }

        return resultados;
    }

    public double calcularValorFinal(double valorBase, Taxa taxa) {
        return taxa.calcular(valorBase);
    }
}
