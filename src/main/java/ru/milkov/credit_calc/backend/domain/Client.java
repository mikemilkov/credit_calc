package ru.milkov.credit_calc.backend.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Entity
@Getter
@Setter
public class Client extends AbstractEntity {

    @Column(name = "first_name")
    @NotNull
    @NotEmpty
    private String firstName = "";

    @Column(name = "last_name")
    @NotNull
    @NotEmpty
    private String lastName = "";

    @Column(name = "patronym")
    @NotNull
    @NotEmpty
    private String patronym = "";

    @Column(name = "passportNo")
    @NotNull
    @NotEmpty
//    @Pattern(regexp = "^(\\d{2}-?\\d{2}-?\\d{6})$")
    private String passportNo = "";

    @Column(name = "phone")
    @NotNull
    @NotEmpty
    private String phone = "";

    @Column(name = "email")
    @Email
    private String email = "";

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "client")
    Set<CreditOffer> creditOfferSet;

    public Client() {
    }

    public Client(@NotNull @NotEmpty String lastName,
                  @NotNull @NotEmpty String firstName,
                  @NotNull @NotEmpty String patronym,
                  @NotNull @NotEmpty
                  @Pattern(regexp = "^(\\d{2}-?\\d{2}-?\\d{6})$") String passportNo,
                  @NotNull @NotEmpty String phone,
                  @Email String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronym = patronym;
        this.passportNo = passportNo;
        this.phone = phone;
        this.email = email;
    }

    public String getFullName() {
        String result = String.format("%s %c. %c.",
                lastName,
                Character.toUpperCase(firstName.charAt(0)),
                Character.toUpperCase(patronym.charAt(0)));
        return result;
    }

    @Override
    public String toString() {
        return "Client{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronym='" + patronym + '\'' +
                ", passportNo='" + passportNo + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
