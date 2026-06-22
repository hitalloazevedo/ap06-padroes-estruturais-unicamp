package br.unicamp.padroesestruturais.decorators;

import br.unicamp.padroesestruturais.legacy.domain.Cobranca;
import br.unicamp.padroesestruturais.legacy.domain.TaxasAdicionais;

public class CobrancaComDescontoFidelidade extends CobrancaDecorator {

    public CobrancaComDescontoFidelidade(Cobranca cobranca) {
        super(cobranca);
    }
    
    @Override
    public double getValorCobrado() {
        return  TaxasAdicionais.DESCONTO_FIDELIDADE.applyAditional(getValorCobrado());
    }
}
