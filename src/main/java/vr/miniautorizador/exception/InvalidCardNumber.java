package vr.miniautorizador.exception;

public class InvalidCardNumber extends AuthorizationException {

    public InvalidCardNumber() {
        super("CARTAO_INEXISTENTE");
    }
}
