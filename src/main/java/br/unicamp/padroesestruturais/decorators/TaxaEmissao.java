package br.unicamp.padroesestruturais.decorators;

import br.unicamp.padroesestruturais.legacy.domain.Taxa;
import br.unicamp.padroesestruturais.legacy.domain.TaxasAdicionais;

public class TaxaEmissao implements Taxa {
    private final Taxa taxa;

    public TaxaEmissao(Taxa taxa) {
        this.taxa = taxa;
    }

    @Override
    public double calcular(double valorBase) {
        return TaxasAdicionais.TAXA_EMISSAO.applyAditional(taxa.calcular(valorBase));
    }
}
