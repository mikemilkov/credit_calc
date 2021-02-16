package ru.milkov.credit_calc.backend.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
public class CreditOffer extends AbstractEntity {
    public enum CalcType {
        DIFFERENTIAL,
        ANNUITY
    }

    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;
    @ManyToOne(fetch = FetchType.EAGER)
    private Credit credit;
    private CalcType calcType;
    private LocalDate dateOfIssue;
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinColumn(name = "credit_offer_id")
    private List<Payment> paymentShedule;
    private Double duration;
    private Double creditAmmount;
    private Double creditPercents;

    public CreditOffer() {
    }

    public List<Payment> calcPaymentShedule() {
        List<Payment> r = new ArrayList<>();
        r.add(new Payment(this.dateOfIssue, 0.0, 0.0, 0.0, credit.getLimit()));
        double mounthRate = credit.getApr()/100.0/12.0;
        double limit = credit.getLimit();
        double term = this.duration;
        switch (this.calcType) {
            case ANNUITY:
                double mounthPayment = limit * ((mounthRate * Math.pow((1 + mounthRate), term)) / (Math.pow((1 + mounthRate), term) - 1));
                for (int i = 0; i < term; i++) {
                    LocalDate date = dateOfIssue.plusMonths(i + 1);
                    Double payment = mounthPayment;
                    Double creditPercents = r.get(i).getRemainder() * mounthRate;
                    Double creditBody = payment - creditPercents;
                    Double remainder = r.get(i).getRemainder() - creditBody;

                    r.add(new Payment(date, payment, creditBody, creditPercents, remainder));
                }
                break;

            case DIFFERENTIAL:
                double mounthCreditBody = limit / term;
                for (int i = 0; i < term; i++) {
                    LocalDate date = dateOfIssue.plusMonths(i + 1);
                    Double creditBody = mounthCreditBody;
                    Double remainder = r.get(i).getRemainder() - creditBody;
                    Double creditPercents = r.get(i).getRemainder() * mounthRate;
                    Double payment = creditBody + creditPercents;

                    r.add(new Payment(date, payment, creditBody, creditPercents, remainder));
                }
                break;
        }
        return r;
    }

    public Double calcCreditPercent() {
        Double result = 0.0;
        for (Payment payment :
                paymentShedule) {
            result += payment.getCreditPercent();
        }
        return result;
    }

    public Double calcCreditAmount() {
        Double result = 0.0;
        for (Payment payment :
                paymentShedule) {
            result += payment.getPayment();
        }
        return result;
    }

    public String getInfo(){
        String result = String.format("%s, %s",
                client.getFullName(), credit.getInfo());
        return result;
    }
}
