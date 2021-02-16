package ru.milkov.credit_calc.backend.service;


import org.springframework.stereotype.Service;
import ru.milkov.credit_calc.backend.DAO.ClientDAO;
import ru.milkov.credit_calc.backend.domain.Bank;
import ru.milkov.credit_calc.backend.domain.Client;

import java.util.List;

@Service
public class ClientService {

    private ClientDAO clientDAO;

    public ClientService(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    public List<Client> findAll() {
        return clientDAO.findAll();
    }

    public List<Client> findAll(String stringFilter){
        if(stringFilter == null || stringFilter.isEmpty()){
            return clientDAO.findAll();
        } else {
            return clientDAO.search(stringFilter);
        }
    }

    public List<Client> findAllByBank(Bank bank) {
        return clientDAO.findAllByBank(bank);
    }

    public void save(Client client) {
        clientDAO.save(client);
    }

    public void delete(Client client) {
        clientDAO.delete(client);
    }
}
