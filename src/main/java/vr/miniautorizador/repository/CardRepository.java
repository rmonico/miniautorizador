package vr.miniautorizador.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import vr.miniautorizador.model.Card;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CardRepository extends MongoRepository<Card, ObjectId> {

    public List<Card> findAll();

    Optional<Card> findByNumero(String cardNumber);

    long findAndIncrementBalanceByNumero(String numeroCartao, BigDecimal valor);
}
