package br.unicamp.padroesestruturais.legacy.domain;

public enum TaxasAdicionais {
    DESCONTO_FIDELIDADE {
        @Override
        public double applyAditional(double amount) {
            return amount * 0.95;
        }
    },
    JUROS_PARCELAMENTO{
        @Override
        public double applyAditional(double amount) {
            return amount * 1.0299;
        }
    },
    TAXA_OPERACAO_INTERNACIONAL{
        @Override
        public double applyAditional(double amount) {
            return amount * 1.05;
        }
    },
    SEGURO_TRANSACAO {
        @Override
        public double applyAditional(double amount) {
            return amount + 4.90;
        }
    },
    TAXA_ANTECIPACAO{
        @Override
        public double applyAditional(double amount) {
            return amount * 1.015;
        }
    },
    TAXA_EMISSAO{
        @Override
        public double applyAditional(double amount) {
            return amount + 2.50;
        }
    };

    public abstract double applyAditional (double amount); 
}
