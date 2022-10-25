package vr.miniautorizador.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vr.miniautorizador.model.Card;
import vr.miniautorizador.repository.CardRepository;
import vr.miniautorizador.service.CardService;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository repository;

    @Override
    public Optional<BigDecimal> getBalance(String cardNumber) {
        return null;
    }
}
