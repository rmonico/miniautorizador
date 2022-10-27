package vr.miniautorizador.exception;

public class InsufficientBalance extends AuthorizationException {

    public InsufficientBalance() {
        super("SALDO_INSUFICIENTE");
    }
}
