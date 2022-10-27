package vr.miniautorizador.host.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransactionRequestDto {

    private String numeroCartao;
    private String senhaCartao;
    private BigDecimal valor;
}
