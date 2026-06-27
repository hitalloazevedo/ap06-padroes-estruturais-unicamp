package br.unicamp.padroesestruturais.decorators;

import br.unicamp.padroesestruturais.legacy.domain.Taxa;
import br.unicamp.padroesestruturais.legacy.domain.TaxasAdicionais;

public class TaxaDescontoFidelidade implements Taxa {
    private final Taxa taxa;

    public TaxaDescontoFidelidade(Taxa taxa) {
        this.taxa = taxa;
    }

    @Override
    public double calcular(double valorBase) {
        return TaxasAdicionais.DESCONTO_FIDELIDADE.applyAditional(taxa.calcular(valorBase));
    }
}
