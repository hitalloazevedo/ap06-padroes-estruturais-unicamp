package br.unicamp.padroesestruturais.decorators;

import br.unicamp.padroesestruturais.legacy.domain.Cobranca;
import br.unicamp.padroesestruturais.legacy.domain.TaxasAdicionais;

public class CobrancaComSeguro extends CobrancaDecorator {

    public CobrancaComSeguro(Cobranca cobranca) {
        super(cobranca);
    }
    
    @Override
    public double getValorCobrado() {
        return  TaxasAdicionais.SEGURO_TRANSACAO.applyAditional(getValorCobrado());
    }
}
