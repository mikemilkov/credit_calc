package ru.milkov.credit_calc.backend.service;


import org.springframework.stereotype.Service;
import ru.milkov.credit_calc.backend.DAO.CreditDAO;
import ru.milkov.credit_calc.backend.domain.Bank;
import ru.milkov.credit_calc.backend.domain.Credit;

import java.util.List;

@Service
public class CreditService {

    private CreditDAO creditDAO;

    public CreditService(CreditDAO creditDAO) {
        this.creditDAO = creditDAO;
    }

    public List<Credit> findAll() {
        return creditDAO.findAll();
    }

    public List<Credit> findAllByBank(Bank bank) {
        return creditDAO.findAllByBank(bank);
    }

    public void save(Credit credit) {
        creditDAO.save(credit);
    }

    public void delete(Credit credit) {
        creditDAO.delete(credit);
    }
}
