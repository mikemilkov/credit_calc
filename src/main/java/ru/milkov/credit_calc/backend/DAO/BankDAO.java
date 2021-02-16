package ru.milkov.credit_calc.backend.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.milkov.credit_calc.backend.domain.Bank;

import java.util.UUID;

public interface BankDAO extends JpaRepository<Bank, UUID> {
}
