package ru.milkov.credit_calc.backend.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.milkov.credit_calc.backend.domain.Bank;
import ru.milkov.credit_calc.backend.domain.Client;

import java.util.List;
import java.util.UUID;

public interface ClientDAO extends JpaRepository<Client, UUID> {

    List<Client> findAllByBank(Bank bank);

    @Query("select c from Client c " +
            "where lower(c.firstName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))")
    List<Client> search(@Param("searchTerm") String searchTerm);


}
