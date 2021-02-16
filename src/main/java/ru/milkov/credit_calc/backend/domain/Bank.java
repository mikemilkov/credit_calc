package ru.milkov.credit_calc.backend.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Bank extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL)
    private List<Client> clients;
    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL)
    private List<Credit> credits = new ArrayList<>();

    public Bank() {
    }

    public Bank(String name) {
        this.name = name;
    }

    public void setClients(List<Client> clients) {
        if (clients != null) {
            clients.forEach(client -> {
                client.setBank(this);
            });
        }
        this.clients = clients;
    }

    public void setCredits(List<Credit> credits) {
        if (credits != null) {
            credits.forEach(creditProduct -> {
                creditProduct.setBank(this);
            });
        }
        this.credits = credits;
    }
}
