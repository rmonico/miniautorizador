package vr.miniautorizador.host;

import lombok.SneakyThrows;
import lombok.val;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import vr.miniautorizador.exception.ExistingCardException;
import vr.miniautorizador.exception.InsufficientBalance;
import vr.miniautorizador.exception.InvalidCardNumber;
import vr.miniautorizador.exception.InvalidPassword;
import vr.miniautorizador.model.Card;
import vr.miniautorizador.service.CardService;

import static java.math.BigDecimal.valueOf;
import static java.util.Optional.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static vr.miniautorizador.model.MockedCard.*;

@WebMvcTest(CardEndpoint.class)
public class CardEndpointTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CardService service;

    @Test
    @SneakyThrows
    void GIVEN_card_number_WHEN_it_exists_THEN_return_its_balance() {
        when(service.getBalance(eq(CARD_NUMBER))).thenReturn(of(defaultCard().getSaldo()));

        mvc.perform(
                get("/cartoes/{numeroCartao}", CARD_NUMBER))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.saldo").value(CARD_BALANCE));
    }

    @Test
    @SneakyThrows
    void GIVEN_card_number_WHEN_its_invalid_THEN_return_404() {
        mvc.perform(
                get("/cartoes/{numeroCartao}", INVALID_CARD_NUMBER))
            .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void GIVEN_card_number_and_password_WHEN_number_dont_exists_THEN_create_it() {
        val newCard = Card.builder()
            .numero("6549873025634501")
            .senha("1234")
            .build();

        val createdCard = Card.builder()
            .id(new ObjectId())
            .numero("6549873025634501")
            .senha("1234")
            .saldo(valueOf(500))
            .build();

        when(service.createCard(eq(newCard))).thenReturn(createdCard);

        val content = "{\n" +
            "    \"numeroCartao\": \"6549873025634501\",\n" +
            "    \"senha\": \"1234\"\n" +
            "}";

        mvc.perform(
                post("/cartoes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.senha").value("1234"))
            .andExpect(jsonPath("$.numeroCartao").value("6549873025634501"));
    }

    @Test
    @SneakyThrows
    void GIVEN_card_number_and_password_WHEN_number_already_exists_THEN_return_422() {
        val newCard = Card.builder()
            .numero("6549873025634501")
            .senha("1234")
            .build();

        when(service.createCard(eq(newCard))).thenThrow(new ExistingCardException(newCard));

        val content = "{\n" +
            "    \"numeroCartao\": \"6549873025634501\",\n" +
            "    \"senha\": \"1234\"\n" +
            "}";

        mvc.perform(
                post("/cartoes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.senha").value("1234"))
            .andExpect(jsonPath("$.numeroCartao").value("6549873025634501"));
    }

    @Test
    @SneakyThrows
    void GIVEN_card_number_password_value_THEN_update_balance() {
        String content = "{\n" +
            "    \"numeroCartao\": \"6549873025634501\",\n" +
            "    \"senhaCartao\": \"1234\",\n" +
            "    \"valor\": 10.00\n" +
            "}\n";

        mvc.perform(
                post("/transacoes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("OK"));
    }

    @Test
    @SneakyThrows
    void GIVEN_card_number_password_value_WHEN_number_doesnt_exists_THEN_return_invalid_card_number() {
        String content = "{\n" +
            "    \"numeroCartao\": \"6549873025634501\",\n" +
            "    \"senhaCartao\": \"1234\",\n" +
            "    \"valor\": 10.00\n" +
            "}\n";

        when(service.createTransaction(any())).thenThrow(InvalidCardNumber.class);

        mvc.perform(
                post("/transacoes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.message").value("CARTAO_INEXISTENTE"));
    }

    @Test
    @SneakyThrows
    void GIVEN_card_number_password_value_WHEN_invalid_password_THEN_return_invalid_password() {
        String content = "{\n" +
            "    \"numeroCartao\": \"6549873025634501\",\n" +
            "    \"senhaCartao\": \"1234\",\n" +
            "    \"valor\": 10.00\n" +
            "}\n";

        when(service.createTransaction(any())).thenThrow(InvalidPassword.class);

        mvc.perform(
                post("/transacoes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.message").value("SENHA_INVALIDA"));
    }

    @Test
    @SneakyThrows
    void GIVEN_card_number_password_value_WHEN_insufficient_balance_THEN_return_insufficient_balance() {
        String content = "{\n" +
            "    \"numeroCartao\": \"6549873025634501\",\n" +
            "    \"senhaCartao\": \"1234\",\n" +
            "    \"valor\": 10.00\n" +
            "}\n";

        when(service.createTransaction(any())).thenThrow(InsufficientBalance.class);

        mvc.perform(
                post("/transacoes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.message").value("SALDO_INSUFICIENTE"));
    }

}
