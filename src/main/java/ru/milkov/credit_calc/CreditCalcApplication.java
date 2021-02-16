package ru.milkov.credit_calc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.milkov.credit_calc.backend.DAO.BankDAO;
import ru.milkov.credit_calc.backend.domain.Bank;
import ru.milkov.credit_calc.backend.domain.Client;
import ru.milkov.credit_calc.backend.domain.Credit;

import java.util.Arrays;

@SpringBootApplication
public class CreditCalcApplication implements CommandLineRunner {
    @Autowired
    BankDAO bankDAO;
    public void run(String... args) throws Exception {
        Bank bank1 = new Bank("Райффайзен");
        Bank bank2 = new Bank("Сбербанк");


        Client client1 = new Client(
                "Мильков",
                "Михаил",
                "Константинович",
                "12-34-933286",
                "+7-923-122-4189",
                "mike.milkov@gmail.com");
        Client client2 = new Client(
                "Пентюгов",
                "Антон",
                "Сергеевич",
                "34-12-876003",
                "+7-999-123-3412",
                null);
        Client client3 = new Client(
                "Зарубина",
                "Ольга",
                "Константиновна",
                "34-87-111397",
                "+7-933-234-8345",
                "zarubina@gmail.com");

        Client client4 = new Client(
                "Памурзина",
                "Лидия",
                "Ивановна",
                "34-20-446534",
                "+7-933-934-2946",
                null);

        Credit credit1 =
                new Credit(300000.0, 10.0, Credit.Currency.RUB);
        Credit credit2 =
                new Credit(20000.0, 15.0, Credit.Currency.USD);
        Credit credit3 =
                new Credit(500000.0, 12.0, Credit.Currency.RUB);

        bank1.setClients(Arrays.asList(client1, client2, client3));
        bank1.setCredits(Arrays.asList(credit1, credit2));
        bank2.setClients(Arrays.asList(client4));
        bank2.setCredits(Arrays.asList(credit3));
        bankDAO.saveAll(Arrays.asList(bank1, bank2));

    }

    public static void main(String[] args) {
        SpringApplication.run(CreditCalcApplication.class, args);
    }

}
