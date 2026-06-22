package br.unicamp.padroesestruturais.legacy.gateway;

public enum PaymentGatewayStatus {
    APROVADA("APROVADA"),
    RECUSADA("RECUSADA"),
    FALHOU("FALHOU");

    private final String status;

    PaymentGatewayStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
