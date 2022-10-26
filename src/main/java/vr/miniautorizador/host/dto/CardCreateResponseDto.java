package vr.miniautorizador.host.dto;

import lombok.Data;
import lombok.NonNull;
import vr.miniautorizador.model.Card;

@Data
public class CardCreateResponseDto {

    private String numeroCartao;
    private String senha;

    public CardCreateResponseDto(@NonNull Card created) {
        this.numeroCartao = created.getNumero();
        this.senha = created.getSenha();
    }
}
