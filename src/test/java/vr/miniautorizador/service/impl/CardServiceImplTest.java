package vr.miniautorizador.service.impl;

import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vr.miniautorizador.model.Card;
import vr.miniautorizador.repository.CardRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static vr.miniautorizador.model.MockedCard.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @InjectMocks
    private CardServiceImpl service;

    @Mock
    private CardRepository repository;

    @Test
    void GIVEN_card_number_WHEN_it_exists_THEN_return_its_balance() {
        when(repository.findByNumero(eq(CARD_NUMBER))).thenReturn(defaultCardOpt());

        Optional<BigDecimal> balance = service.getBalance(CARD_NUMBER);

        assertThat(balance)
            .isPresent()
            .get()
            .isEqualTo(CARD_BALANCE);
    }

    @Test
    void GIVEN_card_number_and_password_WHEN_number_dont_exists_THEN_create_it() {
        Card card = defaultCard();
        card.setId(null);
        card.setSaldo(null);

        when(repository.insert(eq(card))).thenReturn(defaultCard());

        Card created = service.createCard(card);

        ObjectAssert<Card> assertion = assertThat(created);

        assertion.extracting(Card::getId).isNotNull();
        assertion.extracting(Card::getNumero).isEqualTo(CARD_NUMBER);
        assertion.extracting(Card::getSenha).isEqualTo(CARD_PASS);
        assertion.extracting(Card::getSaldo).isEqualTo(CARD_BALANCE);
    }

}
