package ru.milkov.credit_calc.backend.service;


import org.springframework.stereotype.Service;
import ru.milkov.credit_calc.backend.DAO.BankDAO;
import ru.milkov.credit_calc.backend.domain.Bank;

import java.util.List;

@Service
public class BankService {

    private BankDAO bankDAO;

    public BankService(BankDAO bankDAO) {
        this.bankDAO = bankDAO;
    }

    public List<Bank> findAll() {
        return bankDAO.findAll();
    }

    public void save(Bank bank){
        bankDAO.save(bank);
    }

    public void delete(Bank bank) {
        bankDAO.delete(bank);
    }
}
