package ru.milkov.credit_calc.ui.view.bankInfo.client;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import ru.milkov.credit_calc.backend.domain.Bank;
import ru.milkov.credit_calc.backend.domain.Client;
import ru.milkov.credit_calc.backend.service.BankService;
import ru.milkov.credit_calc.backend.service.ClientService;

public class ClientTable extends VerticalLayout {
    private ClientService clientService;
    private BankService bankService;
    private ClientForm clientForm;
    TextField clientFilter = new TextField();
    private Grid<Client> clientGrid = new Grid<>(Client.class);

    public ClientTable(ClientService clientService, BankService bankService) {
        this.clientService = clientService;
        this.bankService = bankService;
        confClientForm();
        confClientGrid();
        add(getClientToolbar(), clientGrid);
        updateClientList();
    }

    public ClientForm getClientForm() {
        return clientForm;
    }

    private void confClientForm() {
        clientForm = new ClientForm(bankService.findAll());
        clientForm.addListener(ClientForm.SaveEvent.class, this::saveClient);
        clientForm.addListener(ClientForm.DeleteEvent.class, this::deleteClient);
        clientForm.addListener(ClientForm.CloseEvent.class, e -> closeClentEditor());
        clientForm.setVisible(false);
    }

    private void confClientGrid() {
        clientGrid.setColumns("lastName", "firstName", "patronym", "passportNo", "phone", "email", "bank");
        clientGrid.removeColumnByKey("bank");
        clientGrid.addColumn(client -> {
            Bank bank = client.getBank();
            return bank == null ? "-" : bank.getName();
        }).setHeader("Bank");
        clientGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        clientGrid.asSingleSelect().addValueChangeListener(event -> editClient(event.getValue()));
    }

    private HorizontalLayout getClientToolbar() {
        clientFilter.setPlaceholder("Find User in All Banks");
        clientFilter.setClearButtonVisible(true);
        clientFilter.setValueChangeMode(ValueChangeMode.LAZY);
        clientFilter.addValueChangeListener(e -> updateClientList());

        Button addClientBtn = new Button("Add Client");
        addClientBtn.addClickListener(click -> addClient());

        return new HorizontalLayout(clientFilter, addClientBtn);
    }

    public void updateClientList() {
        clientGrid.setItems(clientService.findAll(clientFilter.getValue()));
    }

    private void addClient() {
        Client client = new Client();
        editClient(client);
    }

    private void editClient(Client client) {
        if (client == null) {
            closeClentEditor();
        } else {
            clientForm.setClient(client);
            clientForm.setVisible(true);
            addClassName("editing");
        }
    }

    public void closeClentEditor() {
        clientForm.setClient(null);
        clientForm.setVisible(false);
        removeClassName("editing");
    }

    private void saveClient(ClientForm.SaveEvent event) {
        clientService.save(event.getClient());
        updateClientList();
        closeClentEditor();
    }

    private void deleteClient(ClientForm.DeleteEvent event) {
        clientService.delete(event.getClient());
        updateClientList();
        closeClentEditor();
    }
}
