package br.unicamp.padroesestruturais.decorators;

import br.unicamp.padroesestruturais.legacy.domain.Cobranca;
import br.unicamp.padroesestruturais.legacy.domain.TaxasAdicionais;

public class CobrancaComJurosParcela extends CobrancaDecorator {

    public CobrancaComJurosParcela(Cobranca cobranca) {
        super(cobranca);
    }
    
    @Override
    public double getValorCobrado() {
        return  TaxasAdicionais.JUROS_PARCELAMENTO.applyAditional(getValorCobrado());
    }
}
