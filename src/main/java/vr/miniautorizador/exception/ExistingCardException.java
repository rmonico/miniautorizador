package vr.miniautorizador.exception;

import lombok.Getter;
import vr.miniautorizador.model.Card;

@Getter
public class ExistingCardException extends RuntimeException {

    private final Card card;

    public ExistingCardException(Card card) {
        this.card = card;
    }
}
