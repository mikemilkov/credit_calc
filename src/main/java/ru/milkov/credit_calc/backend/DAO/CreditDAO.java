package ru.milkov.credit_calc.backend.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.milkov.credit_calc.backend.domain.Bank;
import ru.milkov.credit_calc.backend.domain.Credit;

import java.util.List;
import java.util.UUID;

public interface CreditDAO extends JpaRepository<Credit, UUID> {

    List<Credit> findAllByBank(Bank bank);
}
