package ru.milkov.credit_calc.ui.view.bankInfo.client;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import ru.milkov.credit_calc.backend.domain.Bank;
import ru.milkov.credit_calc.backend.domain.Client;

import java.util.List;

public class ClientForm extends FormLayout {
    private Client client;

    private TextField lastName = new TextField("Last Name");
    private TextField firstName = new TextField("First Name");
    private TextField patronym = new TextField("Patronym");
    private TextField passportNo = new TextField("Passport №", "xx-xx-xxxxxx");
    private TextField phone = new TextField("Phone");
    private EmailField email = new EmailField("e-mail");
    private Select<Bank> bank = new Select<>();

    Binder<Client> clientBinder = new Binder<>(Client.class);

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Close");

    public ClientForm(List<Bank> bankList) {
        addClassName("client-form");
        clientBinder.bindInstanceFields(this);
        bank.setItems(bankList);
        bank.setLabel("Bank");
        bank.setItemLabelGenerator(Bank::getName);

        add(new VerticalLayout(
                lastName,
                firstName,
                patronym,
                passportNo,
                phone,
                email,
                bank,
                getFormBtn()));
    }

    public void setClient(Client client) {
        this.client = client;
        clientBinder.readBean(client);
    }

    private HorizontalLayout getFormBtn() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        close.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, client)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            clientBinder.writeBean(client);
            fireEvent(new SaveEvent(this, client));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class ClientFormEvent extends ComponentEvent<ClientForm> {
        private Client client;

        public ClientFormEvent(ClientForm source, Client client) {
            super(source, false);
            this.client = client;
        }

        public Client getClient() {
            return client;
        }
    }

    public static class SaveEvent extends ClientFormEvent {
        public SaveEvent(ClientForm source, Client client) {
            super(source, client);
        }
    }

    public static class DeleteEvent extends ClientFormEvent {
        public DeleteEvent(ClientForm source, Client client) {
            super(source, client);
        }
    }

    public static class CloseEvent extends ClientFormEvent {
        public CloseEvent(ClientForm source) {
            super(source, null);
        }
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
