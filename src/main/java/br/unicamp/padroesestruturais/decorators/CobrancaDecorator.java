package br.unicamp.padroesestruturais.decorators;

import br.unicamp.padroesestruturais.legacy.domain.Cobranca;
import br.unicamp.padroesestruturais.legacy.domain.FormaPagamento;


public abstract class CobrancaDecorator implements Cobranca {
    
    protected Cobranca cobranca; 

    public CobrancaDecorator (Cobranca cobranca) {
        this.cobranca = cobranca;        
    }

    @Override
    public FormaPagamento getFormaPagamento() {
        return cobranca.getFormaPagamento();
    }

    @Override
    public String getPedidoId() {
        return cobranca.getPedidoId();
    }

    @Override
    public String getReferencia() {
        return cobranca.getReferencia();
    }

    @Override
    public String getStatus() {
        return cobranca.getStatus();
    }

    @Override
    public double getValorCobrado() {
        return cobranca.getValorCobrado();
    }

    
}
