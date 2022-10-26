package vr.miniautorizador.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vr.miniautorizador.repository.CardRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static vr.miniautorizador.model.MockedCard.CARD_NUMBER;
import static vr.miniautorizador.model.MockedCard.defaultCardOpt;

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
            .isEqualTo(new BigDecimal("19.99"));
    }

}
