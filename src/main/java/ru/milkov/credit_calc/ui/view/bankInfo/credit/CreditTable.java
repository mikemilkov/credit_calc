package ru.milkov.credit_calc.ui.view.bankInfo.credit;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import ru.milkov.credit_calc.backend.domain.Bank;
import ru.milkov.credit_calc.backend.domain.Credit;
import ru.milkov.credit_calc.backend.service.BankService;
import ru.milkov.credit_calc.backend.service.CreditService;
import ru.milkov.credit_calc.ui.view.bankInfo.BankInfoView;

public class CreditTable extends VerticalLayout {
    private CreditService creditService;
    private BankService bankService;
    private CreditForm creditForm;
    private Grid<Credit> creditGrid = new Grid<>(Credit.class);

    public CreditTable(CreditService creditService, BankService bankService) {
        this.creditService = creditService;
        this.bankService = bankService;
        confCreditForm();
        confCreditGrid();
        add(getCreditToolbar(), creditGrid);
        updateCreditList();
    }

    public CreditForm getCreditForm() {
        return creditForm;
    }

    private void confCreditForm() {
        creditForm = new CreditForm(bankService.findAll());
        creditForm.addListener(CreditForm.SaveEvent.class, this::saveCredit);
        creditForm.addListener(CreditForm.DeleteEvent.class, this::deleteCredit);
        creditForm.addListener(CreditForm.CloseEvent.class, e -> closeCreditEditor());
        creditForm.setVisible(false);
    }

    private void confCreditGrid() {
        creditGrid.setColumns("limit", "apr", "currency", "bank");
        creditGrid.removeColumnByKey("limit");
        creditGrid.addColumn(credit -> String.format("%.2f", credit.getLimit()))
                .setHeader("Credit Limit");
        creditGrid.removeColumnByKey("apr");
        creditGrid.addColumn(credit -> String.format("%.2f%%", credit.getApr()))
                .setHeader("APR, %");
        creditGrid.getColumnByKey("currency").setHeader("Currency");
        creditGrid.removeColumnByKey("bank");
        creditGrid.addColumn(credit -> {
            Bank bank = credit.getBank();
            return bank == null ? "-" : bank.getName();
        }).setHeader("Bank");
        creditGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        creditGrid.asSingleSelect().addValueChangeListener(event -> editCredit(event.getValue()));
    }

    private HorizontalLayout getCreditToolbar() {

        Button addCreditBtn = new Button("Add Credit");
        addCreditBtn.addClickListener(click -> addCredit());

        return new HorizontalLayout(addCreditBtn);
    }

    public void updateCreditList() {
        creditGrid.setItems(creditService.findAll());
    }

    private void addCredit() {
        Credit credit = new Credit();
        editCredit(credit);
    }

    private void editCredit(Credit credit) {
        if (credit == null) {
            closeCreditEditor();
        } else {
            creditForm.setCredit(credit);
            if (credit.getLimit() == null || credit.getApr() == null)   creditForm.clear();
            creditForm.setVisible(true);
            addClassName("editing");
        }
    }

    public void closeCreditEditor() {
        creditForm.setCredit(null);
        creditForm.setVisible(false);
        removeClassName("editing");
    }

    private void saveCredit(CreditForm.SaveEvent event) {
        creditService.save(event.getCredit());
        updateCreditList();
        closeCreditEditor();
    }

    private void deleteCredit(CreditForm.DeleteEvent event) {
        creditService.delete(event.getCredit());
        updateCreditList();
        closeCreditEditor();
    }
}
