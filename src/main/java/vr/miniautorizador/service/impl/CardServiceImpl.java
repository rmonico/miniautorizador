package vr.miniautorizador.service.impl;

import lombok.val;
import org.springframework.stereotype.Service;
import vr.miniautorizador.exception.ExistingCardException;
import vr.miniautorizador.exception.InsufficientBalance;
import vr.miniautorizador.exception.InvalidCardNumber;
import vr.miniautorizador.exception.InvalidPassword;
import vr.miniautorizador.model.Card;
import vr.miniautorizador.model.Transaction;
import vr.miniautorizador.repository.CardRepository;
import vr.miniautorizador.service.CardService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.math.BigDecimal.valueOf;

@Service
public class CardServiceImpl implements CardService {

    @FunctionalInterface
    interface CardCreationHandler {
        Card handle(Card card);
    }

    private final CardRepository repository;
    private final Map<Boolean, CardCreationHandler> creationHandler;

    public CardServiceImpl(CardRepository repository) {
        this.repository = repository;

        this.creationHandler = new HashMap<>();
        this.creationHandler.put(TRUE, card -> {
            throw new ExistingCardException(card);
        });
        this.creationHandler.put(FALSE, repository::insert);
    }

    @Override
    public Optional<BigDecimal> getBalance(String cardNumber) {
        val cardOpt = repository.findByNumero(cardNumber);

        return cardOpt.map(Card::getSaldo);
    }

    @Override
    public Card createCard(Card card) {
        val existing = repository.findByNumero(card.getNumero());

        return creationHandler.get(existing.isPresent()).handle(card);
    }

    @Override
    public long createTransaction(Transaction transaction) {
        val numero = transaction.getNumeroCartao();
        val valor = transaction.getValor();

        val card = repository.findByNumero(numero);

        authorize(transaction, card);

        return repository.findAndIncrementBalanceByNumero(numero, valor);
    }

    private void authorize(Transaction t, Optional<Card> card) {
        card.orElseThrow(InvalidCardNumber::new);
        card.map(Card::getSenha).filter(s -> s.equals(t.getSenhaCartao())).orElseThrow(InvalidPassword::new);
        card.map(Card::getSaldo).filter(s -> s.compareTo(t.getValor()) >= 0).orElseThrow(InsufficientBalance::new);
    }
}
