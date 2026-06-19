package br.unicamp.padroesestruturais.legacy;

import br.unicamp.padroesestruturais.legacy.domain.FormaPagamento;
import br.unicamp.padroesestruturais.legacy.domain.ResultadoCobranca;
import br.unicamp.padroesestruturais.legacy.externo.paysecure.PaySecureGateway;
import br.unicamp.padroesestruturais.legacy.gateway.BoletoAdapter;
import br.unicamp.padroesestruturais.legacy.gateway.GatewayPagamentoInterno;
import br.unicamp.padroesestruturais.legacy.gateway.PaySecureAdapter;
import br.unicamp.padroesestruturais.legacy.gateway.PixAdapter;

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
}
