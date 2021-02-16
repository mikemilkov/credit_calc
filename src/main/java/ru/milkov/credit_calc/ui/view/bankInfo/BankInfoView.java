package ru.milkov.credit_calc.ui.view.bankInfo;


import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.milkov.credit_calc.backend.domain.Bank;
import ru.milkov.credit_calc.backend.service.BankService;
import ru.milkov.credit_calc.backend.service.ClientService;
import ru.milkov.credit_calc.backend.service.CreditService;
import ru.milkov.credit_calc.ui.MainLayout;
import ru.milkov.credit_calc.ui.view.bankInfo.client.ClientTable;
import ru.milkov.credit_calc.ui.view.bankInfo.credit.CreditTable;

import java.util.List;


@Route(value = "", layout = MainLayout.class)
@PageTitle("Bank Info | Credit Calc")
public class BankInfoView extends VerticalLayout {
    private ClientService clientService;
    private CreditService creditService;
    private BankService bankService;

    private Accordion infoAccordion = new Accordion();
    private ClientTable clientTable;
    private CreditTable creditTable;

    public BankInfoView(ClientService clientService, CreditService creditService, BankService bankService) {
        this.clientService = clientService;
        this.creditService = creditService;
        this.bankService = bankService;
        addClassName("list-view");
        clientTable = new ClientTable(clientService, bankService);
        creditTable = new CreditTable(creditService, bankService);
        confAccordion();

        Div content = new Div(infoAccordion,
                clientTable.getClientForm(),
                creditTable.getCreditForm());
        content.addClassName("content");
        content.setSizeFull();

        add(content);
        setSizeFull();
    }

    private void confAccordion() {
        infoAccordion.add("Clients", clientTable);
        infoAccordion.add("Credit Products", creditTable);
        infoAccordion.addOpenedChangeListener(e -> closeAllEditors());
        infoAccordion.setSizeFull();
    }

    private void closeAllEditors() {
        clientTable.closeClentEditor();
        creditTable.closeCreditEditor();
    }

    private void updateAllTables() {
        clientTable.updateClientList();
        creditTable.updateCreditList();
    }
}
