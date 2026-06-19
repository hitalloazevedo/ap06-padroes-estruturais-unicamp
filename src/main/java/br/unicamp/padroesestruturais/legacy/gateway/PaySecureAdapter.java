package br.unicamp.padroesestruturais.legacy.gateway;

import java.util.HashMap;
import java.util.Map;

import br.unicamp.padroesestruturais.legacy.domain.FormaPagamento;
import br.unicamp.padroesestruturais.legacy.domain.ResultadoCobranca;
import br.unicamp.padroesestruturais.legacy.externo.paysecure.PaySecureGateway;
import br.unicamp.padroesestruturais.legacy.externo.paysecure.TransacaoExterna;

public class PaySecureAdapter implements PaymentGateway {

    private PaySecureGateway paySecure;

    public PaySecureAdapter(PaySecureGateway paySecure) {
        this.paySecure = paySecure;
    }

    public String mapStatusCodeToMessage(int statusCode) {
        switch (statusCode) {
            case 200:
                return PaymentGatewayStatus.APROVADA.toString();
            case 402:
                return PaymentGatewayStatus.RECUSADA.toString();
            default:
                return PaymentGatewayStatus.FALHOU.toString();
        }
    }

    @Override
    public ResultadoCobranca processPayment(String orderId, String customer, double amount) {
        Map<String, Object> transactionData = new HashMap<>();
        transactionData.put("orderId", orderId);
        transactionData.put("customer", customer);
        transactionData.put("amount", amount);

        try {
            TransacaoExterna paySecureResponse = paySecure.processarTransacao(transactionData);
            ResultadoCobranca resultado = new ResultadoCobranca(
                orderId, 
                paySecureResponse.getValorProcessado(), 
                mapStatusCodeToMessage(paySecureResponse.getCodigoStatus()),
                paySecureResponse.getReferenciaExterna(),
                FormaPagamento.CARTAO_CREDITO
            );
            return resultado;
        } catch (Exception e) {
            System.err.println("Erro ao processar pagamento com PaySecure: " + e.getMessage());
            
            return new ResultadoCobranca(
                orderId, 
                amount, 
                PaymentGatewayStatus.FALHOU.toString(), 
                "", 
                FormaPagamento.CARTAO_CREDITO
            );
        }
    }
}
