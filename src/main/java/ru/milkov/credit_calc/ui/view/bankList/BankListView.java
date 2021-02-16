package ru.milkov.credit_calc.ui.view.bankList;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.milkov.credit_calc.backend.domain.Bank;
import ru.milkov.credit_calc.backend.service.BankService;
import ru.milkov.credit_calc.backend.service.ClientService;
import ru.milkov.credit_calc.backend.service.CreditService;
import ru.milkov.credit_calc.ui.MainLayout;


@Route(value = "bankListView", layout = MainLayout.class)
@PageTitle("Bank List | Credit Calc")
public class BankListView extends VerticalLayout {
    private BankService bankService;
    private ClientService clientService;
    private CreditService creditService;
    private Binder<Bank> bankBinder = new Binder<>(Bank.class);
    private Grid<Bank> bankGrid = new Grid<>(Bank.class);
    private Bank bank;

    Button add = new Button("Add Bank");
    TextField name = new TextField("Bank Name");
    Button update = new Button("Update");
    Button delete = new Button("Delete");
    HorizontalLayout inputForm = new HorizontalLayout();



    public BankListView(BankService bankService, ClientService clientService, CreditService creditService) {
        this.bankService = bankService;
        this.clientService = clientService;
        this.creditService = creditService;
        bankBinder.bindInstanceFields(this);
        confInputForm();
        confBankGrid();
        setInputFormVisible(false);

        add(inputForm, bankGrid);
        updateBankGrid();
    }
    private void confInputForm() {
        add.addClickListener(click -> addBank());
        update.addClickListener(click -> saveBank());
        update.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(click -> deleteBank());
        delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        inputForm.setDefaultVerticalComponentAlignment(Alignment.END);
        inputForm.add(add, name, update, delete);
    }

    private void setInputFormVisible(boolean visible){
        name.setVisible(visible);
        update.setVisible(visible);
        delete.setVisible(visible);
    }

    private void confBankGrid(){
        bankGrid.setColumns("name");
        bankGrid.getColumnByKey("name").setHeader("Name");
        bankGrid.addColumn(bank -> clientService.findAllByBank(bank).size())
                .setHeader("Number of Clients");
        bankGrid.addColumn(bank -> creditService.findAllByBank(bank).size())
                .setHeader("Number of Credit Products");
        bankGrid.asSingleSelect().addValueChangeListener(event -> editBank(event.getValue()));
    }

    private void updateBankGrid(){
        bankGrid.setItems(bankService.findAll());
    }

    private void addBank(){
        editBank(new Bank());
    }

    private void editBank(Bank bank){
        this.bank = bank;
        bankBinder.readBean(bank);
        setInputFormVisible(true);
    }

    private void saveBank(){
        try {
            bankBinder.writeBean(bank);
            bankService.save(bank);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        setInputFormVisible(false);
        updateBankGrid();
    }

    private void deleteBank(){
        bankService.delete(bank);
        setInputFormVisible(false);
        updateBankGrid();
    }
}
