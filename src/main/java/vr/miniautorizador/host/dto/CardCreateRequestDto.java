package vr.miniautorizador.host.dto;

import lombok.Data;

@Data
public class CardCreateRequestDto {

    private String numeroCartao;
    private String senha;

}
