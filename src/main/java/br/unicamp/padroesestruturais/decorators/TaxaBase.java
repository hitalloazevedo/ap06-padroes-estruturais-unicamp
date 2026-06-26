package br.unicamp.padroesestruturais.decorators;

import br.unicamp.padroesestruturais.legacy.domain.Taxa;

public class TaxaBase implements Taxa {

    @Override
    public double calcular(double valorBase) {
        return valorBase;
    }
    
}
