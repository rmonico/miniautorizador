package vr.miniautorizador.model;

import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.Optional;

public class MockedCard {
    public static final String CARD_NUMBER = "1234";

    public static final String INVALID_CARD_NUMBER = "5678";

    public static final BigDecimal CARD_BALANCE = new BigDecimal("19.99");

    public static final BigDecimal DEFAULT_CARD_BALANCE = BigDecimal.valueOf(500);

    public static final String CARD_PASS = "1q2w3e4r";

    public static Card defaultCard() {
        return new Card(new ObjectId(), CARD_NUMBER, CARD_PASS, CARD_BALANCE);
    }

    public static Optional<Card> defaultCardOpt() {
        return Optional.of(defaultCard());
    }
}
