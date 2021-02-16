package ru.milkov.credit_calc.backend.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
@Getter
@Setter
public class Payment {
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "payment")
    private Double payment;
    @Column(name = "credit_body")
    private Double creditBody;
    @Column(name = "credit_percent")
    private Double creditPercent;
    @Column(name = "remainder")
    private Double remainder;

    public Payment() {
    }

    public Payment(LocalDate date,
                   Double payment,
                   Double creditBody,
                   Double creditPercent,
                   Double remainder) {
        this.date = date;
        this.payment = payment;
        this.creditBody = creditBody;
        this.creditPercent = creditPercent;
        this.remainder = remainder;
    }
}
