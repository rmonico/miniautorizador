package vr.miniautorizador.service.impl;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vr.miniautorizador.exception.ExistingCard;
import vr.miniautorizador.exception.InsufficientBalance;
import vr.miniautorizador.exception.InvalidCardNumber;
import vr.miniautorizador.exception.InvalidPassword;
import vr.miniautorizador.model.Card;
import vr.miniautorizador.model.Transaction;
import vr.miniautorizador.repository.CardRepository;

import static java.math.BigDecimal.valueOf;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
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

        val balance = service.getBalance(CARD_NUMBER);

        assertThat(balance)
            .isPresent()
            .get()
            .isEqualTo(CARD_BALANCE);
    }

    @Test
    void GIVEN_card_number_and_password_WHEN_number_dont_exists_THEN_create_it() {
        val card = defaultCard();
        card.setId(null);
        card.setSaldo(null);

        when(repository.insert(eq(card))).thenReturn(defaultCard());

        val created = service.createCard(card);

        val assertion = assertThat(created);

        assertion.extracting(Card::getId).isNotNull();
        assertion.extracting(Card::getNumero).isEqualTo(CARD_NUMBER);
        assertion.extracting(Card::getSenha).isEqualTo(CARD_PASS);
        assertion.extracting(Card::getSaldo).isEqualTo(CARD_BALANCE);
    }

    @Test
    void GIVEN_card_number_and_password_WHEN_number_already_exists_THEN_return_422() {
        val card = Card.builder()
            .numero("6549873025634501")
            .senha("1234")
            .build();

        when(repository.findByNumero(eq(card.getNumero()))).thenReturn(of(card));

        assertThatThrownBy(() -> service.createCard(card)).isInstanceOf(ExistingCard.class);
    }

    @Test
    void GIVEN_card_number_password_value_THEN_update_balance() {
        when(repository.findAndIncrementBalanceByNumero(eq("6549873025634501"), eq(valueOf(10)))).thenReturn(1L);

        val transaction = Transaction.builder()
            .numeroCartao("6549873025634501")
            .senhaCartao("1234")
            .valor(valueOf(10))
            .build();

        val card = of(Card.builder()
            .numero("6549873025634501")
            .senha("1234")
            .saldo(valueOf(100))
            .build());

        when(repository.findByNumero(eq("6549873025634501"))).thenReturn(card);

        long totalUpdated = service.createTransaction(transaction);

        assertThat(totalUpdated).isEqualTo(1L);
        verify(repository).findAndIncrementBalanceByNumero("6549873025634501", valueOf(10));
    }

    @Test
    void GIVEN_card_number_password_value_WHEN_number_doesnt_exists_THEN_return_invalid_card_number() {
        val transaction = Transaction.builder()
            .numeroCartao("6549873025634501")
            .senhaCartao("1234")
            .valor(valueOf(10))
            .build();

        assertThatThrownBy(() -> service.createTransaction(transaction)).isInstanceOf(InvalidCardNumber.class);
    }

    @Test
    void GIVEN_card_number_password_value_WHEN_invalid_password_THEN_return_invalid_password() {
        val transaction = Transaction.builder()
            .numeroCartao("6549873025634501")
            .senhaCartao("5678")
            .valor(valueOf(10))
            .build();

        val card = of(Card.builder()
            .numero("6549873025634501")
            .senha("1234")
            .build());

        when(repository.findByNumero(eq("6549873025634501"))).thenReturn(card);

        assertThatThrownBy(() -> service.createTransaction(transaction)).isInstanceOf(InvalidPassword.class);
    }

    @Test
    void GIVEN_card_number_password_value_WHEN_insufficient_balance_THEN_return_insufficient_balance() {
        val transaction = Transaction.builder()
            .numeroCartao("6549873025634501")
            .senhaCartao("1234")
            .valor(valueOf(10))
            .build();

        val card = of(Card.builder()
            .numero("6549873025634501")
            .senha("1234")
            .saldo(valueOf(8))
            .build());

        when(repository.findByNumero(eq("6549873025634501"))).thenReturn(card);

        assertThatThrownBy(() -> service.createTransaction(transaction)).isInstanceOf(InsufficientBalance.class);
    }
}
