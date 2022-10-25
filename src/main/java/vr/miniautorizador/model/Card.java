package vr.miniautorizador.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "card")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    @Id
    private ObjectId id;
    private String numero;
    private String senha;
    private BigDecimal saldo;
}
