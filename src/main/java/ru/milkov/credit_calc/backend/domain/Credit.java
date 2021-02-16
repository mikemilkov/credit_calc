package ru.milkov.credit_calc.backend.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Credit extends AbstractEntity {
    public enum Currency {
        RUB,
        USD
    }

    @Column(name = "credit_limit")
    private Double limit;

    @Column(name = "APR")
    private Double apr;

    @Column(name = "currency")
    @Enumerated(value = EnumType.STRING)
    private Credit.Currency currency;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "credit")
    Set<CreditOffer> creditOfferSet;

    public Credit() {
    }

    public Credit(Double limit, Double apr, Currency currency) {
        this.limit = limit;
        this.apr = apr;
        this.currency = currency;
    }

    public String getInfo() {
        String result = String.format("%.2f %s, with %.2f %% APR",
                limit, currency.name(), apr);
        return result;
    }

    @Override
    public String toString() {
        return String.format("CreditProduct{limit=%.2f, apr=%.2f, currency=%s}",
                limit, apr, currency.name());
    }
}
