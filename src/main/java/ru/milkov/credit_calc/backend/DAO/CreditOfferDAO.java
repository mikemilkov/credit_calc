package ru.milkov.credit_calc.backend.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.milkov.credit_calc.backend.domain.Client;
import ru.milkov.credit_calc.backend.domain.CreditOffer;

import java.util.List;
import java.util.UUID;

public interface CreditOfferDAO extends JpaRepository<CreditOffer, UUID> {

    List<CreditOffer> findAllByClient(Client client);
}
