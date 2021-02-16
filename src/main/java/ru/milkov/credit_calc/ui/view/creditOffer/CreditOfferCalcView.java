package ru.milkov.credit_calc.ui.view.creditOffer;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.milkov.credit_calc.backend.domain.*;
import ru.milkov.credit_calc.backend.service.BankService;
import ru.milkov.credit_calc.backend.service.ClientService;
import ru.milkov.credit_calc.backend.service.CreditOfferService;
import ru.milkov.credit_calc.backend.service.CreditService;
import ru.milkov.credit_calc.ui.MainLayout;

import java.text.DateFormat;
import java.time.LocalDate;


@Route(value = "creditOfferCalcView", layout = MainLayout.class)
@PageTitle("Credit Offer Calc | Credit Calc")
public class CreditOfferCalcView extends VerticalLayout {
    private ClientService clientService;
    private CreditService creditService;
    private BankService bankService;
    private CreditOfferService creditOfferService;
    private Bank selectedBank;
    private CreditOffer creditOffer;

    private Select<Bank> bankSelect = new Select<>();
    private Select<Client> clientSelect = new Select<>();
    private Select<Credit> creditSelect = new Select<>();
    private DatePicker dateOfIssue = new DatePicker();
    private NumberField durationNF = new NumberField();
    private Select<CreditOffer.CalcType> calcTypeSelect = new Select<>();
    private Button calcBtn = new Button("Calculate Offer");
    private Button saveBtn = new Button("Save");
    private TextField creditAmmountTF = new TextField();
    private TextField creditPercentsTF = new TextField();
    private HorizontalLayout creditOfferForm;

    private Grid<Payment> paymentSheduleGrid = new Grid<>(Payment.class);
    Binder<CreditOffer> creditOfferBinder = new Binder(CreditOffer.class);

    public CreditOfferCalcView(ClientService clientService,
                               CreditService creditService,
                               BankService bankService,
                               CreditOfferService creditOfferService) {
        this.clientService = clientService;
        this.creditService = creditService;
        this.bankService = bankService;
        this.creditOfferService = creditOfferService;
        creditOfferBinder.forField(clientSelect)
                .bind(CreditOffer::getClient, CreditOffer::setClient);
        creditOfferBinder.forField(creditSelect)
                .bind(CreditOffer::getCredit, CreditOffer::setCredit);
        creditOfferBinder.forField(dateOfIssue)
                .bind(CreditOffer::getDateOfIssue, CreditOffer::setDateOfIssue);
        creditOfferBinder.forField(durationNF)
                .bind(CreditOffer::getDuration, CreditOffer::setDuration);
        creditOfferBinder.forField(calcTypeSelect)
                .bind(CreditOffer::getCalcType, CreditOffer::setCalcType);

        confCreditOfferForm();
        confPaymentSheduleGrid();

        confPaymentSheduleGrid();
        add(creditOfferForm, paymentSheduleGrid);
    }

    public void setCreditOffer(CreditOffer creditOffer) {
        this.creditOffer = creditOffer;
        creditOfferBinder.readBean(creditOffer);
    }

    private void confBankSelect() {
        bankSelect.setLabel("Choose Bank");
        bankSelect.setItems(bankService.findAll());
        bankSelect.setItemLabelGenerator(Bank::getName);
        bankSelect.addValueChangeListener(event -> setSelectedBank(event.getValue()));
    }


    private void confCreditOfferForm() {
        confBankSelect();
        clientSelect.setLabel("pick a client:");
        creditSelect.setLabel("pick a credit:");
        HorizontalLayout firstLine = new HorizontalLayout(bankSelect, clientSelect,
                creditSelect, createBtnLayout());
        firstLine.setDefaultVerticalComponentAlignment(Alignment.END);

        durationNF.setLabel("duration in month:");
        durationNF.setHasControls(true);
        durationNF.setMin(1.0);
        durationNF.setMax(48.0);
        durationNF.setStep(1.0);
        calcTypeSelect.setLabel("choose calculation method");
        calcTypeSelect.setItems(CreditOffer.CalcType.values());
        dateOfIssue.setLabel("pick a date of issue:");
        creditAmmountTF.setReadOnly(true);
        creditAmmountTF.setLabel("Credit Amount");
        creditPercentsTF.setReadOnly(true);
        creditPercentsTF.setLabel("Percent Amount");
        HorizontalLayout secondLine = new HorizontalLayout(durationNF,
                calcTypeSelect, dateOfIssue, creditAmmountTF, creditPercentsTF);
        VerticalLayout formInput = new VerticalLayout(firstLine, secondLine);


        creditOfferForm = new HorizontalLayout(formInput);
        creditOfferForm.setDefaultVerticalComponentAlignment(Alignment.END);
        creditOfferForm.setWidthFull();

        setCreditOfferFormEnabled(false);
        saveBtn.setEnabled(false);
    }

    private void confPaymentSheduleGrid() {
        paymentSheduleGrid.setColumns("date", "payment", "creditPercent", "creditBody", "remainder");
        paymentSheduleGrid.removeColumnByKey("payment");
        paymentSheduleGrid.addColumn(payment -> String.format("%.2f", payment.getPayment()))
                .setHeader("Payment");
        paymentSheduleGrid.removeColumnByKey("creditPercent");
        paymentSheduleGrid.addColumn(payment -> String.format("%.2f", payment.getCreditPercent()))
                .setHeader("Percents");
        paymentSheduleGrid.removeColumnByKey("creditBody");
        paymentSheduleGrid.addColumn(payment -> String.format("%.2f", payment.getCreditBody()))
                .setHeader("Credit Body");
        paymentSheduleGrid.removeColumnByKey("remainder");
        paymentSheduleGrid.addColumn(payment -> String.format("%.2f", payment.getRemainder()))
                .setHeader("Remainder");
        paymentSheduleGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        for (Grid.Column c :
                paymentSheduleGrid.getColumns()) {
            c.setSortable(false);
        }
    }

    private void setSelectedBank(Bank selectedBank) {
        this.selectedBank = selectedBank;
        updateCreditOfferForm();
    }

    private void updateCreditOfferForm() {
        clientSelect.setItems(clientService.findAllByBank(selectedBank));
        clientSelect.setItemLabelGenerator(Client::getFullName);

        creditSelect.setItems(creditService.findAllByBank(selectedBank));
        creditSelect.setItemLabelGenerator(Credit::getInfo);

        setCreditOfferFormEnabled(true);
        saveBtn.setEnabled(false);
    }

    private void setCreditOfferFormEnabled(boolean isEnable) {
        clientSelect.setEnabled(isEnable);
        creditSelect.setEnabled(isEnable);
        dateOfIssue.setEnabled(isEnable);
        durationNF.setEnabled(isEnable);
        calcTypeSelect.setEnabled(isEnable);
        calcBtn.setEnabled(isEnable);
    }

    private HorizontalLayout createBtnLayout() {
        calcBtn.addClickShortcut(Key.SPACE);
        calcBtn.addClickListener(event -> calcPaymentShedule());
        saveBtn.addClickShortcut(Key.ENTER);
        saveBtn.addClickListener(event -> saveCreditOffer());
        return new HorizontalLayout(calcBtn, saveBtn);
    }

    private void calcPaymentShedule() {
        try {
            creditOffer = new CreditOffer();
            creditOfferBinder.writeBean(creditOffer);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        if (creditOffer != null) {
            creditOffer.setPaymentShedule(creditOffer.calcPaymentShedule());
            creditOffer.setCreditAmmount(creditOffer.calcCreditAmount());
            creditOffer.setCreditPercents(creditOffer.calcCreditPercent());
            paymentSheduleGrid.setItems(creditOffer.getPaymentShedule());
            creditAmmountTF.setValue(String.format("%.2f",
                    creditOffer.getCreditAmmount()));
            creditPercentsTF.setValue(String.format("%.2f",
                    creditOffer.getCreditPercents()));
            saveBtn.setEnabled(true);
        }
    }

    private void saveCreditOffer() {
        creditOfferService.save(creditOffer);
        updateCreditOfferForm();
    }
}
