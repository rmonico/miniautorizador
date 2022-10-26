package vr.miniautorizador.service;

import vr.miniautorizador.model.Card;
import vr.miniautorizador.model.Transaction;

import java.math.BigDecimal;
import java.util.Optional;

public interface CardService {

    Optional<BigDecimal> getBalance(String cardNumber);

    Card createCard(Card cardData);

    boolean createTransaction(Transaction transaction);
}
