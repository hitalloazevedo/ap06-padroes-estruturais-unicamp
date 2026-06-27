package br.unicamp.padroesestruturais.decorators;

import br.unicamp.padroesestruturais.legacy.domain.Taxa;
import br.unicamp.padroesestruturais.legacy.domain.TaxasAdicionais;

public class TaxaInternacional implements Taxa {
    private final Taxa taxa;

    public TaxaInternacional(Taxa taxa) {
        this.taxa = taxa;
    }

    @Override
    public double calcular(double valorBase) {
        return TaxasAdicionais.TAXA_OPERACAO_INTERNACIONAL.applyAditional(taxa.calcular(valorBase));
    }
    
}
