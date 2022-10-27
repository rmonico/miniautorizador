package vr.miniautorizador.exception;

public class InvalidPassword extends AuthorizationException {

    public InvalidPassword() {
        super("SENHA_INVALIDA");
    }
}
