package br.unicamp.padroesestruturais.decorators;

import br.unicamp.padroesestruturais.legacy.domain.Taxa;
import br.unicamp.padroesestruturais.legacy.domain.TaxasAdicionais;

public class TaxaJurosParcela implements Taxa {
    private final Taxa taxa;

    public TaxaJurosParcela(Taxa taxa) {
        this.taxa = taxa;
    }

    @Override
    public double calcular(double valorBase) {
        return TaxasAdicionais.JUROS_PARCELAMENTO.applyAditional(taxa.calcular(valorBase));
    }
}
