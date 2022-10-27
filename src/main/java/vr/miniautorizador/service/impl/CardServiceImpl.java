package vr.miniautorizador.service.impl;

import lombok.val;
import org.springframework.stereotype.Service;
import vr.miniautorizador.exception.ExistingCardException;
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
        val numeroCartao = transaction.getNumeroCartao();
        val valor = transaction.getValor();

        val card = repository.findByNumero(numeroCartao);

        if (card.isEmpty())
            throw new InvalidCardNumber();
        else if (!card.get().getSenha().equals(transaction.getSenhaCartao()))
            throw new InvalidPassword();

        return repository.findAndIncrementBalanceByNumero(numeroCartao, valor);
    }
}
