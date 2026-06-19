package br.unicamp.padroesestruturais.legacy.gateway;

import br.unicamp.padroesestruturais.legacy.domain.FormaPagamento;
import br.unicamp.padroesestruturais.legacy.domain.ResultadoCobranca;
import br.unicamp.padroesestruturais.legacy.externo.walletpay.ChargeRequest;
import br.unicamp.padroesestruturais.legacy.externo.walletpay.ChargeResponse;
import br.unicamp.padroesestruturais.legacy.externo.walletpay.ChargeStatus;
import br.unicamp.padroesestruturais.legacy.externo.walletpay.WalletPaySDK;

public class WalletPayAdapter implements PaymentGateway {

    private final WalletPaySDK walletPay;

    public WalletPayAdapter(WalletPaySDK walletPay) {
        this.walletPay = walletPay;
    }

    @Override
    public ResultadoCobranca processPayment(String pedidoId, String cliente, double valor) {

        ChargeResponse response = walletPay.charge(
            new ChargeRequest(
                pedidoId, 
                cliente, 
                (long)(valor * 100)
            )
        );

        PaymentGatewayStatus status = null;

        if (response.getStatus() == ChargeStatus.DECLINED) {
            status = PaymentGatewayStatus.RECUSADA;
        } else if (response.getStatus() == ChargeStatus.CONFIRMED) {
            status = PaymentGatewayStatus.APROVADA;
        } else {
            status = PaymentGatewayStatus.FALHOU;
        }

        return new ResultadoCobranca(
            pedidoId, 
            valor, 
            status.toString(), 
            response.getWalletTransactionId(), 
            FormaPagamento.WALLET_PAY
        );
    }
    
}
