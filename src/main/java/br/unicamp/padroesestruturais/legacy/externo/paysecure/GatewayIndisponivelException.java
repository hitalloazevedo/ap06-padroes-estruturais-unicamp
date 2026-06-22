package br.unicamp.padroesestruturais.legacy.externo.paysecure;

/**
 * Excecao lancada pelo SDK PaySecure quando a transacao nao pode ser processada.
 *
 * Faz parte da biblioteca de terceiros e nao deve ser modificada.
 */
public class GatewayIndisponivelException extends Exception {

    public GatewayIndisponivelException(String mensagem) {
        super(mensagem);
    }
}
