package vr.miniautorizador.model;

import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.Optional;

import static java.math.BigDecimal.valueOf;
import static java.util.Optional.of;

public class MockedCard {
    public static final String CARD_NUMBER = "1234";

    public static final String INVALID_CARD_NUMBER = "5678";

    public static final BigDecimal CARD_BALANCE = valueOf(500);

    public static final String CARD_PASS = "1q2w3e4r";

    public static Card defaultCard() {
        return new Card(new ObjectId(), CARD_NUMBER, CARD_PASS, CARD_BALANCE);
    }

    public static Optional<Card> defaultCardOpt() {
        return of(defaultCard());
    }
}
