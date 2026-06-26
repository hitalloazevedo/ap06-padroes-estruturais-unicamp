package br.unicamp.padroesestruturais.decorators;

import br.unicamp.padroesestruturais.legacy.domain.Taxa;
import br.unicamp.padroesestruturais.legacy.domain.TaxasAdicionais;

public class TaxaAntecipacao implements Taxa {
    private Taxa taxa;

    public TaxaAntecipacao(Taxa taxa) {
        this.taxa = taxa;
    }
    
    @Override
    public double calcular(double valorBase) {
        return TaxasAdicionais.TAXA_ANTECIPACAO.applyAditional(taxa.calcular(valorBase));
    }
}
