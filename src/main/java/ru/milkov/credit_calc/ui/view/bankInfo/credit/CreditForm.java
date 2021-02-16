package ru.milkov.credit_calc.ui.view.bankInfo.credit;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import ru.milkov.credit_calc.backend.domain.Bank;
import ru.milkov.credit_calc.backend.domain.Credit;
import ru.milkov.credit_calc.backend.service.converter.DoubleStringConv;

import java.util.List;

public class CreditForm extends FormLayout {
    private Credit credit;

    TextField limit = new TextField("Credit Limit");
    TextField apr = new TextField("APR");
    ComboBox<Credit.Currency> currency = new ComboBox<>("Currency");
    Select<Bank> bank = new Select<>();

    Binder<Credit> creditBinder = new Binder<>(Credit.class);

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Close");

    public CreditForm(List<Bank> bankList) {
        addClassName("credit-form");
        creditBinder.forField(limit)
                .withConverter(new DoubleStringConv())
                .bind(Credit::getLimit, Credit::setLimit);
        creditBinder.forField(apr)
                .withConverter(new DoubleStringConv())
                .bind(Credit::getApr, Credit::setApr);
        creditBinder.forField(currency)
                .bind(Credit::getCurrency, Credit::setCurrency);
        creditBinder.forField(bank)
                .bind(Credit::getBank, Credit::setBank);

        currency.setItems(Credit.Currency.values());
        bank.setItems(bankList);
        bank.setLabel("Bank");
        bank.setItemLabelGenerator(Bank::getName);

        add(new VerticalLayout(
                        limit,
                        apr,
                        currency,
                        bank,
                        getFormBtn()));
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
        creditBinder.readBean(credit);
    }

    private HorizontalLayout getFormBtn() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        close.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, credit)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            creditBinder.writeBean(credit);
            fireEvent(new SaveEvent(this, credit));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void clear(){
        limit.clear();
        apr.clear();
    }

    public static abstract class CreditFormEvent extends ComponentEvent<CreditForm> {
        private Credit credit;

        public CreditFormEvent(CreditForm source, Credit credit) {
            super(source, false);
            this.credit = credit;
        }

        public Credit getCredit() {
            return credit;
        }
    }

    public static class SaveEvent extends CreditFormEvent {
        public SaveEvent(CreditForm source, Credit credit) {
            super(source, credit);
        }
    }

    public static class DeleteEvent extends CreditFormEvent {
        public DeleteEvent(CreditForm source, Credit credit) {
            super(source, credit);
        }
    }

    public static class CloseEvent extends CreditFormEvent {
        public CloseEvent(CreditForm source) {
            super(source, null);
        }
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
