package vr.miniautorizador.host;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vr.miniautorizador.exception.*;
import vr.miniautorizador.host.dto.*;
import vr.miniautorizador.model.Card;
import vr.miniautorizador.model.CreateTransactionErrorResponseDto;
import vr.miniautorizador.model.Transaction;
import vr.miniautorizador.service.CardService;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequiredArgsConstructor
public class CardEndpoint {

    private final CardService cardService;

    @GetMapping("/cartoes/{numeroCartao}")
    public CardBalanceResponseDto getCardBalance(@PathVariable("numeroCartao") String cardNumber) {
        val saldo = cardService.getBalance(cardNumber);

        return new CardBalanceResponseDto(saldo.orElseThrow(CardNotFound::new));
    }

    @PostMapping("/cartoes")
    public ResponseEntity<CardCreateResponseDto> createCard(@RequestBody CardCreateRequestDto body) {
        val newCard = Card.builder()
            .numero(body.getNumeroCartao())
            .senha(body.getSenha())
            .build();

        Card created;
        HttpStatus status;
        try {
            created = cardService.createCard(newCard);
            status = HttpStatus.CREATED;
        } catch (ExistingCard e) {
            created = e.getCard();
            status = HttpStatus.UNPROCESSABLE_ENTITY;
        }

        val response = new CardCreateResponseDto(created);

        return status(status).body(response);
    }

    @PostMapping("/transacoes")
    public ResponseEntity<?> createTransaction(@RequestBody CreateTransactionRequestDto request) {
        val transaction = Transaction.builder()
            .numeroCartao(request.getNumeroCartao())
            .senhaCartao(request.getSenhaCartao())
            .valor(request.getValor())
            .build();

        try {
            cardService.createTransaction(transaction);
        } catch (AuthorizationException e) {
            val body = new CreateTransactionErrorResponseDto(e.getMessage());
            return ResponseEntity.unprocessableEntity().body(body);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateTransactionResponseDto("OK"));
    }
}
