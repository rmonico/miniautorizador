package vr.miniautorizador.host;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import vr.miniautorizador.service.CardService;

import static java.util.Optional.of;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        when(service.getBalance(CARD_NUMBER)).thenReturn(of(defaultCard().getSaldo()));

        mvc.perform(
                get("/cartoes/{numeroCartao}", CARD_NUMBER))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.saldo").value(CARD_BALANCE));
    }

}
