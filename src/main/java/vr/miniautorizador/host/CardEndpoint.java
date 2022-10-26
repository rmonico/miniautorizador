package vr.miniautorizador.host;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vr.miniautorizador.exception.CardNotFoundException;
import vr.miniautorizador.host.dto.CardBalanceResponseDto;
import vr.miniautorizador.host.dto.CardCreateRequestDto;
import vr.miniautorizador.host.dto.CardCreateResponseDto;
import vr.miniautorizador.model.Card;
import vr.miniautorizador.service.CardService;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CardEndpoint {

    private final CardService cardService;

    @GetMapping("/cartoes/{numeroCartao}")
    public CardBalanceResponseDto getCardBalance(@PathVariable("numeroCartao") String cardNumber) {
        val saldo = cardService.getBalance(cardNumber);

        return new CardBalanceResponseDto(saldo.orElseThrow(CardNotFoundException::new));
    }

    @PostMapping("/cartoes")
    @ResponseStatus(HttpStatus.CREATED)
    public CardCreateResponseDto createCard(@RequestBody CardCreateRequestDto body) {
        val newCard = Card.builder()
            .numero(body.getNumeroCartao())
            .senha(body.getSenha())
            .build();

        val created = cardService.createCard(newCard);

        return new CardCreateResponseDto(created);
    }
}
