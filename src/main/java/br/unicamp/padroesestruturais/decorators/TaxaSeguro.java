package br.unicamp.padroesestruturais.decorators;

import br.unicamp.padroesestruturais.legacy.domain.Taxa;
import br.unicamp.padroesestruturais.legacy.domain.TaxasAdicionais;

public class TaxaSeguro implements Taxa {

    private final Taxa taxa;

    public TaxaSeguro(Taxa taxa) {
        this.taxa = taxa;
    }

    @Override
    public double calcular(double valorBase) {
        return TaxasAdicionais.SEGURO_TRANSACAO.applyAditional(taxa.calcular(valorBase));
    }
    
}
