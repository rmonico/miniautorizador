package vr.miniautorizador.host.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CardBalanceResponseDto {

    private BigDecimal saldo;
}
