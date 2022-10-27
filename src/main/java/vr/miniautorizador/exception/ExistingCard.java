package vr.miniautorizador.exception;

import lombok.Getter;
import vr.miniautorizador.model.Card;

@Getter
public class ExistingCard extends RuntimeException {

    private final Card card;

    public ExistingCard(Card card) {
        this.card = card;
    }
}
