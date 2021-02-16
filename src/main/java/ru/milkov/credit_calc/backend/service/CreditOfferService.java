package ru.milkov.credit_calc.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.milkov.credit_calc.backend.DAO.CreditOfferDAO;
import ru.milkov.credit_calc.backend.domain.Client;
import ru.milkov.credit_calc.backend.domain.CreditOffer;

import java.util.List;

@Service
public class CreditOfferService {
    @Autowired
    CreditOfferDAO creditOfferDAO;

    public List<CreditOffer> findAll() {
        return creditOfferDAO.findAll();
    }

    public List<CreditOffer> findAllByClient(Client client) {
        return creditOfferDAO.findAllByClient(client);
    }

    public void save(CreditOffer creditOffer) {
        creditOfferDAO.save(creditOffer);
    }

    public void delete(CreditOffer creditOffer) {
        creditOfferDAO.delete(creditOffer);
    }
}
