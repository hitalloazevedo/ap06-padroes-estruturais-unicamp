package br.unicamp.padroesestruturais.legacy;

import br.unicamp.padroesestruturais.legacy.domain.FormaPagamento;
import br.unicamp.padroesestruturais.legacy.domain.ResultadoCobranca;
import br.unicamp.padroesestruturais.legacy.externo.paysecure.PaySecureGateway;
import br.unicamp.padroesestruturais.legacy.externo.walletpay.WalletPaySDK;
import br.unicamp.padroesestruturais.legacy.gateway.BoletoAdapter;
import br.unicamp.padroesestruturais.legacy.gateway.GatewayPagamentoInterno;
import br.unicamp.padroesestruturais.legacy.gateway.PaySecureAdapter;
import br.unicamp.padroesestruturais.legacy.gateway.PixAdapter;
import br.unicamp.padroesestruturais.legacy.gateway.WalletPayAdapter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentGatewayTest {

    @Test
    void deveAprovarCobrancaViaCartaoCredito() {
        PaySecureAdapter paymentGateway = new PaySecureAdapter(new PaySecureGateway());
        ResultadoCobranca resultado = paymentGateway.processPayment("PED-001", "Joao Silva", 100.0);

        assertEquals("APROVADA", resultado.getStatus());
        assertEquals(100.0, resultado.getValorCobrado());
        assertEquals(FormaPagamento.CARTAO_CREDITO, resultado.getFormaPagamento());
        assertNotNull(resultado.getReferencia());
        assertTrue(resultado.getReferencia().startsWith("PSEC-"));
    }

    @Test
    void deveAprovarCobrancaViaPix() {
        GatewayPagamentoInterno gatewayInterno = new GatewayPagamentoInterno();
        PixAdapter paymentGateway = new PixAdapter(gatewayInterno);
        ResultadoCobranca resultado = paymentGateway.processPayment("PED-002", "Maria Santos", 250.0);

        assertEquals("APROVADA", resultado.getStatus());
        assertEquals(FormaPagamento.PIX, resultado.getFormaPagamento());
        assertEquals("PED-002", resultado.getPedidoId());
    }

    @Test
    void deveAprovarCobrancaViaBoleto() {
        GatewayPagamentoInterno gatewayInterno = new GatewayPagamentoInterno();
        BoletoAdapter paymentGateway = new BoletoAdapter(gatewayInterno);
        ResultadoCobranca resultado = paymentGateway.processPayment("PED-003", "Carlos Oliveira", 150.0);

        assertEquals("APROVADA", resultado.getStatus());
        assertEquals(FormaPagamento.BOLETO, resultado.getFormaPagamento());
        assertEquals("PED-003", resultado.getPedidoId());
    }

    @Test
    void deveAprovarCobrancaViaWalletPay() {
        WalletPayAdapter paymentGateway = new WalletPayAdapter(new WalletPaySDK());
        ResultadoCobranca resultado = paymentGateway.processPayment("PED-004", "Ana Pereira", 300.0);

        assertEquals("APROVADA", resultado.getStatus());
        assertEquals(FormaPagamento.WALLET_PAY, resultado.getFormaPagamento());
        assertEquals("PED-004", resultado.getPedidoId());
        assertNotNull(resultado.getReferencia());
        assertTrue(resultado.getReferencia().startsWith("WPAY-"));
    }

    @Test
    void deveRecusarCobrancaViaCartaoCreditoAcimaDoLimite() {
        PaySecureAdapter paymentGateway = new PaySecureAdapter(new PaySecureGateway());
        ResultadoCobranca resultado = paymentGateway.processPayment("PED-005", "Construtora ABC Ltda", 15000.0);

        assertEquals("RECUSADA", resultado.getStatus());
        assertEquals(FormaPagamento.CARTAO_CREDITO, resultado.getFormaPagamento());
    }

    @Test
    void deveRecusarCobrancaViaWalletPayAcimaDoLimite() {
        WalletPayAdapter paymentGateway = new WalletPayAdapter(new WalletPaySDK());
        ResultadoCobranca resultado = paymentGateway.processPayment("PED-006", "Construtora ABC Ltda", 10001.0);

        assertEquals("RECUSADA", resultado.getStatus());
        assertEquals(FormaPagamento.WALLET_PAY, resultado.getFormaPagamento());
    }
}