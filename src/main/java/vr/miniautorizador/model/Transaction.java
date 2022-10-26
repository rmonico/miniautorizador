package vr.miniautorizador.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Transaction {
    private String numeroCartao;
    private String senhaCartao;
    private BigDecimal valor;
}
