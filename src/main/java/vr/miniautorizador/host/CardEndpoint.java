package vr.miniautorizador.host;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vr.miniautorizador.exception.CardNotFoundException;
import vr.miniautorizador.host.dto.CardBalanceResponseDto;
import vr.miniautorizador.host.dto.CardCreateRequestDto;
import vr.miniautorizador.host.dto.CardCreateResponseDto;
import vr.miniautorizador.service.CardService;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CardEndpoint {

    private final CardService cardService;

    @GetMapping("/cartoes/{numeroCartao}")
    public CardBalanceResponseDto getCardBalance(@PathVariable("numeroCartao") String cardNumber) {
        Optional<BigDecimal> saldo = cardService.getBalance(cardNumber);

        return new CardBalanceResponseDto(saldo.orElseThrow(CardNotFoundException::new));
    }

    @PostMapping("/cartoes")
    @ResponseStatus(HttpStatus.CREATED)
    public CardCreateResponseDto createCard(@RequestBody CardCreateRequestDto body) {
        CardCreateResponseDto response = new CardCreateResponseDto();

        response.setNumeroCartao("6549873025634501");
        response.setSenha("1234");

        return response;
    }
}
