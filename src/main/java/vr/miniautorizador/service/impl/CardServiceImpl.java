package vr.miniautorizador.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import vr.miniautorizador.exception.ExistingCardException;
import vr.miniautorizador.model.Card;
import vr.miniautorizador.repository.CardRepository;
import vr.miniautorizador.service.CardService;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository repository;

    @Override
    public Optional<BigDecimal> getBalance(String cardNumber) {
        val cardOpt = repository.findByNumero(cardNumber);

        return cardOpt.map(Card::getSaldo);
    }

    @Override
    public Card createCard(Card card) {
        throw new ExistingCardException();
    }
}
