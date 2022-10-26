package vr.miniautorizador.service.impl;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
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
        Optional<Card> cardOpt = repository.findByNumero(cardNumber);

        return cardOpt.map(Card::getSaldo);
    }

    @Override
    public Card createCard(Card cardData) {
        Card card = new Card();

        card.setId(new ObjectId());
        card.setNumero("1234");
        card.setSenha("1q2w3e4r");
        card.setSaldo(new BigDecimal(500));

        return card;
    }
}
