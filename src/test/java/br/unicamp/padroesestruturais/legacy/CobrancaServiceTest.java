package br.unicamp.padroesestruturais.legacy;

import br.unicamp.padroesestruturais.decorators.TaxaBase;
import br.unicamp.padroesestruturais.decorators.TaxaDescontoFidelidade;
import br.unicamp.padroesestruturais.decorators.TaxaInternacional;
import br.unicamp.padroesestruturais.decorators.TaxaJurosParcela;
import br.unicamp.padroesestruturais.decorators.TaxaSeguro;
import br.unicamp.padroesestruturais.legacy.domain.FormaPagamento;
import br.unicamp.padroesestruturais.legacy.domain.Pedido;
import br.unicamp.padroesestruturais.legacy.domain.ResultadoCobranca;
import br.unicamp.padroesestruturais.legacy.externo.paysecure.PaySecureGateway;
import br.unicamp.padroesestruturais.legacy.externo.walletpay.WalletPaySDK;
import br.unicamp.padroesestruturais.legacy.gateway.BoletoAdapter;
import br.unicamp.padroesestruturais.legacy.gateway.GatewayPagamentoInterno;
import br.unicamp.padroesestruturais.legacy.gateway.PaySecureAdapter;
import br.unicamp.padroesestruturais.legacy.gateway.PaymentGateway;
import br.unicamp.padroesestruturais.legacy.gateway.PixAdapter;
import br.unicamp.padroesestruturais.legacy.gateway.WalletPayAdapter;
import br.unicamp.padroesestruturais.legacy.service.CobrancaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CobrancaServiceTest {

    private CobrancaService service;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        HashMap<FormaPagamento, PaymentGateway> gateways = new HashMap<>();
        gateways.put(FormaPagamento.PIX, new PixAdapter(new GatewayPagamentoInterno()));
        gateways.put(FormaPagamento.BOLETO, new BoletoAdapter(new GatewayPagamentoInterno()));
        gateways.put(FormaPagamento.CARTAO_CREDITO, new PaySecureAdapter(new PaySecureGateway()));
        gateways.put(FormaPagamento.WALLET_PAY, new WalletPayAdapter(new WalletPaySDK()));
        service = new CobrancaService(gateways);

        pedido = new Pedido("PED-001", "Joao Silva", "Notebook Dell XPS 15", 1000.0);
    }

    @Test
    void deveCobrarViaBoletoSemAjustes() {
        ResultadoCobranca resultado = service.cobrar(pedido, FormaPagamento.BOLETO, new TaxaBase());

        assertEquals("APROVADA", resultado.getStatus());
        assertEquals(1000.0, resultado.getValorCobrado(), 0.001);
        assertEquals(FormaPagamento.BOLETO, resultado.getFormaPagamento());
    }

    @Test
    void deveCobrarViaPixSemAjustes() {
        ResultadoCobranca resultado = service.cobrar(pedido, FormaPagamento.PIX, new TaxaBase());

        assertEquals("APROVADA", resultado.getStatus());
        assertEquals(FormaPagamento.PIX, resultado.getFormaPagamento());
    }

    @Test
    void deveCobrarViaCartaoCreditoSemAjustes() {
        ResultadoCobranca resultado = service.cobrar(pedido, FormaPagamento.CARTAO_CREDITO, new TaxaBase());

        assertEquals("APROVADA", resultado.getStatus());
        assertNotNull(resultado.getReferencia());
        assertTrue(resultado.getReferencia().startsWith("PSEC-"));
    }

    @Test
    void deveRecusarCartaoCreditoParaValorAcimaDoLimite() {
        Pedido pedidoCaro = new Pedido("PED-003", "Construtora ABC Ltda", "Servidor", 15000.0);

        ResultadoCobranca resultado = service.cobrar(pedidoCaro, FormaPagamento.CARTAO_CREDITO, new TaxaBase());

        assertEquals("RECUSADA", resultado.getStatus());
    }

    @Test
    void deveLancarExcecaoParaFormaDePagamentoNaoSuportada() {
        assertThrows(IllegalArgumentException.class,
                () -> service.cobrar(pedido, null, new TaxaBase()));
    }

    @Test
    void naoAplicarNenhumAjusteMantemValorBase() {
        double valor = service.calcularValorFinal(1000.0, new TaxaBase());
        assertEquals(1000.0, valor, 0.001);
    }

    @Test
    void deveAplicarDescontoDeFidelidade() {
        double valor = service.calcularValorFinal(1000.0, new TaxaDescontoFidelidade(new TaxaBase()));
        assertEquals(950.0, valor, 0.001);
    }

    @Test
    void deveAplicarJurosDeParcelamento() {
        double valor = service.calcularValorFinal(1000.0, new TaxaJurosParcela(new TaxaBase()));
        assertEquals(1029.9, valor, 0.001);
    }

    @Test
    void deveAplicarTaxaInternacional() {
        double valor = service.calcularValorFinal(1000.0, new TaxaInternacional(new TaxaBase()));
        assertEquals(1050.0, valor, 0.001);
    }

    @Test
    void deveAplicarSeguro() {
        double valor = service.calcularValorFinal(1000.0, new TaxaSeguro(new TaxaBase()));
        assertEquals(1004.90, valor, 0.001);
    }

    @Test
    void deveAplicarTodosOsAjustesNaOrdemDefinida() {
        double valor = service.calcularValorFinal(1000.0, new TaxaSeguro(new TaxaInternacional(new TaxaJurosParcela(new TaxaDescontoFidelidade(new TaxaBase())))));

        double esperado = 1000.0;
        esperado = esperado - (esperado * 0.05);
        esperado = esperado + (esperado * 0.0299);
        esperado = esperado + (esperado * 0.05);
        esperado = esperado + 4.90;

        assertEquals(esperado, valor, 0.001);
    }


    @Test
    void deveCobrarEmLoteParaTodosPedidos() {
        List<Pedido> pedidos = Arrays.asList(
                new Pedido("PED-001", "Joao Silva", "Notebook", 1000.0),
                new Pedido("PED-002", "Maria Santos", "Cadeira", 500.0)
        );

        List<ResultadoCobranca> resultados = service.cobrarEmLote(pedidos, FormaPagamento.PIX, new TaxaBase());

        assertEquals(2, resultados.size());
        for (ResultadoCobranca resultado : resultados) {
            assertEquals("APROVADA", resultado.getStatus());
        }
    }

    @Test
    void cobrancaEmLoteDeveAplicarAjustesATodosPedidos() {
        List<Pedido> pedidos = Arrays.asList(
                new Pedido("PED-001", "Joao Silva", "Notebook", 1000.0),
                new Pedido("PED-002", "Maria Santos", "Cadeira", 2000.0)
        );

        List<ResultadoCobranca> resultados = service.cobrarEmLote(pedidos, FormaPagamento.BOLETO, new TaxaDescontoFidelidade(new TaxaBase()));

        assertEquals(950.0, resultados.get(0).getValorCobrado(), 0.001);
        assertEquals(1900.0, resultados.get(1).getValorCobrado(), 0.001);
    }

    // ── WalletPay via CobrancaService ─────────────────────────────────────────

    @Test
    void deveCobrarViaWalletPayComSucesso() {
        ResultadoCobranca resultado = service.cobrar(pedido, FormaPagamento.WALLET_PAY, new TaxaBase());

        assertEquals("APROVADA", resultado.getStatus());
        assertEquals(FormaPagamento.WALLET_PAY, resultado.getFormaPagamento());
        assertEquals("PED-001", resultado.getPedidoId());
        assertEquals(1000.0, resultado.getValorCobrado(), 0.001);
    }

    @Test
    void deveRetornarReferenciaWalletPayComPrefixoCorreto() {
        ResultadoCobranca resultado = service.cobrar(pedido, FormaPagamento.WALLET_PAY, new TaxaBase());

        assertNotNull(resultado.getReferencia());
        assertTrue(resultado.getReferencia().startsWith("WPAY-"));
    }

    @Test
    void deveRecusarWalletPayParaValorInteiroAcimaDoLimite() {
        Pedido pedidoCaro = new Pedido("PED-ALTO", "Empresa X", "Servidor Enterprise", 10001.0);

        ResultadoCobranca resultado = service.cobrar(pedidoCaro, FormaPagamento.WALLET_PAY, new TaxaBase());

        assertEquals("RECUSADA", resultado.getStatus());
        assertEquals(FormaPagamento.WALLET_PAY, resultado.getFormaPagamento());
    }

    @Test
    void deveRecusarWalletPayParaValorDecimalAcimaDoLimite() {
        Pedido pedidoLimite = new Pedido("PED-LIMITE", "Empresa Y", "Equipamento", 10000.50);

        ResultadoCobranca resultado = service.cobrar(pedidoLimite, FormaPagamento.WALLET_PAY, new TaxaBase());

        assertEquals("RECUSADA", resultado.getStatus());
    }

    @Test
    void deveMapeiarFalhaDoWalletPayComoFalhou() {
        Pedido pedidoZero = new Pedido("PED-ZERO", "Cliente Z", "Item", 0.0);

        ResultadoCobranca resultado = service.cobrar(pedidoZero, FormaPagamento.WALLET_PAY, new TaxaBase());

        assertEquals("FALHOU", resultado.getStatus());
        assertNull(resultado.getReferencia());
    }
}