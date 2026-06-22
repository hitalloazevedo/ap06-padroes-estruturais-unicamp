package br.unicamp.padroesestruturais.decorators;

import br.unicamp.padroesestruturais.legacy.domain.Cobranca;
import br.unicamp.padroesestruturais.legacy.domain.TaxasAdicionais;

public class CobrancaComEmissao extends CobrancaDecorator {

    public CobrancaComEmissao(Cobranca cobranca) {
        super(cobranca);
    }
    
    @Override
    public double getValorCobrado() {
        return  TaxasAdicionais.TAXA_EMISSAO.applyAditional(getValorCobrado());
    }
}
