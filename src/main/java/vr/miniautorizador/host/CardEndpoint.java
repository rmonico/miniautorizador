package vr.miniautorizador.host;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import vr.miniautorizador.host.dto.CardBalanceResponseDto;
import vr.miniautorizador.service.CardService;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class CardEndpoint {

    private final CardService cardService;

    @GetMapping("/cartoes/{numeroCartao}")
    public CardBalanceResponseDto getCardBalance(@PathVariable("numeroCartao") String cardNumber) {
        BigDecimal saldo = cardService.getBalance(cardNumber).get();

        return new CardBalanceResponseDto(saldo);
    }
}
