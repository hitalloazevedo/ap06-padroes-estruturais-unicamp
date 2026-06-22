package br.unicamp.padroesestruturais.decorators;

import br.unicamp.padroesestruturais.legacy.domain.Cobranca;
import br.unicamp.padroesestruturais.legacy.domain.TaxasAdicionais;

public class CobrancaComTaxaInternacional extends CobrancaDecorator {

    public CobrancaComTaxaInternacional(Cobranca cobranca) {
        super(cobranca);
    }
    
    @Override
    public double getValorCobrado() {
        return  TaxasAdicionais.TAXA_OPERACAO_INTERNACIONAL.applyAditional(getValorCobrado());
    }
    
}
