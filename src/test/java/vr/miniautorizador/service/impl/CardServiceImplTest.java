package vr.miniautorizador.service.impl;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vr.miniautorizador.model.Card;
import vr.miniautorizador.repository.CardRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    private static final String CARD_NUMBER = "1234";
    private static final BigDecimal CARD_BALANCE = new BigDecimal("19.99");
    private static final String CARD_PASS = "1q2w3e4r";

    @InjectMocks
    private CardServiceImpl service;

    @Mock
    private CardRepository repository;

    private Card makeCard() {
        return new Card(new ObjectId(), CARD_NUMBER, CARD_PASS, CARD_BALANCE);
    }

    @Test
    void GIVEN_card_number_WHEN_it_exists_THEN_return_its_balance() {
        Optional<Card> theCard = Optional.of(makeCard());
        when(repository.findByNumero(CARD_NUMBER)).thenReturn(theCard);

        Optional<BigDecimal> balance = service.getBalance(CARD_NUMBER);

        assertTrue(balance.isPresent());
        assertEquals(new BigDecimal("19.99"), balance.get());
    }

}
