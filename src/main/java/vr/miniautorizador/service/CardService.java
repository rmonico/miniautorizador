package vr.miniautorizador.service;

import java.math.BigDecimal;
import java.util.Optional;

public interface CardService {

    Optional<BigDecimal> getBalance(String cardNumber);
}
